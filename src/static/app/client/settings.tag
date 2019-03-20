<settings>
  <div class="settings-body">
    <common-menu></common-menu>
    <div class="settings-container">
      <div class="ui container">
        <h2>DBFlute Client { client.projectName }</h2>

        <su-modal modal="{ checkModal }" class="large" ref="checkModal">
          <div class="description">
            { opts.modal.message }
          </div>
        </su-modal>

        <h3 class="ui header">
          for { client.databaseCode }, { client.languageCode }, { client.containerCode }
        </h3>
        <div class="ui form">
          <div class="row">
            <div class="column">
              <su-tabset class="four column item" settings="{ client.mainSchemaSettings }" playsql="{ playsqlDropDownItems }"
                         log="{ logDropDownItems }" schemapolicy="{ schemaPolicy }" tabtitles="{ tabTitles }" ref="client"
                         active="{ activeTab }">
                <su-tab title="{ opts.tabtitles['databaseInfo']}" settings="{ opts.settings }" ref="settings">
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
                <su-tab title="{ opts.tabtitles['playSql']}" playsql="{ opts.playsql }">
                  <su-dropdown items="{ opts.playsql }" ref="dropdown"></su-dropdown>
                  <div class="ui message message-area">
              <pre>
                <code class="language-sql">
                  <raw content="{ refs.dropdown.value }"></raw>
                </code>
              </pre>
                  </div>
                </su-tab>
                <su-tab title="{ opts.tabtitles['log']}" log="{ opts.log }">
                  <su-dropdown items="{ opts.log }" ref="dropdown"></su-dropdown>
                  <div class="ui message message-area">
                    <pre>{ refs.dropdown.value }</pre>
                  </div>
                </su-tab>
                <su-tab title="{ opts.tabtitles['schemaPolicy']}" schemapolicy="{ opts.schemapolicy }">
                  <div class="">
                    <h3 class="">Whole Schema Policy</h3>
                    <div class="ui divided items" if="{opts.schemapolicy.wholeMap}">
                      <div class="item" each="{ theme in opts.schemapolicy.wholeMap.themeList }">
                        <div class="ui left floated">
                          <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }"
                                       onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'wholeMap', theme.typeCode) }"></su-checkbox>
                        </div>
                        <div class="content">
                          <a class="header">{ theme.name }</a>
                          <div class="description">
                            {theme.description}
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="">
                      <h3>Table Schema Policy</h3>
                      <div class="ui divided items" if="{opts.schemapolicy.tableMap}">
                        <div class="item" each="{ theme in opts.schemapolicy.tableMap.themeList }">
                          <div class="ui left floated">
                            <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }"
                                         onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'tableMap', theme.typeCode) }"></su-checkbox>
                          </div>
                          <div class="content">
                            <a class="header">{ theme.name }</a>
                            <div class="description">
                              {theme.description}
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="">
                        <h3>Column Schema Policy</h3>
                        <div class="ui divided items" if="{opts.schemapolicy.columnMap}">
                          <div class="item" each="{ theme in opts.schemapolicy.columnMap.themeList }">
                            <div class="ui left floated">
                              <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }"
                                           onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'columnMap', theme.typeCode) }"></su-checkbox>
                            </div>
                            <div class="content">
                              <a class="header">{ theme.name }</a>
                              <div class="description">
                                {theme.description}
                              </div>
                            </div>
                          </div>
                        </div>
                        <button class="ui primary button" onclick="{ parent.parent.schemaPolicyCheckTask }">Check Policy (schema-policy-check)
                        </button>
                </su-tab>
              </su-tabset>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <style>
    .settings-body {
      display: flex;
      flex-direction: row;
    }
    .settings-container {
      margin-left: 15rem;
    }
    .message-area {
      overflow: scroll;
    }

    .message-area pre {
      font-family: Monaco, "Courier New", serif;
    }
  </style>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()

    const self = this
    this.client = {} // existing clients
    this.playsqlDropDownItems = {}
    this.logDropDownItems = {}
    const defaultItem = [{label: '-', value: null}]
    this.schemaPolicy = {}

    this.tabTitles = {
      databaseInfo: 'Database info',
      playSql: 'PlaySQL',
      log: 'Log',
      schemaPolicy: 'SchemaPolicy'
    }
    this.activeTab = ''

    this.checkModal = {
      header: 'SchemaPolicyCheck',
      closable: false
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareCurrentProject = (projectName) => {
      self.prepareSettings(projectName)
      self.preparePlaysql(projectName)
      self.prepareLogs(projectName)
      self.prepareSchemaPolicy(projectName)
    }

    this.prepareActiveTab = (activeTab) => {
      self.activeTab = activeTab || self.tabTitles['databaseInfo']
    }

    this.prepareSettings = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
    }

    this.preparePlaysql = (projectName) => {
      ApiFactory.playsqlBeanList(projectName).then(json => {
        const playsqlDropDownItems = json.map(obj => ({
          label: obj.fileName,
          value: `<span style="display: none;">${obj.fileName}</span>` + Prism.highlight(obj.content, Prism.languages.sql, 'sql')
        }))
        self.playsqlDropDownItems = defaultItem.concat(playsqlDropDownItems)
        self.update()
      })
    }

    this.prepareLogs = (projectName) => {
      ApiFactory.logBeanList(projectName).then(json => {
        const logDropDownItems = json.map(obj => ({label: obj.fileName, value: obj.content}))
        self.logDropDownItems = defaultItem.concat(logDropDownItems)
        self.update()
      })
    }

    this.prepareSchemaPolicy = (projectName) => {
      ApiFactory.schemaPolicy(projectName).then(json => {
        self.schemaPolicy = json
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

      ApiFactory.updateSettings(this.client).then(() => {
        self.prepareSettings(self.client.projectName)
        self.showToast()
      })
    }

    this.editSchemaPolicyMap = (targetMap, typeCode) => {
      const targetTheme = this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode)
      const toggledActiveStatus = !targetTheme.isActive

      let body = {
        wholeMap: {themeList: []},
        tableMap: {themeList: []},
        columnMap: {themeList: []}
      }
      body[targetMap].themeList = [{typeCode: typeCode, isActive: toggledActiveStatus}]

      ApiFactory.editSchemaPolicy(opts.projectName, body).then(() => {
        this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode).isActive = toggledActiveStatus
        self.update()
      })
    }

    this.showToast = () => {
      this.suToast({
        title: 'Setting updated',
        class: 'positive'
      })
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.schemaPolicyCheckTask = () => {
      self.checkModal.message = 'Checking...'
      const checkModalRef = self.refs.checkModal
      checkModalRef.show()

      ApiFactory.task(self.opts.projectName, 'doc').then((response) => {
        self.checkModal.message = response.success ? 'Success' : 'Failure'
        ApiFactory.clientOperation(self.opts.projectName).then((response) => {
          self.update({client: response})
        })
      }).finally(() => {
        checkModalRef.hide()
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.prepareCurrentProject(opts.projectName)
      this.prepareActiveTab(opts.activeTab)
    })
  </script>
</settings>
