<ex-replace-schema>
  <div class="ui container">
    <h2>Replace Schema</h2>
    <button class="ui red button" onclick="{ replaceSchemaTask }">Execute ReplaceSchema</button>
    <button class="ui button" onclick="{ openDataDir }"><i class="folder open icon"></i>TestData Directory</button>
    <div class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/generator/task/doc/schemasynccheck.html" target="_blank">"Replace Schema"?</a></div>
      <p>A mechanism to automates (re) building your DB schema.</p>
    </div>
    <div class="latest-result">
      <latest-result></latest-result>
    </div>
    <h3>Play SQL</h3>
    <div class="ui segment" title="PlaySQL">
      <su-dropdown items="{ playsqlDropDownItems }" ref="dropdown"></su-dropdown>
      <div class="ui message message-area">
        <pre>
          <code class="language-sql">
            <raw content="{ refs.dropdown.value }"></raw>
          </code>
        </pre>
      </div>
    </div>
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
    const defaultItem = [{label: '-', value: null}]
    let self = this
    this.playsqlDropDownItems = {}

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
      self.preparePlaysql(self.opts.projectName)
      self.prepareComponents()
    })

    this.prepareSettings = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
    }

    this.preparePlaysql = (projectName) => {
      ApiFactory.playsqlBeanList(projectName).then(json => {
        const playsqlDropDownItems = json.map(obj => ({
          label: obj.fileName,
          value: `<span style="display: none;">${obj.fileName}</span>` + Prism.highlight(obj.content, Prism.languages.sql, 'sql')
        }))
        self.playsqlDropDownItems = defaultItem.concat(playsqlDropDownItems)
        self.update()
      })
    }

    this.prepareComponents = () => {
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'replaceSchema' })[0]
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
    this.openDataDir = () => {
      ApiFactory.openDataDir(self.opts.projectName)
    }
    

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.replaceSchemaTask = () => {
      this.suConfirm('Are you sure to execute Replace Schema task?').then(() => {
        self.refs.executeModal.show()
        DbfluteTask.task('replaceSchema', self.opts.projectName, (message) => {
          self.refs.resultModal.show(message)
        }).finally(() => {
          self.refs.executeModal.hide()
          self.latestResult.updateLatestResult()
        })
      })
    }
  </script>
</ex-replace-schema>
