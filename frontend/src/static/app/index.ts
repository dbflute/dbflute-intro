/**
 * ファーストアクセス時に実行されるトップのTypeScript。
 * Riotを準備して、index.htmlのコンテンツの展開領域にアプリをマウントする。
 *
 * オフィシャルドキュメントのサンプルコードを参考に実装された。
 * https://riot.js.org/ja/documentation/
 */

// riotモジュールのコンテンツすべてをimportする
// (riot変数でexportされてるものにアクセスできる)
import * as riot from 'riot'

// アプリのriot関連の共通コンポーネントをimportする
// (defaultでexportされてるものをAppという名前で扱って後にマウント)
import App from './app.riot'

// アプリ内でsemantic-ui-riotとsemantic-uiのグローバルオブジェクトを使うために
// (初期化処理でui-riotの関数などがriotにinstallされる)
// (初期化だけで良いので受け取りのための変数は特になし)
import 'semantic-ui-riot'

// 国際化対応の初期化処理を行うために
// (初期化だけで良いので受け取りのための変数はない)
import './shared/i18n'

// アプリ独自のプラグイン関数をriotにinstall()するための関数
import introPlugin from './app-plugin'

// グローバルエラーを監視するための関数
import { subscribeGlobalError, triggerGlobalError, triggerShowResult } from './app-events'

// 全てのRiotコンポーネントにアプリ共通の関数を付与していく
riot.install(introPlugin)

// index.htmlのid=rootタグにapp.riotをマウントすることでriotアプリを起動する
// (component()関数でDOM操作のための関数を作成してHTML要素を指定)
const mountApp = riot.component(App)
const root = document.getElementById('root')
if (root) {
  mountApp(root)
} else {
  throw new Error('not found riot root element')
}

// フロントエンドのグローバルエラーの監視を開始
subscribeGlobalError((msg) => {
  // エラーを拾った際、ダイアログでエラーを表示する
  triggerShowResult({ header: 'Unexpected Frontend Error', messages: [msg] })
})
// キャッチされなかったerrorを拾うためEventListenerを設定
window.addEventListener('error', (event) => {
  triggerGlobalError(event.error)
})
// Promiseの中でthrowされたエラーを拾うため、さらにunhandledrejectionにもEventListenerを設定
window.addEventListener('unhandledrejection', (event) => {
  triggerGlobalError(event.type)
})
