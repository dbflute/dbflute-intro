import { appRoutes } from './app-router'
import { route as riotRoute } from '@riotjs/route'

/**
 * Routeをsubscribeしたときのcallbackに渡ってくる値（@riotjs/routeのcreateRouteが返却する値）
 * - {@link URL} がベース
 * - pathで定義した引数（:で定義された値）がparamsというプロパティが辞書型で追加されている
 */
type SubscribedRoute = URL & { params: { [key: string]: string } }

/**
 * アプリのルーティング（URLというか画面）ごとの情報を保持するオブジェクト
 */
type Route = {
  /** URLパス. 仕様は[@riotjs/route]{@link https://github.com/riot/route#documentation}を参照 */
  path: string
  /** 対象のルーティングに遷移する関数*/
  open: (...args: string[]) => void
  /** 子ルーティング */
  children?: Routes
  /** 対象ルーティングへの遷移をsubscribeする */
  subscribe: (callback: (route: SubscribedRoute) => void) => void
}
type RouteDefinition = Omit<Route, 'subscribe'>

/**
 * {@link Route}を同一階層で束ねたオブジェクト
 * key: ルーティング名, value: ルーティングオブジェクト
 */
export type Routes = { [name: string]: Route }
export type RoutesDefinition = { [name: string]: RouteDefinition }

/**
 * ルーティングの作成
 * - Object.freezeで定義変更を行えないようにする
 * - subscribe関数を生やし、URLの変更を監視できるようにしている
 */
export function createRouting(definition: RoutesDefinition): Routes {
  return Object.freeze(
    Object.entries(definition).reduce((newRoutes, [name, route]) => {
      newRoutes[name] = {
        path: route.path,
        children: route.children,
        open: route.open,
        subscribe: (callback: any) => ensureRouteStream(route.path).on.value(callback),
      }
      return newRoutes
    }, {} as Routes)
  )
}

/**
 * ルーティング関連のcleanup処理
 * - 監視しているstreamの破棄を行う
 */
export function endRouting(): void {
  Object.values(appRoutes).forEach((route) => {
    routeStreams.get(route.path)?.end()
  })
  routeStreams.clear()
}

/**
 * 各routeのstreamを格納しておくMap
 * - キャッシュとして利用するのでpathをkeyにstreamを保持する
 */
const routeStreams = new Map()

/**
 * 指定されたパスのstreamを取得する
 * - なければ作成し、キャッシュする
 */
function ensureRouteStream(path: string): any {
  // #thinking jflute Riot側でJSのみのコードで型が指定できない(!?)ので苦肉のany (2024/01/07)
  if (!routeStreams.get(path)) {
    routeStreams.set(path, riotRoute(path, {}))
  }
  return routeStreams.get(path)
}
