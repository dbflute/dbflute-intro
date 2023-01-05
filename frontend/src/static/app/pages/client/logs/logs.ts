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
}

interface Log extends IntroRiotComponent<Props, States> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultItem: { label: string; value: undefined }

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareLogs: () => void
  onLogSelect: () => void
}

export default withIntroTypes<Log>({
  state: {
    logDropDownItems: [],
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
    this.prepareLogs()
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * ログ情報表示部分を準備する。
   */
  prepareLogs() {
    // dropdown要素の表示順序はサーバーのレスポンスデータに依存
    const projectName = this.props.projectName
    api.logBeanList(projectName).then((body) => {
      this.state.logDropDownItems = [
        this.defaultItem,
        ...body.map((item) => ({
          label: item.fileName,
          value: item.content,
        })),
      ]
      this.update()
    })
  },

  onLogSelect() {
    this.update()
  },
})
