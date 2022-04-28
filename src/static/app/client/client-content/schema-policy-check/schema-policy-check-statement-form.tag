<schema-policy-check-statement-form>
  <!-- ClientのSchemaPolicyCheckのStatement追加フォーム (written at 2022/03/17)
   機能:
    o statementを新規追加する

   作りの特徴:
    o selectboxを活用してdfpropを編集するよりも簡単にstatementをつくることができる
    o sampleを見ながら作成できる
   -->
  <form ref="statementForm" class="ui large form">
    <div class="field">
      <label>Preview</label>
      <div class="ui inverted segment">
        <p>{ statement.buildPreview() }</p>
      </div>
    </div>
    <div class="ui divider" />

    <!-- Subject -->
    <div class="grouped fields required">
      <label>Subject</label>

      <!-- document / sample -->
      <p>
        <a if="{ mapType === 'tableMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementifsubject" target="_blank">document</a>
        <a if="{ mapType === 'columnMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementifsubject" target="_blank">document</a>
        / <!-- #hope ここでスラッシュが急に出てきてびっくりするけど、documentとsampleの間の スラッシュ。docuumentリンクを一つのtagにして横並びにすると見やすそう by prprmurakami (2022/03/17) -->
        <a onclick="{ toggleSubjectHelp }">sample</a>
      </p>
      
      <!-- toggle このtagの一番下に切り出してある -->
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

      <!-- input -->
      <div class="ui input field">
        <su-dropdown items="{ subjectDropdownItems }" ref="subject"></su-dropdown>
      </div>
    </div>


    <!-- Condition -->
    <div class="grouped fields required">
      <label>Condition</label>

      <!-- document / sample -->
      <p>
        <a if="{ mapType === 'tableMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementnot" target="_blank">document</a>
        <a if="{ mapType === 'columnMap' }" class="help link"
           href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementcolumnis" target="_blank">document</a>
        /
        <a onclick="{ toggleConditionHelp }">sample</a>
      </p>

      <!-- toggle -->
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

      <!-- input -->
      <div class="ui icon input field" each="{ condition, index in statement.conditions }">
        <input type="text" name="condition" ref="condition_{index}" value="{ condition }" onchange="{ handleChange }">
        <i class="delete link icon" if={statement.conditions.length > 1} onclick="{ statement.deleteConditionField.bind(this, index) }"></i>
      </div>

      <!-- radio button ( and / or ) -->
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

    <!-- Expected -->
    <div class="grouped fields required">
      <!-- Expectedは別tagに切り出し -->
      <schema-policy-check-statement-form-expected
        formtype="{ opts.type }"
        handlefieldadd="{ handleExpectedFieldAdd }"
        handlefieldchange="{ handleExpectedFieldChange }"
        handlefielddelete="{ handleExpectedFieldDelete }"
        handleconditionchange="{ handleExpectedConditionChange }"
        fields="{ state.expected.fields }"
        condition="{ state.expected.condition }"
      />
    </div>

    <!-- Supplementary Comment -->
    <div class="grouped fields required">
      <div class="field required">
        <label>Supplementary Comment</label>
        <!-- document -->
        <p>
          <a class="help link" href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementsupplement" target="_blank">document</a>
        </p>
        <!-- iにnput -->
        <div class="ui input">
          <input type="text" name="comment" ref="comment" value="{ statement.comment }" onchange="{ handleChange }">
        </div>
      </div>
    </div>

    <!-- 追加ボタン -->
    <button
      class="ui primary button"
      type="button"
      onclick="{ registerStatement }">
      Add
    </button>
  </form>

  <script>
    import { v4 as uuid } from 'uuid'
    import _ApiFactory from '../../../common/factory/ApiFactory'

    const ApiFactory = new _ApiFactory()
    let self = this
    self.mounted = false
    self.mapType = ''
    self.projectName = ''
    self.subjectDropdownItems = {}

    self.state = {
      expected: {
        fields: [{
          id: uuid(),
          subjectVerb: null,
          complement: ''
        }],
        condition: 'and' // or 'or'
      }
    }

    self.statement = {
      subject: '',
      conditions: [''],
      comment: '',

      /**
       * プレビューを構築する。
       */
      buildPreview: () => {
        return self.buildPreview()
      },

      /**
       * Conditionのinputフィールドを増やす処理
       */
      addConditionField: () => {
        self.statement.conditions.push('')
      },

      /**
       * Conditionのinputフィールドを減らす処理
       */
      deleteConditionField: (index) => {
        self.statement.conditions.splice(index, 1)
      },

      /**
       * 各フィールドの値を空にする。
       */
      clean: () => {
        self.statement.subject = ''
        self.statement.conditions = ['']
        self.statement.expecteds = ['']
        self.statement.comment = ''
      }
    }

    /**
     * マウント時の処理。
     */
    self.on('mount', () => {
      self.mapType = self.opts.type
      self.projectName = self.opts.projectname
      self.mounted = true
      self.getSubject(self.opts.type)
    })

    /**
     * セレクトボックスとして表示するSubject一覧を初期化する
     * @param {string} mapType - マップ種別. (NotNull, only 'tableMap', 'columnMap')
     */
    this.getSubject = (mapType) => {
      ApiFactory.getSchemapolicyStatementSubject(mapType).then(json => {
        const defaultItems = [{label: 'Select subject', value: null, default: true}]
        const items = json.map(obj => ({
          label: obj,
          value: obj
        }))
        self.subjectDropdownItems = defaultItems.concat(items)
        self.update()
      })
    }

    /**
     * テンプレート変数を更新する。
     */
    this.handleChange = () => {
      self.saveConditionField()
      self.update()
    }

    /**
     * subjectのtoggleを開閉する。
     */
    this.toggleSubjectHelp = () => {
      self.refs.subjecthelp.toggle()
    }

    /**
     * conditionのtoggleを開閉する。
     */
    this.toggleConditionHelp = () => {
      self.refs.conditionhelp.toggle()
    }

    // #thinking このtagの外から読んでる。publicメソッドとprivateメソッドの区別をなにかしたい。 by s-murakami (2022/03/24)
    /**
     * フォームを入力しやすい位置に画面をスクロールする。
     */
    this.scrollToTop = () => {
      // statementFormは標準のformタグなので, 標準APIのscrollIntoViewを呼び出せる
      self.refs.statementForm.scrollIntoView({ behavior: 'smooth' })
    }

    /**
     * Conditionの値をテンプレート変数に保存する。
     */
    this.saveConditionField = () => {
      for (let i = 0; i < self.statement.conditions.length; i++) {
        self.statement.conditions[i] = self.refs['condition_' + i].value
      }
    }

    /**
     * Expectedの値が変わったときのイベントハンドラー
     * @param id - expectedのフィールドを一意に特定するキー (NotNull)
     * @param subjectVerb 主語動詞
     * @param complement 補足
     */
    self.handleExpectedFieldChange = (id, subjectVerb, complement) => {
      const fields = self.state.expected.fields
      self.state.expected.fields = fields.map(field => {
        if (id === field.id) {
          field.subjectVerb = subjectVerb
          field.complement = complement
        }
        return field
      })
      self.update() // updateを呼び出すのは画面の状態を書き換えるとき。
    }

    /**
     * Expectedのinputフィールドが追加されたときのイベントハンドラー
     */
    this.handleExpectedFieldAdd = () => {
      self.state.expected.fields.push({
        id: uuid(),
        subjectVerb: null,
        complement: ''
      })
      self.update()
    }

    /**
     * Expectedのinputフィールドが削除されたときのインベントハンドラー
     * @param {String} id - expectedのフィールドを一意に特定するキー (NotNull)
     */
    this.handleExpectedFieldDelete = (id) => {
      const fields = self.state.expected.fields
      self.state.expected.fields = fields.filter(field => field.id !== id)
      self.update()
    }

    /**
     * Expectedのコンディションが変わったときのイベントハンドラー
     * @param {string} condition - コンディションの値. (NotNull)
     */
    this.handleExpectedConditionChange = (condition) => {
      self.state.expected.condition = condition
      self.update()
    }

    /**
     * Add Statement フォームの各フィールドの値を初期表示に戻す
     */
    this.cleanInput = () => {
      self.statement.clean()
      self.refs.subject.value = ''
      self.refs.comment.value = ''
      self.state.expected = {
        fields: [{
          id: uuid(),
          subjectVerb: null,
          complement: ''
        }],
        condition: 'and' // or 'or'
      }
      self.update()
    }

    /**
     * プレビューを構築する。
     * @return {String} 作成されたプレビューの文字列
     */
    this.buildPreview = () => {
      if (!self.mounted) { // 何も値が反映されていない時はこれを表示
        return 'if <Subject> is <Condition> then <Expected> => <Supplementary Comment>'
      }

      // subjectのパートを作成
      let subject = self.refs.subject.value ? self.refs.subject.value : '<Subject>'
      let statementPrefix = 'if ' + subject + ' is '

      // conditionのパートを作成
      let conditionOperator = self.refs.isAndCondition.checked ? ' and ' : ' or '
      let conditions = []

      for (let i = 0; i < self.statement.conditions.length; i++) {
        let conditionRef = self.refs['condition_' + i]
        if (conditionRef && conditionRef.value) {
          conditions.push(conditionRef.value) // conditionsに詰める
        }
      }
      let joinedConds = conditions.join(conditionOperator)
      let conditionsStr = joinedConds ? joinedConds : '<Condition>'

      // expectedのパートを作成
      const expecteds = self.state.expected.fields
        .filter(field => field.subjectVerb)
        .map(field => field.subjectVerb + (field.complement ? ` ${field.complement}` : ''))
      const expectedOperator = self.state.expected.condition
      const joinedExps = expecteds.join(` ${expectedOperator} `)
      const expectedsStr = joinedExps ? joinedExps : '<Expected>'

      // commentのパートを作成
      let comment = self.refs.comment.value ? ' => ' + self.refs.comment.value : ' => <Supplementary Comment>'
      
      // ex. if tableName is not ABC_DATE then hasPK => カラムっぽいテーブル名ダメ
      return statementPrefix + conditionsStr + ' then ' + expectedsStr + comment
    }

    /**
     * registerSchemapolicyStatementを呼ぶ際のリクエストボディを作成する。
     */
    this.buildBody = () => {
      // conditionsを用意
      let conditionOperator = self.refs.isAndCondition.checked ? 'and' : 'or'
      let conditions = []
      for (let i = 0; i < self.statement.conditions.length; i++) {
        if (self.refs['condition_' + i].value) {
          conditions.push(self.refs['condition_' + i].value)
        }
      }

      // expectedを用意
      const expecteds = self.state.expected.fields
        .filter(field => field.subjectVerb)
        .map(field => field.subjectVerb + (field.complement ? ` ${field.complement}` : ''))
      const expectedOperator = self.state.expected.condition

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

    /**
     * statementを登録する。
     */
    self.registerStatement = () => {
      ApiFactory.registerSchemapolicyStatement(self.projectName, self.buildBody())
        .then(() => {
          self.cleanInput()
          self.opts.onregistersuccess()
        })
    }
  </script>
</schema-policy-check-statement-form>

<!-- toggleの部分を切り出してある -->
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

    /**
     * マウント時の処理
     */
    this.on('mount', () => {
      if (self.opts.showed) {
        self.showed = self.opts.showed
      }
    })

    /**
     * toggleを開閉する。
     */
    this.toggle = () => {
      self.showed = !self.showed
      self.update()
    }
  </script>

</toggle-help>
