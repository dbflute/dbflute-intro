import { api } from '../../../api/api'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  /** プロジェクト名 */
  projectName: string
  /** AlterDDL作成完了後の処理 */
  onCompleteCreate(inputFileName?: string): void
}
interface State {
  /** 入力されたファイル名が不正であるか */
  invalidFileName: boolean
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface AlterCheckBeginForm extends IntroRiotComponent<Props, State> {
  /**
   * AlterCheck の alterディレクトリに、AlterDDLファイルを新規作成する。
   */
  createAlterSql(): void

  /**
   * 入力されたAlterDDLのファイル名をバリデートする。
   * @param ticketName ファイル名。チケット名が入ることを期待 (NotEmpty)
   * @return true:OK, false:NG
   */
  validate(ticketName: string): boolean

  /**
   * inputタグのstyle用classを定義する。
   * @return class属性の文字列 e.g. "error disabled" (EmptyAllowed)
   */
  inputClasses(): string
}

export default withIntroTypes<AlterCheckBeginForm>({
  state: {
    invalidFileName: false,
  },
  createAlterSql() {
    const ticketName = this.inputElementBy('[ref="alterNameInput"]').value
    if (!this.validate(ticketName)) {
      // ファイル名が不正の場合、何もせずに終了
      return
    }
    api.prepareAlterSql(this.props.projectName).then(() => {
      const alterFileName = 'alter-schema-' + ticketName + '.sql'
      api.createAlterSql(this.props.projectName, alterFileName).then(() => {
        this.props.onCompleteCreate(alterFileName) // 呼び元から渡された後続処理を実行
      })
    })
  },

  validate(ticketName: string): boolean {
    const invalidFileName = !ticketName || ticketName.trim() === '' // 空文字は許さない
    this.update({ invalidFileName })
    return !invalidFileName
  },

  inputClasses(): string {
    return this.classNames({ error: this.state.invalidFileName })
  },
})
