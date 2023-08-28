// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// semantic-ui-riotの関数をTypeScriptで補完されるようにするため。
// 初期化時にriotにinstall()された関数を定義する。
//
// 最初から全部列挙しているわけではないので必要になったら随時追加しましょう。
//
// ゆくゆくコントリビュートできるように関数コメントは最終的には英語で書く。
// ただ、途中の段階では日本語でもOK。(ずうむにて) by jflute(2023/05/01)
// _/_/_/_/_/_/_/_/_/_/
export interface SemanticUiRiotPlugin {
  /**
   * 処理中を示すLoadingの表示を制御する。
   * Loading表示中は後ろの画面にはアクセスできなくなる。
   * @param visible Loadingを表示するかどうか？
   */
  suLoading(visible: boolean): void

  /**
   * 確認画面のモーダルを表示する。
   * @param message 確認画面上のメッセージ (NotNull)
   */
  suConfirm(message: string): Promise<void>
}
