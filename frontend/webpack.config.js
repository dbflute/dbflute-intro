const webpack = require('webpack');
const ESLintPlugin = require('eslint-webpack-plugin')

module.exports = {
  entry: './src/static/app/index.ts',
  output: {
    path: __dirname + '/dist/'
  },
  devServer: {
    proxy: { // proxy URLs to backend development server
      '/api': 'http://localhost:8925'
    },
    static: {
      directory: './src/static'
    }
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
              hot: true
            }
          }
        ]
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader']
      },
      {
        test: /\.ts$/,
        exclude: /node_modules/,
        use: ['ts-loader'],
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      }
    ]
  },
  resolve: {
    extensions: ['.ts', '.js'],
    // riot.route で出るエラー回避
    fallback: { 'url': false }
  },
  devtool: 'source-map',
  plugins: [
    new webpack.LoaderOptionsPlugin({options: {}}),
    new webpack.ProvidePlugin({
      riot: 'riot',
    }),
    new ESLintPlugin({
      fix: true,
      emitWarning: true,
    })
  ]
};
