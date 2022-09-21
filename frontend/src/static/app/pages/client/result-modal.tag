<result-modal>
  <su-modal modal="{ resultModal }" class="large" ref="resultModal">
    <div class="description">
      { opts.modal.message }
    </div>
  </su-modal>

  <script>
    let self = this
    this.resultModal = {
      closable: true,
      buttons: [
        {
          text: 'CLOSE',
          default: true
        }
      ],
      message: ''
    }
    this.show = (message) => {
      self.resultModal.message = message
      self.refs.resultModal.show()
    }
  </script>
</result-modal>
