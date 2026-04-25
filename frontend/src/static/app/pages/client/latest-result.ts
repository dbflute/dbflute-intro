import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import Raw from '../../components/common/raw.riot'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  /** 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb */
  projectName: string

  /** その直近のDBFluteタスクの名前 */
  task: string // #thinking jflute unusedに見えるが、いつか使うかもで念のため受けってる？ (2026/04/10)

  /** その直近のDBFluteタスク実行が "成功" で終了したか？ */
  success: boolean

  /** 表示領域のヘッダー (タイトル表示) を表示するかどうか？ */
  showHeader: boolean

  // #thinking jflute 使ってる人がいないような？ (2026/04/25)
  /** 表示領域のヘッダーに表示するタイトル文字列 */
  headerTitle?: string

  /** DBFluteタスク実行結果を表現するタイトル e.g. Execution Result: Success */
  resultTitle: string

  // #thinking jflute 使ってる人がいないような？あったらあったで良いと思うところだが... (2026/04/25)
  /** 実行結果タイトルの下に表示される結果メッセージ (エラーに対する短文の案内とか!?) */
  resultMessage?: string

  /** 実行結果の具体的な内容いっぱい (エラーログそのまんまなど) e.g. "[df-replace-schema] /* * ..." */
  content?: string

  // #thinking jflute これまた使ってる人がいないような？ (2026/04/25)
  /** エラー時案内のリンクのタイトル (一個前の画面に戻るとか、ドキュメントページを表示とか) */
  linkTitle?: string

  /** エラー時案内のリンクの押下イベント (一個前の画面に戻るとか、ドキュメントページを表示とか) */
  onClickLink?: () => void
}

interface State {
  // #thinking jflute でも、利用側でif書いて制御してるので、この制御使われてるだろうか？ (2026/04/25)
  /** このlatest-result自体の表示領域を表示するかどうか？ */
  loaded: boolean

  /** 大量メッセージのcontent領域を表示するかどうか？ (toggleでhide/show制御するためのもの) */
  showContent: boolean
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface LatestResult extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  onMounted(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * 大量メッセージのcontent領域を表示on/offを反転させる。
   */
  toggleLatestResult(): void
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

export default withIntroTypes<LatestResult>({
  components: {
    Raw,
  },
  state: {
    loaded: false,
    showContent: false,
  },
  onMounted() {
    this.state.loaded = true
    this.update()
  },
  toggleLatestResult() {
    this.state.showContent = !this.state.showContent
    this.update()
  },
})
