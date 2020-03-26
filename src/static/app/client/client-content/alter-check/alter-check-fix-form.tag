<alter-check-fix-form>
  <h5 class="header">Fix existing Alter SQL</h5>
  <form class="ui form">
    <div class="fields">
      <div class="field">
        <button class="ui button" onclick="{ prepareAlterCheck }">Fix</button>
      </div>
    </div>
  </form>

  <script>
    import _ApiFactory from '../../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    const self = this

    const projectName = self.opts.projectname
    const updateHandler = self.opts.updatehandler

    this.prepareAlterCheck = () => {
      ApiFactory.prepareAlterSql(projectName).then(() => {
        ApiFactory.openAlterDir(projectName).then(() => {
          updateHandler()
        })
      })
    }
  </script>
</alter-check-fix-form>
