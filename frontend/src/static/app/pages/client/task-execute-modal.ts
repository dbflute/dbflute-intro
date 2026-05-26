import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'

// done jflute None はどういう状態を想定しているものなのか？モーダルを出さない便宜上のデフォルトの状態？ (2026/03/20)
// task-execute-modalタグは常に評価されるから、最初の画面描画時などタスク実行してない時はNoneで何も起きないようにしている？
// → という解釈で良いと思う (2026/04/10)
/**
 * DBFluteタスク実行ステータス。
 */
export type TaskExecuteStatus = 'None' | 'Executing' | 'Completed' | 'Error'

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

  /** DBFluteタスク実行ステータス。実際に実行した結果だったり、画面再初期化のためにNoneだったり。 */
  status: TaskExecuteStatus

  /** モーダルを閉じた時の処理のフック。呼び出し側が好きな処理を実行できるように。 (EmptyAllowed: 好きな処理なければ) */
  onModalHide?: () => void
}

interface State {
  /**
   * DBFluteタスク実行ステータス。
   * コンポーネント更新前に Props の status の値を引き継ぐ。
   * モーダル表示中は実際に実行した結果で、初期状態や隠れているときはNoneになる。
   */
  status: TaskExecuteStatus
}

/**
 * モーダル上のボタンオブジェクト
 */
type SuModalButton = {
  /** ボタン表示名 */
  text: string
  /** デフォルトでフォーカスが当たってるどうか？ (Enterですぐに押せるかどうか？でいいのかな？) */
  default: boolean
}

/**
 * モーダル自体のオブジェクト
 */
type SuModal = {
  /** モーダルダイアログをUIで閉じることができるかどうか？ */
  closable: boolean
  /** モーダル上のボタンオブジェクトたち (EmptyAllowed) */
  buttons: SuModalButton[]
  /** モーダルヘッダーに表示するタイトル。 */
  header?: string
}

/** 完了を示すモーダルダイアログ。su-modalに引き渡すオブジェクト。 */
const COMPLETED_MODAL: SuModal = {
  closable: true,
  buttons: [
    {
      text: 'CLOSE',
      default: true,
    },
  ],
}

/** 例外発生を示すモーダルダイアログ。su-modalに引き渡すオブジェクト。 */
const ERROR_MODAL: SuModal = {
  closable: true,
  header: 'Unexpected Error',
  buttons: [
    {
      text: 'CLOSE',
      default: true,
    },
  ],
}

/** 実行中を示すモーダルダイアログ。su-modalに引き渡すオブジェクト。 */
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
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onBeforeUpdate(): void // コンポーネントの更新前に呼ばれる

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * su-modal の show 相当。
   * @return モーダルを表示するかどうか？
   */
  show(): boolean

  /**
   * モーダル全体の見た目に利用するclass属性値。
   * @return 表示用のclass属性値
   */
  modalClass(): string

  /**
   * su-modal の modal 相当。
   * @return 表示するモーダルオブジェクト (非表示 if undefined)
   */
  modal(): SuModal | undefined

  /**
   * su-modal の onhide 相当。モーダルが隠れた時の処理。
   */
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

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onBeforeUpdate() {
    this.state.status = this.props.status
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  show(): boolean {
    return this.state.status !== 'None'
  },
  modalClass(): string {
    return this.state.status === 'Error' ? 'architrave task-execute-error-modal' : ''
  },
  modal(): SuModal | undefined {
    switch (this.state.status) {
      case 'None':
        return undefined
      case 'Executing':
        return EXECUTING_MODAL
      case 'Completed':
        return COMPLETED_MODAL
      case 'Error':
        return ERROR_MODAL
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
