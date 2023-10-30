import i18n from '../../components/common/i18n.riot'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
}

interface State {
  syncSchemaSetting: DfpropSchemasyncResult
}

interface SchemaSyncCheck extends IntroRiotComponent<Props, State> {
  prepareSyncSchemaSetting: () => Promise<void>
}

export default withIntroTypes<SchemaSyncCheck>({
  //===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  async onMounted() {
    await this.prepareSyncSchemaSetting()
  },

  async prepareSyncSchemaSetting() {
    const projectName = this.props.projectName
    api.syncSchema(projectName).then
  },
})
