import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  /** プロジェクト名 */
  projectName: string
  /** ディレクトリを開いた後の処理 */
  onCompleteOpenDir(inputFileName?: string): void
}

interface AlterCheckFixForm extends IntroRiotComponent<Props, never> {
  prepareAlterCheck(): void
}

export default withIntroTypes<AlterCheckFixForm>({
  prepareAlterCheck() {
    api.prepareAlterSql(this.props.projectName).then(() => {
      api.openAlterDir(this.props.projectName).then(() => {
        this.props.onCompleteOpenDir()
      })
    })
  },
})
