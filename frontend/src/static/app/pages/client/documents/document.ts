import i18n from '../../../components/common/i18n.riot'
import { api } from '../../../api/api'
import { appRoutes } from '../../../app-router'
// import { readFile } from '../../shared/io-utils'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { DropdownItem } from '../../../components/dropdown/dropdown'

interface State {}

interface Document extends IntroRiotComponent<never, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          =========
  client: ClientPropbaseResult

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  // prepareCurrentProject: () => void
  // prepareComponents: () => void
  // onRegisterModal: () => void
  // documentSettingModal: () => void
  // generateModal: () => void
  // showDocumentSettingModal: () => void
  // openSchemaHTML: () => void
  // openHistoryHTML: () => void
  // generateTask: () => void
}
export default withIntroTypes<Document>({
  components: {
    i18n,
  },
  // state: {
  // },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  client: {},
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    // this.prepareCurrentProject()
    //   // this.prepareComponents()
    //   // this.registerModalEvent()
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
})
