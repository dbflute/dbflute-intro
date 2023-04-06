// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// semantic-ui-riotの関数をTypeScriptで補完されるようにするため。
// 初期化時にriotにinstall()された関数を定義する。
//
// 最初から全部列挙しているわけではないので必要になったら随時追加しましょう。
// TODO you 関数のコメントもあると嬉しいかも？ by jflute
// _/_/_/_/_/_/_/_/_/_/
export interface SemanticUiRiotPlugin {
  suLoading(b: boolean): void
  suConfirm(message: string): Promise<void>
}
