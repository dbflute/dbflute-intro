import { RiotComponent } from 'riot'

// =======================================================================================
//                                                                        Plugin Interface
//                                                                        ================
/**
 * DBFlute Intro固有の共通関数をriotに定義するプラグイン用インターフェース。
 * - 単独で参照されるのでexport対象
 * - riotコンポーネントにフラットに展開されるので、本当に共通的なものだけ定義
 */
export interface DBFluteIntroPlugin {
  /**
   * Componentにclass名を動的に与えるためのオブジェクトを構築する
   * 具体例は [riotのマイグレーションガイド]{@link https://riot.js.org/ja/migration-guide/#%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88%E3%81%AE%E3%82%B7%E3%83%A7%E3%83%BC%E3%83%88%E3%82%AB%E3%83%83%E3%83%88}を参照
   * @param classes - class list as object (NotNull)
   * @returns return only the classes having a truthy value (NotNull)
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
   * 指定されたselectorでHTMLのDOM要素を探す。(ないかもしれないときに使う)
   * - $関数で取得したElementに型を付与する際のヘルパー関数
   * - 複数のplugin関数で再利用するためglobalな関数として定義
   * @param selector $関数に入れるselector文字列 e.g. [ref=jdbcDriverFqcn] (NotNull)
   * @returns 対象のタグのHTMLオブジェクト (NullAllowed: if not found)
   */
  elementAs: <EL extends HTMLElement>(selector: string) => EL | null

  /**
   * 指定されたselectorでinputのDOM要素を探す。(ないかもしれないときに使う)
   * @param selector $関数に入れるselector文字列 e.g. [ref=jdbcDriverFqcn] (NotNull)
   * @returns 対象のタグのHTMLオブジェクト (NullAllowed: if not found)
   */
  inputElementBy: (selector: string) => HTMLInputElement | null

  /**
   * 指定されたselectorでinputのDOM要素を探す。(存在するはずのときに使う)
   * @param selector $関数に入れるselector文字列 e.g. [ref=jdbcDriverFqcn] (NotNull)
   * @returns 対象のタグのHTMLオブジェクト (NotNull: exception if not found)
   * @throws {ElementNotFoundError} 指定されたselectorに合致するinputのelementが存在しなかったら
   */
  inputElementByRequired: (selector: string) => HTMLInputElement
}

// ---------------------------------------------------------
//                                            Error Handling
//                                            --------------
/**
 * 以下のサイトを参考に作成:
 * - // JavaScript/TypeScriptで独自のエラークラスを利用する
 * - https://www.memory-lovers.blog/entry/2021/01/18/022021
 *
 * // #thinking jflute と思ったが、そのまま書いてもTypeScript的なコンパイルエラーが発生してしまった... (2023/09/25)
 * // なので、いったんシンプルに実装して、実際に使ってみて情報が足らないようだったらまた考える。
 */
export class ElementNotFoundError extends Error {
  constructor(msg: string) {
    super(msg)

    // #thinking ChatGPTに言われて、タイトル的なnameを入れてみた jflute (2023/12/25)
    this.name = '指定されたselectorに対応する要素がないぞ例外'

    // #thinking こうするとスタックトレースを保持される？的なことをChatGPTに言われて書いてみたが、いったん無しで試してみよう jflute (2023/12/25)
    //if (Error.captureStackTrace) {
    //  Error.captureStackTrace(this, ElementNotFoundError)
    //}
  }
}

// =======================================================================================
//                                                                   Plugin Implementation
//                                                                   =====================
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
      title,
      message,
      class: 'pink positive',
    })
  },

  elementAs<EL extends HTMLElement>(selector: string): EL | null {
    return elementAs.bind(this, selector).apply() // 戻り型推論ちゃんと効いてるっぽい
  },

  // #thinking jflute 見つかったのがinputタグじゃなかったら？それはそれでnullか例外で良いような？その制御があると良い (2023/09/25)
  inputElementBy(selector: string): HTMLInputElement | null {
    return elementAs.bind(this, selector).apply() // こっちも
  },

  inputElementByRequired: function (selector: string): HTMLInputElement {
    const foundElement = this.inputElementBy(selector)
    if (foundElement === null) {
      throw new ElementNotFoundError('Not found the element by the selector: ' + selector)
    }
    return foundElement
  },
}

// ---------------------------------------------------------
//                                          Element Handling
//                                          ----------------
// dbflutePluginの中で共通的に利用される関数をプライベート関数的に扱うために外に出している
function elementAs<EL extends HTMLElement>(selector: string): EL | null {
  // #thinking jflute $()の戻りってHTMLElementじゃなくてElementだと思うんだけど、ノーチェックでいいのかな？ (2023/12/25)
  return this.$(selector) as EL
}

// =======================================================================================
//                                                                            Riot Install
//                                                                            ============
// #thinking jflute JSDocの書き方、[]と@linkはつながってる書き方なのかな？ (2023/09/25)
/**
 * DBFluteIntroのComponentで共通的に利用する処理をpluginとして提供。
 *
 * 下記で設定された関数は [riot.install]{@link https://riot.js.org/ja/api/#riotinstall} することで、
 * 任意のComponentから直接呼び出せる。
 * @param component Riotコンポーネント自身、ここにプロパティを追加したりする (NotNull)
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
