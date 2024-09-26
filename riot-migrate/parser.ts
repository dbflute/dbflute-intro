import * as recast from "recast";
import * as parser from "@babel/parser";
import { debugASTNode, kebabToPascalCase } from "./util";

export type InputMeta = {
  riotFile: string;
  customComponentName?: string;
};

export type OutputMeta = {
  componentName: string;
  props: string[];
  state: string[];
  lifecycleMethods: RiotV3LifecycleMethod[];
};

export function extractOutputMeta(input: InputMeta): OutputMeta {
  const { riotFile, customComponentName } = input;
  const componentName = customComponentName ?? extractComponentName(riotFile);
  const script = extractScript(riotFile);
  const ast = recast.parse(script);
  const { props, state } = extractPropsAndState(ast);
  let lifecycleMethods = extractLifecycleEvents(ast);

  return { componentName, props, state, lifecycleMethods };
}

function extractComponentName(riotFile: string) {
  const outerTagRegex = /<([a-zA-Z0-9-]+)[\s\S]*?>[\s\S]*?<\/\1>/;
  const match = riotFile.match(outerTagRegex);
  return (match && match[1]) ?? "no-name";
}

function extractScript(riotFile: string) {
  const scriptRegex = /<script>([\s\S]*?)<\/script>/;
  const scriptMatch = riotFile.match(scriptRegex);
  return scriptMatch ? scriptMatch[1].trim() : "";
}

export function parseToTypescriptAST(templateCode: string) {
  return recast.parse(templateCode, {
    parser: {
      parse(source: string) {
        return parser.parse(source, {
          sourceType: "module",
          plugins: ["typescript"],
        });
      },
    },
  });
}

export function extractPropsAndState(ast: any): {
  props: string[];
  state: string[];
} {
  const props: string[] = [];
  const state: string[] = [];

  recast.visit(ast, {
    visitMemberExpression(path) {
      const node = path.node;

      // self.*.* のパターン
      if (
        node.property.type === "Identifier" &&
        node.object.type === "MemberExpression" &&
        node.object.object.type === "Identifier" &&
        node.object.object.name === "self" &&
        node.object.property.type === "Identifier"
      ) {
        const propName = node.object.property.name;
        if (propName === "opts") props.push(node.property.name);
        else state.push(node.property.name);
      }

      // this.*.* のパターン
      if (
        node.property.type === "Identifier" &&
        node.object.type === "MemberExpression" &&
        node.object.object.type === "ThisExpression" &&
        node.object.property.type === "Identifier"
      ) {
        const propName = node.object.property.name;
        if (propName === "opts") props.push(node.property.name);
        else state.push(node.property.name);
      }

      this.traverse(path);
    },
  });

  return {
    props: [...new Set(props)],
    state: [...new Set(state)],
  };
}

function applyProperties(
  props: string[],
  node: any,
  optional: boolean = false
) {
  const properties = props.map((prop) =>
    recast.types.builders.tsPropertySignature(
      recast.types.builders.identifier(prop),
      recast.types.builders.tsTypeAnnotation(
        recast.types.builders.tsAnyKeyword()
      ),
      optional // この引数をtrueに設定することで、プロパティをオプショナルにします
    )
  );
  node.body.body.push(...properties);
}

export const createAndModifyThoroughAST = (
  templateContent: string,
  componentInfo: OutputMeta
): string => {
  const { props, state, lifecycleMethods } = componentInfo;
  const ast = parseToTypescriptAST(templateContent);
  modifyLifecycleMethods(ast, lifecycleMethods);
  recast.visit(ast, {
    visitTSInterfaceDeclaration(path) {
      let node = path.node;
      if (node.id.type === "Identifier" && node.id.name === "Props") {
        applyProperties(props, node);
      }
      if (node.id.type === "Identifier" && node.id.name === "State") {
        applyProperties(state, node, true);
      }
      return false;
    },
  });
  return recast.print(ast).code;
};

