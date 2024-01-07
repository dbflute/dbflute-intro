import * as riot from 'riot'

import App from '../src/static/app/app.riot'
import 'semantic-ui-riot'
import '../src/static/app/shared/i18n'
import introPlugin from '../src/static/app/app-plugin'
import { api } from '../src/static/app/api/api'

// TODO: setup処理をutilとして切り出す
// 全てのComponentで共通的に利用する処理を登録する
riot.install(introPlugin)

jest.mock('../src/static/app/app-router', () => {
  const originalModule = jest.requireActual('../src/static/app/app-router')
  return {
    ...originalModule,
    initialRoute: 'welcome',
  }
})
jest.spyOn(api, 'clientList').mockResolvedValue([])
jest.spyOn(api, 'findEngineLatest')
jest.spyOn(api, 'findClassifications')

describe('Welcome', () => {
  beforeAll(() => {
    // index.htmlのid=rootタグにapp.riotをマウントすることでriotアプリを起動する
    const mountApp = riot.component(App)
    const elementId = 'root'
    const root = document.getElementById(elementId)
    if (!root) throw Error(`not found element by id=${elementId}`)
    mountApp(root)
  })

  it('サイドメニューに共通メニューが表示されていること', () => {
    const body = document.querySelector('body')
    const commonMenu = body?.querySelector('[is=common-menu]')
    expect(commonMenu).not.toBeNull()
  })
})
