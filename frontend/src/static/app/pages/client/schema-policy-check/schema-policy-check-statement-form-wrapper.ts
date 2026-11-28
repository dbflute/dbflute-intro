import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import SchemaPolicyCheckStatementForm from './schema-policy-check-statement-form.riot'

type StatementMapType = 'tableMap' | 'columnMap'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  /** 現在対象としているDBFluteクライアントのプロジェクト名 */
  projectName: string
  /** 対象とするマップ種別 (tableMap / columnMap) */
  formType: StatementMapType
  /** 登録成功時に親へ通知 (一覧再フェッチを期待) */
  onRegisterSuccess: () => Promise<void> | void
}

interface State {
  /** フォームを表示中かどうか */
  showsForm: boolean
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface SchemaPolicyCheckStatementFormWrapper extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /** フォームの開閉を切り替える。開いた時はフォーム位置までスクロールする。 */
  toggleForm(): void
  /** 子フォームから登録成功通知を受けた時の処理。フォームを閉じてトーストを表示し親へ通知。 */
  onRegisterSuccess(): Promise<void>
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

export default withIntroTypes<SchemaPolicyCheckStatementFormWrapper>({
  components: {
    SchemaPolicyCheckStatementForm,
  },

  state: {
    showsForm: false,
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  toggleForm() {
    const next = !this.state.showsForm
    this.update({ showsForm: next })
    if (next) {
      // 開いた時はフォーム位置までスクロールする。
      // this.$() は DOM 要素を返すので、子コンポーネント内の <form> 要素を直接取得して
      // scrollIntoView を呼ぶ (Riot v7 では子コンポーネントのインスタンスメソッドは
      // ここから呼べない)。DOM 更新完了を待つため setTimeout(0) でキューイング。
      setTimeout(() => {
        const formEl = this.$('schema-policy-check-statement-form form') as HTMLElement | null
        formEl?.scrollIntoView({ behavior: 'smooth' })
      }, 0)
    }
  },

  async onRegisterSuccess() {
    this.update({ showsForm: false })
    this.successToast({
      title: 'Create statement completed',
      message: 'statement was successfully created!!',
    })
    await this.props.onRegisterSuccess()
  },
})
