import { RiotComponent } from 'riot'

export interface DBFluteIntroPlugin {
  classNames: (classes: { [key: string]: boolean }) => string
  successToast: (input: { title: string | undefined; message: string | undefined }) => void
  elementAs: <HTMLElement extends Element>(selector: string) => HTMLElement
  inputElementBy: (selector: string) => HTMLInputElement
}

/**
 * $関数で取得したElementに型を付与する際のヘルパー関数
 * - 複数のplugin関数で再利用するためglobalな関数として定義
 * @param {string} selector querySelector
 */
function elementAs<T extends Element>(selector: string): T {
  return this.$(selector) as T
}

const dbflutePlugin: DBFluteIntroPlugin = {
  /**
   * Componentにclass名を動的に与えるためのオブジェクトを構築する
   * 具体例は [riotのマイグレーションガイド]{@link https://riot.js.org/ja/migration-guide/#%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88%E3%81%AE%E3%82%B7%E3%83%A7%E3%83%BC%E3%83%88%E3%82%AB%E3%83%83%E3%83%88}を参照
   * @param   {{ [ key: string]: boolean }} classes - class list as object
   * @returns {string} return only the classes having a truthy value
   */
  classNames(classes: { [key: string]: boolean }) {
    return Object.entries(classes)
      .reduce((acc, item) => {
        const [key, value] = item

        if (value) return [...acc, key]

        return acc
      }, [])
      .join(' ')
  },
  /**
   * 成功時のトーストを表示する
   * - semantic-ui-riotのsuToast関数がinstallされている（= index.jsなどでimport 'semantic-ui-riot'されている）ことを前提とする
   * - 使用できるパラメータは[semantic-ui-riotのtoast]{@link https://semantic-ui-riot.web.app/addon/toast}を参照
   * @param {string|undefined} title タイトル（未設定の場合は省略される）
   * @param {string|undefined} message メッセージ（未設定の場合は省略される）
   */
  successToast({ title = undefined, message = undefined }: { title: string | undefined; message: string | undefined }) {
    this.suToast({
      title, // keyと渡す変数名が同じため省略する ("title: title,"としているのと同じ)
      message, // keyと渡す変数名が同じため省略する ("message: message,"としているのと同じ)
      class: 'pink positive',
    })
  },
  /**
   * @see {elementAs}
   */
  elementAs<T extends Element>(selector: string): T {
    return elementAs<T>.bind(this, selector).apply()
  },
  /**
   * 指定されたselectorのDOM要素を{@link HTMLInputElement}として返す
   * @param {string} selector querySelector
   */
  inputElementBy(selector: string): HTMLInputElement {
    return elementAs<HTMLInputElement>.bind(this, selector).apply()
  },
}

/**
 * DBFluteIntroのComponentで共通的に利用する処理をpluginとして提供
 * 下記で設定された関数は[riot.install]{@link https://riot.js.org/ja/api/#riotinstall}することで、任意のComponentから直接呼び出せる
 * @param component
 */
export default function plugin(component: RiotComponent) {
  const plugins: PropertyDescriptorMap & ThisType<any> = Object.getOwnPropertyNames(dbflutePlugin).reduce((acc, methodName) => {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore TODO: typesafeになるように修正
    acc[methodName] = { value: dbflutePlugin[methodName] }
    return acc
  }, {})
  Object.defineProperties(component, plugins)
  return component
}
