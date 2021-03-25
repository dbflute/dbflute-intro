<ex-schema-policy-check>
  <div class="ui container">
    <h2>Schema Policy Check</h2>
    <button class="ui primary button" onclick="{ schemaPolicyCheckTask }">Execute SchemaPolicyCheck ( doc task )</button>
    <div class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/genbafit/implfit/schemapolicy/index.html" target="_blank">"SchemaPolicyCheck"?</a></div>
      <p>A checking tool for DB design, embedded with <a href="http://dbflute.seasar.org/ja/manual/function/generator/task/doc/index.html" target="_blank">Doc task</a>.</p>
    </div>
    <div class="latest-result">
      <latest-result></latest-result>
    </div>
    <h3>Policy Settings</h3>
    <div class="ui segment" title="SchemaPolicy">
      <div class="ui form">
        <div class="row">
          <div class="column">
            <su-tabset
              class="three column item"
              schemapolicy="{ schemaPolicy }"
              tabtitles="{ tabTitles }"
              projectname="{ projectName }"
              onregistersuccess="{ onRegisterSuccess }"
            >
              <su-tab label="{ opts.tabtitles['wholeMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h5 class="spolicy-category">Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.wholeMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.wholeMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'wholeMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <div class="header">{ theme.name }</div>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
              </su-tab>
              <su-tab
                label="{ opts.tabtitles['tableMap']}"
                schemapolicy="{ opts.schemapolicy }"
                projectname="{ opts.projectname }"
                onregistersuccess="{ opts.onregistersuccess }">
                <h5 class="spolicy-category">Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.tableMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.tableMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'tableMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <div class="header">{ theme.name }</div>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <h5 class="spolicy-category">Statement</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.tableMap}">
                  <div class="statement item" each="{ statement in opts.schemapolicy.tableMap.statementList }">
                    <div class="statement content">
                      <div class="header" if="{!parent.parent.parent.isIncludeComment(statement)}">
                        { statement }
                      </div>
                      <div class="header" if="{parent.parent.parent.isIncludeComment(statement)}">
                        { parent.parent.parent.extractStatement(statement) }
                      </div>
                      <div if="{parent.parent.parent.isIncludeComment(statement)}">
                        <span class="frm">&#61&gt;{ parent.parent.parent.extractComment(statement) }</span>
                      </div>
                    </div>
                    <i class="statement delete link icon" onclick="{ parent.parent.parent.deleteStatement.bind(this, 'tableMap', statement) }"></i>
                  </div>
                </div>
                <schema-policy-check-statement-form-wrapper
                  formtype="tableMap"
                  projectname="{ opts.projectname }"
                  onregistersuccess="{ opts.onregistersuccess }"
                />
              </su-tab>
              <su-tab
                label="{ opts.tabtitles['columnMap']}"
                schemapolicy="{ opts.schemapolicy }"
                projectname="{ opts.projectname }"
                onregistersuccess="{ opts.onregistersuccess }"
              >
                <h5 class="spolicy-category">Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.columnMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.columnMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'columnMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <a class="header">{ theme.name }</a>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <h5 class="spolicy-category">Statement</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.columnMap}">
                  <div class="statement item" each="{ statement in opts.schemapolicy.columnMap.statementList }">
                    <div class="statement content">
                      <div class="header" if="{!parent.parent.parent.isIncludeComment(statement)}">
                        { statement }
                      </div>
                      <div class="header" if="{parent.parent.parent.isIncludeComment(statement)}">
                        { parent.parent.parent.extractStatement(statement) }
                      </div>
                      <div if="{parent.parent.parent.isIncludeComment(statement)}">
                        <span class="frm">&#61&gt;{ parent.parent.parent.extractComment(statement) }</span>
                      </div>
                    </div>
                    <i class="statement delete link icon" onclick="{ parent.parent.parent.deleteStatement.bind(this, 'columnMap', statement) }"></i>
                  </div>
                </div>
                <schema-policy-check-statement-form-wrapper
                  formtype="columnMap"
                  projectname="{ opts.projectname }"
                  onregistersuccess="{ opts.onregistersuccess }"
                />
              </su-tab>
            </su-tabset>
          </div>
        </div>
      </div>
    </div>
  </div>

  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="content" if="{opts.modal.status === 'Check'}">
      <div class="header" >{ opts.modal.message }</div>
    </div>
    <div class="ui positive message" if="{opts.modal.status === 'Success'}">
      <h4>{ opts.modal.message }</h4>
    </div>
    <div class="ui negative message" if="{opts.modal.status === 'Failure'}">
      <h4>{ opts.modal.message }</h4>
    </div>
  </su-modal>

  <su-modal modal="{ tableMapStatementModal }" projectName="{ projectName }" class="large" ref="tableMapStatementModal">
    <statement-form projectName="{ opts.projectname }" type="tableMap" ref="form"></statement-form>
  </su-modal>

  <su-modal modal="{ columnMapStatementModal }" projectName="{ projectName }" class="large" ref="columnMapStatementModal">
    <statement-form projectName="{ opts.projectname }" type="columnMap" ref="form"></statement-form>
  </su-modal>

  <style>
    .latest-result {
      margin-top: 1em;
    }
    .statement.delete.link.icon {
      display: none;
    }
    .statement.item:hover .statement.delete.link.icon {
      display: inline-block;
    }
  </style>

  <script>
    let riot = require('riot')
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()

    let self = this
    self.client = opts.client
    this.schemaPolicy = {}
    this.projectName = ''

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('before-mount', () => {
      self.projectName = opts.projectName
    })

    this.on('mount', () => {
      this.prepareSchemaPolicy(opts.projectName)
      this.prepareComponents(opts.projectName)
      this.registerModalEvent()
    })

    this.prepareSchemaPolicy = (projectName) => {
      self.fetchSchemaPolicy(projectName)
    }

    this.prepareComponents = (projectName) => {
      self.latestResult = riot.mount('latest-result', { projectName: projectName, task: 'doc' })[0]
      self.updateLatestResult(self.client)
    }

    this.registerModalEvent = () => {
      self.refs.tableMapStatementModal.on('submit', () => {
        self.refs.tableMapStatementModal.refs.form.register((statement) => {
          self.schemaPolicy.tableMap.statementList.push(statement)
          self.refs.tableMapStatementModal.hide()
          self.update()
        })
      })
      self.refs.columnMapStatementModal.on('submit', () => {
        self.refs.columnMapStatementModal.refs.form.register((statement) => {
          self.schemaPolicy.columnMap.statementList.push(statement)
          self.refs.columnMapStatementModal.hide()
          self.update()
        })
      })
    }

    this.onRegisterSuccess = () => {
      self.fetchSchemaPolicy(opts.projectName)
    }

    this.showTableMapModal = () => {
      self.refs.tableMapStatementModal.show()
    }

    this.showColumnMapModal = () => {
      self.refs.columnMapStatementModal.show()
    }

    this.updateLatestResult = (client) => {
      if (!self.latestResult) {
        return
      }
      if (client.violatesSchemaPolicy) {
        self.latestResult.failure = {
          title: 'Result: Failure',
          link: {
            message: 'Open your SchemaPolicyCheck result (HTML)',
            clickAction: self.openSchemaHTML
          }
        }
      }
      self.latestResult.updateLatestResult()
    }

    this.isIncludeComment = (statement) => {
      return statement.includes('=>')
    }

    this.extractStatement = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return statement
      }
      const splitStatements = statement.split('=>')
      return splitStatements[0]
    }

    this.extractComment = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return ''
      }
      const splitStatements = statement.split('=>')
      return splitStatements[1]
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.checkModal = {
      header : 'SchemaPolicyCheck',
      status : 'Check',
      closable: false,
      check : function() {
        self.checkModal.status = 'Check'
        self.checkModal.message = 'Checking...'
        self.checkModal.closable = false
      },
      success : function() {
        self.checkModal.status = 'Success'
        self.checkModal.message = 'Success!!'
        self.checkModal.closable = true
      },
      fail : function () {
        self.checkModal.status = 'Failure'
        self.checkModal.message = 'Failure: You need check violation.'
        self.checkModal.closable = true
      }
    }

    this.tableMapStatementModal = {
      header: 'Add TableMap Statement',
      buttons: [{
        text: 'Submit',
        action: 'submit',
        type: 'primary',
        icon: 'checkmark',
        closable: false
      }, {
        text: 'Cancel'
      }],
    }

    this.columnMapStatementModal = {
      header: 'Add ColumnMap Statement',
      buttons: [{
        text: 'Submit',
        action: 'submit',
        type: 'primary',
        icon: 'checkmark',
        closable: false
      }, {
        text: 'Cancel'
      }],
    }

    this.tabTitles = {
      wholeMap : 'Whole Schema Policy',
      tableMap : 'Table Schema Policy',
      columnMap : 'Column Schema Policy'
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.editSchemaPolicyMap = (targetMap, typeCode) => {
      const targetTheme = this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode)
      const toggledActiveStatus = !targetTheme.isActive

      let body = {
        wholeMap : {themeList : []},
        tableMap : {themeList : []},
        columnMap : {themeList : []}
      }
      body[targetMap].themeList = [{typeCode : typeCode, isActive : toggledActiveStatus}]

      ApiFactory.editSchemaPolicy(opts.projectName, body).then(() => {
        this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode).isActive = toggledActiveStatus
        self.update()
      })
    }
    // ===================================================================================
    //                                                                           Operation
    //                                                                           =========
    this.fetchSchemaPolicy = (projectName) => {
      ApiFactory.schemaPolicy(projectName).then(json => {
        self.schemaPolicy = json
        self.update()
      })
    }

    this.deleteStatement = (mapType, statement) => {
      self.suConfirm('Are you sure to delete this statement?').then(() => {
        ApiFactory.deleteSchemapolicyStatement(opts.projectName, {mapType: mapType, statement: statement}).then(() => {
          self.fetchSchemaPolicy(self.projectName)
          self.update()
        })
      })
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.schemaPolicyCheckTask = () => {
      self.refs.checkModal.show()
      self.checkModal.check()
      DbfluteTask.task('doc', self.opts.projectName, (message) => {
        if (message === 'success') {
          self.checkModal.success()
        } else if (message === 'failure'){
          self.checkModal.fail()
        }
      }).finally(() => {
        if (self.checkModal.status === 'Check') {
          self.refs.checkModal.hide()
        }
        ApiFactory.clientOperation(self.opts.projectName).then((response) => {
          self.client = response
          self.updateLatestResult(self.client)
          self.update()
        })
      })
    }

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    this.openSchemaHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/schemahtml/')
    }
  </script>
</ex-schema-policy-check>
