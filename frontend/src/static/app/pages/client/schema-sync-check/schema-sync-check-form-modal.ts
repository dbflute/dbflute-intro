import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
  show: boolean
  syncSchemaSetting: DfpropSchemasyncResult
  onModalHide?: () => void
}

interface State {
  show: boolean
  syncSchemaSetting: DfpropSchemasyncResult
}

type SuModalButton = {
  text: string
  action: string
  default?: boolean
  closable?: boolean
}

type SuModal = {
  header: string
  closable: boolean
  buttons: SuModalButton[]
}

const FORM_MODAL: SuModal = {
  header: 'Schema Sync Check Settings',
  closable: true,
  buttons: [
    {
      text: 'OK',
      action: 'save',
      default: true,
      closable: false,
    },
  ],
}

interface SchemaSyncCheckFormModal extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  modal(): SuModal

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                          ==========
  onBeforeMount(): void
  onBeforeUpdate(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                                 Modal
  //                                                 -----
  onHide(): void
  // -----------------------------------------------------
  //                                                  Form
  //                                                  ----
  onChangeUrl(e: InputEvent): void
  onChangeSchema(e: InputEvent): void
  onChangeUser(e: InputEvent): void
  onChangePassword(e: InputEvent): void
  onChangeIsSuppressCraftDiff(e: InputEvent): void
  saveSettings(): Promise<void>
}

export default withIntroTypes<SchemaSyncCheckFormModal>({
  state: {
    show: false,
    syncSchemaSetting: {},
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  modal(): SuModal {
    return FORM_MODAL
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                          ==========
  onBeforeMount() {
    this.state.syncSchemaSetting = this.props.syncSchemaSetting
  },

  onBeforeUpdate() {
    this.state.show = this.props.show
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                                 Modal
  //                                                 -----
  onHide() {
    this.update({ show: false })
    if (this.props.onModalHide) {
      this.props.onModalHide()
    }
  },

  // -----------------------------------------------------
  //                                                  Form
  //                                                  ----
  onChangeUrl(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangeUrl called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, url: value },
    })
  },

  onChangeSchema(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangeSchema called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, schema: value },
    })
  },

  onChangeUser(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangeUser called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, user: value },
    })
  },

  onChangePassword(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangePassword called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, password: value },
    })
  },

  onChangeIsSuppressCraftDiff(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    console.log('onChangeIsSuppressCraftDiff called:', checked)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, isSuppressCraftDiff: checked },
    })
  },

  async saveSettings() {
    const projectName = this.props.projectName
    const formData = this.state.syncSchemaSetting
    await api
      .editSyncSchema(projectName, formData)
      .then(() => {
        this.onHide()
      })
      .catch((_) => {
        // API Client で modal 出す以上のハンドリングはしない
      })
  },
})
