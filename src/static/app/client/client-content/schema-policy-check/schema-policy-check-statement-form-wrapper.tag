<schema-policy-check-statement-form-wrapper>
  <a onclick="{ toggleForm }" show="{ !state.showForm }">
    Add Statement
  </a>
  <a onclick="{ toggleForm }" show="{ state.showForm }">
    Hide Form
  </a>
  <div class="ui divided items segment" show="{ state.showForm }">
    <schema-policy-check-statement-form
      ref="statementFormComponent"
      projectName="{ props.projectName }"
      type="{ props.formType }"
      onRegisterSuccess="{ props.onRegisterSuccess }"
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

    self.on('mount', () => {
      self.update()
    })

    self.toggleForm = () => {
      self.state.showForm = !self.state.showForm
      self.update()
      // フォームが表示されていればスクロールする
      // DOM要素を参照する必要があるため、画面更新(self.update())の完了を待ってから行う必要があることに注意
      if (self.state.showForm) {
        self.refs.statementFormComponent.scrollToTop()
      }
    }
  </script>
</schema-policy-check-statement-form-wrapper>
