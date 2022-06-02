<schema-policy-check-statement-form-expected-field>
  <!-- ClientのSchemaPolicyCheckのStatement追加のExpected項目のinput部分 (written at 2022/04/7)  
  機能:
    o Expectedの項目をドロップダウンやテキストフィールドで入力できる。

  作りの特徴:
    o ドロップダウンで選択したsubjectVerbによってcomplementのテキストフィールドの活性・非活性がされる。
  -->
  <div class="fields">
    <!-- TODO ドロップダウンとテキストフィールドの比率が6:10? by s-murakami (2022/04/07)-->
    <div class="six wide field">
      <su-dropdown ref="subjectVerb" items="{ props.subjectItems }" value="{ props.subjectVerb }" />
    </div>
    <div class="ten wide field" show="{ enableValueInput() }">
      <div class="ui icon input">
        <!-- valueが必要であれば活性化する -->
        <input type="text" ref="complement" name="expected" onchange="{ handleChange }" >
        <i class="delete link icon" show="{ props.isDeletable() }" onclick="{ handleDelete }" />
      </div>
    </div>
    <div class="ten wide field" show="{ !enableValueInput() }">
      <div class="ui icon input">
        <input type="text" name="expected" onchange="{ handleChange }" disabled>
        <i class="delete link icon" show="{ props.isDeletable() }" onclick="{ handleDelete }" />
      </div>
    </div>
  </div>

  <script>
    import * as definition from './definition.js'

    const self = this

    self.props = {
      id: self.opts.id,
      subjectVerb: self.opts.subjectverb,
      complement: self.opts.complement,
      isDeletable: self.opts.isdeletable,
      handleDelete: self.opts.handledelete,
      handleChange: self.opts.handlechange,
      subjectItems: definition.expectedSubjectItems,
    }

    /**
     * valueのinputを許可するかどうか
     * @return {boolean} true:許可,false:不許可
     */
    self.enableValueInput = () => {
      const subjectVerb = self.refs.subjectVerb.value
      return self.isHasIs(subjectVerb)
    }

    /**
     * subjectVerbにisがあるかどうか
     * @return {boolean} true:isがある,false:isがない
     */
    self.isHasIs = (subjectVerb) => {
      const subjectItems = self.props.subjectItems
      const item = subjectItems.find(item => subjectVerb === item.value)
      return item && item.type === 'hasIs'
    }

    /**
     * inputの値が変わったときのイベントハンドラー
     */
    self.handleChange = () => {
      const id = self.props.id
      const subjectVerb = self.refs.subjectVerb.value
      const complement = self.isHasIs(subjectVerb) ? self.refs.complement.value : null
      self.props.handleChange(id, subjectVerb, complement)
    }

    /**
     * inputの値を削除するイベントハンドラー
     */
    self.handleDelete = () => {
      const id = self.props.id
      self.props.handleDelete(id)
    }

    /**
     * has-is or non-is のドロップダウンが選択された時の処理を設定する。
     */
    self.addDropdownEvent = () => {
      self.refs.subjectVerb.on('select', () => {
        // このタグの呼び出し元のさらに呼び出し元のhandleExpectedFieldChange()を呼んでいる。
        // やってることは、self.state.expected.fields のフィールドへの、選択された subjectVerb の反映。
        self.handleChange()
        self.update()
      })
    }

    /**
     * フィールドの初期化
     */
    self.initField = () => {
      self.initComplement()
      self.initSubjectVerb()
    }

    /**
     * SubjectVerbの初期化
     */
    self.initSubjectVerb = () => {
      // do nothing
    }

    /**
     * complementの初期化
     */
    self.initComplement = () => {
      self.refs.complement.value = self.props.complement // このcomplementはどこからもらったもの...？ by s-murakami (2022/04/07)
    }

    /**
     * マウント時の処理。
     */
    self.on('mount', () => {
      self.addDropdownEvent()
      self.initField()
      self.update()
    })

  </script>
</schema-policy-check-statement-form-expected-field>
