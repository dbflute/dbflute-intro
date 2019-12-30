<statement-form>
  <div class="ui large form">
    <div class="field">
      <label>Preview</label>
      <div class="ui inverted segment">
        <p>{ statement.buildPreview() }</p>
      </div>
    </div>
    <div class="ui divider"></div>
    <div class="field">
      <label>Subject
        <a if="{ mapType === 'tableMap' }" class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementifsubject" target="_blank"><i class="info circle icon"></i></a>
        <a if="{ mapType === 'columnMap' }" class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementifsubject" target="_blank"><i class="info circle icon"></i></a>
      </label>
      <input class="ui search" type="text" name="subject" ref="subject" value="{ statement.subject }" onchange="{ handleChange }">
    </div>
    <div class="grouped fields">
      <label>Condition
        <a if="{ mapType === 'tableMap' }" class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementnot" target="_blank"><i class="info circle icon"></i></a>
        <a if="{ mapType === 'columnMap' }" class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementcolumnis" target="_blank"><i class="info circle icon"></i></a>
      </label>
      <div class="ui icon input field" each="{ condition, index in statement.conditions }">
        <input type="text" name="condition" ref="condition_{index}" value="{ condition }" onchange="{ handleChange }">
        <i class="delete link icon" if={statement.conditions.length > 1} onclick="{ statement.deleteConditionField.bind(this, index) }"></i>
      </div>
      <div class="ui grid">
        <div class="four wide right floated column">
          <i class="plus link icon" style="float: right" onclick="{ statement.addConditionField }" ></i>
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
    <div class="grouped fields">
      <label>Expected
        <a if="{ mapType === 'tableMap' }" class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementthentheme" target="_blank"><i class="info circle icon"></i></a>
        <a if="{ mapType === 'columnMap' }" class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementthentheme" target="_blank"><i class="info circle icon"></i></a>
      </label>
      <div class="ui icon input field" each="{ expected, index in statement.expecteds }">
        <input type="text" name="expected" ref="expected_{index}" value="{ expected }" onchange="{ handleChange }">
        <i class="delete link icon" if={statement.expecteds.length > 1} onclick="{ statement.deleteExpectedField.bind(this, index) }"></i>
      </div>
      <div class="ui grid">
        <div class="four wide right floated column">
          <i class="plus link icon" style="float: right" onclick="{ statement.addExpectedField }" ></i>
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
      <div class="field">
        <label>Supplementary Comment
          <a class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementsupplement" target="_blank"><i class="info circle icon"></i></a>
        </label>
        <input type="text" name="comment" ref="comment" value="{ statement.comment }" onchange="{ handleChange }">
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
