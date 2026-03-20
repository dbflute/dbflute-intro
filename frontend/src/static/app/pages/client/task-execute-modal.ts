import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'

// #thinking jflute None はどういう状態を想定しているものなのか？モーダルを出さない便宜上のデフォルトの状態？ (2026/03/20)
// task-execute-modalタグは常に評価されるから、最初の画面描画時などタスク実行してない時はNoneで何も起きないようにしている？
/**
 * DBFluteタスク実行ステータス。
 */
export type TaskExecuteStatus = 'None' | 'Executing' | 'Completed'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

/**
 * タスク実行結果のメッセージなどタグ引数で受け取る。
 */
interface Props {
  /** モーダルに表示するIntroユーザー向けメッセージ。 */
  message: string

  /** DBFluteタスク実行ステータス。 */
  status: TaskExecuteStatus

  /** モーダルを閉じた時の処理のフック。呼び出し側が好きな処理を実行できるように。 (EmptyAllowed: 好きな処理なければ) */
  onModalHide?: () => void
}

// #thinking jflute onBeforeUpdate()にてstatusをPropsから受け取ってchangeさせてるけど、Propsのstatusを全部直接使うじゃダメなのかな？ (2026/03/20)
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

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface TaskExecuteModal extends IntroRiotComponent<Props, State> {
  onBeforeUpdate(): void
  show(): boolean
  modal(): SuModal | undefined
  onHide(): void
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

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
    if (this.props.onModalHide) {
      this.props.onModalHide()
    }
  },
})
