import observable from 'riot-observable'

// riot3の時代のObservableのものを使っている
// https://github.com/riot/observable/tree/v3
// (v3ブランチがriot3のもの)
//
// 最新のObservableもインターフェース同じような感じなら移行できるかな？
// (riot7ではアプリコードの中ではあまり使ってなく、仕組みの中がメインなのでいつか移行したい)
// https://github.com/riot/observable
// (mainブランチが新しいもの)
//
// done you これexportする必要あるだろうか？ (今のところimportしてる人はいなさそう) by jflute (2023/06/05)
// (できるだけ、このモジュールファイル内で完結させて、公開関数だけ呼んでもらうがいいかな？)
// → プルリク上でhakibaに確認をしたので、もうexportを削除しちゃった (2023/07/10)
const appObservable = observable()

// ===================================================================================
//                                                                         Show Result
//                                                                         ===========
/**
 * 結果ビューの表示情報。
 *
 * (結果ビュー: サーバー側でエラーがあった場合に表示されるモーダル)
 */
type ShowResultContent = {
  /** モーダルのサイズ。指定できるサイズは <a href="https://semantic-ui-riot.web.app/module/modal#size">こちら</a> 参照 */
  modalSize: string | undefined

  /** モーダルのヘッダーに表示するタイトル */
  header: string | undefined

  /** モーダルのに表示するメッセージ群 */
  messages: string[]
}

/**
 * 結果ビュー表示イベントを購読する。
 * @param {(content: ShowResultContent) => void} callback 結果ビューに表示するデータを受け取るコールバック (NotNull)
 */
export function subscribeShowResult(callback: (content: ShowResultContent) => void) {
  appObservable.on('show-result', (content: ShowResultContent) => callback(content))
}

/**
 * 結果ビュー表示イベントを発行する。
 * @param {ShowResultContent} content 結果ビューの表示情報 (NotNull)
 */
export function triggerShowResult(content: ShowResultContent) {
  appObservable.trigger('show-result', content)
}
