import * as riot from 'riot'

import 'semantic-ui-riot'
import introPlugin from '../src/static/app/app-plugin'
import { api } from '../src/static/app/api/api'
import DocumentsRiot from '../src/static/app/pages/client/documents/documents.riot'
import Documents from '../src/static/app/pages/client/documents/documents'

riot.install(introPlugin)

describe('Documentsの最新実行結果', () => {
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
    jest.spyOn(api, 'findDocumentDfprop').mockResolvedValue({})
    jest.spyOn(api, 'findClientPropbase').mockResolvedValue({
      hasSchemaHtml: false,
      hasHistoryHtml: false,
    } as ClientPropbaseResult)
    jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue(latestLog)
    const update = jest.fn()
    const context = {
      props: { projectName: 'maihamadb' },
      fetchLatestResult: (Documents as any).fetchLatestResult,
      update,
    }

    await (Documents as any).updateContents.call(context)

    expect(api.findLatestTaskLog).toHaveBeenCalledWith('maihamadb', 'doc')
    expect(update).toHaveBeenCalledWith(
      expect.objectContaining({
        latestResult,
      }),
    )
  })

  it('実行履歴がない state では Latest Result を描画しない', () => {
    jest.spyOn(api, 'findDocumentDfprop').mockResolvedValue({})
    jest.spyOn(api, 'findClientPropbase').mockResolvedValue({
      hasSchemaHtml: false,
      hasHistoryHtml: false,
    } as ClientPropbaseResult)
    jest.spyOn(api, 'findLatestTaskLog').mockResolvedValue(null)
    const container = document.createElement('div')
    document.body.appendChild(container)
    const mounted = riot.component(DocumentsRiot)(container, { projectName: 'maihamadb' })

    mounted.update({ latestResult: undefined } as any)

    expect(container.querySelector('latest-result')).toBeNull()

    mounted.unmount()
    container.remove()
  })
})
