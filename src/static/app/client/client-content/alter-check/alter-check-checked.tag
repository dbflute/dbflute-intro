<alter-check-checked>
  <div class="ui items">
    <div class="item">
      <alter-check-checked-zip if="{ existsCheckedFiles }" checkedzip="{ opts.checkedzip }" />
    </div>
    <div class="item">
      <alter-check-unrelease-dir if="{ existsUnreleasedFiles }" unreleaseddir="{ opts.unreleaseddir }" />
    </div>
  </div>

  <script>
    const self = this

    const checkedZip = self.opts.checkedzip
    const unreleasedDir = self.opts.unreleaseddir

    this.existsCheckedFiles = () => {
      return checkedZip !== undefined && checkedZip.checkedFiles !== undefined && checkedZip.checkedFiles.length > 0
    }

    this.existsUnreleasedFiles = () => {
      return unreleasedDir !== undefined && unreleasedDir.checkedFiles !== undefined && unreleasedDir.checkedFiles.length > 0
    }
  </script>
</alter-check-checked>
