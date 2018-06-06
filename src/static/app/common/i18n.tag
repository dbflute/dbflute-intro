<i18n>
  <span>{ message }</span>

  <span ref="original" name="original" class="original">
    <yield />
  </span>

  <style>
    .original {
      display: none;
    }
  </style>
  <script>
    this.on('mount', () => {
      this.update()
    })
    this.on('update', function () {
      const key = this.refs.original.innerText.trim()
      this.message = this[this.riotI18nlet.settings.getMessageFunctionName](key, opts.vals, opts.options) || key
    })
  </script>
</i18n>