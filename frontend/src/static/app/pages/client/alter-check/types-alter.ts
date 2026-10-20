/**
 * AlterDDL (alter-schema[-...].sql) のファイル情報を保持するオブジェクト。
 */
export type AlterDDLFile = {
  /** AlterDDLのファイル名 e.g. alter-schema.sql, alter-schema-sea.sql */
  fileName: string

  /** ファイルの中身テキスト (SQL文字列のはず) */
  content: string

  // #thinking jflute さすがにshowだけだとものたらない。showingDDL とかどうだろう？ (2026/09/29)
  /** ファイルの中身(SQL)を表示している状態か？ */
  show: boolean
}

// done jflute クラス名、CheckedZip の方が良い!? (2026/09/01)
// → UnreleasedCheckedAlterDirの方と同様な感じで。通称は checkedZip。 (2026/09/29)
/**
 * チェック済みZIP (checked-alter-to...zip, 未リリース) のファイル情報を保持するオブジェクト。
 */
export type CheckedAlterToZip = {
  /** zipのファイル名 e.g. 20190831_2249/checked-alter-to-20190422-2332.zip */
  fileName: string

  /** zip内のチェック済みAlterDDLのファイルたち */
  checkedDDLFiles: AlterDDLFile[]
}

// done jflute クラス名、UnreleasedDir の方が良い!? (2026/09/01)
// → APIレスポンスのクラスと紛れないように、実際のディレクトリ名そのままで直感的にした。 (2026/09/29)
// → でもその変数名は unreleasedDir と通称な感じで。
/**
 * 未リリースチェック済みディレクトリ (unreleased-checked-alter) のファイル情報を保持するオブジェクト。
 */
export type UnreleasedCheckedAlterDir = {
  /** ディレクトリ内のチェック済みAlterDDLのファイルたち */
  checkedDDLFiles: AlterDDLFile[]
}
