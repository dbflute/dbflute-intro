<welcome>
  <h2 class="heading">Welcome to DBFlute</h2>
  <div class="ui form">
    <div class="ui stackable two column divided grid">
      <div class="row">
        <div class="column">
          <div class="required field">
            <label data-is="i18n">LABEL_projectName</label>
            <input type="text" ref="projectName" placeholder="maihamadb" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <div class="required field">
            <label data-is="i18n">LABEL_databaseCode</label>
            <su-dropdown ref="databaseCode" items="{ targetDatabaseItems }"></su-dropdown>
          </div>
          <div class="required field">
            <label data-is="i18n">LABEL_jdbcDriverFqcn</label>
            <input type="text" ref="jdbcDriverFqcn" placeholder="com.mysql.jdbc.Driver" value="com.mysql.jdbc.Driver" />
          </div>
          <div class="required field" if="{ needsJdbcDriver }">
            <label data-is="i18n">LABEL_jdbcDriverFile</label>
            <input type="file" onchange="{ changeFile }"/>
          </div>
        </div>
        <div class="column">
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_languageCode</label>
            <su-dropdown ref="languageCode" items="{ targetLanguageItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_containerCode</label>
            <su-dropdown ref="containerCode" items="{ targetContainerItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_packageBase</label>
            <input type="text" ref="packageBase" value="org.docksidestage.dbflute" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <div class="required field">
            <label data-is="i18n">LABEL_url</label>
            <input type="text" ref="url" placeholder="jdbc:mysql://localhost:3306/maihamadb" />
          </div>
          <div class="field">
            <label data-is="i18n">LABEL_schema</label>
            <input type="text" ref="schema" placeholder="MAIHAMADB" />
          </div>
          <div class="required field">
            <label data-is="i18n">LABEL_user</label>
            <input type="text" ref="user" placeholder="maihamadb" />
          </div>
          <div class="field">
            <label data-is="i18n">LABEL_password</label>
            <input type="text" ref="password" />
          </div>
          <div class="field">
            <su-checkbox ref="testConnection">Connect as test</su-checkbox>
          </div>
          <div class="field">
            <button class="ui button primary" onclick="{ create }">Create</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    import _UiAssist from '../common/UiAssist'
    const ApiFactory = new _ApiFactory()
    const UiAssist = new _UiAssist()

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    this.classificationMap = {} // e.g. targetDatabase
    this.jdbcDriver = null
    this.needsJdbcDriver = false
    this.oRMapperOptionsFlg = true
    this.latestVersion = null
    const self = this

    // ===================================================================================
    //                                                                      Initial Method
    //                                                                      ==============
    this.findClassifications = () => {
      ApiFactory.classifications().then((json) => {
        self.targetDatabaseItems = Object.keys(json.targetDatabaseMap).map(key => {
          return { value: key, label: json.targetDatabaseMap[key].databaseName }
        })
        self.targetLanguageItems = Object.keys(json.targetLanguageMap).map(key => {
          return { value: key, label: json.targetLanguageMap[key] }
        })
        self.targetContainerItems = Object.keys(json.targetContainerMap).map(key => {
          return { value: key, label: json.targetContainerMap[key] }
        })
        UiAssist.setBlankItem(self.targetDatabaseItems)
        UiAssist.setBlankItem(self.targetLanguageItems)
        UiAssist.setBlankItem(self.targetContainerItems)

        self.classificationMap = json
        self.update()
      })
    }
    this.setLatestEngineVersion = () => {
      ApiFactory.engineLatest().then((json) => {
        self.latestVersion = json.latestReleaseVersion
        self.update()
      })
    }
    this.setDefaultLangAndDIContainer = () => {
      self.refs.languageCode.value = 'java'
      self.refs.containerCode.value = 'lasta_di'
      self.update()
    }

    // ===================================================================================
    //                                                                        Event Method
    //                                                                        ============
    const changeDatabase = (databaseCode) => {
      let database = self.classificationMap['targetDatabaseMap'][databaseCode]
      // switch showing JDBCDriver select form
      self.needsJdbcDriver = !database.embeddedJar
      // initialize JDBC Driver
      self.jdbcDriver = null
      self.refs.jdbcDriverFqcn.value = database.driverName
      self.refs.url.value = database.urlTemplate
      self.refs.schema.value = database.defaultSchema
    }
    this.create = () => {
      const client = {
        projectName: self.refs.projectName.value,
        databaseCode: self.refs.databaseCode.value,
        create: true,
        mainSchemaSettings: {
          user: self.refs.user.value,
          url: self.refs.url.value,
          schema: self.refs.schema.value
        },
        schemaSyncCheckMap: {},
        dbfluteVersion: self.latestVersion,
        packageBase: self.refs.packageBase.value,
        containerCode: self.refs.containerCode.value,
        languageCode: self.refs.languageCode.value,
        jdbcDriverFqcn: self.refs.jdbcDriverFqcn.value,
      }
      if (self.jdbcDriver) {
        client.jdbcDriver = self.jdbcDriver
      }
      const testConnection = self.refs.testConnection.checked
      observable.trigger('loading', true)
      ApiFactory.createWelcomeClient(client, testConnection).then(() => {
        route('')
        this.showToast(client.projectName)
      }).finally(() => {
        observable.trigger('loading', false)
      })
    }
    this.changeFile = (event) => {
      let file = event.target.files[0]
      let reader = new FileReader()
      reader.onload = (function() {
        return () => {
          // encode base64
          let result = window.btoa(reader.result)
          self.jdbcDriver = { fileName: null, data: null }
          self.jdbcDriver.fileName = file.name
          self.jdbcDriver.data = result
        }
      }(file))

      if (file) {
        reader.readAsBinaryString(file)
      }
    }
    this.showToast = (projectName) => {
      this.suToast({
        title: 'Create task completed',
        message: 'Client for project \'' + projectName + '\', was successfully created!!',
        class: 'pink positive'
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.findClassifications()
      this.setLatestEngineVersion()
      this.setDefaultLangAndDIContainer()

      this.refs.databaseCode.on('change', target => {
        changeDatabase(target.value)
        this.update()
      })
    })
  </script>
</welcome>
