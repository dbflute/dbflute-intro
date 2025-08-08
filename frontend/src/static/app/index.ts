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

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// アプリのriot関連の共通コンポーネントをimportする
// (defaultでexportされてるものをAppという名前で扱って後にマウント)
//
// app.riot                // RiotアプリケーションのRootコンポーネント
//  |-app-router.riot      // それぞれの画面のコンポーネントをimportしてcomponent化
//  |  |
//  |  |-import Main from './pages/main/main.riot'              // 大分類の画面
//  |  |-import Client from './pages/client/client.riot'        // 大分類の画面
//  |  |  |-import ClientRouter from './pages/client/client-router.riot'
//  |  |     |
//  |  |     | // 中分類の画面たち
//  |  |     |-import ExDocuments from './pages/client/documents/ex-documents.riot'
//  |  |     |-import ExReplaceSchema from './pages/client/replace-schema/ex-replace-schema.riot'
//  |  |     |-import (その他、Clientの機能ごとの画面たち)
//  |  |
//  |  |-import Welcome from './pages/welcome/welcome.riot'     // 大分類の画面
//  |  |-import Create from './pages/create/create.riot'        // 大分類の画面
//  |  |
//  |  |    (各画面の.riot)
//  |  |         |-app-component-types.ts // as IntroRiotComponent, withIntroTypes
//  |  |            |-app-plugin.ts       // as DBFluteIntroPlugin
//  |  |
//  |  |-app-router.ts     // export const appRoutes(rootになる画面のpath設定), initialRoute
//  |  |  |-app-route.ts   // createRouting() called as export const
//  |  |-app-route.ts      // endRouting() called in onUnmounted()
//  |
//  |-app-router.ts        // onBeforeMount()で初期状態のときにWelcome画面を表示させるため
// _/_/_/_/_/_/_/_/
import App from './app.riot'

// アプリ内でsemantic-ui-riotとsemantic-uiのグローバルオブジェクトを使うために
// (初期化処理でui-riotの関数などがriotにinstallされる)
// (初期化だけで良いので受け取りのための変数は特になし)
import 'semantic-ui-riot'

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// 国際化対応の初期化処理を行うために
// (初期化だけで良いので受け取りのための変数はない)
//
// i18n.ts
//  |-import i18n_ja from '../../assets/i18n/locale-ja.json'
//  |-import i18n_en from '../../assets/i18n/locale-en.json'
// _/_/_/_/_/_/_/_/
import './shared/i18n'

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// アプリ独自のプラグイン関数をriotにinstall()するための関数
// app-plugin.ts
//  |-import { RiotComponent } from 'riot'
// _/_/_/_/_/_/_/_/
import introPlugin from './app-plugin'

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// グローバルエラーを監視するための関数
//
// app-events.ts
//  |-import observable from 'riot-observable'
// _/_/_/_/_/_/_/_/
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
