import { route as riotRoute, router } from '@riotjs/route'
import { routes as ClientRoutes } from './client/client-router'

/**
 * URL（ルーティング）の定義
 * - path: URLパス
 * - content: コンテンツエリアに表示するコンポーネント
 * - sideMenu: サイドメニューエリアに表示するコンポーネント
 */
export const routes = Object.freeze({
  /**
   * メイン画面
   * @riotjs/route の仕組み上、空文字を選択すると挙動がおかしくなるので、mainというパスを定義している
   */
  main: {
    path: 'main',
    open: () => {
      router.push('main')
    }
  },
  /**
   * クライアント画面
   *  o 「:」をつけることでパスパラメータとして取得することができる
   */
  client: {
    path: 'client/:projectName/:menuType/:menuName',
    routes: ClientRoutes,
    open: (projectName, menuType, menuName) => {
      if (!projectName || !menuType || !menuName) {
        return
      }
      router.push(`client/${projectName}/${menuType}/${menuName}`)
    },
  },
  /**
   * ウェルカム画面（プロジェクト作成画面）
   */
  welcome: {
    path: 'welcome',
    open: () => {
      router.push('welcome')
    }
  },
  /**
   * プロジェクト作成画面
   */
  create: {
    path: 'create',
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

export const pages = Object.freeze(Object.entries(routes).map(([key, route]) => {
  const stream = riotRoute(route.path, {})
  return [key, {
    path: route.path,
    routes: route.routes,
    open: route.open,
    stream,
    subscribe: (callback) => stream.on.value(callback)
  }]
}).reduce((initial, [key, page]) => {
  initial[key] = page
  return initial
}, {}))

export function endRouting() {
  Object.values(pages).forEach(page => {
    page.stream.end()
  })
}
