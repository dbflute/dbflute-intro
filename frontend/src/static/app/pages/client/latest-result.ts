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

  /** 表示領域のヘッダーに表示するタイトル文字列 */
  headerTitle?: string // #thinking jflute ないかも項目だけど、.riotでそのまま使ってるけどOK？ (2026/04/10)

  resultTitle: string
  resultMessage?: string
  content?: string
  linkTitle?: string
  onClickLink?: () => void
}

interface State {
  loaded: boolean
  showContent: boolean
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface LatestResult extends IntroRiotComponent<Props, State> {
  onMounted(): void
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
