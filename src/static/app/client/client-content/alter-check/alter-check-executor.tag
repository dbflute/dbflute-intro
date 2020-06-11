<alter-check-executor>
  <button class="ui red button" onclick="{ alterCheckTask }"><i class="play icon" />Execute AlterCheck</button>

  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>

  <script>
    import _DbfluteTask from '../../../common/DbfluteTask'
    const DbfluteTask = new _DbfluteTask()

    const self = this

    this.executeModal = {}

    this.alterCheckTask = () => {
      this.suConfirm('Are you sure to execute AlterCheck task?').then(() => {
        self.refs.executeModal.show()
        DbfluteTask.task('alterCheck', self.opts.projectname, (message) => {
          console.log('作成')
          self.refs.resultModal.show(message)
        }).finally(() => {
          self.updateContents()
          self.refs.executeModal.hide()
        })
      })
    }
  </script>
</alter-check-executor>
