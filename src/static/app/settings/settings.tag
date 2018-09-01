<settings>
  <h2>DBFlute Client { client.projectName }</h2>

  <h3 class="ui header">
    for { client.databaseCode }, { client.languageCode }, { client.containerCode }
  </h3>
  <div class="ui form">
    <div class="row">
      <div class="column">
        <su-tabset class="three column item" settings="{ client.mainSchemaSettings }" playsql="{ playsqlDropdownItems }" log="{ logDropdownItems }">
          <su-tab title="Database info" settings="{ opts.settings }">
            <div class="required field">
              <label data-is="i18n">LABEL_url</label>
              <input type="text" value="{ opts.settings.url }" placeholder="jdbc:mysql://localhost:3306/maihamadb"/>
            </div>
            <div class="field">
              <label data-is="i18n">LABEL_schema</label>
              <input type="text" value="{ opts.settings.schema }" placeholder="MAIHAMADB"/>
            </div>
            <div class="required field">
              <label data-is="i18n">LABEL_user</label>
              <input type="text" value="{ opts.settings.user }" placeholder="maihamadb"/>
            </div>
            <div class="field">
              <label data-is="i18n">LABEL_password</label>
              <input type="text" value="{ opts.settings.password }"/>
            </div>
            <div class="field">
              <button class="ui button primary" onclick="{ editClient }">Edit</button>
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
    import route from 'riot-route'

    const ApiFactory = new _ApiFactory()

    const self = this
    this.client = {} // existing clients
    this.playsqlDropdownItems = {}
    this.logDropdownItems = {}

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareCurrentProject = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
      ApiFactory.playsqlBeanList(projectName).then(json => {
        self.playsqlDropdownItems = json.map(obj => ({ label: obj.fileName, value: obj.content }))
        self.update()
      })
      ApiFactory.logBeanList(projectName).then(json => {
        self.logDropdownItems = json.map(obj => ({ label: obj.fileName, value: obj.content }))
        self.update()
      })
    }

    this.editClient = () => {
      this.client.mainSchemaSettings = {
        url: self.refs.url.value,
        schema: self.refs.schema.value,
        user: self.refs.user.value,
        password: self.refs.password.value
      }
      ApiFactory.updateSettings(this.client)
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.prepareCurrentProject(opts.projectName)
    })
  </script>
</settings>
