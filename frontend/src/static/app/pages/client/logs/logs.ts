import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
}
interface States {
  // label:fileName, value:ログの中身
  logDropDownItems: Array<{
    label: string
    value: string | undefined
  }>
  selectedLogValue: string
  client: any
}

interface Log extends IntroRiotComponent<Props, States> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultItem: { label: string; value: undefined }

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareSettings: () => void
  prepareLogs: () => void
  onLogSelect: () => void
}

export default withIntroTypes<Log>({
  state: {
    logDropDownItems: [],
    selectedLogValue: '',
    client: {},
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultItem: { label: '-', value: undefined },

  //===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  async onMounted() {
    this.prepareSettings()
    this.prepareLogs()
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * DBFluteクライアントの基本設定情報を準備する。
   */
  prepareSettings() {
    const projectName = this.props.projectName
    api.settings(projectName).then((body) => {
      this.state.client = body
      this.update()
    })
  },

  /**
   * ログ情報表示部分を準備する。
   */
  prepareLogs() {
    // dropdown要素の表示順序はサーバーのレスポンスデータに依存
    const projectName = this.props.projectName
    api.logBeanList(projectName).then((body) => {
      // TODO cabos dropdown Items という型があるはずなのでそれを利用する
      // TODO cabos default Item の扱いを統一する
      const logDropDownItems: Array<{ label: string; value: string | undefined }> = body.map((item) => ({
        label: item.fileName,
        value: item.content,
      }))
      logDropDownItems.unshift(this.defaultItem)
      this.state.logDropDownItems = logDropDownItems
      this.update()
    })
  },

  onLogSelect() {
    this.update()
  },
})
