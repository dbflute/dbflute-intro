type DatabaseDefBean = {
  databaseName: string
  driverName: string
  urlTemplate: string
  defaultSchema: string
  schemaRequired: boolean
  schemaUpperCase: boolean
  userInputAssist: boolean
  embeddedJar: boolean
}
type IntroClassificationsResult = {
  targetDatabaseMap: { [key: string]: DatabaseDefBean }
  targetLanguageMap: { [key: string]: string }
  targetContainerMap: { [key: string]: string }
}

type NgMark = 'previous-NG' | 'alter-NG' | 'next-NG'

type AlterSQLResultNgMarkFilePart = {
  ngMark: NgMark
  content: string
}

type AlterSQLResultSQLFilePart = {
  fileName: string
  content: string
}

type AlterSQLResultCheckedZipPart = {
  fileName: string
  checkedFiles: AlterSQLResultSQLFilePart[]
}

type AlterSQLResultUnreleasedDirPart = {
  checkedFiles: AlterSQLResultSQLFilePart[]
}

type AlterSQLResult = {
  ngMarkFile: AlterSQLResultNgMarkFilePart
  editingFiles: AlterSQLResultSQLFilePart[]
  checkedZip: AlterSQLResultCheckedZipPart
  unreleasedDir: AlterSQLResultUnreleasedDirPart
}

type LogBean = {
  fileName: string
  content: string
}

type TaskExecutionResult = {
  success: boolean
}
