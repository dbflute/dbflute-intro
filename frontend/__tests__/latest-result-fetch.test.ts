import { api } from '../src/static/app/api/api'
import AlterCheck from '../src/static/app/pages/client/alter-check/alter-check'
import Documents from '../src/static/app/pages/client/documents/documents'
import ReplaceSchema from '../src/static/app/pages/client/replace-schema/replace-schema'
import SchemaPolicyCheck from '../src/static/app/pages/client/schema-policy-check/schema-policy-check'
import SchemaSyncCheck from '../src/static/app/pages/client/schema-sync-check/schema-sync-check'

const TEST_PROJECT = 'maihamadb'

const targets = [
  ['Documents', Documents, 'doc'],
  ['SchemaSyncCheck', SchemaSyncCheck, 'schemaSyncCheck'],
  ['SchemaPolicyCheck', SchemaPolicyCheck, 'doc'],
  ['ReplaceSchema', ReplaceSchema, 'replaceSchema'],
  ['AlterCheck', AlterCheck, 'alterCheck'],
] as const

describe.each(targets)('%s fetchLatestResult', (_name, component, task) => {
  afterEach(() => {
    jest.restoreAllMocks()
  })

  it('実行履歴がない場合はundefinedを返すこと', async () => {
    const findLatestTaskLog = jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue(null)

    const latestResult = await (component as any).fetchLatestResult.call({ props: { projectName: TEST_PROJECT } })

    expect(findLatestTaskLog).toHaveBeenCalledWith(TEST_PROJECT, task)
    expect(latestResult).toBeUndefined()
  })

  it('指定されたプロジェクトの最新実行結果を取得すること', async () => {
    const findLatestTaskLog = jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue(null)

    await (component as any).fetchLatestResult.call({ props: { projectName: TEST_PROJECT } }, 'seaquensedb')

    expect(findLatestTaskLog).toHaveBeenCalledWith('seaquensedb', task)
  })

  it('成功ログを共通の最新実行結果へ変換すること', async () => {
    jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue({
      fileName: 'dbflute_intro_task_success_20260901.log',
      content: 'task success',
    })

    const latestResult = await (component as any).fetchLatestResult.call({ props: { projectName: TEST_PROJECT } })

    expect(latestResult).toEqual({ success: true, content: 'task success' })
  })

  it('失敗ログを共通の最新実行結果へ変換すること', async () => {
    jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue({
      fileName: 'dbflute_intro_task_failure_20260901.log',
      content: 'task failure',
    })

    const latestResult = await (component as any).fetchLatestResult.call({ props: { projectName: TEST_PROJECT } })

    expect(latestResult).toEqual({ success: false, content: 'task failure' })
  })
})
