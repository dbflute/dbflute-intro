<schema-policy-check-statement-form-expected>
  <div class="grouped fields required">

    <label>Expected</label>
    <p>
      <schema-policy-check-statement-form-docuement-link formtype="{ props.formType }" />
      /
      <a onclick="{ showSample }">sample</a>
    </p>

    <div if="{ state.showSample }" class="ui info message">
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
    </div>

    <schema-policy-check-statement-form-expected-field
      each="{ field, index in state.fields }" key="{ field.id }"
      id="{ field.id }"
      subjectverb="{ field.subjectVerb }"
      complement="{ field.complement }"
      isdeletable="{ isDeletable }"
      handledelete="{ handleFileldDelete }"
      handlechange="{ props.handleFieldChange }"
    />

    <div class="ui grid">
      <div class="four wide right floated column">
        <i class="plus link icon" style="float: right" onclick="{ handleFieldAdd }" />
        <div class="inline fields" style="float: right" show="{ needsCondition() }">
          <div class="field">
            <div class="ui radio checkbox">
              <!-- checkedの制御はすべて関数側で行うので、ここでは定義しない -->
              <input type="radio" name="expected-mode" ref="isAnd" onchange="{ handleConditionChange }">
              <label>and</label>
            </div>
          </div>
          <div class="field">
            <div class="ui radio checkbox">
              <!-- checkedの制御はすべて関数側で行うので、ここでは定義しない -->
              <input type="radio" name="expected-mode" ref="isOr" onchange="{ handleConditionChange }">
              <label>or</label>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>

  <script>
    const self = this

    self.props = {
      formType: self.opts.formtype,
      handleFieldAdd: self.opts.handlefieldadd,
      handleFieldChange: self.opts.handlefieldchange,
      handleFieldDelete: self.opts.handlefielddelete,
      handleConditionChange: self.opts.handleconditionchange,
    }

    self.state = {
      showSample: false,
      fields: self.opts.fields,
      condition: self.opts.condition
    }

    self.handleFieldAdd = () => {
      self.props.handleFieldAdd()
      self.state.fields = self.opts.fields
    }

    self.handleFileldDelete = (id) => {
      self.props.handleFieldDelete(id)
      const fields = self.state.fields
      self.state.fields = fields.filter(field => field.id !== id)
      self.update()
    }

    self.isDeletable = () => {
      return self.isMultipleFields()
    }

    self.needsCondition = () => {
      return self.isMultipleFields()
    }

    self.isMultipleFields = () => {
      return self.state.fields.length > 1
    }

    self.handleConditionChange = () => {
      const handler = self.props.handleConditionChange
      const isAnd = self.refs.isAnd.checked
      const isOr = self.refs.isOr.checked
      if (isAnd) {
        handler('and')
      } else if (isOr) {
        handler('or')
      } else {
        handler('and') // as default
      }
    }

    self.showSample = () => {
      self.state.showSample = !self.state.showSample
      self.update()
    }

    self.on('mount', () => {
      const condition = self.state.condition
      if ('and' === condition) {
        self.refs.isAnd.checked = true
      } else if ('or' === condition) {
        self.refs.isOr.checked = true
      } else {
        self.refs.isAnd.checked = true // as default
      }
    })
  </script>

  <style>
    .variable {
      font-style: italic;
      font-weight: bold;
    }
    .comment {
      color: darkgray;
    }
  </style>
</schema-policy-check-statement-form-expected>
