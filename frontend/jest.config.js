// jest の各種設定
// 設定できる項目は https://jestjs.io/ja/docs/configuration 参照
module.exports = {
  verbose: true, // テスト結果を1つずつ出力するかどうか
  testEnvironment: 'jsdom', // テスト環境をDOMに変更（デフォルトはnodeになっているのでWebアプリのテストができない。windowとか取れない）
  testMatch: [
    '**/__tests__/**/*.+(ts|js)',
    '**/?(*.)+(spec|test).+(ts|js)'
  ],
  transform: {
    '^.+\\.js$': 'babel-jest', // テストをCJS方式（require）ではなく、ESM方式（import）で書けるようにbabelを噛ませておく
    '^.+\\.ts$': 'ts-jest',
    '^.+\\.riot$': './jest.riot-transformer.js' // riotファイルは初めにコンパイルが必要なので独自でtransformerを噛ませる
  },
  setupFilesAfterEnv: ['./jest.setup-test.js'] // 各テストファイル実行前のsetup用script
}
