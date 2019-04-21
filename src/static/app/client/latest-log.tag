<latest-log>
  <div show="{ latestLog }">
    <h4 class="ui header">Latest Log</h4>
    <div class="ui { latestLog.success ? 'positive' : 'negative' } message">
      <h4>{ latestLog.success ? success.title : failure.title }</h4>
      <p show="{ latestLog.success && success.message }">{ success.message }</p>
      <p show="{ !latestLog.success && failure.message }">{ failure.message }</p>
      <a onclick="{ toggleLatestLog }">{ latestLog.show ? 'hide detail...' : 'show more detail...' }</a>
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

    this.success = {
      title: 'Result: Success',
      message: null,
    }

    this.failure = {
      title: 'Result: Failure',
      message: null,
    }

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
