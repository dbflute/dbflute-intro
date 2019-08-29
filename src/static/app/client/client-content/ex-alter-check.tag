<ex-alter-check>
  <div class="ui container">
    <h3>Alter Check</h3>

    <section class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/generator/task/replaceschema/altercheck.html" target="_blank">"Alter Check"?</a></div>
      <p>A mechanism to validate differential DDL with ReplaceSchema.</p>
    </section>

    <div class="ui divider"></div>

    <section if="{ !editing }">
      <h4 class="ui header">Step1. Prepare alter sql</h4>

      <h5 class="ui header">Checked Alter SQL List</h5>
      <div class="ui list">
        <div class="item" each="{ alterItem in stackedAlterSqls }">
          <a onclick="{ alterItemClick.bind(this, alterItem) }">{ alterItem.fileName }</a>
          <div show="{ alterItem.show }" class="ui message message-area">
          <pre>
            <code>
              <raw content="{ alterItem.content }"></raw>
            </code>
          </pre>
          </div>
        </div>
      </div>

      <form class="ui form">
        <div class="fields">
          <div class="seven wide inline field">
            <label>alter_schema_</label>
            <input type="text" ref="alterNameInput" placeholder="input your ticket name and so on">
            <label>.sql</label>
          </div>
          <div class="three wide field">
            <button class="ui primary button" onclick="{ prepareAlterCheck }">Prepare Alter Check</button>
          </div>
        </div>
      </form>
    </section>

    <section if="{ editing }">
      <h4 class="ui header">Step2. Execute Alter Check</h4>

      <h5 class="ui header">Editing Alter SQL List</h5>
      <div class="ui list">
        <div class="item" each="{ alterItem in editingAlterSqls }">
          <a onclick="{ alterItemClick.bind(this, alterItem) }">{ alterItem.fileName }</a>
          <div show="{ alterItem.show }" class="ui message message-area">
          <pre>
            <code>
              <raw content="{ alterItem.content }"></raw>
            </code>
          </pre>
          </div>
        </div>
      </div>

      <a><button class="ui button"><i class="folder open icon"></i>Open Editing Alter Directory</button></a>
      <button class="ui red button" onclick="{ alterCheckTask }">Execute Alter Check</button>

      <div class="latest-result">
        <latest-result></latest-result>
      </div>
    </section>
  </div>

  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>

  <result-modal ref="resultModal"></result-modal>

  <style>
    .latest-result {
      margin-top: 1em;
    }
  </style>

  <script>
    let riot = require('riot')
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()
    let self = this

    self.client = opts.client
    self.editing = false
    self.editingAlterSqls = []
    self.stackedAlterSqls = []

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareEditingAlterSqls(self.client.editingAlterSqls)
      self.prepareStackedAlterSqls(self.client.stackedAlterSqls)
      self.prepareComponents()
      self.update()
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
      if (self.editingAlterSqls.length > 0) {
        self.editing = true
      }
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
    }

    this.prepareComponents = () => {
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'alterCheck' })[0]
      self.latestResult.latestResult.header.show = false
      self.updateLatestResult(self.client)
    }

    this.updateLatestResult = (client) => {
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
      self.latestResult.updateLatestResult()
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
          self.refs.resultModal.show(message)
        }).finally(() => {
          self.updateContents()
          self.refs.executeModal.hide()
        })
      })
    }

    this.prepareAlterCheck = () => {
      const alterFileName = 'alter_schema_' + self.refs.alterNameInput.value + '.sql'
      ApiFactory.prepareAlterCheck(self.opts.projectName)
        .then(() => ApiFactory.createAlterSql(self.opts.projectName, alterFileName))
        .then(() => ApiFactory.openAlterDir(self.opts.projectName))
        .finally(() => self.updateContents())
    }

    this.updateContents = () => {
      ApiFactory.clientOperation(self.opts.projectName).then((response) => {
        self.client = response
        self.updateLatestResult(self.client)
        self.prepareEditingAlterSqls(self.client.editingAlterSqls)
        self.prepareStackedAlterSqls(self.client.stackedAlterSqls)
        self.update()
      })
    }
  </script>
</ex-alter-check>
