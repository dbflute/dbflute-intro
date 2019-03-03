<ex-schema-policy-check>
  <h3>Schema Policy Check</h3>
  <button class="ui positive button" onclick="{ goToSchemaPolicySetting }">Edit Policy Check</button>
  <button class="ui primary button" onclick="{ schemaPolicyCheckTask }">Check Policy (schema-policy-check)</button>
  <div class="ui info message">
    <div class="header">What is "Check Policy"?</div>
    <p>The doc task is executed, because there is no SchemaPolicyCheck task.</p>
  </div>

  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="description">
      Checking...
    </div>
  </su-modal>

  <su-modal modal="{ resultModal }" class="large" ref="resultModal">
    <div class="description">
      { opts.modal.message }
    </div>
  </su-modal>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    let self = this
    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.checkModal = {
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

    // -----------------------------------------------------
    //                                                  GoTo
    //                                                  ----
    this.goToSchemaPolicySetting = () => {
      route(`settings/${self.opts.projectName}?activeTab=SchemaPolicy`)
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.schemaPolicyCheckTask = () => {
      this.task('doc', self.refs.checkModal)
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
</ex-schema-policy-check>
