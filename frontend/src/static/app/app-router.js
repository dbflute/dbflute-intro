import { route as riotRoute, router } from '@riotjs/route'
import { routes as ClientRoutes } from './client/client-router'

/**
 * @typedef Routes
 * @type {Object.<string, Route>} - key: ルーティング名, value: ルーティングオブジェクト
 * @description {@link Route}を同一階層で束ねたオブジェクト
 *
 * @typedef Route
 * @property {string} path - URLパス. 仕様は[@riotjs/route]{@link https://github.com/riot/route#documentation}を参照
 * @property {Routes} route - 対象のルーティングに遷移する関数
 * @property {() => void} open - 対象のルーティングに遷移する関数
 * @description アプリのルーティング（URLというか画面）ごとの情報を保持するオブジェクト
 */

/**
 * アプリのRootURL（ルーティング）の定義
 * @type {Routes}
 */
const routes = Object.freeze({
  /**
   * メイン画面
   * - @riotjs/route の仕組み上、空文字を選択すると挙動がおかしくなるので、mainというパスを定義している
   */
  main: {
    path: 'main',
    open: () => {
      router.push('main')
    }
  },
  /**
   * クライアント画面
   *  - pathに「:」をつけることでパスパラメータとして取得することができる
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
 * 初期URLパス
 * - localhost:3000 → "main"にルーティング → "localhost:3000#main"に遷移
 * - localhost:3000#welcome → "welcome"にルーティング → "localhost:3000#welcome"に遷移
 * @type {string} URLパス
 */
export const initialRoute = `${window.location.hash ? window.location.hash.replace('#', '') : 'main'}`

/**
 * アプリケーションのrouteを束ねたオブジェクト
 * - {@link routes}にいくつか便利関数を生やしたオブジェクト
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
  Object.values(appRoutes).forEach(route => {
    routeStreams.get(route.path)?.end()
  })
  routeStreams.clear()
}
