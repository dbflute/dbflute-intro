<shema-policy-check-statement-form-wrapper>
  <a onclick="{ toggleForm }" show="{ !state.showForm }">
    Edit Statement
  </a>
  <a onclick="{ toggleForm }" show="{ state.showForm }">
    Hide Form
  </a>
  <div class="ui divided items segment" show="{ state.showForm }">
    <statement-form
      projectName="{ props.projectName }"
      type="{ props.formType }"
    />
  </div>

  <script>
    const self = this

    self.props = {
      projectName: self.opts.projeectname,
      formType: self.opts.formtype
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
</shema-policy-check-statement-form-wrapper>
