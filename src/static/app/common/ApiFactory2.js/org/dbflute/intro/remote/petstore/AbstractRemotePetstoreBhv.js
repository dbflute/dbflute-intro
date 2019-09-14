
/**
* Api Client.
* @author FreeGen
*/
export default class ApiFactory {

  function Swagger/ () {
    return ffetch.post('/swagger/')
  }
  function SwaggerJson () {
    return ffetch.post('/swagger/json')
  }
  function ClientList () {
    return ffetch.post('/client/list')
  }
  function ClientOperation{clientProject} () {
    return ffetch.post('/client/operation/{clientProject}')
  }
  function ClientCreate () {
    return ffetch.post('/client/create')
  }
  function ClientEdit{projectName} () {
    return ffetch.post('/client/edit/{projectName}')
  }
  function ClientDelete{clientProject} () {
    return ffetch.post('/client/delete/{clientProject}')
  }
  function Dfprop{project}List () {
    return ffetch.post('/dfprop/{project}/list')
  }
  function Dfprop{project}Update{fileName} () {
    return ffetch.post('/dfprop/{project}/update/{fileName}')
  }
  function Dfprop{project}Syncschema () {
    return ffetch.post('/dfprop/{project}/syncschema')
  }
  function Dfprop{project}SyncschemaEdit () {
    return ffetch.post('/dfprop/{project}/syncschema/edit')
  }
  function Dfprop{project}Schemapolicy () {
    return ffetch.post('/dfprop/{project}/schemapolicy')
  }
  function Dfprop{project}SchemapolicyEdit () {
    return ffetch.post('/dfprop/{project}/schemapolicy/edit')
  }
  function Dfprop{project}Document () {
    return ffetch.post('/dfprop/{project}/document')
  }
  function Dfprop{project}DocumentEdit () {
    return ffetch.post('/dfprop/{project}/document/edit')
  }
  function Document{clientProject}Schemahtml () {
    return ffetch.post('/document/{clientProject}/schemahtml')
  }
  function Document{clientProject}Historyhtml () {
    return ffetch.post('/document/{clientProject}/historyhtml')
  }
  function Document{clientProject}Propertieshtml () {
    return ffetch.post('/document/{clientProject}/propertieshtml')
  }
  function Document{clientProject}Synccheckresulthtml () {
    return ffetch.post('/document/{clientProject}/synccheckresulthtml')
  }
  function Document{clientProject}Altercheckresulthtml () {
    return ffetch.post('/document/{clientProject}/altercheckresulthtml')
  }
  function Document{clientProject}Lastadochtml{moduleName} () {
    return ffetch.post('/document/{clientProject}/lastadochtml/{moduleName}')
  }
  function DocumentDecomment{projectName}Save () {
    return ffetch.post('/document/decomment/{projectName}/save')
  }
  function DocumentDecomment{projectName}Pickup () {
    return ffetch.post('/document/decomment/{projectName}/pickup')
  }
  function DocumentDecomment{projectName}Mapping () {
    return ffetch.post('/document/decomment/{projectName}/mapping')
  }
  function DocumentHacomment{projectName}Save () {
    return ffetch.post('/document/hacomment/{projectName}/save')
  }
  function DocumentHacomment{projectName}Pickup () {
    return ffetch.post('/document/hacomment/{projectName}/pickup')
  }
  function EngineLatest () {
    return ffetch.post('/engine/latest')
  }
  function EngineVersions () {
    return ffetch.post('/engine/versions')
  }
  function EngineDownload{dbfluteVersion} () {
    return ffetch.post('/engine/download/{dbfluteVersion}')
  }
  function EngineRemove{version} () {
    return ffetch.post('/engine/remove/{version}')
  }
  function IntroManifest () {
    return ffetch.post('/intro/manifest')
  }
  function IntroClassifications () {
    return ffetch.post('/intro/classifications')
  }
  function IntroConfiguration () {
    return ffetch.post('/intro/configuration')
  }
  function Log/ () {
    return ffetch.post('/log/')
  }
  function Log{project}{task}Latest () {
    return ffetch.post('/log/{project}/{task}/latest')
  }
  function Log{project}List () {
    return ffetch.post('/log/{project}/list')
  }
  function Playsql{project}List () {
    return ffetch.post('/playsql/{project}/list')
  }
  function Playsql{project}Update{fileName} () {
    return ffetch.post('/playsql/{project}/update/{fileName}')
  }
  function PlaysqlMigration{clientProject}Alter () {
    return ffetch.post('/playsql/migration/{clientProject}/alter')
  }
  function Settings{clientProject} () {
    return ffetch.post('/settings/{clientProject}')
  }
  function SettingsEdit{projectName} () {
    return ffetch.post('/settings/edit/{projectName}')
  }
  function TaskExecute{project}{instruction} () {
    return ffetch.post('/task/execute/{project}/{instruction}')
  }
  function TaskExecute{project}{instruction}{env} () {
    return ffetch.post('/task/execute/{project}/{instruction}/{env}')
  }
  function WelcomeCreate () {
    return ffetch.post('/welcome/create')
  }
}
