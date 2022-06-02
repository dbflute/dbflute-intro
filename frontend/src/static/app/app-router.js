import { route as riotRoute, router } from '@riotjs/route'
import { routes as ClientRoutes } from './client/client-router'

/**
 * URL（ルーティング）の定義
 * - path: URLパス
 * - content: コンテンツエリアに表示するコンポーネント
 * - sideMenu: サイドメニューエリアに表示するコンポーネント
 */
const routes = Object.freeze({
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
   *  - 「:」をつけることでパスパラメータとして取得することができる
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
 * 各routeのstreamを格納しておくMap
 * - キャッシュとして利用するのでpathをkeyにstreamを保持する
 */
const routeStreams = new Map()

/**
 * 指定されたパスのstreamを取得する
 * - なければ作成し、キャッシュする
 */
function ensureRouteStream(path) {
  if (!routeStreams.get(path)) {
    routeStreams.set(path, riotRoute(path, {}))
  }
  return routeStreams.get(path)
}

/**
 * 初期URL
 * - localhost:3000 → "main"にルーティング → "localhost:3000#main"に遷移
 * - localhost:3000#welcome → "welcome"にルーティング → "localhost:3000#welcome"に遷移
 */
export const initialRoute = `${window.location.hash ? window.location.hash.replace('#', '') : 'main'}`

/**
 * アプリケーションのrouteを束ねたオブジェクト
 * - {@link routes } にいくつか便利関数を生やしたオブジェクト
 * - subscribe関数を生やし、URLの変更を監視できるようにしている
 */
export const appRoutes = Object.freeze(Object.entries(routes)
  .reduce((newRoutes, [name, route]) => {
    newRoutes[name] = {
      path: route.path,
      routes: route.routes,
      open: route.open,
      subscribe: (callback) => ensureRouteStream(route.path).on.value(callback)
    }
    return newRoutes
  }, {}))

/**
 * ルーティング関連のcleanup処理
 * - 監視しているstreamの破棄を行う
 */
export function endRouting() {
  Object.values(appRoutes).forEach(page => {
    routeStreams.get(page.path)?.end()
  })
  routeStreams.clear()
}
