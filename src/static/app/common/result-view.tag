<result-view>
  <su-modal class="{ sizeStyle } architrave" ref="modal" modal="{ option }">
    <div class="message-area">
      <pre each="{ message in messages }">{ message }</pre>
    </div>
  </su-modal>

  <style>
    .ui.modal.architrave > .header,
    .ui.modal.architrave > .actions {
      background-color: #FFEEEE
    }

    .ui.modal.architrave > .header {
      border-radius: 0px 0px 50px 0px / 0px 0px 50px 0px;
      color: #AA4455
    }
    .ui.modal.architrave > .actions {
      border-radius: 50px 0px 0px 0px / 50px 0px 0px 0px;
    }

    .message-area {
      max-height: 500px;
      overflow: scroll;
      border-radius: 20px 0px 0px 0px / 20px 0px 0px 0px;
      padding-left: 10px;
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
      this.sizeStyle = content.modalSize
      this.option.header = content.header
      this.refs.modal.messages = content.messages
      this.refs.modal.show()
    })
  </script>
</result-view>
