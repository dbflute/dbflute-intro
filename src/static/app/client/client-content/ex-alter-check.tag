<ex-alter-check>
  <div class="ui container">
    <h3>AlterCheck</h3>

    <section class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/generator/intro/alterbyintro.html" target="_blank">"AlterCheck"?</a></div>
      <p>A mechanism to validate differential DDL with ReplaceSchema.</p>
    </section>

    <div class="ui divider"></div>

    <!-- Step 1 -->
    <section if="{ !isEditing() }">
      <h4 class="ui header">Step1. Prepare alter sql</h4>

      <h5 class="ui header" if="{ existsCheckedFiles() }">Checked Alter SQL Files ( {checkedZip.fileName} )</h5>
      <div class="ui list" if="{ existsCheckedFiles() }">
        <div class="item" each="{ alterItem in checkedZip.checkedFiles }">
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

      <h5 class="ui header" if="{ existsUnreleasedFiles() }">Unreleased Alter SQL Files</h5>
      <div class="ui list" if="{ existsUnreleasedFiles() }">
        <div class="item" each="{ alterItem in unreleasedDir.checkedFiles }">
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

      <div class="ui placeholder segment">
        <div class="ui two column very relaxed stackable grid">
          <div class="column">
            <h5 class="header">Begin New AlterCheck!!</h5>
            <form class="ui form">
              <div class="fields">
                <div class="inline field">
                  <label>alter-schema-</label>
                  <input type="text" ref="alterNameInput" placeholder="input ticket name">
                  <label>.sql</label>
                </div>
                <div class="inline field">
                  <button class="ui primary button" onclick="{ prepareAlterCheck }">Begin</button>
                </div>
              </div>
            </form>
          </div>
          <div class="middle aligned column">
            <h5 class="header">Fix existing Alter SQL</h5>
            <form class="ui form">
              <div class="fields">
                <div class="three wide field">
                  <a class="ui button" onclick="{ prepareAlterCheck }">Fix</a>
                </div>
              </div>
            </form>
          </div>
        </div>
        <div class="ui vertical divider">
          Or
        </div>
      </div>

    </section>

    <!-- Step 2 -->
    <section show="{ isEditing() }">
      <h4 class="ui header">Step2. Execute AlterCheck</h4>

      <h5 class="ui header">Open Editing Alter SQL Files</h5>
      <div class="ui list">
        <div class="item" each="{ alterItem in editingSqls }">
          <a onclick="{ alterItemClick.bind(this, alterItem) }">{ alterItem.fileName } <span show="{ nowPrepared(alterItem.fileName) }">()</span></a>
          <div show="{ alterItem.show }" class="ui message message-area">
          <pre>
            <code>
              <raw content="{ alterItem.content }"></raw>
            </code>
          </pre>
          </div>
        </div>
      </div>

      <button class="ui button" onclick="{ openAlterDir }"><i class="folder open icon"></i>SQL Files Directory</button>

      <h5 class="ui header">Executor</h5>
      <button class="ui red button" onclick="{ alterCheckTask }"><i class="play icon"></i>Execute AlterCheck</button>

      <h5 class="ui header">Latest AlterCheck Result</h5>
      <button class="ui button blue" show="{ client.hasAlterCheckResultHtml }" onclick="{ openAlterCheckResultHTML }"><i class="linkify icon"></i>Open Check Result HTML</button>
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
    self.projectName = opts.projectName

    // api response
    self.alter = {}

    // view params
    self.ngMark = undefined
    self.editingSqls = []
    self.checkedZip = {
      fileName : '',
      checkedFiles : []
    }
    self.unreleasedDir = {
      checkedFiles : []
    }
    self.preparedFileName = ''

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.updateContents()
    })

    this.prepareAlterInfo = (projectName) => {
      return ApiFactory.alter(projectName).then(resp => {
        self.alter = resp
      })
    }

    this.prepareNgMark = () => {
      self.ngMark = self.alter.ngMark
    }

    this.prepareEditing = () => {
      self.editingSqls = []
      self.alter.editingFiles.forEach(file => {
        self.editingSqls.push({
          fileName: file.fileName,
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
    }

    this.prepareChecked = () => {
      if (!self.alter.checkedZip) {
        return
      }
      self.checkedZip = {}
      self.checkedZip.fileName = self.alter.checkedZip.fileName
      self.checkedZip.checkedFiles = []
      self.alter.checkedZip.checkedFiles.forEach(file => {
        self.checkedZip.checkedFiles.push({
          fileName: file.fileName,
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
    }

    this.prepareUnreleased = () => {
      if (!self.alter.unreleasedDir) {
        return
      }
      self.unreleasedDir = {}
      self.unreleasedDir.checkedFiles = []
      self.alter.unreleasedDir.checkedFiles.forEach(file => {
        self.unreleasedDir.checkedFiles.push({
          fileName: file.fileName,
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
    }

    this.prepareLatestResult = () => {
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'alterCheck' })[0]
      self.updateLatestResult()
    }

    this.updateLatestResult = () => {
      if (!self.latestResult) {
        return
      }
      self.latestResult.latestResult.header.show = false
      if (self.ngMark ==='previous-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Previous DDL.',
          message: 'Retry save previous.',
        }
      } else if (self.ngMark ==='alter-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Alter DDL.',
          message: 'Complete your alter DDL, referring to AlterCheckResultHTML.',
        }
      } else if (self.ngMark ==='next-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Next DDL.',
          message: 'Fix your DDL and data grammatically.',
        }
      }
      self.latestResult.success = {
        title: 'AlterCheck Successfully finished',
      }
      self.latestResult.updateLatestResult()
    }

    this.isEditing = () => {
      return self.editingSqls !== undefined && self.editingSqls.length > 0
    }

    this.existsCheckedFiles = () => {
      return self.checkedZip !== undefined && self.checkedZip.checkedFiles !== undefined && self.checkedZip.checkedFiles.length > 0
    }

    this.existsUnreleasedFiles = () => {
      return self.unreleasedDir !== undefined && self.unreleasedDir.checkedFiles !== undefined && self.unreleasedDir.checkedFiles.length > 0
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
      this.suConfirm('Are you sure to execute AlterCheck task?').then(() => {
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
      const alterFileName = 'alter-schema-' + self.refs.alterNameInput.value + '.sql'
      ApiFactory.prepareAlterSql(self.opts.projectName, alterFileName)
        .then(() => {
          ApiFactory.openAlterDir(self.opts.projectName)
          self.preparedFileName = alterFileName
        }).finally(() => {
          self.updateContents()
        })
    }

    this.updateContents = () => {
      self.prepareAlterInfo(self.projectName).then(() => {
        self.prepareNgMark()
        self.prepareEditing()
        self.prepareChecked()
        self.prepareUnreleased()
        self.prepareLatestResult()
      }).finally(() => {
        self.update()
      })
    }
  </script>
</ex-alter-check>
