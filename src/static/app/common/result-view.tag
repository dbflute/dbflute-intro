<result-view>
  <su-modal class="large" ref="modal" modal="{ option }">
    <pre each="{ message in messages }">{ message }</pre>
  </su-modal>

  <script>
    this.option = {
      buttons: [{
        text: 'Close',
      }]
    }
    observable.on('result', content => {
      this.option.header = content.header
      this.refs.modal.messages = content.messages
      this.refs.modal.show()
    })
  </script>
</result-view>
