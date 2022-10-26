import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
  updateHandler(): void
}

interface AlterCheckFixForm extends IntroRiotComponent<Props, never> {
  prepareAlterCheck(): void
}

export default withIntroTypes<AlterCheckFixForm>({
  prepareAlterCheck() {
    api.prepareAlterSql(this.props.projectName).then(() => {
      api.openAlterDir(this.props.projectName).then(() => {
        this.props.updateHandler()
      })
    })
  },
})
