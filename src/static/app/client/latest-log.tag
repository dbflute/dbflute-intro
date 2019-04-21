<latest-log>
  <div show="{ latestLog }">
    <h4 class="ui header">Latest Log</h4>
    <div class="ui { latestLog.success ? 'positive' : 'negative' } message">
      <span>Result: { latestLog.success ? 'Success' : 'Failure' }&nbsp;&nbsp;&nbsp;&nbsp;</span><a onclick="{ toggleLatestLog }">show more detail...</a>
      <div show="{ latestLog.show }" class="ui message message-area">
        <pre>
          <code>
            <raw content="{ latestLog.content }"></raw>
          </code>
        </pre>
      </div>
    </div>
  </div>

  <style>
    pre {
      overflow: auto;
      white-space: pre-wrap;
      word-break: break-word;
    }
  </style>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory'

    const ApiFactory = new _ApiFactory()

    let self = this

    this.on('mount', () => {
      self.updateLatestLog()
    })

    this.updateLatestLog = () => {
      ApiFactory.latestLog(self.opts.projectName, self.opts.task).then((response) => {
        if (response.fileName) {
          self.latestLog = {
            success: response.fileName.includes('success'),
            content: response.content,
            show: false,
          }
        }
        self.update()
      })
    }

    this.toggleLatestLog = () => {
      self.latestLog.show = !self.latestLog.show
      return false
    }
  </script>
</latest-log>
