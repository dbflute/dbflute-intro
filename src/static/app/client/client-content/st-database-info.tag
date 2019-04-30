<st-database-info>
  <div class="ui container">
    <div class="ui form">
      <div class="row">
        <div class="column">
          <div class="ui segment" title="Database info" ref="settings">
            <div class="required field" if="{ client.mainSchemaSettings }">
              <label data-is="i18n">LABEL_url</label>
              <input type="text" value="{ client.mainSchemaSettings.url }" ref="url" placeholder="jdbc:mysql://localhost:3306/maihamadb"/>
            </div>
            <div class="field" if="{ client.mainSchemaSettings }">
              <label data-is="i18n">LABEL_schema</label>
              <input type="text" value="{ client.mainSchemaSettings.schema }" ref="schema" placeholder="MAIHAMADB"/>
            </div>
            <div class="required field" if="{ client.mainSchemaSettings }">
              <label data-is="i18n">LABEL_user</label>
              <input type="text" value="{ client.mainSchemaSettings.user }" ref="user" placeholder="maihamadb"/>
            </div>
            <div class="field" if="{ client.mainSchemaSettings }">
              <label data-is="i18n">LABEL_password</label>
              <input type="text" value="{ client.mainSchemaSettings.password }" ref="password"/>
            </div>
            <div class="field">
              <button class="ui button primary" onclick="{ editClient }">Edit</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    const self = this
    this.client = {} // existing clients

    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
    })
    this.prepareSettings = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
    }

    this.editClient = () => {
      const url = self.refs.url.value
      const schema = self.refs.schema.value
      const user = self.refs.user.value
      const password = self.refs.password.value
      this.client.mainSchemaSettings = {url, schema, user, password}

      ApiFactory.updateSettings(self.client).then(() => {
        self.prepareSettings(self.opts.projectName)
        self.showToast()
      })
    }

    this.showToast = () => {
      this.suToast({
        title: 'Setting updated',
        class: 'positive'
      })
    }
  </script>
</st-database-info>
