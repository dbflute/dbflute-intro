const webpack = require('webpack')
const ESLintPlugin = require('eslint-webpack-plugin')

module.exports = {
  entry: './src/static/app/index.ts',
  output: {
    path: __dirname + '/dist/',
  },
  devServer: {
    proxy: [
      // proxy URLs to backend development server
      {
        context: ['/api'],
        target: 'http://localhost:8925',
      },
    ],
    static: {
      directory: './src/static',
    },
    client: {
      overlay: {
        // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
        // ApiClient呼び出しでcatch必須を避けるためunhandledrejectionで例外内容次第でmodal上書き回避しているが、
        // (ローカル環境で)余計なエラーオーバーレイが出てしまうのでデフォルトではOFFにしておく。
        // それ以外の不意の例外はunhandledrejectionでmodal表示されるので、オーバーレイがなくても問題ないはず。
        // もしわからないエラーが出たら、一時的にtrueにしてオーバーレイを見るようにするくらいでOKかと。
        //  by jflute (2026/06/11)
        // _/_/_/_/_/_/_/_/
        runtimeErrors: false,
      },
    },
  },
  module: {
    rules: [
      {
        test: /\.riot$/,
        exclude: /node_modules/,
        use: [
          {
            loader: '@riotjs/webpack-loader',
            options: {
              hot: true,
            },
          },
        ],
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader'],
      },
      {
        test: /\.ts$/,
        exclude: /node_modules/,
        use: ['ts-loader'],
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  resolve: {
    extensions: ['.ts', '.js'],
    // riot.route で出るエラー回避
    fallback: { url: false },
  },
  devtool: 'source-map',
  plugins: [
    new webpack.ProvidePlugin({
      riot: 'riot',
    }),
    new ESLintPlugin({
      fix: true,
      emitWarning: true,
    }),
  ],
}
