<schema-policy-check-statement-form-wrapper>
  <a onclick="{ toggleForm }" show="{ !state.showForm }">
    Add Statement
  </a>
  <a onclick="{ toggleForm }" show="{ state.showForm }">
    Hide Form
  </a>
  <div class="ui divided items segment" show="{ state.showForm }">
    <schema-policy-check-statement-form
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
    }
  </script>
</schema-policy-check-statement-form-wrapper>
