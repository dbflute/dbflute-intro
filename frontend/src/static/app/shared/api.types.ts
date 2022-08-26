type DatabaseDefBean = {
  databaseName: string,
    driverName: string,
    urlTemplate: string,
    defaultSchema: string,
    schemaRequired: boolean,
    schemaUpperCase: boolean,
    userInputAssist: boolean,
    embeddedJar: boolean,
}
type IntroClassificationsResult = {
  targetDatabaseMap: { [key: string]: DatabaseDefBean},
  targetLanguageMap: { [key: string]: string },
  targetContainerMap: { [key: string]: string }
}
