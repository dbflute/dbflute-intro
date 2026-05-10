import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import SchemaPolicyCheckStatementFormDocuementLink from './schema-policy-check-statement-form-docuement-link.riot'
import SchemaPolicyCheckStatementFormExpectedField from './schema-policy-check-statement-form-expected-field.riot'

type StatementMapType = 'tableMap' | 'columnMap'

/**
 * Expectedの一行 (subjectVerb + complement)。
 */
export type ExpectedField = {
  /** 一意キー */
  id: string
  /** 左ドロップダウンの値 (未選択は null) */
  subjectVerb: string | null
  /** 右テキスト入力の値 (NotApplicable: subjectVerbがnonIs型のとき null) */
  complement: string | null
}

/**
 * Expectedの複数行を結合する論理演算子。
 */
export type ExpectedCondition = 'and' | 'or'

interface Props {
  /** 対象とするマップ種別 (tableMap / columnMap) */
  formType: StatementMapType
  /** Expectedの行リスト */
  fields: ExpectedField[]
  /** 行を結合する論理演算子 */
  condition: ExpectedCondition
  /** 行追加ボタン押下時の通知 */
  onFieldAdd: () => void
  /** 行内容変更時の通知 */
  onFieldChange: (id: string, subjectVerb: string | null, complement: string | null) => void
  /** 行削除時の通知 */
  onFieldDelete: (id: string) => void
  /** 結合演算子(and/or)変更時の通知 */
  onConditionChange: (condition: ExpectedCondition) => void
}

interface State {
  /** sample領域を開いているかどうか */
  showSample: boolean
}

interface SchemaPolicyCheckStatementFormExpected extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted(): void
  onUpdated(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /** sample領域の開閉を切り替える。 */
  toggleSample(): void
  /** Expected行を追加する。 */
  onclickAddField(): void
  /** Expected行を削除する。(子コンポーネント由来) */
  onDeleteField(id: string): void
  /** 結合演算子のラジオボタン変更時の処理。 */
  onChangeCondition(): void

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  /** Expected行が削除可能か返す。1行のみのとき不可。 */
  isDeletable: () => boolean
  /** 結合演算子の表示が必要か返す。複数行のとき必要。 */
  needsCondition(): boolean
}

/** ラジオボタンの ref 名。 */
const REF_AND = 'isAnd'
const REF_OR = 'isOr'

export default withIntroTypes<SchemaPolicyCheckStatementFormExpected>({
  components: {
    SchemaPolicyCheckStatementFormDocuementLink,
    SchemaPolicyCheckStatementFormExpectedField,
  },

  state: {
    showSample: false,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted() {
    syncConditionRadio(this)
  },

  onUpdated() {
    // 親 (form) から condition が更新された時にラジオの状態を同期する
    syncConditionRadio(this)
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  toggleSample() {
    this.update({ showSample: !this.state.showSample })
  },

  onclickAddField() {
    this.props.onFieldAdd()
  },

  onDeleteField(id: string) {
    this.props.onFieldDelete(id)
  },

  onChangeCondition() {
    const isAnd = (this.$(`[ref=${REF_AND}]`) as HTMLInputElement | null)?.checked ?? false
    const isOr = (this.$(`[ref=${REF_OR}]`) as HTMLInputElement | null)?.checked ?? false
    if (isAnd) {
      this.props.onConditionChange('and')
    } else if (isOr) {
      this.props.onConditionChange('or')
    } else {
      this.props.onConditionChange('and')
    }
  },

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  isDeletable() {
    return this.props.fields.length > 1
  },

  needsCondition() {
    return this.props.fields.length > 1
  },
})

// 結合演算子のラジオボタン状態を props.condition に同期する
function syncConditionRadio(component: SchemaPolicyCheckStatementFormExpected) {
  const isAnd = component.$(`[ref=${REF_AND}]`) as HTMLInputElement | null
  const isOr = component.$(`[ref=${REF_OR}]`) as HTMLInputElement | null
  if (!isAnd || !isOr) {
    return
  }
  if (component.props.condition === 'or') {
    isAnd.checked = false
    isOr.checked = true
  } else {
    isAnd.checked = true
    isOr.checked = false
  }
}
