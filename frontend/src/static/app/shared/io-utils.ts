/**
 * ファイルを読み込む
 * @param {Blob} file
 * @return {Promise<string>} 読み込み結果
 */
export function readFile(file: Blob): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    // 読み込み成功時はPromiseに読み込み結果を渡す（thenに流れる）ように設定
    reader.onload = () => {
      resolve(reader.result as string) // readAsBinaryStringで読み込んでいるのでstringになるはず
    }
    // 読み込み失敗時はPromiseにerrorを渡す（catch文に流れる）ように設定
    reader.onerror = reject
    // ファイルを読み込み開始
    reader.readAsBinaryString(file)
  })
}
