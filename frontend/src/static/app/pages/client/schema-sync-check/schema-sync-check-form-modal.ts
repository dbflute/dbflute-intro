import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
  syncSchemaSetting: DfpropSchemasyncResult
  showModal: boolean
  onSettingSaved?: () => void
}

interface State {
  syncSchemaSetting: DfpropSchemasyncResult
}

type SuModalButton = {
  text: string
  action: string
  default?: boolean
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
    },
  ],
}

interface SchemaSyncCheckFormModal extends IntroRiotComponent<Props, State> {
  show(): boolean
  modal(): SuModal
  onBeforeMount(): void
}

export default withIntroTypes<SchemaSyncCheckFormModal>({
  state: {
    syncSchemaSetting: {},
  },

  onBeforeMount() {
    this.state.syncSchemaSetting = this.props.syncSchemaSetting
  },

  show(): boolean {
    return this.props.showModal
  },

  modal(): SuModal {
    return FORM_MODAL
  },
})
