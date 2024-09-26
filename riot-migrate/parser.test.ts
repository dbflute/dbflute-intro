import {
  extractLifecycleEvents,
  modifyLifecycleMethods,
  parseToTypescriptAST,
} from "./parser";
import * as recast from "recast";

const logModifiedCode = (code: string) => {
  console.log("[modified code]\n", code)
}

describe("parser", () => {
  it("modifyLifecycleMethods", () => {
    // 使用例
    const riotCode = `
this.on('mount', () => { })
this.on('update', () => { })
this.on('before-mount', () => { })
this.prepareSettings()
`;

    const templateCode = `
interface TemplateComponent extends IntroRiotComponent<Props, State> {
}

export default withIntroTypes<TemplateComponent>({
  components: {},
  state: {}
})
`;

    const riotAst = recast.parse(riotCode);
    const templateAst = parseToTypescriptAST(templateCode);
    const lifecycleMethodNames = extractLifecycleEvents(riotAst);
    modifyLifecycleMethods(templateAst, lifecycleMethodNames);
    const modifiedCode = recast.print(templateAst).code;
    logModifiedCode(modifiedCode);

    expect(modifiedCode).toEqual(`
interface TemplateComponent extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMount(): void;
  onUpdate(): void;
  onBeforeMount(): void;
}

export default withIntroTypes<TemplateComponent>({
  components: {},
  state: {},
  onMount() {},
  onUpdate() {},
  onBeforeMount() {}
})
`)
  });
});

