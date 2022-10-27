// jest の各種設定
// 設定できる項目は https://jestjs.io/ja/docs/configuration 参照
module.exports = {
  verbose: true, // テスト結果を1つずつ出力するかどうか
  testEnvironment: 'jsdom', // テスト環境をDOMに変更（デフォルトはnodeになっているのでWebアプリのテストができない。windowとか取れない）
  testMatch: ['**/__tests__/**/*.+(ts|js)', '**/?(*.)+(spec|test).+(ts|js)'],
  transform: {
    '^.+\\.js$': 'babel-jest', // テストをCJS方式（require）ではなく、ESM方式（import）で書けるようにbabelを噛ませておく
    '^.+\\.ts$': 'ts-jest',
    '^.+\\.riot$': './jest.riot-transformer.js', // riotファイルは初めにコンパイルが必要なので独自でtransformerを噛ませる
  },
  moduleNameMapper: {
    // 静的アセットはテストに利用しないのでモック化 参照: https://jestjs.io/ja/docs/webpack#%E9%9D%99%E7%9A%84%E3%82%A2%E3%82%BB%E3%83%83%E3%83%88%E3%81%AE%E7%AE%A1%E7%90%86
    '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$': '<rootDir>/__mocks__/fileMock.js',
    '\\.(css|less)$': '<rootDir>/__mocks__/styleMock.js',
  },
  setupFilesAfterEnv: ['./jest.setup-test.js'], // 各テストファイル実行前のsetup用script
}
