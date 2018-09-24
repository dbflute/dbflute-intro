<loading>
  <div class="ui page dimmer inverted {active: counter > 0}">
    <div class="ui huge text loader">Loading</div>
  </div>
  <style>
    .ui.dimmer {
      z-index: 20000
    }
  </style>
  <script>
    let loadingCounter = 0

    observable.on('loading', visible => {
      if (visible) {
        loadingCounter++
      } else {
        loadingCounter--
      }
      this.update({ counter: loadingCounter })
    })
  </script>
</loading>