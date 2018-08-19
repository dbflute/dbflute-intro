<result-view>
  <su-modal class="large" ref="modal" modal="{ option }">
    <div class="message-area">
      <pre each="{ message in messages }">{ message }</pre>
    </div>
  </su-modal>

  <style>
    .message-area {
      height: 600px;
      overflow: scroll;
    }

    .message-area pre {
      font-family: Monaco, "Courier New", serif;
    }
  </style>

  <script>
    this.option = {
      closable: true,
      buttons: [{
        text: 'Close',
        default: true
      }]
    }
    observable.on('result', content => {
      this.option.header = content.header
      this.refs.modal.messages = content.messages
      this.refs.modal.show()
    })
  </script>
</result-view>
