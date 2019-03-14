<ex-replace-schema>
  <div class="ui container">
    <h3>Replace Schema</h3>
    <button class="ui red button" onclick="{ replaceSchemaTask }">Replace Schema (replace-schema)</button>
    <h4 class="ui header">Play SQL</h4>
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
    const defaultItem = [{label: '-', value: null}]
    let self = this
    this.playsqlDropDownItems = {}

    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
      self.preparePlaysql(self.opts.projectName)
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

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.replaceSchemaTask = () => {
      this.suConfirm('Are you sure to execute Replace Schema task?').then(() => {
        this.task('replaceSchema', self.refs.executeModal)
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
</ex-replace-schema>
