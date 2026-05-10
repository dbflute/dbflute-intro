import { v4 as uuid } from 'uuid'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'
import SchemaPolicyCheckStatementFormExpected from './schema-policy-check-statement-form-expected.riot'
import { ExpectedCondition, ExpectedField } from './schema-policy-check-statement-form-expected'
import { DropdownItem } from '../../../components/dropdown/dropdown'

type StatementMapType = 'tableMap' | 'columnMap'

/**
 * Subject (左ドロップダウン) 用のドロップダウン項目。
 */
type SubjectDropdownItem = {
  label: string
  value: string | null
  default?: boolean
}

interface Props {
  /** 現在対象としているDBFluteクライアントのプロジェクト名 */
  projectName: string
  /** 対象とするマップ種別 (tableMap / columnMap) */
  mapType: StatementMapType
  /** 登録成功時に親へ通知 (フォーム閉じや再フェッチを期待) */
  onRegisterSuccess: () => Promise<void> | void
}

interface State {
  /** Subjectドロップダウンの選択肢 */
  subjectDropdownItems: SubjectDropdownItem[]
  /** Subjectドロップダウンで選択されている値 */
  subject: string
  /** Conditionの入力欄リスト (テキスト値を保持) */
  conditions: string[]
  /** Conditionの結合演算子 ('and' or 'or') */
  conditionMode: 'and' | 'or'
  /** Expectedの行リスト */
  expectedFields: ExpectedField[]
  /** Expectedの結合演算子 */
  expectedCondition: ExpectedCondition
  /** Supplementary Comment テキスト入力の値 */
  comment: string
  /** Subject用ヘルプ領域を開いているかどうか */
  showSubjectHelp: boolean
  /** Condition用ヘルプ領域を開いているかどうか */
  showConditionHelp: boolean
  /** マウント完了フラグ (Previewの初期表示判定に利用) */
  mounted: boolean
}

interface SchemaPolicyCheckStatementForm extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted(): Promise<void>

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /** Subjectドロップダウン変更時の処理。 */
  onChangeSubject(item: DropdownItem): void
  /** Conditionの入力フィールド変更時の処理。 */
  onChangeCondition(index: number, event: Event): void
  /** Conditionの入力フィールドを追加する。 */
  onclickAddCondition(): void
  /** Conditionの入力フィールドを削除する。 */
  onclickDeleteCondition(index: number): void
  /** Conditionの結合演算子 (and/or) ラジオボタン変更時の処理。 */
  onChangeConditionMode(mode: 'and' | 'or'): void
  /** Expected行を追加する。 */
  onAddExpectedField(): void
  /** Expected行内容変更時の処理。 */
  onChangeExpectedField(id: string, subjectVerb: string | null, complement: string | null): void
  /** Expected行削除時の処理。 */
  onDeleteExpectedField(id: string): void
  /** Expected結合演算子変更時の処理。 */
  onChangeExpectedCondition(condition: ExpectedCondition): void
  /** Supplementary Comment 入力変更時の処理。 */
  onChangeComment(event: Event): void
  /** Subject用のヘルプ領域を開閉する。 */
  toggleSubjectHelp(): void
  /** Condition用のヘルプ領域を開閉する。 */
  toggleConditionHelp(): void
  /** ステートメント登録ボタン押下時の処理。 */
  onclickRegister(): Promise<void>

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  /** Previewの文字列を組み立てる。 */
  buildPreview(): string
}

const DEFAULT_SUBJECT_ITEM: SubjectDropdownItem = { label: 'Select subject', value: null, default: true }

/**
 * 初期Expectedフィールドを生成する。
 */
const buildEmptyExpectedField = (): ExpectedField => ({
  id: uuid(),
  subjectVerb: null,
  complement: '',
})

/**
 * フォーム入力に関するStateの初期値を返す。
 * (登録成功後に入力欄をリセットする際にも使う)
 */
const buildResetState = (): Pick<
  State,
  'subject' | 'conditions' | 'conditionMode' | 'expectedFields' | 'expectedCondition' | 'comment' | 'showSubjectHelp' | 'showConditionHelp'
> => ({
  subject: '',
  conditions: [''],
  conditionMode: 'and',
  expectedFields: [buildEmptyExpectedField()],
  expectedCondition: 'and',
  comment: '',
  showSubjectHelp: false,
  showConditionHelp: false,
})

