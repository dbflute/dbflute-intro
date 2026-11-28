/**
 * ドロップダウンの1項目を表現する汎用クラス。
 */
export type DropdownItem = {
  /** 内部制御のコード */
  value: string

  /** 表示名、人間向けの文言 */
  label: string

  /** ドロップダウンのデフォルト表示項目かどうか？ (HTML上でselectedが付くかどうか) */
  default: boolean
}
