<replace-schema>
  <h3>Replace Schema</h3>
  <button class="ui red button" onclick="{ replaceSchemaTask }">Replace Schema (replace-schema)</button>

  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>
  <script>
    this.executeModal = {
      closable: false
    }
    this.replaceSchemaTask = () => {
      this.suConfirm('Are you sure to execute Replace Schema task?').then(() => {
        this.task('replaceSchema', self.refs.executeModal)
      })
    }
  </script>
</replace-schema>
