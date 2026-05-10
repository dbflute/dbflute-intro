import * as riot from 'riot'

import 'semantic-ui-riot'
import introPlugin from '../src/static/app/app-plugin'
import { api } from '../src/static/app/api/api'
import ExSchemaPolicyCheck from '../src/static/app/pages/client/schema-policy-check/ex-schema-policy-check.riot'

// 全てのComponentで共通的に利用する処理を登録する
riot.install(introPlugin)
// jsdom 上では semantic-ui-riot の suConfirm はモーダル UI が動かず Promise が resolve しないため、
// テスト用に即時 resolve する関数で上書きする (suToast も最低限のスタブを入れておく)
riot.install((component) => {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  ;(component as any).suConfirm = () => Promise.resolve()
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  ;(component as any).suToast = () => undefined
  return component
})

const TEST_PROJECT = 'maihamadb'

const baseSchemaPolicyResult: DfpropSchemapolicyResult = {
  wholeMap: {
    themeList: [
      { name: 'sameColumnNameIfSameColumnAlias', description: '同じカラム別名なら...', typeCode: 'SAME_NAME', isActive: true },
      { name: 'uniqueTableAlias', description: 'テーブル別名はユニーク...', typeCode: 'UNIQUE_ALIAS', isActive: false },
    ],
  },
  tableMap: {
    themeList: [{ name: 'hasPK', description: 'PKを持っていること', typeCode: 'HAS_PK', isActive: true }],
    statementList: [
      'if tableName is suffix:_HISTORY then bad => ヒストリー表のサフィックスはNG',
      'if firstDate is after:2018/05/01 then hasPK',
    ],
  },
  columnMap: {
    themeList: [{ name: 'upperCaseBasis', description: '大文字基本', typeCode: 'UPPER_CASE', isActive: true }],
    statementList: [],
  },
}

const baseClientPropbase = {
  projectName: TEST_PROJECT,
  databaseCode: 'mysql',
  violatesSchemaPolicy: false,
  hasSyncCheckResultHtml: false,
} as ClientPropbaseResult

/**
 * テスト用に component を mount する補助関数。
 * 各テストで独立した DOM 領域に mount して、後始末する。
 */
function mountExSchemaPolicyCheck(): { unmount: () => void; container: HTMLElement } {
  const container = document.createElement('div')
  document.body.appendChild(container)
  const mount = riot.component(ExSchemaPolicyCheck)
  const component = mount(container, { projectName: TEST_PROJECT })
  return {
    container,
    unmount: () => {
      component.unmount()
      container.remove()
    },
  }
}

/** マウント後に Promise resolve と Riot 再描画を待つ。 */
async function flush() {
  // 数回 microtask を流して Promise.all 完了 → state 更新 → 再描画 を待つ
  for (let i = 0; i < 5; i++) {
    await Promise.resolve()
  }
}

beforeEach(() => {
  jest.spyOn(api, 'schemaPolicy').mockResolvedValue(baseSchemaPolicyResult)
  jest.spyOn(api, 'clientPropbase').mockResolvedValue(baseClientPropbase)
  jest.spyOn(api, 'latestResult').mockResolvedValue(null)
  jest.spyOn(api, 'editSchemaPolicy').mockResolvedValue(undefined)
  jest.spyOn(api, 'deleteSchemapolicyStatement').mockResolvedValue(undefined)
  jest.spyOn(api, 'moveSchemapolicyStatement').mockResolvedValue(undefined)
  jest.spyOn(api, 'registerSchemapolicyStatement').mockResolvedValue('built statement')
  jest.spyOn(api, 'getSchemapolicyStatementSubject').mockResolvedValue(['tableName', 'alias', 'firstDate'])
  jest.spyOn(api, 'task').mockResolvedValue({ success: true } as TaskExecuteResult)
})

afterEach(() => {
  jest.restoreAllMocks()
})

