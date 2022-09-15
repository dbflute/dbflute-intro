<schema-policy-check-statement-form-wrapper>
  <!-- ClientのSchemaPolicyCheckのStatement追加フォームのラッパー (written at 2022/02/10)
   機能:
    o フォームを出したり閉じたりする

   作りの特徴:
    o フォーム本体は別tagに切り出し、ここではフォームの開閉の役割のみ担う
   -->

  <!-- フォームを開閉するボタン -->
  <a onclick="{ toggleForm }" show="{ !state.showForm }">
    Add Statement
  </a>
  <a onclick="{ toggleForm }" show="{ state.showForm }">
    Hide Form
  </a>

  <!-- Statement追加フォーム本体 -->
  <div class="ui divided items segment" show="{ state.showForm }">
    <schema-policy-check-statement-form
      ref="statementFormComponent"
      projectName="{ props.projectName }"
      type="{ props.formType }"
      onRegisterSuccess="{ onRegisterSuccess }"
    />
  </div>

  <script>
    const self = this

    self.props = {
      projectName: self.opts.projectname,
      formType: self.opts.formtype,
      onRegisterSuccess: self.opts.onregistersuccess
    }

    self.state = {
      showForm: false
    }

    // #thinking この空っぽのマウントは必要なのか？ by prprmurakami (2022/03/12)
    /**
     * マウント時の処理。
     */
    self.on('mount', () => {
      self.update()
    })

    /**
     * 登録が成功したときのコールバック処理
     * - フォームを閉じて、成功トーストを表示することでユーザーにフィードバックをする
     */
    self.onRegisterSuccess = () => {
      self.closeForm()
      self.successToast({
        title: 'Create statement completed',
        message: 'statement was successfully created!!',
      })
      self.props.onRegisterSuccess()
    }

    /**
     * フォームの開閉状態を切り替える
     * - 開く場合はフォームが表示される位置までスクロールする
     */
    self.toggleForm = () => {
      self.state.showForm = !self.state.showForm
      self.update()
      // フォームが表示されていればスクロールする
      // DOM要素を参照する必要があるため、画面更新(self.update())の完了を待ってから行う必要があることに注意
      if (self.state.showForm) {
        self.refs.statementFormComponent.scrollToTop()
      }
    }

    /**
     * フォームを閉じる (現在の開閉状態に関わらず非表示にする)
     */
    self.closeForm = () => {
      self.state.showForm = false
      self.update()
    }
  </script>
</schema-policy-check-statement-form-wrapper>
