import { api } from '../src/static/app/api/api'
import ReplaceSchema from '../src/static/app/pages/client/replace-schema/replace-schema'

describe('ReplaceSchemaタスクの実行', () => {
  afterEach(() => {
    jest.restoreAllMocks()
  })

  it('タスク成功後に最新結果と実行状態を一度に反映すること', async () => {
    jest.spyOn(api, 'executeTask').mockResolvedValue({ success: true } as TaskExecuteResult)
    const update = jest.fn()
    const fetchLatestResult = jest.fn().mockResolvedValue({ success: true, content: 'latest log' })

    await (ReplaceSchema as any).replaceSchema.call({ fetchLatestResult, update }, 'maihamadb')

    expect(fetchLatestResult).toHaveBeenCalledWith('maihamadb')
    expect(update).toHaveBeenCalledTimes(1)
    expect(update).toHaveBeenCalledWith({
      executeStatus: 'Completed',
      executeResultMessage: 'Success',
      latestResult: { success: true, content: 'latest log' },
    })
  })

  it('タスク失敗結果の後も最新結果を再取得すること', async () => {
    jest.spyOn(api, 'executeTask').mockResolvedValue({ success: false } as TaskExecuteResult)
    const update = jest.fn()
    const fetchLatestResult = jest.fn().mockResolvedValue({ success: false, content: 'failure log' })

    await (ReplaceSchema as any).replaceSchema.call({ fetchLatestResult, update }, 'maihamadb')

    expect(fetchLatestResult).toHaveBeenCalledWith('maihamadb')
    expect(update).toHaveBeenCalledWith({
      executeStatus: 'Completed',
      executeResultMessage: 'Failure',
      latestResult: { success: false, content: 'failure log' },
    })
  })

  it('タスクAPI例外の後も最新結果を再取得すること', async () => {
    jest.spyOn(console, 'error').mockImplementation(() => undefined)
    jest.spyOn(api, 'executeTask').mockRejectedValue(new Error('network error'))
    const update = jest.fn()
    const fetchLatestResult = jest.fn().mockResolvedValue(undefined)

    await (ReplaceSchema as any).replaceSchema.call({ fetchLatestResult, update }, 'maihamadb')

    expect(fetchLatestResult).toHaveBeenCalledWith('maihamadb')
    expect(update).toHaveBeenCalledWith({ executeStatus: 'None', latestResult: undefined })
  })
})

describe('ReplaceSchemaの初期表示', () => {
  it('設定の初期化に失敗しても取得済みの最新実行結果を反映すること', async () => {
    const update = jest.fn()
    const context = {
      props: { projectName: 'maihamadb' },
      prepareSettings: jest.fn().mockRejectedValue(new Error('settings error')),
      preparePlaysql: jest.fn().mockResolvedValue(undefined),
      fetchLatestResult: jest.fn().mockResolvedValue({ success: true, content: 'latest log' }),
      update,
    }

    await expect((ReplaceSchema as any).onMounted.call(context)).rejects.toThrow('settings error')
    await Promise.resolve()

    expect(update).toHaveBeenCalledWith({ latestResult: { success: true, content: 'latest log' } })
  })

  it('PlaySQLの初期化に失敗しても取得済みの最新実行結果を反映すること', async () => {
    const update = jest.fn()
    const context = {
      props: { projectName: 'maihamadb' },
      prepareSettings: jest.fn().mockResolvedValue(undefined),
      preparePlaysql: jest.fn().mockRejectedValue(new Error('playsql error')),
      fetchLatestResult: jest.fn().mockResolvedValue({ success: false, content: 'failure log' }),
      update,
    }

    await expect((ReplaceSchema as any).onMounted.call(context)).rejects.toThrow('playsql error')
    await Promise.resolve()

    expect(update).toHaveBeenCalledWith({ latestResult: { success: false, content: 'failure log' } })
  })
})
