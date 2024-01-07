import i18n from '../../components/common/i18n.riot'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import LatestResult from '../latest-result.riot'
import { api } from '../../../api/api'

type SchemaSyncCheckLatestResult = {
  success: boolean
  content: string
}

interface Props {
  projectName: string
}

interface State {
  syncSchemaSetting: DfpropSchemasyncResult
  latestResult?: SchemaSyncCheckLatestResult
}

interface SchemaSyncCheck extends IntroRiotComponent<Props, State> {
  prepareComponents: () => Promise<void>

  // private
  updateContents: () => Promise<void>
}

export default withIntroTypes<SchemaSyncCheck>({
  components: {
    LatestResult,
  },
  state: {
    syncSchemaSetting: {},
    latestResult: undefined,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  async onMounted() {
    await this.prepareComponents()
  },

  async prepareComponents() {
    await this.updateContents()
  },

  async updateContents() {
    const projectName = this.props.projectName
    const syncSchemaSetting = await api.syncSchema(projectName)
    const latestResult = await api.latestResult(projectName, 'schemaSyncCheck').then((body) => {
      console.log(body)
      if (body) {
        return {
          success: body.fileName.includes('success'),
          content: body.content,
        }
      }
    })
    this.update({
      syncSchemaSetting,
      latestResult,
    })
  },
})
