<ex-alter-check>
  <div class="ui container">
    <h2>AlterCheck</h2>

    <section class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/generator/intro/alterbyintro.html" target="_blank">"AlterCheck"?</a></div>
      <p>A mechanism to validate differential DDL with ReplaceSchema.</p>
    </section>

    <div class="ui divider"></div>

    <!-- Step 1 -->
    <section if="{ !isEditing() }">
      <h3>Step1. Prepare alter sql</h3>
      <alter-check-checked checkedzip="{ checkedZip }" unreleaseddir="{ unreleasedDir }" />
      <alter-check-form ref="altercheckform" projectname="{ opts.projectName }" updatehandler="{ updateBegin }" />
    </section>

    <!-- Step 2 -->
    <alter-check-step2
      if="{ isEditing() }"
      editingsqls="{ editingSqls }"
      preparedfilename="{ preparedFileName }"
      projectname="{ projectName }"
      hasAlterCheckResultHtml="{ client.hasAlterCheckResultHtml }"
    />
  </div>

  <result-modal ref="resultModal" />

  <script>
    let riot = require('riot')
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    let self = this

    self.client = opts.client
    self.projectName = opts.projectName

    // api response
    self.alter = {}

    // view params
    self.ngMarkFile = undefined
    self.editingSqls = []
    self.checkedZip = {
      fileName : '',
      checkedFiles : []
    }
    self.unreleasedDir = {
      checkedFiles : []
    }
    self.preparedFileName = ''
    self.validated = false

    self.state = {
      inputFileName : ''
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.updateContents()
    })

    // TODO cabos remove this because this method replace parent object
    this.prepareClient = () => {
      return ApiFactory.clientOperation(self.projectName).then(resp => {
        self.client = resp
      })
    }

    this.prepareAlterInfo = (projectName) => {
      return ApiFactory.alter(projectName).then(resp => {
        self.alter = resp
      })
    }

    this.prepareNgMark = () => {
      self.ngMarkFile = self.alter.ngMarkFile
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
      const unreleasedFileNames = self.unreleasedDir.checkedFiles.map(checkedFile => checkedFile.fileName.replace('READONLY_', ''))
      self.checkedZip = {}
      self.checkedZip.fileName = self.alter.checkedZip.fileName
      self.checkedZip.checkedFiles = []
      self.alter.checkedZip.checkedFiles.forEach(file => {
        // for hybrid state 0.2.0, 0.2.1
        if (unreleasedFileNames.includes(file.fileName)) {
          return
        }
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
        if (file.fileName.indexOf('.sql') === -1) {
          return
        }
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
      if (self.ngMarkFile.ngMark ==='previous-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Previous DDL.',
          message: 'Retry save previous.',
        }
      } else if (self.ngMarkFile.ngMark ==='alter-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Alter DDL.',
          message: self.ngMarkFile.content.split('\n')[0],
        }
      } else if (self.ngMarkFile.ngMark ==='next-NG') {
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
    //                                                                              Update
    //                                                                              ======
    this.updateBegin = () => {
      self.state = {
        inputFileName : self.refs.altercheckform.refs.beginform.refs.alterNameInput.value
      }
      self.updateContents()
    }

    this.updateContents = () => {
      self.prepareAlterInfo(self.projectName).then(() => {
        self.prepareClient().then(() => {
          self.prepareNgMark()
          self.prepareEditing()
          self.prepareUnreleased()
          self.prepareChecked()
          self.prepareLatestResult()
        }).finally(() => {
          self.update()
        })
      })
    }
  </script>
</ex-alter-check>