describe('SchemaPolicyCheck 画面', () => {
  // -----------------------------------------------------
  //                                        Initial Render
  //                                        --------------
  describe('初期描画', () => {
    it('schemaPolicy 取得後、3つのタブと各テーマが描画されること', async () => {
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const tabs = container.querySelectorAll('su-tab')
      expect(tabs).toHaveLength(3)

      // Whole Schema Policy のテーマがレンダリングされている
      const wholeThemes = container.textContent ?? ''
      expect(wholeThemes).toContain('sameColumnNameIfSameColumnAlias')
      expect(wholeThemes).toContain('uniqueTableAlias')

      unmount()
    })

    it('Table タブのステートメント一覧がレンダリングされること', async () => {
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const text = container.textContent ?? ''
      // statement 本体 (=> でコメントを除いた部分) が出ている
      expect(text).toContain('if tableName is suffix:_HISTORY then bad')
      // コメント部 (=> 以降) が出ている
      expect(text).toContain('ヒストリー表のサフィックスはNG')

      unmount()
    })

    it('登録フォームのトグルリンクは初期状態で "Add Statement" が表示されていること', async () => {
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const formWrappers = container.querySelectorAll('schema-policy-check-statement-form-wrapper')
      // Table と Column の2タブにラッパーがある
      expect(formWrappers.length).toBeGreaterThanOrEqual(1)
      // "Add Statement" リンクが存在し、"Hide Form" は存在しない
      const wrapperText = formWrappers[0].textContent ?? ''
      expect(wrapperText).toContain('Add Statement')
      expect(wrapperText).not.toContain('Hide Form')

      unmount()
    })
  })

  // -----------------------------------------------------
  //                                         Latest Result
  //                                         -------------
  describe('最新実行結果', () => {
    it('実行履歴がない場合は latest-result が描画されないこと', async () => {
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const latestResult = container.querySelector('latest-result')
      expect(latestResult).toBeNull()

      unmount()
    })

    it('成功ログがある場合は "Result: Success" (positive) が描画されること', async () => {
      jest.spyOn(api, 'latestResult').mockResolvedValue({
        fileName: 'dbflute_intro_doc_success_20260510.log',
        content: 'doc task ok',
      } as LogLatestResult)

      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const message = container.querySelector('.ui.positive.message')
      expect(message).not.toBeNull()
      expect(message?.textContent ?? '').toContain('Result: Success')

      unmount()
    })

    it('失敗ログがある場合は "Result: Failure" (negative) が描画されること', async () => {
      jest.spyOn(api, 'latestResult').mockResolvedValue({
        fileName: 'dbflute_intro_doc_failure_20260510.log',
        content: 'doc task ng',
      } as LogLatestResult)

      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const message = container.querySelector('.ui.negative.message')
      expect(message).not.toBeNull()
      expect(message?.textContent ?? '').toContain('Result: Failure')

      unmount()
    })

    it('違反ありのときは SchemaPolicy 結果 HTML へのリンクが描画されること', async () => {
      jest.spyOn(api, 'clientPropbase').mockResolvedValue({ ...baseClientPropbase, violatesSchemaPolicy: true })
      jest.spyOn(api, 'latestResult').mockResolvedValue({
        fileName: 'dbflute_intro_doc_failure_20260510.log',
        content: 'violation content',
      } as LogLatestResult)

      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const text = container.textContent ?? ''
      expect(text).toContain('Open your SchemaPolicyCheck result (HTML)')

      unmount()
    })

    it('違反なしのときは SchemaPolicy 結果 HTML へのリンクは描画されないこと', async () => {
      jest.spyOn(api, 'latestResult').mockResolvedValue({
        fileName: 'dbflute_intro_doc_success_20260510.log',
        content: 'ok',
      } as LogLatestResult)

      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const text = container.textContent ?? ''
      expect(text).not.toContain('Open your SchemaPolicyCheck result (HTML)')

      unmount()
    })
  })

  // -----------------------------------------------------
  //                                          Theme Toggle
  //                                          ------------
  describe('テーマトグル', () => {
    it('Whole タブのテーマチェックボックス変更で editSchemaPolicy が呼ばれること', async () => {
      const editSpy = jest.spyOn(api, 'editSchemaPolicy')
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      // 最初の su-checkbox をクリック (Whole の最初のテーマ)
      const checkbox = container.querySelector('su-checkbox') as HTMLElement | null
      expect(checkbox).not.toBeNull()
      // su-checkbox は内部で input を持つので click イベントを発火
      const input = checkbox?.querySelector('input[type=checkbox]') as HTMLInputElement | null
      input?.click()
      await flush()

      expect(editSpy).toHaveBeenCalledWith(
        TEST_PROJECT,
        expect.objectContaining({
          wholeMap: { themeList: [{ typeCode: 'SAME_NAME', isActive: false }] },
          tableMap: { themeList: [] },
          columnMap: { themeList: [] },
        })
      )

      unmount()
    })
  })

  // -----------------------------------------------------
  //                                      Statement Delete
  //                                      ----------------
  describe('ステートメント削除', () => {
    it('削除アイコンクリックで suConfirm 後に deleteSchemapolicyStatement が呼ばれること', async () => {
      const deleteSpy = jest.spyOn(api, 'deleteSchemapolicyStatement')
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      // 最初の delete アイコンをクリック (suConfirm はファイル冒頭の plugin で即 resolve される)
      const list = container.querySelector('schema-policy-check-statement-list')
      expect(list).not.toBeNull()
      const deleteIcon = list?.querySelector('.delete.link.icon') as HTMLElement | null
      expect(deleteIcon).not.toBeNull()
      deleteIcon?.click()
      await flush()

      expect(deleteSpy).toHaveBeenCalledWith(
        TEST_PROJECT,
        expect.objectContaining({
          mapType: 'tableMap',
          statement: 'if tableName is suffix:_HISTORY then bad => ヒストリー表のサフィックスはNG',
        })
      )

      unmount()
    })
  })

  // -----------------------------------------------------
  //                                           Form Toggle
  //                                           -----------
  describe('登録フォームの開閉', () => {
    it('"Add Statement" クリックでフォームが開き、リンクが "Hide Form" に切り替わること', async () => {
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const wrapper = container.querySelector('schema-policy-check-statement-form-wrapper') as HTMLElement
      // "Add Statement" リンクを取得 (a要素)
      const links = wrapper.querySelectorAll('a')
      const addLink = Array.from(links).find((a) => (a.textContent ?? '').trim() === 'Add Statement')
      expect(addLink).toBeDefined()
      addLink?.click()
      await flush()

      const linksAfter = wrapper.querySelectorAll('a')
      const hideLink = Array.from(linksAfter).find((a) => (a.textContent ?? '').trim() === 'Hide Form')
      expect(hideLink).toBeDefined()
      // 開いた後は form 本体が描画されている
      expect(wrapper.querySelector('schema-policy-check-statement-form')).not.toBeNull()

      unmount()
    })

    it('"Hide Form" クリックでフォームが閉じ、リンクが "Add Statement" に戻ること', async () => {
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const wrapper = container.querySelector('schema-policy-check-statement-form-wrapper') as HTMLElement
      // 一度開く
      const addLink = Array.from(wrapper.querySelectorAll('a')).find((a) => (a.textContent ?? '').trim() === 'Add Statement')
      addLink?.click()
      await flush()
      // 閉じる
      const hideLink = Array.from(wrapper.querySelectorAll('a')).find((a) => (a.textContent ?? '').trim() === 'Hide Form')
      hideLink?.click()
      await flush()

      // 再び "Add Statement" のみ
      const finalLinks = Array.from(wrapper.querySelectorAll('a')).map((a) => (a.textContent ?? '').trim())
      expect(finalLinks).toContain('Add Statement')
      expect(finalLinks).not.toContain('Hide Form')
      // form 本体は描画されていない
      expect(wrapper.querySelector('schema-policy-check-statement-form')).toBeNull()

      unmount()
    })
  })

  // -----------------------------------------------------
  //                                              Doc Task
  //                                              --------
  describe('SchemaPolicyCheck (doc task) 実行', () => {
    it('"Execute SchemaPolicyCheck" ボタンで api.task("doc") が呼ばれること', async () => {
      const taskSpy = jest.spyOn(api, 'task')
      const { container, unmount } = mountExSchemaPolicyCheck()
      await flush()

      const executeButton = Array.from(container.querySelectorAll('button')).find((b) =>
        (b.textContent ?? '').includes('Execute SchemaPolicyCheck')
      )
      expect(executeButton).toBeDefined()
      executeButton?.click()
      await flush()

      expect(taskSpy).toHaveBeenCalledWith(TEST_PROJECT, 'doc')

      unmount()
    })
  })
})
