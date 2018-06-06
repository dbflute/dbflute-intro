<create>
  <h2 class="heading">Create Client</h2>
  <div class="ui form">
    <div class="ui stackable two column divided grid">
      <div class="row">
        <div class="column">
          <div class="required field">
            <label>
              <i18n>LABEL_projectName</i18n>
            </label>
            <input type="text" ref="projectName" placeholder="maihamadb" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <div class="required field">
            <label>
              <i18n>LABEL_dbfluteVersion</i18n>
            </label>
            <su-dropdown ref="dbfluteVersion" items="{ versions }"></su-dropdown>
          </div>
          <div class="required field">
            <label>
              <i18n>LABEL_databaseCode</i18n>
            </label>
            <su-dropdown ref="databaseCode" items="{ targetDatabaseItems }"></su-dropdown>
          </div>
          <div class="required field">
            <label>
              <i18n>LABEL_jdbcDriverFqcn</i18n>
            </label>
            <input type="text" ref="jdbcDriverFqcn" placeholder="com.mysql.jdbc.Driver" value="com.mysql.jdbc.Driver" />
          </div>
          <div class="required field" if="{ needsJdbcDriver }">
            <label>
              <i18n>LABEL_jdbcDriverFile</i18n>
            </label>
            <input type="file" file-input delegate-file-drop max-bytes="5242880" ng-model="update.file" ng-file-change="changeFile">
          </div>
        </div>
        <div class="column">
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label>
              <i18n>LABEL_languageCode</i18n>
            </label>
            <su-dropdown ref="languageCode" items="{ targetLanguageItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label>
              <i18n>LABEL_containerCode</i18n>
            </label>
            <su-dropdown ref="containerCode" items="{ targetContainerItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label>
              <i18n>LABEL_packageBase</i18n>
            </label>
            <input type="text" ref="packageBase" value="org.docksidestage.dbflute" />
          </div>
        </div>
      </div>
      <div class="row">
        <div class="column">
          <div class="required field">
            <label>
              <i18n>LABEL_url</i18n>
            </label>
            <input type="text" ref="url" placeholder="jdbc:mysql://localhost:3306/maihamadb" />
          </div>
          <div class="field">
            <label>
              <i18n>LABEL_schema</i18n>
            </label>
            <input type="text" ref="schema" placeholder="MAIHAMADB" />
          </div>
          <div class="required field">
            <label>
              <i18n>LABEL_user</i18n>
            </label>
            <input type="text" ref="user" placeholder="maihamadb" />
          </div>
          <div class="field">
            <label>
              <i18n>LABEL_password</i18n>
            </label>
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
    const ApiFactory = new _ApiFactory()

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    this.classificationMap = {} // e.g. targetDatabase
    this.jdbcDriver = null
    this.needsJdbcDriver = false
    this.oRMapperOptionsFlg = true
    this.versions = []
    const self = this

    // ===================================================================================
    //                                                                      Initial Method
    //                                                                      ==============
    this.findClassifications = function () {
      ApiFactory.classifications().then(function (json) {
        self.targetDatabaseItems = Object.keys(json.targetDatabaseMap).map(key => {
          return { value: key, label: json.targetDatabaseMap[key].databaseName }
        })
        self.targetLanguageItems = Object.keys(json.targetLanguageMap).map(key => {
          return { value: key, label: json.targetLanguageMap[key] }
        })
        self.targetContainerItems = Object.keys(json.targetContainerMap).map(key => {
          return { value: key, label: json.targetContainerMap[key] }
        })
        self.classificationMap = json
        self.update()
      })
    }
    this.engineVersions = function () {
      ApiFactory.engineVersions().then(function (json) {
        self.versions = json.map(element => {
          return { label: element, value: element }
        })
        self.update()
      })
    }
    this.setLatestEngineVerison = function () {
      ApiFactory.engineLatest().then(function (json) {
        self.refs.dbfluteVersion.value = json.latestReleaseVersion
        self.update()
      })
    }

    // ===================================================================================
    //                                                                        Event Method
    //                                                                        ============
    const changeDatabase = function (databaseCode) {
      let database = self.classificationMap['targetDatabaseMap'][databaseCode]
      // switch showing JDBCDriver select form
      self.needsJdbcDriver = !database.embeddedJar
      // initialize JDBC Driver
      self.jdbcDriver = null
      self.refs.jdbcDriverFqcn.value = database.driverName
      self.refs.url.value = database.urlTemplate
      self.refs.schema.value = database.defaultSchema
    }
    this.create = function () {
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
        dbfluteVersion: self.refs.dbfluteVersion.value,
        packageBase: self.refs.packageBase.value,
        containerCode: self.refs.containerCode.value,
        languageCode: self.refs.languageCode.value,
        jdbcDriverFqcn: self.refs.jdbcDriverFqcn.value,
      }
      if (self.jdbcDriver) {
        client.jdbcDriver = self.jdbcDriver
      }
      const testConnection = self.refs.testConnection.checked
      ApiFactory.createClient(client, testConnection).then(() => {
        route('main')
      })
    }
    this.changeFile = function (files) {
      let file = files[0]
      let reader = new FileReader()
      reader.onload = (function () {
        return function () {
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

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.findClassifications()
      this.engineVersions()
      this.setLatestEngineVerison()

      this.refs.databaseCode.on('change', target => {
        changeDatabase(target.value)
        this.update()
      })
    })
  </script>
</create>