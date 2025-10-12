import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
  syncSchemaSetting: DfpropSchemasyncResult
  onSettingSaved?: () => void
}

interface State {
  syncSchemaSetting: DfpropSchemasyncResult
  showModal: boolean
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

interface SchemaSyncCheckFormModal extends IntroRiotComponent<Props, State> {
  show(): void
  hide(): void
  modal(): SuModal
  onModalAction(action: string): void
  onHide(): void

  // private
  saveSettings(): Promise<void>
}

export default withIntroTypes<SchemaSyncCheckFormModal>({
  state: {
    syncSchemaSetting: {},
    showModal: false,
  },

  onBeforeUpdate(props: Props, state: State) {
    if (props.syncSchemaSetting) {
      this.state.syncSchemaSetting = props.syncSchemaSetting
    }
  },

  show() {
    this.update({ showModal: true })
  },

  hide() {
    this.update({ showModal: false })
  },

  modal(): SuModal {
    return {
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
  },

  onModalAction(action: string) {
    if (action === 'save') {
      this.saveSettings()
    }
  },

  async saveSettings() {
    const projectName = this.props.projectName
    const formData = this.state.syncSchemaSetting

    await api.editSyncSchema(projectName, formData)
    this.hide()

    // 親コンポーネントに保存完了を通知
    if (this.props.onSettingSaved) {
      this.props.onSettingSaved()
    }
  },

  onHide() {
    this.hide()
  },
})
