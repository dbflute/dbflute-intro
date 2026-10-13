import * as riot from 'riot'

import 'semantic-ui-riot'
import introPlugin from '../src/static/app/app-plugin'
import { api } from '../src/static/app/api/api'
import AlterCheck from '../src/static/app/pages/client/alter-check/alter-check.riot'

riot.install(introPlugin)

const TEST_PROJECT = 'maihamadb'

type LatestLog = LogLatestResult | null

function mountAlterCheck() {
  const container = document.createElement('div')
  document.body.appendChild(container)
  const mounted = riot.component(AlterCheck)(container, { projectName: TEST_PROJECT })
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

function setupAlterCheck(latestLog: LatestLog, ngMarkFile?: PlaysqlMigrationAlterResult_NgMarkFilePart) {
  jest.spyOn(api, 'findAlterInfra').mockResolvedValue({
    editingFiles: [{ fileName: 'alter-schema.sql', content: 'alter table member add foo int;' }],
    ngMarkFile,
  })
  jest.spyOn(api, 'findClientPropbase').mockResolvedValue({
    hasAlterCheckResultHtml: false,
  } as ClientPropbaseResult)
  jest.spyOn(api, 'findLatestTaskLog').mockImplementation(async (projectName, task) => {
    expect(projectName).toBe(TEST_PROJECT)
    expect(task).toBe('alterCheck')
    return latestLog
  })
}

describe('AlterCheckの実行結果表示', () => {
  afterEach(() => {
    jest.restoreAllMocks()
  })

  it('実行履歴がない場合は結果を表示しない', async () => {
    setupAlterCheck(null)
    const mounted = mountAlterCheck()
    await flush()

    expect(mounted.container.querySelector('latest-result')).toBeNull()

    mounted.unmount()
  })

  it('最新のAlterCheckが成功している場合は過去の成功ログを表示しない', async () => {
    setupAlterCheck({ fileName: 'dbflute_intro_alterCheck_success_20260901.log', content: 'success log' })
    const mounted = mountAlterCheck()
    await flush()

    expect(mounted.container.querySelector('latest-result')).toBeNull()

    mounted.unmount()
  })

  it('NgMarkがない失敗の場合は通常の失敗結果を表示する', async () => {
    setupAlterCheck({ fileName: 'dbflute_intro_alterCheck_failure_20260901.log', content: 'failure log' })
    const mounted = mountAlterCheck()
    await flush()

    const result = mounted.container.querySelector('.ui.negative.message')
    expect(result?.textContent).toContain('Result: Failure')

    mounted.unmount()
  })

  test.each([
    ['previous-NG', 'Found problems on Previous DDL.', 'Retry save previous.'],
    ['alter-NG', 'Found problems on Alter DDL.', 'first error'],
    ['next-NG', 'Found problems on Next DDL.', 'Fix your DDL and data grammatically.'],
  ])('%sの場合は問題箇所に応じた案内を表示する', async (ngMark, title, message) => {
    setupAlterCheck(
      { fileName: 'dbflute_intro_alterCheck_failure_20260901.log', content: 'failure log' },
      { ngMark, content: 'first error\nsecond error' },
    )
    const mounted = mountAlterCheck()
    await flush()

    const resultText = mounted.container.querySelector('.ui.negative.message')?.textContent ?? ''
    expect(resultText).toContain(title)
    expect(resultText).toContain(message)

    mounted.unmount()
  })
})
