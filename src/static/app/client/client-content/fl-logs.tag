<fl-logs>
  <div class="ui container">
    <div class="ui segment" title="Log">
      <su-dropdown items="{ logDropDownItems }" ref="dropdown"></su-dropdown>
      <div class="ui message message-area">
        <pre>{ refs.dropdown.value }</pre>
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
    import _ApiFactory from '../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    const self = this
    const defaultItem = [{label: '-', value: null}]
    this.client = {} // existing clients
    this.logDropDownItems = {}

    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
      self.prepareLogs(self.opts.projectName)
    })
    this.prepareSettings = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
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
  </script>
</fl-logs>
