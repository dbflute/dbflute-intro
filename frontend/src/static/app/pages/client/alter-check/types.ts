export type AlterFileState = {
  fileName: string
  content: string
  show: boolean
}

export type AlterZipState = {
  fileName: string
  checkedFiles: AlterFileState[]
}
