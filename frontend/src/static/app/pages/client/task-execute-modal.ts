import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'

export type TaskExecuteStatus = 'None' | 'Executing' | 'Completed'

interface Props {
  message: string
  status: TaskExecuteStatus
}

interface State {
  status: TaskExecuteStatus
}

type SuModalButton = {
  text: string
  default: boolean
}

type SuModal = {
  closable: boolean
  buttons: SuModalButton[]
}

const COMPLETED_MODAL: SuModal = {
  closable: true,
  buttons: [
    {
      text: 'CLOSE',
      default: true,
    },
  ],
}

const EXECUTING_MODAL: SuModal = {
  closable: false,
  buttons: [],
}

interface TaskExecuteModal extends IntroRiotComponent<Props, State> {
  show(): boolean
  modal(): SuModal | undefined
  onHide(): void
}

export default withIntroTypes<TaskExecuteModal>({
  state: {
    status: 'None',
  },
  onBeforeUpdate() {
    this.state.status = this.props.status
  },
  show(): boolean {
    return this.state.status !== 'None'
  },
  modal(): SuModal | undefined {
    switch (this.state.status) {
      case 'None':
        return undefined
      case 'Executing':
        return EXECUTING_MODAL
      case 'Completed':
        return COMPLETED_MODAL
    }
  },
  onHide() {
    this.state.status = 'None'
    this.update()
  },
})
