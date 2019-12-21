<statement-form>
  <div class="ui form">
    <div class="field">
      <label>Preview</label>
      <div class="ui inverted segment">
        <p>{ statement.buildPreview() }</p>
      </div>
    </div>
    <div class="ui divider"></div>
    <div class="field">
      <label>Subject</label>
      <input class="ui search" type="text" name="subject" ref="subject" value="{ statement.subject }">
    </div>
    <div class="grouped fields">
      <label>Condition</label>
      <div class="ui icon input field" each="{ condition, index in statement.conditions }">
        <input type="text" name="condition" ref="condition_{index}" value="{ condition }">
        <i class="delete link icon" if={statement.conditions.length > 1} onclick="{ statement.deleteConditionField.bind(this, index) }"></i>
      </div>
      <div class="ui grid">
        <div class="four wide right floated column">
          <i class="plus link icon" style="float: right" onclick="{ statement.addConditionField }" ></i>
          <div class="inline fields" style="float: right" show={statement.conditions.length > 1}>
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
      <div class="ui icon input field" each="{ expected, index in statement.expecteds }">
        <input type="text" name="expected" ref="expected_{index}" value="{ expected }">
        <i class="delete link icon" if={statement.expecteds.length > 1} onclick="{ statement.deleteExpectedField.bind(this, index) }"></i>
      </div>
      <div class="ui grid">
        <div class="four wide right floated column">
          <i class="plus link icon" style="float: right" onclick="{ statement.addExpectedField }" ></i>
          <div class="inline fields" style="float: right" show={statement.expecteds.length > 1}>
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
        <label>Supplementary Comment</label>
        <input type="text" name="comment" ref="comment" value="{ statement.comment }">
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
      self.addChangeEvent()
      self.mounted = true
    })

    self.on('updated', () => {
      self.addChangeEvent()
    })

    this.addChangeEvent = () => {
      [].forEach.call(document.querySelectorAll('statement-form input'), (input) => {
        input.addEventListener('change', () => self.update())
      })
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
      saveConditionField: () => {
        for (let i = 0; i < self.statement.conditions.length; i++) {
          self.statement.conditions[i] = self.refs['condition_' + i].value
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
          self.statement.expecteds[i] = self.refs['expected_' + i].value
        }
      },
      deleteExpectedField: (index) => {
        self.statement.saveExpectedField()
        self.statement.expecteds.splice(index, 1)
      },
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
        if (self.refs['condition_' + i]) {
          conditions.push(self.refs['condition_' + i].value)
        }
      }
      let joinedConds = conditions.join(conditionOperator)
      let conditionsStr = joinedConds ? joinedConds : '<Condition>'

      let expectedOperator = self.refs.isAndExpected.checked ? ' and ' : ' or '
      let expecteds = []
      for (let i = 0; i < self.statement.expecteds.length; i++) {
        if (self.refs['expected_' + i]) {
          expecteds.push(self.refs['expected_' + i].value)
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
        conditions.push(self.refs['condition_' + i].value)
      }
      let expectedOperator = self.refs.isAndExpected.checked ? 'and' : 'or'
      let expecteds = []
      for (let i = 0; i < self.statement.expecteds.length; i++) {
        expecteds.push(self.refs['expected_' + i].value)
      }
      return {
        type: self.mapType,
        subject: self.refs.subject.value,
        condition: {
          operator: conditionOperator,
          values: conditions
        },
        expected: {
          operator: expectedOperator,
          values: expecteds
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
