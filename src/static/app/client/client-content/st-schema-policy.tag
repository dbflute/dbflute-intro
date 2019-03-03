<st-schema-policy>
  <div class="ui segment" title="SchemaPolicy">
    <div class="">
      <h3 class="">Whole Schema Policy</h3>
      <div class="ui divided items" if="{ schemaPolicy.wholeMap }">
        <div class="item" each="{ theme in schemaPolicy.wholeMap.themeList }">
          <div class="ui left floated">
            <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }"
                         onchange="{ editSchemaPolicyMap.bind(this, 'wholeMap', theme.typeCode) }"></su-checkbox>
          </div>
          <div class="content">
            <a class="header">{ theme.name }</a>
            <div class="description">
              {theme.description}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="">
      <h3>Table Schema Policy</h3>
      <div class="ui divided items" if="{ schemaPolicy.tableMap }">
        <div class="item" each="{ theme in schemaPolicy.tableMap.themeList }">
          <div class="ui left floated">
            <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }"
                         onchange="{ editSchemaPolicyMap.bind(this, 'tableMap', theme.typeCode) }"></su-checkbox>
          </div>
          <div class="content">
            <a class="header">{ theme.name }</a>
            <div class="description">
              {theme.description}
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="">
      <h3>Column Schema Policy</h3>
      <div class="ui divided items" if="{ schemaPolicy.columnMap }">
        <div class="item" each="{ theme in schemaPolicy.columnMap.themeList }">
          <div class="ui left floated">
            <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }"
                         onchange="{ editSchemaPolicyMap.bind(this, 'columnMap', theme.typeCode) }"></su-checkbox>
          </div>
          <div class="content">
            <a class="header">{ theme.name }</a>
            <div class="description">
              {theme.description}
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

    this.schemaPolicy = {}

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.prepareSchemaPolicy(self.opts.projectName)
    })
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

  </script>
</st-schema-policy>
