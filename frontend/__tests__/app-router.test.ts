import * as riot from 'riot'
import axios from 'axios'

import App from '../src/static/app/app.riot'
import 'semantic-ui-riot'
import '../src/static/app/shared/i18n'
import introPlugin from '../src/static/app/app-plugin'
import { appRoutes } from '../src/static/app/app-router'
import { clientRoutes, getCurrentClientRoute } from '../src/static/app/pages/client/client-router'

riot.install(introPlugin)

// ルーティングの検証が目的なので、API は応答しない (ローディング状態のまま) にしておく
const pending = () => new Promise(() => {})
jest.spyOn(axios, 'get').mockImplementation(pending)
jest.spyOn(axios, 'post').mockImplementation(pending)
jest.spyOn(axios, 'put').mockImplementation(pending)
jest.spyOn(axios, 'delete').mockImplementation(pending)

const flush = () => new Promise((resolve) => setTimeout(resolve, 0))

const query = (selector: string) => document.querySelector(`.fixed-user-area ${selector}`)

describe('AppRouter', () => {
  beforeAll(async () => {
    const root = document.getElementById('root')
    if (!root) throw Error('not found element by id=root')
    riot.component(App)(root)
    await flush()
  })

  it('初期表示ではメイン画面が表示されること', () => {
    expect(query('content[is=main]')).not.toBeNull()
    expect(query('side-menu[is=common-menu]')).not.toBeNull()
    expect(query('content[is=client]')).toBeNull()
  })

  it('サイドメニューと本文が横並び用の要素 (.fixed-user-area) 直下に配置されること', () => {
    expect(document.querySelector('.fixed-user-area > side-menu[is=common-menu]')).not.toBeNull()
    expect(document.querySelector('.fixed-user-area > content[is=main]')).not.toBeNull()
  })

  it('ウェルカム画面に遷移できること', async () => {
    appRoutes.welcome.open()
    await flush()

    expect(window.location.hash).toBe('#welcome')
    expect(query('content[is=welcome]')).not.toBeNull()
    expect(query('content[is=main]')).toBeNull()
  })

  it('プロジェクト作成画面に遷移できること', async () => {
    appRoutes.create.open()
    await flush()

    expect(window.location.hash).toBe('#create')
    expect(query('content[is=create]')).not.toBeNull()
    expect(query('content[is=welcome]')).toBeNull()
  })

  it('クライアント画面に遷移でき、パスパラメータが取得できること', async () => {
    appRoutes.client.open('maihamadb', 'execute', 'documents')
    await flush()

    expect(window.location.hash).toBe('#client/maihamadb/execute/documents')
    expect(query('side-menu[is=client-menu]')).not.toBeNull()
    expect(query('content[is=client] h1')?.textContent).toContain('maihamadb')
    expect(query('content[is=client] documents')).not.toBeNull()
    expect(getCurrentClientRoute()).toEqual({
      path: 'client/maihamadb/execute/documents',
      params: { projectName: 'maihamadb', clientMenuType: 'execute', clientMenuName: 'documents' },
    })
  })

  it('クライアント画面内でメニューを切り替えられること', async () => {
    clientRoutes.executeAlterCheck.open('maihamadb')
    await flush()

    expect(window.location.hash).toBe('#client/maihamadb/execute/alter-check')
    expect(query('content[is=client] alter-check')).not.toBeNull()
    expect(query('content[is=client] documents')).toBeNull()
  })

  it('クライアント画面で別プロジェクトに切り替えられること', async () => {
    appRoutes.client.open('resortlinedb', 'settings', 'database-info')
    await flush()

    expect(query('content[is=client] h1')?.textContent).toContain('resortlinedb')
    expect(query('content[is=client] database-info')).not.toBeNull()
  })

  it('メイン画面に戻れること', async () => {
    appRoutes.main.open()
    await flush()

    expect(window.location.hash).toBe('#main')
    expect(query('content[is=main]')).not.toBeNull()
    expect(query('content[is=client]')).toBeNull()
  })
})
