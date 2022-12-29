import { api } from '../../../api/api'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

interface Props {
  projectName: string
  onComplete(inputFileName?: string): void
}
interface State {
  invalidFileName: boolean
}

interface AlterCheckBeginForm extends IntroRiotComponent<Props, State> {
  createAlterSql(): void
  validate(ticketName: string): boolean
}

export default withIntroTypes<AlterCheckBeginForm>({
  state: {
    invalidFileName: false,
  },
  /**
   * マウント前に実行される処理
   */
  onMounted() {
    this.state = {
      invalidFileName: false,
    }
  },
  /**
   * AlterDDLファイルを作成します
   */
  createAlterSql() {
    const ticketName = this.inputElementBy('[ref="alterNameInput"]').value
    // ファイル名が不正の場合、何もせずに終了
    if (this.validate(ticketName)) {
      return
    }
    api.prepareAlterSql(this.props.projectName).then(() => {
      const alterFileName = 'alter-schema-' + ticketName + '.sql'
      api.createAlterSql(this.props.projectName, alterFileName).then(() => {
        // 呼び元から渡された後続処理を実行
        this.props.onComplete(alterFileName)
      })
    })
  },
  /**
   * AlterDDLのファイル名をバリデートします
   * @param {string} ticketName ファイル名。チケット名が入ることを期待 (NotEmpty)
   * @return {boolean} true:OK, false:NG
   */
  validate(ticketName: string): boolean {
    // 空文字は許さない
    this.state.invalidFileName = !ticketName || ticketName === ''
    return this.state.invalidFileName
  },
})
