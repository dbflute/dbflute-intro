<ex-schema-policy-check>
  <div class="ui container">
    <h3>Schema Policy Check</h3>
    <button class="ui primary button" onclick="{ schemaPolicyCheckTask }">Check Policy (schema-policy-check)</button>
    <div class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/genbafit/implfit/schemapolicy/index.html" target="_blank">"Check Policy"?</a></div>
      <p>The doc task is executed, because there is no SchemaPolicyCheck task.</p>
    </div>
    <div class="latest-result">
      <latest-result></latest-result>
    </div>
    <h4>Settings</h4>
    <div class="ui segment" title="SchemaPolicy">
      <div class="ui form">
        <div class="row">
          <div class="column">
            <su-tabset class="three column item" schemapolicy="{ schemaPolicy }" tabtitles="{ tabTitles }">
              <su-tab label="{ opts.tabtitles['wholeMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h5>Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.wholeMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.wholeMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'wholeMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <a class="header">{ theme.name }</a>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
              </su-tab>
              <su-tab label="{ opts.tabtitles['tableMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h5>Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.tableMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.tableMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'tableMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <a class="header">{ theme.name }</a>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <h5>Statement</h5>
                <button class="ui button" onclick="{ parent.parent.showModal }">Add</button>
                <div class="ui divided items segment" if="{opts.schemapolicy.tableMap}">
                  <a class="item" each="{ statement in opts.schemapolicy.tableMap.statementList }">
                    <div class="content">
                      <div class="header">{ statement }</div>
                    </div>
                  </a>
                </div>
              </su-tab>
              <su-tab label="{ opts.tabtitles['columnMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h5>Theme</h5>
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
                <h5>Statement</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.columnMap}">
                  <a class="item" each="{ statement in opts.schemapolicy.columnMap.statementList }">
                    <div class="content">
                      <div class="header">{ statement }</div>
                    </div>
                  </a>
                </div>
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

  <su-modal modal="{ statementModal }" statement="{ statement }" class="large" ref="statementModal">
    <div class="ui form">
      <div class="field">
        <label>Subject</label>
        <input class="ui search" type="text" name="subject" ref="subject" value="{ opts.statement.subject }">
      </div>
      <div class="grouped fields">
        <label>Condition</label>
        <div class="ui icon input field" each="{ condition, index in opts.statement.conditions }">
          <input type="text" name="condition" ref="condition_{index}" value="{ condition }">
          <i class="delete link icon" if={opts.statement.conditions.length > 1} onclick="{ opts.statement.deleteConditionField.bind(this, index) }"></i>
        </div>
        <div class="ui grid">
          <div class="four wide right floated column">
            <i class="plus link icon" style="float: right" onclick="{ opts.statement.addConditionField }" ></i>
            <div class="inline fields" style="float: right" show={opts.statement.conditions.length > 1}>
              <div class="field">
                <div class="ui radio checkbox">
                  <input type="radio" name="condition-mode" ref="isAndCondition" checked="checked">
                  <label>and</label>
                </div>
              </div>
              <div class="field">
                <div class="ui radio checkbox">
                  <input type="radio" name="condition-mode">
                  <label>or</label>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="grouped fields">
        <label>Expected</label>
        <div class="ui icon input field" each="{ expected, index in opts.statement.expecteds }">
          <input type="text" name="expected" ref="expected_{index}" value="{ expected }">
          <i class="delete link icon" if={opts.statement.expecteds.length > 1} onclick="{ opts.statement.deleteExpectedField.bind(this, index) }"></i>
        </div>
        <div class="ui grid">
          <div class="four wide right floated column">
              <i class="plus link icon" style="float: right" onclick="{ opts.statement.addExpectedField }" ></i>
              <div class="inline fields" style="float: right" show={opts.statement.expecteds.length > 1}>
                <div class="field">
                  <div class="ui radio checkbox">
                    <input type="radio" name="expected-mode" ref="isAndExpected" checked="checked">
                    <label>and</label>
                  </div>
                </div>
                <div class="field">
                  <div class="ui radio checkbox">
                    <input type="radio" name="expected-mode">
                    <label>or</label>
                  </div>
                </div>
              </div>
          </div>
        </div>
        <div class="field">
          <label>Error Message</label>
          <input type="text" name="comment" ref="comment" value="{ opts.statement.comment }">
        </div>

        <div class="ui divider"></div>
        <div class="field">
          <label>preview</label>
          <div class="ui inverted segment">
            <p>{ opts.statement.buildStatement() }</p>
          </div>
        </div>
      </div>
    </div>
  </su-modal>

  <style>
    .latest-result {
      margin-top: 1em;
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

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.prepareSchemaPolicy(opts.projectName)
      self.prepareComponents()
      self.registerModalEvent()
    })

    this.buildStatement = () => {
      let sModelRefs = self.refs.statementModal.refs
      let subject = sModelRefs.subject.value ? sModelRefs.subject.value : '<Subject>'
      let statementPrefix = 'if ' + subject + ' is '

      let conditionOperator = sModelRefs.isAndCondition.checked ? ' and ' : ' or '
      let conditions = []
      for (let i = 0; i < self.statement.conditions.length; i++) {
        conditions.push(sModelRefs['condition_' + i].value)
      }
      let joinedConds = conditions.join(conditionOperator)
      let conditionsStr = joinedConds ? joinedConds : '<Condition>'

      let expectedOperator = sModelRefs.isAndExpected.checked ? ' and ' : ' or '
      let expecteds = []
      for (let i = 0; i < self.statement.expecteds.length; i++) {
        expecteds.push(sModelRefs['expected_' + i].value)
      }
      let joinedExps = expecteds.join(expectedOperator)
      let expectedsStr = joinedExps ? joinedExps : '<Expected>'

      let comment = sModelRefs.comment.value ? ' => ' + sModelRefs.comment.value : ''
      return statementPrefix + conditionsStr + ' then ' + expectedsStr + comment
    }

    this.registerModalEvent = () => {
      self.refs.statementModal.on('submit', () => {
        // TODO
      })
    }

    this.showModal = () => {
      self.refs.statementModal.show()
    }

    this.statement = {
      subject: '',
      conditions: [''],
      expecteds: [''],
      comment: '',
      buildStatement: () => {
        return self.buildStatement()
      },
      addConditionField: () => {
        self.statement.conditions.push('')
      },
      saveConditionField: () => {
        for (let i = 0; i < self.statement.conditions.length; i++) {
          self.statement.conditions[i] = self.refs.statementModal.refs['condition_' + i].value
        }
      },
      deleteConditionField: (index) => {
        self.statement.saveConditionField()
        self.statement.conditions.splice(index, 1)
      },
      addExpectedField: () => {
        self.statement.expecteds.push('')
      },
      saveExpectedField: () => {
        for (let i = 0; i < self.statement.expecteds.length; i++) {
          self.statement.expecteds[i] = self.refs.statementModal.refs['expected_' + i].value
        }
      },
      deleteExpectedField: (index) => {
        self.statement.saveExpectedField()
        self.statement.expecteds.splice(index, 1)
      },
    }

    this.statementModal = {
      header: 'Add Statement',
      buttons: [{
        text: 'Submit',
        action: 'submit',
        type: 'primary',
        icon: 'checkmark'
      }, {
        text: 'Cancel'
      }],
    }

    this.prepareComponents = () => {
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'doc' })[0]
      self.updateLatestResult(self.client)
    }

    this.updateLatestResult = (client) => {
      if (!self.latestResult) {
        return
      }
      if (client.violatesSchemaPolicy) {
        self.latestResult.failure = {
          title: 'Result: Failure',
          link: {
            message: 'Check Schema Policy Violation (from DBFluteEngine 1.2.0).',
            clickAction: self.openSchemaHTML
          }
        }
      }
      self.latestResult.updateLatestResult()
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

    this.tabTitles = {
      wholeMap : 'Whole Schema Policy',
      tableMap : 'Table Schema Policy',
      columnMap : 'Column Schema Policy'
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareSchemaPolicy = (projectName) => {
      ApiFactory.schemaPolicy(projectName).then(json => {
        self.schemaPolicy = json
        self.update()
      })
    }

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
