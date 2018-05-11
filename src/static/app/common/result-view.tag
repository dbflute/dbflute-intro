<result-view>
  <su-modal class="large" ref="modal" modal="{ option }">
    <p each="{ message in messages }">{ message }</p>
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