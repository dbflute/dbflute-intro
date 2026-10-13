import * as riot from 'riot'

import 'semantic-ui-riot'
import introPlugin from '../src/static/app/app-plugin'
import { api } from '../src/static/app/api/api'
import Documents from '../src/static/app/pages/client/documents/documents.riot'
import ReplaceSchema from '../src/static/app/pages/client/replace-schema/replace-schema.riot'
import SchemaSyncCheck from '../src/static/app/pages/client/schema-sync-check/schema-sync-check.riot'

riot.install(introPlugin)

const TEST_PROJECT = 'maihamadb'

type MountedComponent = {
  container: HTMLElement
  unmount: () => void
}

type LatestLog = LogLatestResult | null

function mountComponent(component: any): MountedComponent {
  const container = document.createElement('div')
  document.body.appendChild(container)
  const mounted = riot.component(component)(container, { projectName: TEST_PROJECT })
  return {
    container,
    unmount: () => {
      mounted.unmount()
      container.remove()
    },
  }
}

async function flush() {
  for (let i = 0; i < 10; i++) {
    await Promise.resolve()
  }
}

function mockLatestTaskLog(task: string, latestLog: LatestLog) {
  jest.spyOn(api, 'findLatestTaskLog').mockImplementation(async (projectName, actualTask) => {
    expect(projectName).toBe(TEST_PROJECT)
    expect(actualTask).toBe(task)
    return latestLog
  })
}

function setupDocuments(latestLog: LatestLog) {
  jest.spyOn(api, 'findDocumentDfprop').mockResolvedValue({})
  jest.spyOn(api, 'findClientPropbase').mockResolvedValue({
    hasSchemaHtml: false,
    hasHistoryHtml: false,
  } as ClientPropbaseResult)
  mockLatestTaskLog('doc', latestLog)
}

function setupSchemaSyncCheck(latestLog: LatestLog) {
  jest.spyOn(console, 'log').mockImplementation(() => undefined)
  jest.spyOn(api, 'findSchemaSyncDfprop').mockResolvedValue({})
  jest.spyOn(api, 'findClientPropbase').mockResolvedValue({
    hasSyncCheckResultHtml: false,
  } as ClientPropbaseResult)
  mockLatestTaskLog('schemaSyncCheck', latestLog)
}

function setupReplaceSchema(latestLog: LatestLog) {
  jest.spyOn(api, 'findCoreDfprop').mockResolvedValue({} as DfpropSettingsResult)
  jest.spyOn(api, 'findPlaysqlFileList').mockResolvedValue([])
  mockLatestTaskLog('replaceSchema', latestLog)
}

const standardScreens = [
  ['Documents', Documents, setupDocuments],
  ['SchemaSyncCheck', SchemaSyncCheck, setupSchemaSyncCheck],
  ['ReplaceSchema', ReplaceSchema, setupReplaceSchema],
] as const

describe.each(standardScreens)('%sの最新実行結果', (_name, component, setupApi) => {
  afterEach(() => {
    jest.restoreAllMocks()
  })

  it('実行履歴がない場合は結果を表示しない', async () => {
    setupApi(null)
    const mounted = mountComponent(component)
    await flush()

    expect(mounted.container.querySelector('latest-result')).toBeNull()

    mounted.unmount()
  })

  it('最新のタスクが成功している場合は成功結果を表示する', async () => {
    setupApi({ fileName: 'dbflute_intro_task_success_20260901.log', content: 'success log' })
    const mounted = mountComponent(component)
    await flush()

    const result = mounted.container.querySelector('.ui.positive.message')
    expect(result).not.toBeNull()
    expect(result?.querySelector('.latest-result-title')?.textContent).toContain('Execution Result: Success')

    mounted.unmount()
  })

  it('最新のタスクが失敗している場合は失敗結果を表示する', async () => {
    setupApi({ fileName: 'dbflute_intro_task_failure_20260901.log', content: 'failure log' })
    const mounted = mountComponent(component)
    await flush()

    const result = mounted.container.querySelector('.ui.negative.message')
    expect(result).not.toBeNull()
    expect(result?.querySelector('.latest-result-title')?.textContent).toContain('Execution Result: Failure')

    mounted.unmount()
  })
})
