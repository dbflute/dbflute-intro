import observable from 'riot-observable'

export const appObservable = observable()

type ShowResultContent = {
  /** モーダルのサイズ。指定できるサイズは <a href="https://semantic-ui-riot.web.app/module/modal#size">こちら</a> 参照 */
  modalSize: string | undefined
  /** モーダルのヘッダーに表示するタイトル */
  header: string | undefined
  /** モーダルのに表示するメッセージ群 */
  messages: string[]
}

/**
 * 結果ビュー表示イベントを購読します
 * @param {(content: ShowResultContent) => void} callback 結果ビューに表示するデータを受け取るコールバック
 * @typedef ShowResultContent
 * @property {string | undefined} modalSize モーダルのサイズ。指定できるサイズは <a href="https://semantic-ui-riot.web.app/module/modal#size">こちら</a> 参照
 * @property {string | undefined} header モーダルのヘッダーに表示するタイトル
 * @property {string[]} messages モーダルのに表示するメッセージ群
 */
export function subscribeShowResult(callback: (content: ShowResultContent) => void) {
  appObservable.on('show-result', (content: ShowResultContent) => callback(content))
}

/**
 * 結果ビュー表示イベントを発行します
 * @param {ShowResultContent} content
 */
export function triggerShowResult(content: ShowResultContent) {
  appObservable.trigger('show-result', content)
}

