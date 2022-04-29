import * as riot from 'riot'
import Main from './main/main.riot'
import Client from './client/client.riot'
import Welcome from './welcome/welcome.riot'
import Create from './client/create.riot'
import CommonMenu from './common/common-menu.riot'
import ClientMenu from './client/client-menu.riot'
import { router } from '@riotjs/route'

/**
 * URL（ルーティング）の定義
 * - path: URLパス
 * - content: コンテンツエリアに表示するコンポーネント
 * - sideMenu: サイドメニューエリアに表示するコンポーネント
 */
export const pages = Object.freeze({
  /**
   * メイン画面
   * @riotjs/route の仕組み上、空文字を選択すると挙動がおかしくなるので、mainというパスを定義している
   */
  main: {
    path: 'main',
    content: Main,
    sideMenu: CommonMenu,
    open: () => {
      router.push('main')
    }
  },
  /**
   * クライアント画面
   *  o 「:」をつけることでパスパラメータとして取得することができる
   *  o 「?」がついている場合、パスパラメータが省略される可能性があることを示している
   */
  client: {
    path: 'client/:project_name/:menu_type?/:menu_name?',
    content: Client,
    sideMenu: ClientMenu,
    open: (projectName, menuType, menuName) => {
      if (!projectName) {
        return
      }
      router.push(`client/${[projectName, menuType, menuName].filter(param => param).join('/')}`)
    }
  },
  /**
   * ウェルカム画面（プロジェクト作成画面）
   *  o プロジェクトを1つも作っていない場合、強制的に遷移する
   */
  welcome: {
    path: 'welcome',
    content: Welcome,
    sideMenu: CommonMenu,
    open: () => {
      router.push('welcome')
    }
  },
  /**
   * プロジェクト作成画面
   */
  create: {
    path: 'create',
    content: Create,
    sideMenu: CommonMenu,
    open: () => {
      router.push('create')
    }
  }
})

/**
 * 初期URL
 * o localhost:3000 → "main"にルーティング → "localhost:3000#main"に遷移
 * o localhost:3000#welcome → "welcome"にルーティング → "localhost:3000#welcome"に遷移
 */
export const initialRoute = `${window.location.hash ? window.location.hash.replace('#', '') : 'main'}`

/**
 * ルーティング対象のコンポーネントをriotコンポーネントとして登録
 */
export function initRouting() {
  const _pages = Object.values(pages)
  _pages.forEach(page => riot.register(page.content.name, page.content))
  // riot.registerは重複登録するとエラーになるので、sideMenuの重複を排除してから登録する
  Array.from(new Set(_pages.map(page => page.sideMenu))).forEach(menu => riot.register(menu.name, menu))
  return _pages
}
