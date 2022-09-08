#!/bin/sh

set -e -o pipefail

# プロジェクトルートディレクトリ
cd `dirname $0`
ROOT=`pwd`

# フロントエンドアプリディレクトリに移動する
cd "$ROOT/frontend"

# 前回ビルドしたファイルを削除
rm -rf dist

# bundleファイル(dist/main.js)を作成する
npm install
npm run build

# SPAに必要なファイルをDBFluteIntroのjarに同梱するためのディレクトリ($ROOT/dist)にコピーする
mkdir -p "$ROOT/dist"
cp -r src/static/ "$ROOT/dist"
cp dist/main.js "$ROOT/dist"
