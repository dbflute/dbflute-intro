<ex-alter-check>
  <div class="ui container">
    <h3>Alter Check</h3>
    <div class="ui list">
      <div show="{ opts.client.hasAlterCheckResultHtml }" class="item"><a onclick="{ openAlterCheckResultHTML }">AlterCheckResultHTML</a></div>
      <div show="{ opts.client.hasAlterCheckResultHtml }" class="item"><a onclick="{ openAlterDir }">Open alter directory</a></div>
    </div>
    <button class="ui red button" onclick="{ alterCheckTask }">Alter Check (alter-check)</button>
    <div show="{ editingAlterSqls !== undefined && editingAlterSqls.length > 0 }" class="ui list">
      <h4 class="ui header">Checked Alter SQL List</h4>
      <ul>
        <div each="{ alterItem in editingAlterSqls }">
          <li>
            <a onclick="{ alterItemClick.bind(this, alterItem) }">{ alterItem.fileName }</a>
          </li>
          <div show="{ alterItem.show }" class="ui message message-area">
            <pre>
              <code>
                <raw content="{ alterItem.content }"></raw>
              </code>
            </pre>
          </div>
        </div>
      </ul>
    </div>
    <div show="{ stackedAlterSqls !== undefined && stackedAlterSqls.length > 0 }" class="ui list">
      <h4 class="ui header">Checked Alter SQL List</h4>
      <ul>
        <div each="{ alterItem in stackedAlterSqls }">
          <li>
            <a onclick="{ alterItemClick.bind(this, alterItem) }">{ alterItem.fileName }</a>
          </li>
          <div show="{ alterItem.show }" class="ui message message-area">
            <pre>
              <code>
                <raw content="{ alterItem.content }"></raw>
              </code>
            </pre>
          </div>
        </div>
      </ul>
    </div>
    <div class="latest-result">
      <latest-result></latest-result>
    </div>
  </div>

  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>

  <result-modal></result-modal>

  <style>
    .latest-result {
      margin-top: 1em;
    }
  </style>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()
    let self = this

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareEditingAlterSqls(self.opts.client.editingAlterSqls)
      self.prepareStackedAlterSqls(self.opts.client.stackedAlterSqls)
      self.prepareComponents()
    })

    this.prepareEditingAlterSqls = (editingAlterSqls) => {
      self.editingAlterSqls = []
      editingAlterSqls.forEach(sql => {
        self.editingAlterSqls.push({
          fileName: sql.fileName,
          content: Prism.highlight('\n' + sql.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
      self.update()
    }

    this.prepareStackedAlterSqls = (stackedAlterSqls) => {
      self.stackedAlterSqls = []
      stackedAlterSqls.forEach(sql => {
        self.stackedAlterSqls.push({
          fileName: sql.fileName,
          content: Prism.highlight('\n' + sql.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
      self.update()
    }

    this.prepareComponents = () => {
      self.resultModal = riot.mount('result-modal')[0]
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'alterCheck' })[0]
      self.updateLatestResultText(opts.client)
    }

    this.updateLatestResultText = (client) => {
      if (!self.latestResult) {
        return
      }
      if (client.ngMark ==='previous-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Previous DDL.',
          message: 'Retry save previous.',
        }
      } else if (client.ngMark ==='alter-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Alter DDL.',
          message: 'Complete your alter DDL, referring to AlterCheckResultHTML.',
        }
      } else if (client.ngMark ==='next-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Next DDL.',
          message: 'Fix your DDL and data grammatically.',
        }
      }
      self.latestResult.success = {
        title: 'Alter Check Successfully finished',
      }
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.executeModal = {
      closable: false
    }

    // -----------------------------------------------------
    //                                                  Show
    //                                                  ----
    this.showAlterFailureLog = () => {
      let fileName = 'intro-last-execute-failure-alterCheck.log'
      ApiFactory.getLog(self.opts.projectName, fileName).then((res) => {
        observable.trigger('result', {header: fileName, messages: [res.content], modalSize: 'large'})
      }).catch(() => {
        self.resultModal.show('log file not found')
      })
    }

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    this.openAlterCheckResultHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/altercheckresulthtml/')
    }

    this.openAlterDir = () => {
      ApiFactory.openAlterDir(self.opts.projectName)
    }

    this.alterItemClick = (alterItem) => {
      alterItem.show = !(alterItem.show)
      return false
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.alterCheckTask = () => {
      this.suConfirm('Are you sure to execute Alter Check task?').then(() => {
        self.refs.executeModal.show()
        DbfluteTask.task('alterCheck', self.opts.projectName, (message) => {
          self.resultModal.show(message)
        }).finally(() => {
          self.refs.executeModal.hide()
        })
      }).finally(() => {
        ApiFactory.clientOperation(self.opts.projectName).then((response) => {
          self.update({
            client: response
          })
        })
        self.latestResult.updateLatestResult()
        self.updateLatestResultText(client)
      })
    }
  </script>
</ex-alter-check>
