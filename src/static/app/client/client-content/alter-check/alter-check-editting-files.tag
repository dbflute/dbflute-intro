<alter-check-editting-files>
  <div class="ui list">
    <div class="item" each="{ file in opts.editingsqls }">
      <a onclick="{ clickFileName.bind(this, file) }">
        { file.fileName } <span show="{ nowPrepared(file.fileName) }">(now prepared)</span>
      </a>
      <div show="{ file.show }" class="ui message message-area">
        <pre>
          <code>
            <raw content="{ file.content }" />
          </code>
        </pre>
      </div>
    </div>
  </div>
  <button class="ui button" onclick="{ openAlterDir }"><i class="folder open icon" />SQL Files Directory</button>
  <script>
    import _ApiFactory from '../../../common/factory/ApiFactory.js'

    const self = this
    const ApiFactory = new _ApiFactory()

    this.clickFileName = (file) => {
      file.show = !(file.show)
    }

    this.nowPrepared = (fileName) => {
      const preparedFileName = self.opts.preparedfilename
      return !preparedFileName && fileName === preparedFileName
    }

    this.openAlterDir = () => {
      ApiFactory.openAlterDir(self.opts.projectname)
    }
  </script>
</alter-check-editting-files>
