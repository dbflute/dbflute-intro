// ESM方式(import)に対応していないのでCJS方式(require)でライブラリを読み込む
const { compile } = require('@riotjs/compiler');
const { createTransformer } = require('babel-jest')

/**
 * .riotファイルをjestで利用できるように変換する
 * - .riotをコンパイルしたあとはbabel-jestに全部お任せしているだけ
 *
 * @param sourceText .riotファイルの中身
 * @param sourcePath .riotファイルのパス
 * @param options jestの各種設定
 * @return {TransformedSource} 変換後のソースbabelでトランスパイルされた後のjsコードになっているはず
 */
exports.process = (sourceText, sourcePath, options) => {
  const compiled = compile(sourceText)
  const transformer = createTransformer()
  return transformer.process(compiled.code, sourcePath, options)
}