/**
 * registerSchemapolicyStatement を呼ぶ際のリクエストボディを state から構築する。
 */
function buildBody(state: State, mapType: StatementMapType): DfpropSchemapolicyStatementRegisterBody {
  const conditions = state.conditions.filter((v) => v !== '')
  const expecteds = state.expectedFields
    .filter((f) => !!f.subjectVerb)
    .map((f) => (f.complement ? `${f.subjectVerb} ${f.complement}` : `${f.subjectVerb}`))
  return {
    type: mapType,
    subject: state.subject,
    condition: {
      operator: state.conditionMode,
      conditions,
    },
    expected: {
      operator: state.expectedCondition,
      expected: expecteds,
    },
    comment: state.comment,
  }
}

export default withIntroTypes<SchemaPolicyCheckStatementForm>({
  components: {
    SchemaPolicyCheckStatementFormExpected,
  },

  state: {
    subjectDropdownItems: [DEFAULT_SUBJECT_ITEM],
    ...buildResetState(),
    mounted: false,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    const subjects = await api.getSchemapolicyStatementSubject(this.props.mapType)
    const items: SubjectDropdownItem[] = subjects.map((s) => ({ label: s, value: s }))
    this.update({
      subjectDropdownItems: [DEFAULT_SUBJECT_ITEM].concat(items),
      mounted: true,
    })
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onChangeSubject(item) {
    this.update({ subject: item && item.value ? item.value : '' })
  },

  onChangeCondition(index, event) {
    const value = (event.target as HTMLInputElement).value
    const conditions = [...this.state.conditions]
    conditions[index] = value
    this.update({ conditions })
  },

  onclickAddCondition() {
    this.update({ conditions: [...this.state.conditions, ''] })
  },

  onclickDeleteCondition(index: number) {
    const conditions = [...this.state.conditions]
    conditions.splice(index, 1)
    this.update({ conditions })
  },

  onChangeConditionMode(mode) {
    this.update({ conditionMode: mode })
  },

  onAddExpectedField() {
    this.update({
      expectedFields: [...this.state.expectedFields, buildEmptyExpectedField()],
    })
  },

  onChangeExpectedField(id, subjectVerb, complement) {
    const expectedFields = this.state.expectedFields.map((field) =>
      field.id === id ? { ...field, subjectVerb, complement: complement ?? '' } : field
    )
    this.update({ expectedFields })
  },

  onDeleteExpectedField(id) {
    const expectedFields = this.state.expectedFields.filter((field) => field.id !== id)
    this.update({ expectedFields })
  },

  onChangeExpectedCondition(condition) {
    this.update({ expectedCondition: condition })
  },

  onChangeComment(event) {
    this.update({ comment: (event.target as HTMLInputElement).value })
  },

  toggleSubjectHelp() {
    this.update({ showSubjectHelp: !this.state.showSubjectHelp })
  },

  toggleConditionHelp() {
    this.update({ showConditionHelp: !this.state.showConditionHelp })
  },

  async onclickRegister() {
    const body = buildBody(this.state, this.props.mapType)
    await api.registerSchemapolicyStatement(this.props.projectName, body)
    // 入力欄を初期状態に戻す (state を真値にしているので update だけで DOM もクリアされる)
    this.update(buildResetState())
    await this.props.onRegisterSuccess()
  },

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  buildPreview() {
    if (!this.state.mounted) {
      return 'if <Subject> is <Condition> then <Expected> => <Supplementary Comment>'
    }
    const subject = this.state.subject || '<Subject>'
    const conditions = this.state.conditions.filter((v) => v !== '')
    const operator = this.state.conditionMode === 'or' ? ' or ' : ' and '
    const conditionsStr = conditions.length > 0 ? conditions.join(operator) : '<Condition>'
    const expecteds = this.state.expectedFields
      .filter((f) => !!f.subjectVerb)
      .map((f) => (f.complement ? `${f.subjectVerb} ${f.complement}` : `${f.subjectVerb}`))
    const expectedOperator = ` ${this.state.expectedCondition} `
    const expectedsStr = expecteds.length > 0 ? expecteds.join(expectedOperator) : '<Expected>'
    const commentText = this.state.comment || '<Supplementary Comment>'
    return `if ${subject} is ${conditionsStr} then ${expectedsStr} => ${commentText}`
  },
})
