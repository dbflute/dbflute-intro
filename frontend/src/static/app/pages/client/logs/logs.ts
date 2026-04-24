import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'
import { DropdownItem } from '../../../components/dropdown/dropdown'

/**
 * デフォルトで表示するドロップダウンの項目 (未選択時)
 */
const DEFAULT_DROPDOWN_ITEM: DropdownItem = {
  label: '-',
  value: '',
  default: true,
}

interface Props {
  /** DBFluteクライアントのプロジェクト名 e.g. maihamadb */
  projectName: string
}

interface State {
  /** ログ表示用ドロップダウンの項目一覧 */
  logDropDownItems: DropdownItem[]
}

interface Logs extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  onMounted(): Promise<void>

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * ドロップダウンでログを選択した時の処理。
   */
  onSelectLog: () => void
}

export default withIntroTypes<Logs>({
  state: {
    logDropDownItems: [DEFAULT_DROPDOWN_ITEM],
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    const logBeanList = await api.logBeanList(this.props.projectName)
    const logDropDownItems: DropdownItem[] = logBeanList.map((logBean) => ({
      label: logBean.fileName,
      value: logBean.content,
      default: false,
    }))
    this.update({
      logDropDownItems: [DEFAULT_DROPDOWN_ITEM, ...logDropDownItems],
    })
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onSelectLog() {
    this.update()
  },
})