const n = recast.types.namedTypes;
const b = recast.types.builders;
// 参照: https://typescriptbook.jp/reference/type-reuse/indexed-access-types#%E3%82%BF%E3%83%97%E3%83%AB%E5%9E%8B%E3%81%A8%E3%82%A4%E3%83%B3%E3%83%87%E3%83%83%E3%82%AF%E3%82%B9%E3%82%A2%E3%82%AF%E3%82%BB%E3%82%B9%E5%9E%8B
const riotV3EventTypes = [
  "update",
  "updated",
  "before-mount",
  "mount",
  "before-unmount",
  "unmount",
] as const;
type RiotV3EventType = typeof riotV3EventTypes[number];

type RiotV3LifecycleMethod = {
  type: RiotV3EventType;
  func: recast.types.namedTypes.Expression;
};

const isRiotV3Event = (value: unknown): value is RiotV3EventType => {
  return riotV3EventTypes.some((t) => t === value);
};

export const extractLifecycleEvents = (
  riotAst: any
): RiotV3LifecycleMethod[] => {
  const lifecycleEvents: RiotV3LifecycleMethod[] = [];

  // Riot.js v3のコードからライフサイクルメソッドを抽出
  recast.visit(riotAst, {
    visitCallExpression(path) {
      const node = path.node;
      if (
        n.MemberExpression.check(node.callee) &&
        n.ThisExpression.check(node.callee.object) &&
        n.Identifier.check(node.callee.property) &&
        node.callee.property.name === "on" &&
        node.arguments.length >= 2 &&
        n.Literal.check(node.arguments[0]) &&
        isRiotV3Event(node.arguments[0].value)
      ) {
        const type = node.arguments[0].value;
        const func = node.arguments[1];
        lifecycleEvents.push({
          type,
          func,
        });
      }
      return false;
    },
  });

  return lifecycleEvents;
};

const lifecycleTagComments = [
  " ===================================================================================",
  "                                                                           Lifecycle",
  "                                                                           =========",
].map((c) => b.commentLine(c));

export const modifyLifecycleMethods = (
  ast: any,
  riotV3LifecycleMethods: RiotV3LifecycleMethod[]
) => {
  let interfaceModified = false;
  let exportDefaultModified = false;

  // componentのinterfaceとexport defaultの両方にライフサイクルメソッドを追加する操作を行う
  recast.visit(ast, {
    visitTSInterfaceDeclaration(path) {
      const node = path.node;
      if (
        node.id.type === "Identifier" &&
        node.id.name === "TemplateComponent" &&
        !interfaceModified
      ) {
        const body = node.body;

        // v4系以降のライフサイクルメソッドイベントのNodeを作成
        const lifecycleMethodNodes = riotV3LifecycleMethods.map((event) =>
          b.tsMethodSignature.from({
            key: b.identifier(`on${kebabToPascalCase(event.type)}`),
            parameters: [],
            typeAnnotation: b.tsTypeAnnotation(b.tsVoidKeyword()),
            computed: false,
            optional: false,
          })
        );

        // コメントとライフサイクルメソッドを追加
        if (lifecycleMethodNodes.length > 0) {
          lifecycleMethodNodes[0].comments = lifecycleTagComments;
          body.body.unshift(...lifecycleMethodNodes);
        }

        interfaceModified = true;
      }
      this.traverse(path);
    },

    visitExportDefaultDeclaration(path) {
      const node = path.node;
      if (n.CallExpression.check(node.declaration) && !exportDefaultModified) {
        const callExpr = node.declaration;
        if (
          n.Identifier.check(callExpr.callee) &&
          callExpr.callee.name === "withIntroTypes"
        ) {
          const objExpr = callExpr.arguments[0];
          debugASTNode(objExpr);
          if (n.ObjectExpression.check(objExpr)) {
            // ライフサイクルメソッドを追加
            const lifecycleMethods = riotV3LifecycleMethods.map((event) =>
              b.objectMethod(
                "method",
                b.identifier(`on${kebabToPascalCase(event.type)}`),
                [],
                b.blockStatement([])
              )
            );

            // 既存のプロパティの後にライフサイクルメソッドを追加
            objExpr.properties.push(...lifecycleMethods);

            exportDefaultModified = true;
          }
        }
      }
      this.traverse(path);
    },
  });
};
