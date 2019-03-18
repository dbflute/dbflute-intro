<ex-schema-policy-check>
  <div class="ui container">
    <h3>Schema Policy Check</h3>
    <button class="ui positive button" onclick="{ goToSchemaPolicySetting }">Edit Policy Check</button>
    <button class="ui primary button" onclick="{ schemaPolicyCheckTask }">Check Policy (schema-policy-check)</button>
    <div class="ui info message">
      <div class="header">What is "Check Policy"?</div>
      <p>The doc task is executed, because there is no SchemaPolicyCheck task.</p>
    </div>
  </div>

  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="description">
      Checking...
    </div>
  </su-modal>

  <result-modal></result-modal>

  <script>
    import _DbfluteTask from '../../common/DbfluteTask'

    const DbfluteTask = new _DbfluteTask()
    let self = this

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareModal()
    })
    this.prepareModal = () => {
      self.resultModal = riot.mount('result-modal')[0]
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.checkModal = {
      closable: false
    }

    // -----------------------------------------------------
    //                                                  GoTo
    //                                                  ----
    this.goToSchemaPolicySetting = () => {
      route(`operate/${self.opts.projectName}/settings/schema-policy`)
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.schemaPolicyCheckTask = () => {
      self.refs.checkModal.show()
      DbfluteTask.task('doc', self.opts.projectName, (message) => {
        self.resultModal.show(message)
      }).finally(() => {
        self.refs.checkModal.hide()
      })
    }
  </script>
</ex-schema-policy-check>
