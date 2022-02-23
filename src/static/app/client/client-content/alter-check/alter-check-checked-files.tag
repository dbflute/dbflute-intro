<alter-check-checked-files>
  <div class="ui list">
    <div class="item" each="{ file in opts.checkedfiles }">
      <a onclick="{ clickFileName.bind(this, file) }">{ file.fileName }</a>
      <div show="{ file.show }" class="ui message message-area">
          <pre>
            <code>
              <raw content="{ file.content }">
                <!-- -->
              </raw>
            </code>
          </pre>
      </div>
    </div>
  </div>

  <script>
    /**
     * ファイルの表示・非表示を切り替えます
     * @param file
     */
    this.clickFileName = (file) => {
      file.show = !(file.show)
    }
  </script>
</alter-check-checked-files>
