import * as riot from 'riot'

// TODO: setup処理をutilとして切り出す
import App from '../src/static/app/app.riot'
import 'semantic-ui-riot'
import '../src/static/app/shared/i18n'
import introPlugin from '../src/static/app/shared/app-plugin'

// 全てのComponentで共通的に利用する処理を登録する
riot.install(introPlugin)

jest.mock('../src/static/app/app-router', () => {
  const originalModule = jest.requireActual('../src/static/app/app-router');
  return {
    ...originalModule,
    initialRoute: 'welcome'
  }
})

jest.mock('../src/static/app/common/ApiClient', () => {
  const originalModule = jest.requireActual('../src/static/app/common/ApiClient');
  return {
    ...originalModule
  }
})

describe('Welcome', () => {
  beforeAll( () => {
    // index.htmlのid=rootタグにapp.riotをマウントすることでriotアプリを起動する
    const mountApp = riot.component(App)
    mountApp(document.getElementById('root'))
  });

  it('サイドメニューに共通メニューが表示されていること', () => {
    const body = document.querySelector('body')
    const commonMenu = body.querySelector('[is=common-menu]')
    expect(commonMenu).not.toBeNull()
  });
});
