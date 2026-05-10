import Sortable from 'sortablejs'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

/**
 * statementを持つマップ種別。
 */
type StatementMapType = 'tableMap' | 'columnMap'

interface Props {
  /** 現在対象としているDBFluteクライアントのプロジェクト名 */
  projectName: string
  /** ステートメントが属するマップ種別 */
  mapType: StatementMapType
  /** 表示するステートメント文字列のリスト */
  statements: string[]
  /** 並び替え/削除がサーバーに反映された後に呼ばれるコールバック (再フェッチなどを期待) */
  onChanged: () => Promise<void> | void
}

interface State {
  /**
   * 画面表示用のステートメントのローカルコピー。
   * ドラッグ&ドロップによるDOM操作と Riot の再描画が衝突しないように保持する。
   * propsの statements が更新されたら同期される。
   */
  items: string[]

  /**
   * 最後に props から items に同期した時の props.statements の参照。
   * 次の onBeforeUpdate で props.statements が変わったかの差分検出に使う。
   * (this.props は onBeforeUpdate 時点で既に新しい値を指す可能性があるため、
   *  state 側にスナップショットを持ってこれと比較する)
   */
  syncedFromProps?: string[]

  /**
   * Sortable.js のインスタンス。onBeforeUnmount でリスナを破棄するために保持する。
   */
  sortable?: Sortable
}

interface SchemaPolicyCheckStatementList extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted(): void
  onBeforeUpdate(props: Props, state: State): void
  onBeforeUnmount(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * ステートメントを削除する。
   * @param statement 削除対象のステートメント文字列
   */
  onclickDelete(statement: string): Promise<void>

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  /**
   * ステートメント文字列にコメント部 (=>) があるかを判定する。
   */
  hasComment(statement: string): boolean

  /**
   * ステートメント文字列からコメント部を除いた本体を返す。
   */
  removeComment(statement: string): string

  /**
   * ステートメント文字列からコメント部のテキストを抜き出して返す。
   */
  extractComment(statement: string): string
}

/** ステートメント本文とコメントのセパレータ。 */
const COMMENT_SEPARATOR = '=>'

export default withIntroTypes<SchemaPolicyCheckStatementList>({
  state: {
    items: [],
    syncedFromProps: undefined,
    sortable: undefined,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted() {
    this.state.items = [...this.props.statements]
    this.state.syncedFromProps = this.props.statements
    this.update()
    // sortablejsを対象のDOMにbindしてドラッグ&ドロップ並び替えを有効化する。
    // onBeforeUnmount で破棄するためインスタンスを state に保持する。
    this.state.sortable = Sortable.create(this.$('[ref=items]') as HTMLElement, {
      animation: 150,
      ghostClass: 'sorted',
      dragClass: 'dragging',
      onEnd: async (event) => {
        const fromIndex = event.oldIndex
        const toIndex = event.newIndex
        if (fromIndex === undefined || toIndex === undefined || fromIndex === toIndex) {
          return
        }
        // ローカルの items を並び替えて Riot 再描画と DOM の不整合を防ぐ
        const items = [...this.state.items]
        const [moved] = items.splice(fromIndex, 1)
        items.splice(toIndex, 0, moved)
        this.state.items = items
        this.update()
        await api.moveSchemapolicyStatement(this.props.projectName, {
          mapType: this.props.mapType,
          fromIndex,
          toIndex,
        })
        await this.props.onChanged()
      },
    })
  },

  onBeforeUpdate(props, state) {
    // 親 (ex-schema-policy-check) の state.schemaPolicy が再フェッチで更新された時に
    // ローカル items も同期する。
    // 注意: Riot v7 ではこの時点で this.props は既に新しい props を指す可能性があるため、
    // state 側に保持したスナップショット (syncedFromProps) と比較する。
    if (props.statements !== state.syncedFromProps) {
      state.items = [...props.statements]
      state.syncedFromProps = props.statements
    }
  },

  onBeforeUnmount() {
    // sortablejs のリスナがリークしないよう、コンポーネント破棄時に明示的に destroy する
    this.state.sortable?.destroy()
    this.state.sortable = undefined
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  async onclickDelete(statement) {
    await this.suConfirm('Are you sure to delete this statement?')
    await api.deleteSchemapolicyStatement(this.props.projectName, {
      mapType: this.props.mapType,
      statement,
    })
    await this.props.onChanged()
  },

  // ===================================================================================
  //                                                                              Helper
  //                                                                              ======
  hasComment(statement) {
    return statement.includes(COMMENT_SEPARATOR)
  },

  removeComment(statement) {
    if (!this.hasComment(statement)) {
      return statement
    }
    return statement.split(COMMENT_SEPARATOR)[0]
  },

  extractComment(statement) {
    if (!this.hasComment(statement)) {
      return ''
    }
    return statement.split(COMMENT_SEPARATOR)[1].trimStart()
  },
})
