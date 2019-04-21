<latest-result>
  <div show="{ latestResult }">
    <h4 class="ui header">Latest Result</h4>
    <div class="ui { latestResult.success ? 'positive' : 'negative' } message">
      <h4>{ latestResult.success ? success.title : failure.title }</h4>
      <p show="{ latestResult.success && success.message }">{ success.message }</p>
      <p show="{ !latestResult.success && failure.message }">{ failure.message }</p>
      <a onclick="{ toggleLatestResult }">{ latestResult.show ? 'hide latest log' : 'show latest log' }</a>
      <div show="{ latestResult.show }" class="ui message message-area">
        <pre>
          <code>
            <raw content="{ latestResult.content }"></raw>
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

    self.latestResult = {}

    this.on('mount', () => {
      self.updateLatestResult()
    })

    this.success = {
      title: 'Result: Success',
      message: null,
    }

    this.failure = {
      title: 'Result: Failure',
      message: null,
    }

    this.updateLatestResult = () => {
      ApiFactory.latestResult(self.opts.projectName, self.opts.task).then((response) => {
        if (response.fileName) {
          self.latestResult = {
            success: response.fileName.includes('success'),
            content: response.content,
            show: false,
          }
        }
        self.update()
      })
    }

    this.toggleLatestResult = () => {
      self.latestResult.show = !self.latestResult.show
      return false
    }
  </script>
</latest-result>
