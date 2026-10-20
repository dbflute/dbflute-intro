import * as riot from 'riot'

import 'semantic-ui-riot'
import introPlugin from '../src/static/app/app-plugin'
import { api } from '../src/static/app/api/api'
import SchemaSyncCheckRiot from '../src/static/app/pages/client/schema-sync-check/schema-sync-check.riot'
import SchemaSyncCheck from '../src/static/app/pages/client/schema-sync-check/schema-sync-check'

riot.install(introPlugin)

describe('SchemaSyncCheckの最新実行結果', () => {
  afterEach(() => {
    jest.restoreAllMocks()
  })

  test.each([
    ['実行履歴がない場合', null, undefined],
    ['最新のタスクが成功している場合', { fileName: 'task_success.log', content: 'success log' }, { success: true, content: 'success log' }],
    [
      '最新のタスクが失敗している場合',
      { fileName: 'task_failure.log', content: 'failure log' },
      { success: false, content: 'failure log' },
    ],
  ])('%sは取得して画面 state に反映すること', async (_name, latestLog, latestResult) => {
    jest.spyOn(console, 'log').mockImplementation(() => undefined)
    jest.spyOn(api, 'findSchemaSyncDfprop').mockResolvedValue({})
    jest.spyOn(api, 'findClientPropbase').mockResolvedValue({
      hasSyncCheckResultHtml: false,
    } as ClientPropbaseResult)
    jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue(latestLog)
    const update = jest.fn()
    const context = {
      props: { projectName: 'maihamadb' },
      fetchLatestResult: (SchemaSyncCheck as any).fetchLatestResult,
      update,
    }

    await (SchemaSyncCheck as any).updateContents.call(context)

    expect(api.findLatestTaskLog).toHaveBeenCalledWith('maihamadb', 'schemaSyncCheck')
    expect(update).toHaveBeenCalledWith(
      expect.objectContaining({
        latestResult,
      }),
    )
  })

  it('実行履歴がない state では Latest Result を描画しない', () => {
    jest.spyOn(console, 'log').mockImplementation(() => undefined)
    jest.spyOn(api, 'findSchemaSyncDfprop').mockResolvedValue({})
    jest.spyOn(api, 'findClientPropbase').mockResolvedValue({
      hasSyncCheckResultHtml: false,
    } as ClientPropbaseResult)
    jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue(null)
    const container = document.createElement('div')
    document.body.appendChild(container)
    const mounted = riot.component(SchemaSyncCheckRiot)(container, { projectName: 'maihamadb' })

    mounted.update({ latestResult: undefined } as any)

    expect(container.querySelector('latest-result')).toBeNull()

    mounted.unmount()
    container.remove()
  })
})
