/**
 * APIレスポンスのBodyが型として定義されているクラス
 * それぞれの項目の詳しい説明は、該当すうAPIのJava側のコメント、もしくはSwaggerを参照してください
 */

type LogBean = {
  fileName: string
  content: string
}

type TaskExecutionResult = {
  success: boolean
}
