<alter-check-begin-form>
  <h5 class="header">Begin New AlterCheck!!</h5>
  <form class="ui form">
    <div class="fields">
      <div class="inline field error{!state.invalidFileName}">
        <label>alter-schema-</label>
        <input type="text" ref="alterNameInput" placeholder="input ticket name">
        <label>.sql</label>
      </div>
      <div class="inline field">
        <button class="ui primary button" onclick="{ createAlterSql }">Begin</button>
      </div>
    </div>
  </form>

  <script>
    import _ApiFactory from '../../../common/factory/ApiFactory.js'

    const apiFactory = new _ApiFactory()
    const self = this

    const projectName = self.opts.projectname
    const updateHandler = self.opts.updatehandler

    this.on('before-mount', () => {
      self.state = {
        invalidFileName: false
      }
    })

    this.createAlterSql = () => {
      const ticketName = self.refs.alterNameInput.value
      if (self.validate(ticketName)) {
        return
      }
      apiFactory.prepareAlterSql(projectName).then(() => {
        const alterFileName = 'alter-schema-' + ticketName + '.sql'
        apiFactory.createAlterSql(projectName, alterFileName).then(() => {
          updateHandler()
        })
      })
    }

    this.validate = (ticketName) => {
      self.state.invalidFileName = !ticketName || ticketName === ''
      return self.state.invalidFileName
    }
  </script>
</alter-check-begin-form>
