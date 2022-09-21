import { router } from '@riotjs/route'
import { clientRoutes } from './pages/client/client-router'
import { createRouting } from './app-route'

/**
 * アプリのRootURL（ルーティング）の定義
 */
export const appRoutes = createRouting({
  /**
   * メイン画面
   * - @riotjs/route の仕組み上、空文字を選択すると挙動がおかしくなるので、mainというパスを定義している
   */
  main: {
    path: 'main',
    open: () => {
      router.push('main')
    },
  },
  /**
   * クライアント画面
   *  - pathに「:」をつけることでパスパラメータとして取得することができる
   */
  client: {
    path: 'client/:projectName/:menuType/:menuName',
    children: clientRoutes,
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
    },
  },
  /**
   * プロジェクト作成画面
   */
  create: {
    path: 'create',
    open: () => {
      router.push('create')
    },
  },
})

/**
 * 初期URLパス
 * - localhost:3000 → "main"にルーティング → "localhost:3000#main"に遷移
 * - localhost:3000#welcome → "welcome"にルーティング → "localhost:3000#welcome"に遷移
 * @type {string} URLパス
 */
export const initialRoute = `${window.location.hash ? window.location.hash.replace('#', '') : 'main'}`
