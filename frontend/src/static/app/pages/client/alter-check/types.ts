export type AlterFile = {
  fileName: string
  content: string
  show: boolean
}

export type AlterZip = {
  fileName: string
  checkedFiles: AlterFile[]
}
