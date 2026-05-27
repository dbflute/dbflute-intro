import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { expectedSubjectItems } from './definition.js'

/**
 * Expected項目のSubjectVerb (左ドロップダウン) の選択肢。
 * `type === 'hasIs'` の場合は右側のcomplement入力が活性、'nonIs' の場合は非活性。
 */
type ExpectedSubjectItem = {
  label: string
  value: string | null
  type: 'hasIs' | 'nonIs'
}

interface Props {
  /** Expected fieldを一意に特定するキー */
  id: string
  /** 初期表示するSubjectVerb値 */
  subjectVerb: string | null
  /** 初期表示するcomplement値 */
  complement: string
  /** 削除アイコンを表示するかどうかを返す関数 */
  isDeletable: () => boolean
  /** 値が変更された時の通知 */
  onChange: (id: string, subjectVerb: string | null, complement: string | null) => void
  /** 削除アイコン押下時の通知 */
  onDelete: (id: string) => void
}

interface State {
  /** 現在選択中のSubjectVerb (complement入力の活性判定に利用) */
  currentSubjectVerb: string | null
}

interface SchemaPolicyCheckStatementFormExpectedField extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * SubjectVerbドロップダウンが変更された時の処理。
   * @param item 選択されたドロップダウン項目
   */
  onChangeSubjectVerb(item: ExpectedSubjectItem): void

  /**
   * Complementテキストフィールドが変更された時の処理。
   */
  onChangeComplement(): void

  /**
   * 削除アイコン押下時の処理。
   */
  onclickDelete(): void

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  /**
   * complement入力フィールドを活性化するかを返す。SubjectVerbがhasIs型のときに活性。
   */
  enableValueInput(): boolean

  /**
   * SubjectVerbドロップダウンの選択肢を返す。
   */
  subjectItems(): ExpectedSubjectItem[]

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * SubjectVerbがhasIs型かどうかを判定する。
   * @param subjectVerb 判定対象の値
   */
  isHasIs(subjectVerb: string | null): boolean
}

/** SubjectVerbドロップダウンの選択肢 (definition.jsより) */
const SUBJECT_ITEMS = expectedSubjectItems as ExpectedSubjectItem[]

export default withIntroTypes<SchemaPolicyCheckStatementFormExpectedField>({
  state: {
    currentSubjectVerb: null,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted() {
    this.state.currentSubjectVerb = this.props.subjectVerb
    const complementInput = this.$('[ref=complement]') as HTMLInputElement | null
    if (complementInput) {
      complementInput.value = this.props.complement
    }
    this.update()
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onChangeSubjectVerb(item) {
    const subjectVerb = item ? item.value : null
    this.state.currentSubjectVerb = subjectVerb
    const complement = this.isHasIs(subjectVerb) ? readComplementValue(this) : null
    this.update()
    this.props.onChange(this.props.id, subjectVerb, complement)
  },

  onChangeComplement() {
    const subjectVerb = this.state.currentSubjectVerb
    const complement = this.isHasIs(subjectVerb) ? readComplementValue(this) : null
    this.props.onChange(this.props.id, subjectVerb, complement)
  },

  onclickDelete() {
    this.props.onDelete(this.props.id)
  },

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  enableValueInput() {
    return this.isHasIs(this.state.currentSubjectVerb)
  },

  subjectItems() {
    return SUBJECT_ITEMS
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  isHasIs(subjectVerb) {
    const item = SUBJECT_ITEMS.find((it) => it.value === subjectVerb)
    return !!item && item.type === 'hasIs'
  },
})

// 現在のcomplement入力値を読み取る
function readComplementValue(component: SchemaPolicyCheckStatementFormExpectedField): string {
  const input = component.$('[ref=complement]') as HTMLInputElement | null
  return input ? input.value : ''
}
