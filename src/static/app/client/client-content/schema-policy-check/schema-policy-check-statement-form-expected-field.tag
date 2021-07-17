<schema-policy-check-statement-form-expected-field>
  <div class="fields">
    <div class="six wide field">
      <su-dropdown ref="subjectVerb" items="{ props.subjectItems }" value="{ props.subjectVerb }" />
    </div>
    <div class="ten wide field" show="{ enableValueInput() }">
      <div class="ui icon input">
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

    self.enableValueInput = () => {
      const subjectVerb = self.refs.subjectVerb.value
      return self.isHasIs(subjectVerb)
    }

    self.isHasIs = (subjectVerb) => {
      const subjectItems = self.props.subjectItems
      const item = subjectItems.find(item => subjectVerb === item.value)
      return item && item.type === 'hasIs'
    }

    self.handleChange = () => {
      const id = self.props.id
      const subjectVerb = self.refs.subjectVerb.value
      const complement = self.isHasIs(subjectVerb) ? self.refs.complement.value : null
      self.props.handleChange(id, subjectVerb, complement)
    }

    self.handleDelete = () => {
      const id = self.props.id
      self.props.handleDelete(id)
    }

    self.addDropdownEvent = () => {
      self.refs.subjectVerb.on('select', () => {
        self.handleChange()
        self.update()
      })
    }

    self.initField = () => {
      self.initComplement()
      self.initSubjectVerb()
    }

    self.initSubjectVerb = () => {
      // do nothing
    }

    self.initComplement = () => {
      self.refs.complement.value = self.props.complement
    }

    self.on('mount', () => {
      self.addDropdownEvent()
      self.initField()
      self.update()
    })

  </script>
</schema-policy-check-statement-form-expected-field>
