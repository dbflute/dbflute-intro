/**
 * DBFluteタスクのログファイル名から、タスクが成功したかを判定する。
 * ログファイルの命名規則に関する知識を画面側へ散らさないため、この関数に集約する。
 * @param fileName ログファイル名
 * @returns ログファイルが成功結果を表す場合はtrue
 */
export const isTaskLogSuccess = (fileName: string): boolean => fileName.includes('success')
