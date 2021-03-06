<st-schema-policy>
  <div class="ui container">
    <div class="ui segment" title="SchemaPolicy">

      <div class="ui form">
        <div class="row">
          <div class="column">
            <su-tabset class="three column item" schemapolicy="{ schemaPolicy }" tabtitles="{ tabTitles }">
              <su-tab label="{ opts.tabtitles['wholeMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h3>Theme</h3>
                <div class="ui divided items" if="{opts.schemapolicy.wholeMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.wholeMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'wholeMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <a class="header">{ theme.name }</a>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <button class="ui primary button" onclick="{ parent.parent.schemaPolicyCheckTask }">Check Policy (schema-policy-check)</button>
              </su-tab>
              <su-tab label="{ opts.tabtitles['tableMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h3>Theme</h3>
                <div class="ui divided items" if="{opts.schemapolicy.tableMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.tableMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'tableMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <a class="header">{ theme.name }</a>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <button class="ui primary button" onclick="{ parent.parent.schemaPolicyCheckTask }">Check Policy (schema-policy-check)</button>
              </su-tab>
              <su-tab label="{ opts.tabtitles['columnMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h3>Theme</h3>
                <div class="ui divided items" if="{opts.schemapolicy.columnMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.columnMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'columnMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <a class="header">{ theme.name }</a>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <button class="ui primary button" onclick="{ parent.parent.schemaPolicyCheckTask }">Check Policy (schema-policy-check)</button>
              </su-tab>
            </su-tabset>
          </div>
        </div>
      </div>
    </div>
  </div>
  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="description">
      Checking...
    </div>
  </su-modal>
  <result-modal></result-modal>

  <script>
    let riot = require('riot')
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()

    const self = this

    this.schemaPolicy = {}

    this.tabTitles = {
      wholeMap : 'Whole Schema Policy',
      tableMap : 'Table Schema Policy',
      columnMap : 'Column Schema Policy'
    }

    this.checkModal = {
      header : 'SchemaPolicyCheck',
      closable: false
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareSchemaPolicy = (projectName) => {
      ApiFactory.schemaPolicy(projectName).then(json => {
        self.schemaPolicy = json
        self.update()
      })
    }

    this.editSchemaPolicyMap = (targetMap, typeCode) => {
      const targetTheme = this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode)
      const toggledActiveStatus = !targetTheme.isActive

      let body = {
        wholeMap : {themeList : []},
        tableMap : {themeList : []},
        columnMap : {themeList : []}
      }
      body[targetMap].themeList = [{typeCode : typeCode, isActive : toggledActiveStatus}]

      ApiFactory.editSchemaPolicy(opts.projectName, body).then(() => {
        this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode).isActive = toggledActiveStatus
        self.update()
      })
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.schemaPolicyCheckTask = () => {
      self.refs.checkModal.show()
      DbfluteTask.task('doc', self.opts.projectName, (message) => {
        self.resultModal.show(message)
      }).finally(() => {
        self.refs.checkModal.hide()
      })
    }
    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.prepareSchemaPolicy(opts.projectName)
      this.prepareComponents()
    })

    this.prepareComponents = () => {
      self.resultModal = riot.mount('result-modal')[0]
    }
  </script>
</st-schema-policy>
