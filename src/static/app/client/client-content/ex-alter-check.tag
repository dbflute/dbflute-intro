<ex-alter-check>
  <div class="ui container">
    <h3>Alter Check</h3>
    <div class="ui list">
      <div show="{ opts.client.hasAlterCheckResultHtml }" class="item"><a onclick="{ openAlterCheckResultHTML }">AlterCheckResultHTML</a></div>
      <div show="{ opts.client.hasAlterCheckResultHtml }" class="item"><a onclick="{ openAlterDir }">Open alter directory</a></div>
      <div show="{ opts.client.ngMark != undefined }" class="item"><a onclick="{ showAlterFailureLog }">Check last execute log</a></div>
    </div>
    <div class="ui list">
      <div show="{ opts.client.ngMark === 'previous-NG' }" class="ui negative message">
        <p>Found problems on <b>Previous DDL.</b><br/>
          Retry save previous.</p>
      </div>
      <div show="{ opts.client.ngMark === 'alter-NG' }" class="ui negative message">
        <p>Found problems on <b>Alter DDL.</b><br/>
          Complete your alter DDL, referring to AlterCheckResultHTML.</p>
      </div>
      <div show="{ opts.client.ngMark === 'next-NG' }" class="ui negative message">
        <p>Found problems on <b>Next DDL.</b><br/>
          Fix your DDL and data grammatically.</p>
      </div>
    </div>
    <button class="ui red button" onclick="{ alterCheckTask }">Alter Check (alter-check)</button>
    <div show="{ stackedAlterSqls !== undefined && stackedAlterSqls.length > 0 }" class="ui list">
      <h4 class="ui header">Stacked Alter SQL List</h4>
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
  </div>

  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>

  <su-modal modal="{ resultModal }" class="large" ref="resultModal">
    <div class="description">
      { opts.modal.message }
    </div>
  </su-modal>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    let self = this
    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareAlterSqls(self.opts.client.stackedAlterSqls)
      self.update()
    })
    this.prepareAlterSqls = (stackedAlterSqls) => {
      self.stackedAlterSqls = []
      stackedAlterSqls.forEach(sql => {
        self.stackedAlterSqls.push({
          fileName: sql.fileName,
          content: Prism.highlight('\n' + sql.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
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
    this.resultModal = {
      closable: true,
      buttons: [
        {
          text: 'CLOSE',
          default: true
        }
      ],
      message: ''
    }
    // -----------------------------------------------------
    //                                                  Show
    //                                                  ----
    this.showResultModal = (message) => {
      self.resultModal.message = message
      self.refs.resultModal.show()
    }
    this.showAlterFailureLog = () => {
      let fileName = 'intro-last-execute-failure-alterCheck.log'
      ApiFactory.getLog(self.opts.projectName, fileName).then((res) => {
        observable.trigger('result', {header: fileName, messages: [res.content], modalSize: 'large'})
      }).catch(() => {
        self.resultModal.message = 'log file not found'
        self.refs.resultModal.show()
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
    this.alterItemClick = (alterItem, e) => {
      alterItem.show = !(alterItem.show)
      return false
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.alterCheckTask = () => {
      this.suConfirm('Are you sure to execute Alter Check task?').then(() => {
        this.task('alterCheck', self.refs.executeModal)
      }).finally(() => {
        ApiFactory.clientOperation(self.opts.projectName).then((response) => {
          self.update({
            client: response
          })
        })
      })
    }
    this.task = (task, modal) => {
      modal.show()
      ApiFactory.task(self.opts.projectName, task).then((response) => {
        const message = response.success ? 'success' : 'failure'
        self.showResultModal(message)
        ApiFactory.clientOperation(self.opts.projectName).then((response) => {
          self.update({
            client: response
          })
        })
      }).finally(() => {
        modal.hide()
      })
    }

  </script>
</ex-alter-check>
