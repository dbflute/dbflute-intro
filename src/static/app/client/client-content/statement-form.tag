<statement-form>
  <div class="ui large form">
    <div class="field">
      <label>Preview</label>
      <div class="ui inverted segment">
        <p>{ statement.buildPreview() }</p>
      </div>
    </div>
    <div class="ui divider"></div>
    <div class="grouped fields required">
      <label>Subject</label>
      <p>
        <a if="{ mapType === 'tableMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementifsubject" target="_blank">document</a>
        <a if="{ mapType === 'columnMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementifsubject" target="_blank">document</a>
        /
        <a onclick="{ toggleSubjectHelp }">sample</a></p>
      <toggle-help ref="subjecthelp">
        <h5>Sample</h5>
          <div class="ui two column grid">
            <div class="column">
              <div class="ui inverted segment">
                <div><span class="variable">tableName</span> <span class="comment">// table name on DB</span></div>
                <div><span class="variable">columnName</span> <span class="comment">// column name on DB (only for column)</span></div>
                <div><span class="variable">alias</span> <span class="comment">// table/column alias name e.g. Japanese name</span></div>
              </div>
            </div>
            <div class="column">
              <div class="ui inverted segment">
                <div><span class="variable">dbType</span> <span class="comment">// column's DB type (only for column)</span></div>
                <div><span class="variable">firstDate</span> <span class="comment">// table/column registration date</span></div>
              </div>
            </div>
          </div>
        <a href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#example" target="_blank">more sample</a>
    </toggle-help>
      <div class="ui input field">
        <input class="ui search" type="text" name="subject" ref="subject" value="{ statement.subject }" onchange="{ handleChange }">
      </div>
    </div>
    <div class="grouped fields required">
      <label>Condition</label>
      <p>
        <a if="{ mapType === 'tableMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementnot" target="_blank">document</a>
        <a if="{ mapType === 'columnMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementcolumnis" target="_blank">document</a>
        /
        <a onclick="{ toggleConditionHelp }">sample</a>
      </p>
      <toggle-help ref="conditionhelp">
        <h5>Sample</h5>
        <div class="ui two column grid">
          <div class="column">
            <div class="ui inverted segment">
              <div><span class="variable">$$ALL$$</span> <span class="comment">// all tables/columns are target</span></div>
              <div><span class="variable">prefix:MST_</span> <span class="comment">// that starts with "MST_" are target</span></div>
              <div><span class="variable">suffix:_DAT</span> <span class="comment">// that ends with "_DATE" are target</span></div>
            </div>
          </div>
          <div class="column">
            <div class="ui inverted segment">
              <div><span class="variable">not ABC_DATE</span> <span class="comment">// "ABC_DATE" are NOT target</span></div>
              <div><span class="variable">after:2018/05/03</span> <spab class="comment">// only for "firstDate" subject</spab></div>
            </div>
          </div>
        </div>
        <a href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#example" target="_blank">more sample</a>
      </toggle-help>
      <div class="ui icon input field" each="{ condition, index in statement.conditions }">
        <input type="text" name="condition" ref="condition_{index}" value="{ condition }" onchange="{ handleChange }">
        <i class="delete link icon" if={statement.conditions.length > 1} onclick="{ statement.deleteConditionField.bind(this, index) }"></i>
      </div>
      <div class="ui grid">
        <div class="four wide right floated column">
          <i class="plus link icon" style="float: right" onclick="{ statement.addConditionField }"></i>
          <div class="inline fields" style="float: right" show={statement.conditions.length > 1}>
            <div class="field">
              <div class="ui radio checkbox">
                <input type="radio" name="condition-mode" ref="isAndCondition" checked="checked" onchange="{ handleChange }">
                <label>and</label>
              </div>
            </div>
            <div class="field">
              <div class="ui radio checkbox">
                <input type="radio" name="condition-mode" onchange="{ handleChange }">
                <label>or</label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="grouped fields required">
      <label>Expected</label>
      <p>
        <a if="{ mapType === 'tableMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementthentheme" target="_blank">document</a>
        <a if="{ mapType === 'columnMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementthentheme" target="_blank">document</a>
        /
        <a onclick="{ toggleExpectedHelp }">sample</a>
      </p>
      <toggle-help ref="expectedhelp">
        <div class="ui two column grid">
          <div class="column">
            <h5>Format</h5>
            <div class="ui segment">
              <span class="variable">SUBJECT</span> is (not) <span class="variable">VALUE</span>
            </div>
            <p>or</p>
            <div class="ui segment">
              <span class="variable">THEME</span>
            </div>
          </div>
          <div class="column">
            <h5>Sample</h5>
            <div class="ui inverted segment">
              <div class="comment">// FK constraint name must be FK_[tableName]_...</div>
              <div><span class="variable">fkName</span> is <span class="variable">prefix:FK_$$table$$</span></div>
              <div class="comment">// column DB type must be VARCHAR (only for column)</div>
              <div><span class="variable">dbType</span> is <span class="variable">VARCHAR</span></div>
              <div class="ui divider"></div>
              <div><span class="variable">hasPK</span> <span class="comment">// must has PK constraint</span></div>
              <div><span class="variable">notNull</span> <span class="comment">// must has NotNull constraint</span></div>
            </div>
            <a href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#example" target="_blank">more sample</a>
          </div>
        </div>
      </toggle-help>
      <div class="ui icon input field" each="{ expected, index in statement.expecteds }">
        <input type="text" name="expected" ref="expected_{index}" value="{ expected }" onchange="{ handleChange }">
        <i class="delete link icon" if={statement.expecteds.length > 1} onclick="{ statement.deleteExpectedField.bind(this, index) }"></i>
      </div>
      <div class="ui grid">
        <div class="four wide right floated column">
          <i class="plus link icon" style="float: right" onclick="{ statement.addExpectedField }"></i>
          <div class="inline fields" style="float: right" show={statement.expecteds.length > 1}>
            <div class="field">
              <div class="ui radio checkbox">
                <input type="radio" name="expected-mode" ref="isAndExpected" checked="checked" onchange="{ handleChange }">
                <label>and</label>
              </div>
            </div>
            <div class="field">
              <div class="ui radio checkbox">
                <input type="radio" name="expected-mode" onchange="{ handleChange }">
                <label>or</label>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="field required">
        <label>Supplementary Comment</label>
        <a class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementsupplement" target="_blank">document</a>
        <div class="ui input">
          <input type="text" name="comment" ref="comment" value="{ statement.comment }" onchange="{ handleChange }">
        </div>
      </div>
    </div>
  </div>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory'

    const ApiFactory = new _ApiFactory()
    let self = this
    self.mounted = false
    self.mapType = ''
    self.projectName = ''

    self.on('mount', () => {
      self.mapType = self.opts.type
      self.projectName = self.opts.projectname
      self.mounted = true
    })

    this.handleChange = () => {
      self.saveConditionField()
      self.saveExpectedField()
      self.update()
    }

    this.toggleSubjectHelp = () => {
      self.refs.subjecthelp.toggle()
    }

    this.toggleConditionHelp = () => {
      self.refs.conditionhelp.toggle()
    }


    this.toggleExpectedHelp = () => {
      self.refs.expectedhelp.toggle()
    }

    self.statement = {
      subject: '',
      conditions: [''],
      expecteds: [''],
      comment: '',
      buildPreview: () => {
        return self.buildPreview()
      },
      addConditionField: () => {
        self.statement.conditions.push('')
      },
      deleteConditionField: (index) => {
        self.statement.conditions.splice(index, 1)
      },
      addExpectedField: () => {
        self.statement.expecteds.push('')
      },
      deleteExpectedField: (index) => {
        self.statement.expecteds.splice(index, 1)
      },
    }

    this.saveConditionField = () => {
      for (let i = 0; i < self.statement.conditions.length; i++) {
        self.statement.conditions[i] = self.refs['condition_' + i].value
      }
    }

    this.saveExpectedField = () => {
      for (let i = 0; i < self.statement.expecteds.length; i++) {
        self.statement.expecteds[i] = self.refs['expected_' + i].value
      }
    }

    this.buildPreview = () => {
      if (!self.mounted) {
        return 'if <Subject> is <Condition> then <Expected> => <Supplementary Comment>'
      }

      let subject = self.refs.subject.value ? self.refs.subject.value : '<Subject>'
      let statementPrefix = 'if ' + subject + ' is '
      let conditionOperator = self.refs.isAndCondition.checked ? ' and ' : ' or '

      let conditions = []
      for (let i = 0; i < self.statement.conditions.length; i++) {
        let conditionRef = self.refs['condition_' + i]
        if (conditionRef && conditionRef.value) {
          conditions.push(conditionRef.value)
        }
      }
      let joinedConds = conditions.join(conditionOperator)
      let conditionsStr = joinedConds ? joinedConds : '<Condition>'

      let expectedOperator = self.refs.isAndExpected.checked ? ' and ' : ' or '
      let expecteds = []
      for (let i = 0; i < self.statement.expecteds.length; i++) {
        let expectedRef = self.refs['expected_' + i]
        if (expectedRef && expectedRef.value) {
          expecteds.push(expectedRef.value)
        }
      }
      let joinedExps = expecteds.join(expectedOperator)
      let expectedsStr = joinedExps ? joinedExps : '<Expected>'

      let comment = self.refs.comment.value ? ' => ' + self.refs.comment.value : ' => <Supplementary Comment>'
      return statementPrefix + conditionsStr + ' then ' + expectedsStr + comment
    }

    this.buildBody = () => {
      let conditionOperator = self.refs.isAndCondition.checked ? 'and' : 'or'
      let conditions = []
      for (let i = 0; i < self.statement.conditions.length; i++) {
        if (self.refs['condition_' + i].value) {
          conditions.push(self.refs['condition_' + i].value)
        }
      }
      let expectedOperator = self.refs.isAndExpected.checked ? 'and' : 'or'
      let expecteds = []
      for (let i = 0; i < self.statement.expecteds.length; i++) {
        if (self.refs['expected_' + i].value) {
          expecteds.push(self.refs['expected_' + i].value)
        }
      }
      return {
        type: self.mapType,
        subject: self.refs.subject.value,
        condition: {
          operator: conditionOperator,
          conditions: conditions
        },
        expected: {
          operator: expectedOperator,
          expected: expecteds
        },
        comment: self.refs.comment.value
      }
    }

    this.register = (callback) => {
      let statement = self.buildPreview()
      ApiFactory.registerSchemapolicyStatement(self.projectName, self.buildBody()).then(() => {
        callback(statement)
      })
    }
  </script>
</statement-form>

<toggle-help>
  <div show="{ showed }">
    <div class="ui info message">
      <yield />
    </div>
  </div>

  <style>
    .variable {
      font-style: italic;
      font-weight: bold;
    }
    .comment {
      color: darkgray;
    }
  </style>

  <script>
    let self = this
    self.showed = false
    this.on('mount', () => {
      if (self.opts.showed) {
        self.showed = self.opts.showed
      }
    })

    this.toggle = () => {
      self.showed = !self.showed
      self.update()
    }
  </script>

</toggle-help>
