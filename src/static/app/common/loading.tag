<loading>
  <!-- ローディング中を画面いっぱいに表示する画面 (written at 2022/05/24)
   機能:
    o ローディング中の主張
    o その間、あなたには何もさせない

   作りの特徴:
    o riot の observable() が表示のトリガーになっている
   -->
  <!-- 画面いっぱいに表示されるローディング表示 -->
  <div class="ui page dimmer inverted {active: counter > 0}">
    <div class="ui huge text loader">Loading</div>
  </div>
  <style>
    <!-- #thinking z-indexさんを使っている理由は？z-indexじゃないとダメなのかな？ by jflute (2022/05/24) -->
    .ui.dimmer {
      z-index: 20000
    }
  </style>
  <script>
    // 0: 表示しない
    // 1: 表示する
    let loadingCounter = 0

    // 呼び出し側で observable.trigger() するとコールバックが呼ばれてcounterが更新される
    // #thinking 二連続でtrue呼び出しするとCounterが2になるのでbooleanにできる方が良い？ by jflute (2022/05/24)
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