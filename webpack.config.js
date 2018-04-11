const webpack = require('webpack');
const path = require('path');

module.exports = {
  entry: './src/static/app/index.js',
  output: {
    path: __dirname + '/dist/'
  },
  devServer: {
    proxy: { // proxy URLs to backend development server
      '/api': 'http://localhost:8925'
    }
  },
  module: {
    rules: [
      {
        test: /\.tag$/,
        exclude: /node_modules/,
        use: [
          {
            loader: 'riot-tag-loader',
            options: {
              type: 'es6', // transpile the riot tags using babel
              hot: true
            }
          },
          {
            loader: 'eslint-loader',
            options: {
              fix: true,
              emitWarning: true,
            }
          },
        ]
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: ['babel-loader']
      }
    ]
  },
  devtool: 'source-map',
  plugins: [
    new webpack.LoaderOptionsPlugin({ options: {} }),
    new webpack.ProvidePlugin({
      riot: 'riot',
    })
  ]
};