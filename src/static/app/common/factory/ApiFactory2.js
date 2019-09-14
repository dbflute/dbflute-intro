
/**
* Api Client.
* @author FreeGen
*/
export default class ApiFactory {

  swagger () {
    return ffetch.post(`/swagger/`)
  }

  swaggerJson () {
    return ffetch.post(`/swagger/json`)
  }

  alter (clientProject) {
    return ffetch.post(`/alter/${clientProject}`)
  }

  alterCreate (clientProject) {
    return ffetch.post(`/alter/${clientProject}/create`)
  }

  clientList () {
    return ffetch.post(`/client/list`)
  }

  clientOperation (clientProject) {
    return ffetch.post(`/client/operation/${clientProject}`)
  }

  clientCreate () {
    return ffetch.post(`/client/create`)
  }

  clientEdit (projectName) {
    return ffetch.post(`/client/edit/${projectName}`)
  }

  clientDelete (clientProject) {
    return ffetch.post(`/client/delete/${clientProject}`)
  }

  dfpropList (project) {
    return ffetch.post(`/dfprop/${project}/list`)
  }

  dfprop (project, fileName) {
    return ffetch.post(`/dfprop/${project}/update/${fileName}`)
  }

  dfpropSyncschema (project) {
    return ffetch.post(`/dfprop/${project}/syncschema`)
  }

  dfpropSyncschemaEdit (project) {
    return ffetch.post(`/dfprop/${project}/syncschema/edit`)
  }

  dfpropSchemapolicy (project) {
    return ffetch.post(`/dfprop/${project}/schemapolicy`)
  }

  dfpropSchemapolicyEdit (project) {
    return ffetch.post(`/dfprop/${project}/schemapolicy/edit`)
  }

  dfpropDocument (project) {
    return ffetch.post(`/dfprop/${project}/document`)
  }

  dfpropDocumentEdit (project) {
    return ffetch.post(`/dfprop/${project}/document/edit`)
  }

  documentSchemahtml (clientProject) {
    return ffetch.post(`/document/${clientProject}/schemahtml`)
  }

  documentHistoryhtml (clientProject) {
    return ffetch.post(`/document/${clientProject}/historyhtml`)
  }

  documentPropertieshtml (clientProject) {
    return ffetch.post(`/document/${clientProject}/propertieshtml`)
  }

  documentSynccheckresulthtml (clientProject) {
    return ffetch.post(`/document/${clientProject}/synccheckresulthtml`)
  }

  documentAltercheckresulthtml (clientProject) {
    return ffetch.post(`/document/${clientProject}/altercheckresulthtml`)
  }

  document (clientProject, moduleName) {
    return ffetch.post(`/document/${clientProject}/lastadochtml/${moduleName}`)
  }

  documentDecommentSave (projectName) {
    return ffetch.post(`/document/decomment/${projectName}/save`)
  }

  documentDecommentPickup (projectName) {
    return ffetch.post(`/document/decomment/${projectName}/pickup`)
  }

  documentDecommentMapping (projectName) {
    return ffetch.post(`/document/decomment/${projectName}/mapping`)
  }

  documentHacommentSave (projectName) {
    return ffetch.post(`/document/hacomment/${projectName}/save`)
  }

  documentHacommentPickup (projectName) {
    return ffetch.post(`/document/hacomment/${projectName}/pickup`)
  }

  engineLatest () {
    return ffetch.post(`/engine/latest`)
  }

  engineVersions () {
    return ffetch.post(`/engine/versions`)
  }

  engineDownload (dbfluteVersion) {
    return ffetch.post(`/engine/download/${dbfluteVersion}`)
  }

  engineRemove (version) {
    return ffetch.post(`/engine/remove/${version}`)
  }

  introManifest () {
    return ffetch.post(`/intro/manifest`)
  }

  introClassifications () {
    return ffetch.post(`/intro/classifications`)
  }

  introConfiguration () {
    return ffetch.post(`/intro/configuration`)
  }

  log () {
    return ffetch.post(`/log/`)
  }

  logLatest (project, task) {
    return ffetch.post(`/log/${project}/${task}/latest`)
  }

  logList (project) {
    return ffetch.post(`/log/${project}/list`)
  }

  playsqlList (project) {
    return ffetch.post(`/playsql/${project}/list`)
  }

  playsql (project, fileName) {
    return ffetch.post(`/playsql/${project}/update/${fileName}`)
  }

  playsqlMigrationAlter (clientProject) {
    return ffetch.post(`/playsql/migration/${clientProject}/alter`)
  }

  settings (clientProject) {
    return ffetch.post(`/settings/${clientProject}`)
  }

  settingsEdit (projectName) {
    return ffetch.post(`/settings/edit/${projectName}`)
  }

  taskExecute (project, instruction) {
    return ffetch.post(`/task/execute/${project}/${instruction}`)
  }

  taskExecute (project, instruction, env) {
    return ffetch.post(`/task/execute/${project}/${instruction}/${env}`)
  }

  welcomeCreate () {
    return ffetch.post(`/welcome/create`)
  }

}
