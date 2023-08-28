// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// riotの周辺ライブラリでTypeScript対応がされていないものを定義。
// ただ、ちゃんと型付けするの大変なので、とりあえず実行時に解決だけ。
// 将来、このファイル自身がなくなったらいいなというところで。
// _/_/_/_/_/_/_/_/_/_/
declare module '*.riot'
// TODO: 正しく d.ts を定義し、jsファイルのソース定義にコードジャンプできるようにする
declare module 'riot-observable'
declare module '@riotjs/route'
