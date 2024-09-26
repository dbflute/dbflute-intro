import { Command } from "commander";
import fs from "fs";
import path from "path";
import MagicString from "magic-string";
import { createAndModifyThoroughAST, extractOutputMeta } from "./parser";
import { kebabToPascalCase } from "./util";

const program = new Command();

program
  .version("1.0.0")
  .option("-t, --target <path>", "Path to the Riot.js file")
  .option("-n, --name <path>", "Component name")
  .option("-o, --output <path>", "Output path")
  .parse(process.argv);

const { target, name, output } = program.opts();

// インプット情報の用意
const inputRiotFile = fs.readFileSync(
  path.resolve(process.cwd(), "../" + target),
  "utf8"
);
const outputMeta = extractOutputMeta({
  riotFile: inputRiotFile,
  customComponentName: name,
});

console.log(`
targetRiotFile: ${path.resolve(process.cwd(), "../" + target)}
componentName: ${outputMeta.componentName}
props: ${outputMeta.props}
state: ${outputMeta.state}
output: ${output}
`);

// 加工処理
const templateContent = readFile("template-component.ts");
const modifiedContent = createAndModifyThoroughAST(templateContent, outputMeta);

// ファイル出力
if (output) {
  generateComponentFile(
    modifiedContent,
    kebabToPascalCase(outputMeta.componentName),
    path.resolve(process.cwd(), "../" + output)
  );
} else {
  console.log("skip file generate. please input output path (-o)");
}

// ファイルを読み込む関数
function readFile(filePath: string): string {
  return fs.readFileSync(filePath, "utf8");
}

// ファイルを書き込む関数
function writeFile(filePath: string, content: string): void {
  fs.writeFileSync(filePath, content, "utf8");
}

// メイン処理
function generateComponentFile(
  content: string,
  newComponentName: string,
  outputPath: string
): void {
  const s = new MagicString(content);

  // インターフェース名の置換
  s.replace(/TemplateComponent/g, `${newComponentName}`);

  // 変更されたコンテンツを新しいファイルに書き込む
  writeFile(outputPath, s.toString());

  console.log(`Generated ${newComponentName} in ${outputPath}`);
}
