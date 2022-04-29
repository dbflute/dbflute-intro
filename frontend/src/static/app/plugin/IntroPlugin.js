/**
 * Componentにclass名を動的に与えるためのオブジェクトを構築する
 * 具体例は [riotのマイグレーションガイド]{@link https://riot.js.org/ja/migration-guide/#%E3%83%86%E3%83%B3%E3%83%97%E3%83%AC%E3%83%BC%E3%83%88%E3%81%AE%E3%82%B7%E3%83%A7%E3%83%BC%E3%83%88%E3%82%AB%E3%83%83%E3%83%88}を参照
 * @param   {Object} classes - class list as object
 * @returns {string} return only the classes having a truthy value
 */
function classNames(classes) {
  return Object.entries(classes).reduce((acc, item) => {
    const [key, value] = item

    if (value) return [...acc, key]

    return acc
  }, []).join(' ')
}

/**
 * 成功時のトーストを表示する
 * - semantic-ui-riotのsuToast関数がinstallされている（= index.jsなどでimport 'semantic-ui-riot'されている）ことを前提とする
 * - 使用できるパラメータは[semantic-ui-riotのtoast]{@link https://semantic-ui-riot.web.app/addon/toast}を参照
 * @param {string|null} title タイトル（未設定の場合は省略される）
 * @param {string|null} message メッセージ（未設定の場合は省略される）
 */
function successToast({ title= null, message= null }) {
  this.suToast({
    title, // keyと渡す変数名が同じため省略する ("title: title,"としているのと同じ)
    message, // keyと渡す変数名が同じため省略する ("message: message,"としているのと同じ)
    class: 'pink positive'
  })
}

/**
 * 配列に空アイテムを追加する
 * @param itemList
 */
export function setBlankItem(itemList) {
  itemList.unshift({ value: null, label: '' })
}

/**
 * DBFluteIntroのComponentで共通的に利用する処理をpluginとして提供
 * 下記で設定された関数は[riot.install]{@link https://riot.js.org/ja/api/#riotinstall}することで、任意のComponentから直接呼び出せる
 * @param component
 */
export default function plugin(component) {
  component.classNames = classNames
  component.successToast = successToast
  component.setBlankItem = setBlankItem
}
