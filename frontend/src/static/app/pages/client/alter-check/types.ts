/**
 * AlterDDLのSQLファイルの情報を保持するオブジェクト。
 */
export type AlterFile = {
  fileName: string
  content: string
  show: boolean
}

// #thinking jflute クラス名、CheckedZip の方が良い!? (2026/09/01)
/**
 * チェック済みZIP (checkedZip, 未リリース) のファイル情報を保持するオブジェクト。
 */
export type AlterZip = {
  fileName: string
  checkedFiles: AlterFile[]
}

// #thinking jflute クラス名、UnreleasedDir の方が良い!? (2026/09/01)
/**
 * 未リリースチェック済みディレクトリ (unreleasedDir) のファイル情報を保持するオブジェクト。
 */
export type AlterDir = {
  checkedFiles: AlterFile[]
}
