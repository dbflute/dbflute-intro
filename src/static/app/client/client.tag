<client>
  <h1>DBFlute Client { opts.projectName }</h1>
    <!-- hide for_now, not important and should be designed more by jflute (2020/09/28)
    <p>for { client.databaseCode }, { client.languageCode }, { client.containerCode }</p>
     -->
  <client-content></client-content>

  <script>
    let riot = require('riot')
    import _ApiFactory from '../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    let self = this
    this.client = {}

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareCurrentProject(self.opts.projectName)
    })

    this.prepareCurrentProject = () => {
      ApiFactory.clientPropbase(self.opts.projectName).then((response) => {
        self.client = response
        self.mountClientContent()
        self.update()
      })
    }

    this.mountClientContent = () => {
      let tagName = null
      const menuType = this.opts.clientMenuType
      const menuName = this.opts.clientMenuName

      if (menuType === 'execute' && menuName === 'documents') {
        tagName = 'ex-documents'
      } else if (menuType === 'execute' && menuName === 'schema-sync-check') {
        tagName = 'ex-schema-sync-check'
      } else if (menuType === 'execute' && menuName === 'replace-schema') {
        tagName = 'ex-replace-schema'
      } else if (menuType === 'execute' && menuName === 'alter-check') {
        tagName = 'ex-alter-check'
      } else if (menuType === 'execute' && menuName === 'schema-policy-check') {
        tagName = 'ex-schema-policy-check'
      } else if (menuType === 'settings' && menuName === 'database-info') {
        tagName = 'st-database-info'
      } else if (menuType === 'files' && menuName === 'logs') {
        tagName = 'fl-logs'
      }
      if (tagName) {
        riot.mount('client-content', tagName, { projectName: this.opts.projectName, client: this.client })
      }
    }
  </script>
</client>
