import fs from 'fs';

export function kebabToPascalCase(str: string): string {
  return str
    .split("-") // ハイフンで分割
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1)) // 各単語の頭文字を大文字に
    .join(""); // 単語を結合
}

export function debugASTNode(node: any) {
  const debugJson = JSON.stringify(node, null, 2);
  fs.writeFileSync('debug-output.json', debugJson);
}

