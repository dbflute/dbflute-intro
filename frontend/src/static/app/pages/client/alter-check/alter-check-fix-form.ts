import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  /** プロジェクト名 */
  projectName: string
  /** ディレクトリを開いた後の処理 */
  onCompleteOpenDir(inputFileName?: string): void
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface AlterCheckFixForm extends IntroRiotComponent<Props, never> {
  /**
   * 既存AlterDDLを修正する準備をする。(既存AlterDDLをalterディレクトリの復元する)
   */
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
