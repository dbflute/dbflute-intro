<schema-policy-check-statement-form-expected>
  <!-- ClientのSchemaPolicyCheckのStatement追加フォームのExpectedの部分 (written at 2022/03/17)
  　機能:
    o expected部分の情報を入力することができる
    o and/orでつなげて複数のexpectedを設定することができる。

  　作りの特徴:
    o input部分は別tagに切り出されている。
  　-->

  <div class="grouped fields required">

    <label>Expected</label>
    <!-- document / sample -->
    <p>
      <schema-policy-check-statement-form-docuement-link formtype="{ props.formType }" />
      /
      <a onclick="{ showSample }">sample</a>
    </p>

    <!-- sampleのtoggle -->
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

    <!-- input部分は別tagに切り出し -->
    <schema-policy-check-statement-form-expected-field
      each="{ field, index in state.fields }" key="{ field.id }"
      id="{ field.id }"
      subjectverb="{ field.subjectVerb }"
      complement="{ field.complement }"
      isdeletable="{ isDeletable }"
      handledelete="{ handleFileldDelete }"
      handlechange="{ props.handleFieldChange }"
    />

    <!-- radio button ( and / or ) -->
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

    /**
     * inputフィールドの追加処理。
     */
    self.handleFieldAdd = () => {
      self.props.handleFieldAdd()
      self.state.fields = self.opts.fields
    }

    /**
     * inputフィールドの削除処理。
     * @param {String} id - expectedのフィールドを一意に特定するキー (NotNull)
     */
    self.handleFileldDelete = (id) => {
      self.props.handleFieldDelete(id)
      const fields = self.state.fields
      self.state.fields = fields.filter(field => field.id !== id)
      self.update()
    }

    /**
     * inputフィールドが削除可能かどうか。
     * @return {boolean} true:削除可能,false:削除不可能
     */
    self.isDeletable = () => {
      return self.isMultipleFields()
    }

    /**
     * and, orが必要かどうか。
     * ここでいうconditionはand,orのことを指す。
     */
    self.needsCondition = () => {
      return self.isMultipleFields()
    }

    /**
     * 複数フィールドがあるかどうか。
     * @return {boolean} true:複数フィールドがある,false:複数フィールドがない
     */
    self.isMultipleFields = () => {
      return self.state.fields.length > 1
    }

    /**
     * expectedを繋ぐand/orの選択を変更する。
     */
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

    // #thinking 名前、toggleSampleのほうがいい？showって開く方しかイメージない。 by prprmurakami (2022/03/24)
    /**
     * sampleを開閉する。
     */
    self.showSample = () => {
      self.state.showSample = !self.state.showSample
      self.update()
    }

    /**
     * マウント時の処理。
     */
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

    // self.updateのときに呼ばれるコールバック処理。画面を描画しなおすときに呼ばれる。
    self.on('update', () => {
      self.state = {
        fields: self.opts.fields,
        condition: self.opts.condition
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
