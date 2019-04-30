import _ApiFactory from '../common/factory/ApiFactory.js'

const ApiFactory = new _ApiFactory()

export default class DbfluteTask {
  task(task, projectName, callback) { // callback: (message: string) => void
    return ApiFactory.task(projectName, task).then((response) => {
      const message = response.success ? 'success' : 'failure'
      callback(message)
      ApiFactory.clientOperation(projectName).then((response) => {
        self.update({
          client: response
        })
      })
    })
  }
}
