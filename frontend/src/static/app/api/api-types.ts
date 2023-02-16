/**
 * APIレスポンスのBodyが型として定義されているクラス
 * それぞれの項目の詳しい説明は、該当すうAPIのJava側のコメント、もしくはSwaggerを参照してください
 */

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
