import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

interface Props {
  syncSchemaSetting: DfpropSchemasyncResult
}

interface State {
  syncSchemaSetting: DfpropSchemasyncResult
}

interface SchemaSyncCheckFormModal extends IntroRiotComponent<Props, State> {}

export default withIntroTypes<SchemaSyncCheckFormModal>({
  state: {
    syncSchemaSetting: {},
  },

  onMounted() {
    this.update({
      syncSchemaSetting: this.props.syncSchemaSetting,
    })
  },
})
