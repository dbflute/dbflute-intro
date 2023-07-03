import { RiotComponent } from 'riot'

/**
 * DBFlute Intro固有の共通関数をriotに定義するプラグイン用インターフェース。
 * - 単独で参照されるのでexport対象
 * - riotコンポーネントにフラットに展開されるので、本当に共通的なものだけ定義
 */
export interface DBFluteIntroPlugin {
  /**
   * Componentにclass名を動的に与えるためのオブジェクトを構築する
   * 具体例は [riotのマイグレーションガイド]{@link https://riot.js.org/ja/migration-guide/#%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88%E3%81%AE%E3%82%B7%E3%83%A7%E3%83%BC%E3%83%88%E3%82%AB%E3%83%83%E3%83%88}を参照
   * @param classes - class list as object
   * @returns return only the classes having a truthy value
   */
  classNames: (classes: { [key: string]: boolean }) => string

  /**
   * 成功時のトーストを表示する
   * - semantic-ui-riotのsuToast関数がinstallされている（= index.jsなどでimport 'semantic-ui-riot'されている）ことを前提とする
   * - 使用できるパラメータは[semantic-ui-riotのtoast]{@link https://semantic-ui-riot.web.app/addon/toast}を参照
   * @param title トーストで表示されるタイトル （未設定の場合は省略される）
   * @param message トーストで表示されるメッセージ （未設定の場合は省略される）
   */
  successToast: (input: { title: string | undefined; message: string | undefined }) => void

  /**
   * $関数で取得したElementに型を付与する際のヘルパー関数
   * - 複数のplugin関数で再利用するためglobalな関数として定義
   * @param selector $関数に入れるselector文字列 e.g. [ref=jdbcDriverFqcn]
   * @returns 対象のタグのHTMLオブジェクト (NullAllowed: if not found)
   */
  elementAs: <HTMLElement extends Element>(selector: string) => HTMLElement

  /**
   * 指定されたselectorのDOM要素を{@link HTMLInputElement}として返す
   * @param @pselector $関数に入れるselector文字列 e.g. [ref=jdbcDriverFqcn]
   * @returns 対象のタグのHTMLオブジェクト (NullAllowed: if not found)
   */
  inputElementBy: (selector: string) => HTMLInputElement
}

// dbflutePluginの中で共通的に利用される関数をプライベート関数的に扱うために外に出している
function elementAs<T extends Element>(selector: string): T {
  return this.$(selector) as T
}

/**
 * DBFlute Intro固有の共通関数の実装インスタンス。
 */
const dbflutePlugin: DBFluteIntroPlugin = {
  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
  // JSDocはインターフェースの方に記述している。その方が利用する側で補完時に見られるため。
  // _/_/_/_/_/_/_/_/_/_/

  classNames(classes: { [key: string]: boolean }) {
    return Object.entries(classes)
      .reduce((acc, item) => {
        const [key, value] = item

        if (value) return [...acc, key]

        return acc
      }, [])
      .join(' ')
  },

  successToast({ title = undefined, message = undefined }: { title: string | undefined; message: string | undefined }) {
    this.suToast({
      title, // keyと渡す変数名が同じため省略する ("title: title,"としているのと同じ)
      message, // keyと渡す変数名が同じため省略する ("message: message,"としているのと同じ)
      class: 'pink positive',
    })
  },

  elementAs<T extends Element>(selector: string): T {
    return elementAs.bind(this, selector).apply() // 戻り型推論ちゃんと効いてるっぽい
  },

  inputElementBy(selector: string): HTMLInputElement {
    return elementAs.bind(this, selector).apply() // こっちも
  },
}

/**
 * DBFluteIntroのComponentで共通的に利用する処理をpluginとして提供。
 *
 * 下記で設定された関数は [riot.install]{@link https://riot.js.org/ja/api/#riotinstall} することで、任意のComponentから直接呼び出せる。
 *
 * @param component Riotコンポーネント自身、ここにプロパティを追加したりする
 */
export default function plugin(component: RiotComponent) {
  // 定義されている関数(厳密にはプロパティ)のMapを用意
  const propertyDescriptors = Object.getOwnPropertyDescriptors(dbflutePlugin)

  // 各関数(厳密にはプロパティ)をcomponentに定義していく
  // (Riotコンポーネントのインスタンスに、後から関数定義を追加している: Javaではこういうのできない)
  Object.getOwnPropertyNames(dbflutePlugin).forEach((methodName) => {
    Object.defineProperty(component, methodName, propertyDescriptors[methodName])
  })
  return component
}
