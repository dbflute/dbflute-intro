<settings>
  <h2>DBFlute Client { client.projectName }</h2>

  <h3 class="ui header">
    for { client.databaseCode }, { client.languageCode }, { client.containerCode }
  </h3>
  <div class="ui form">
    <div class="row">
      <div class="column">
        <su-tabset class="three column item" settings="{ client.mainSchemaSettings }" playsql="{ playsqlDropDownItems }"
                   log="{ logDropDownItems }" ref="client">
          <su-tab title="Database info" settings="{ opts.settings }" ref="settings">
            <div class="required field" if="{ opts.settings }">
              <label data-is="i18n">LABEL_url</label>
              <input type="text" value="{ opts.settings.url }" ref="url" placeholder="jdbc:mysql://localhost:3306/maihamadb"/>
            </div>
            <div class="field" if="{ opts.settings }">
              <label data-is="i18n">LABEL_schema</label>
              <input type="text" value="{ opts.settings.schema }" ref="schema" placeholder="MAIHAMADB"/>
            </div>
            <div class="required field" if="{ opts.settings }">
              <label data-is="i18n">LABEL_user</label>
              <input type="text" value="{ opts.settings.user }" ref="user" placeholder="maihamadb"/>
            </div>
            <div class="field" if="{ opts.settings }">
              <label data-is="i18n">LABEL_password</label>
              <input type="text" value="{ opts.settings.password }" ref="password"/>
            </div>
            <div class="field">
              <button class="ui button primary" onclick="{ parent.parent.editClient.bind(this, url) }">Edit</button>
            </div>
          </su-tab>
          <su-tab title="PlaySQL" playsql="{ opts.playsql }">
            <su-dropdown items="{ opts.playsql }" ref="dropdown"></su-dropdown>
            <div class="ui message message-area">
              <pre>{ refs.dropdown.value }</pre>
            </div>
          </su-tab>
          <su-tab title="Log" log="{ opts.log }">
            <su-dropdown items="{ opts.log }" ref="dropdown"></su-dropdown>
            <div class="ui message message-area">
              <pre>{ refs.dropdown.value }</pre>
            </div>
          </su-tab>
        </su-tabset>
      </div>
    </div>
  </div>

  <style>
    .message-area {
      overflow: scroll;
    }

    .message-area pre {
      font-family: Monaco, "Courier New", serif;
    }
  </style>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()

    const self = this
    this.client = {} // existing clients
    this.playsqlDropDownItems = {}
    this.logDropDownItems = {}

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareCurrentProject = (projectName) => {
      self.prepareSettings(projectName)
      self.preparePlaysql(projectName)
      self.prepareLogs(projectName)
    }

    this.prepareSettings = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
    }

    this.preparePlaysql = (projectName) => {
      ApiFactory.playsqlBeanList(projectName).then(json => {
        self.playsqlDropDownItems = json.map(obj => ({label: obj.fileName, value: obj.content}))
        self.update()
      })
    }

    this.prepareLogs = (projectName) => {
      ApiFactory.logBeanList(projectName).then(json => {
        self.logDropDownItems = json.map(obj => ({label: obj.fileName, value: obj.content}))
        self.update()
      })
    }

    this.editClient = () => {
      const settings = self.refs.client.refs.settings
      const url = settings.refs.url.value
      const schema = settings.refs.schema.value
      const user = settings.refs.user.value
      const password = settings.refs.password.value
      this.client.mainSchemaSettings = {url, schema, user, password}

      ApiFactory.updateSettings(this.client).finally(() => {
        self.prepareSettings(self.client.projectName)
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.prepareCurrentProject(opts.projectName)
    })
  </script>
</settings>
