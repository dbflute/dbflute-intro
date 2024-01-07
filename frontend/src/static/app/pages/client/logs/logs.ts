import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
}
interface State {
  // label:fileName, value:ログの中身
  logDropDownItems: Array<{
    label: string
    value: string | undefined
  }>
}

interface Logs extends IntroRiotComponent<Props, State> {
  onMounted: () => void
  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareLogs: () => void
  onselectLog: () => void
}

export default withIntroTypes<Logs>({
  state: {
    logDropDownItems: [],
  },

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
        { label: '-', value: undefined },
        ...body.map((item) => ({
          label: item.fileName,
          value: item.content,
        })),
      ]
      this.update()
    })
  },

  onselectLog() {
    this.update()
  },
})
