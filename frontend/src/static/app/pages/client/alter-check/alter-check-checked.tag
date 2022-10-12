<alter-check-checked>
  <!-- Clientの未リリースのAlterDDLを表示する画面 (written at 2022/03/13) -->
  <div class="ui items basic segment">
    <div class="item">
      <alter-check-checked-zip if="{ existsCheckedFiles() }" checkedzip="{ opts.checkedzip }" />
    </div>
    <div class="item">
      <alter-check-unrelease-dir if="{ existsUnreleasedFiles() }" unreleaseddir="{ opts.unreleaseddir }" />
    </div>
  </div>

  <script>
    const self = this

    /**
     * checked zipにAlterDDLが存在するかを判定する
     * @return {boolean} true:存在する, false:zipファイルが存在しない or ファイルが0件
     */
    this.existsCheckedFiles = () => {
      const checkedZip = self.opts.checkedzip
      return checkedZip !== undefined && checkedZip.checkedFiles !== undefined && checkedZip.checkedFiles.length > 0
    }

    /**
     * 未リリースのAlterDDLが存在するかを判定する
     * @return {boolean} true:存在する, false:未リリースDDL用ディレクトリが存在しない or ファイルが0件
     */
    this.existsUnreleasedFiles = () => {
      const unreleasedDir = self.opts.unreleaseddir
      return unreleasedDir !== undefined && unreleasedDir.checkedFiles !== undefined && unreleasedDir.checkedFiles.length > 0
    }
  </script>
</alter-check-checked>
