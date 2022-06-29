<result-view>
  <!-- モーダルによる処理結果表示の画面 (written at 2022/06/06)
   機能:
    o 指定されたメッセージをモーダルで表示する
    (400系500系のレスポンスが戻ってきた時のエラー内容)

   作りの特徴:
    o observableを使って、モーダルの表示を制御できるようにしている
    o architraveというかっこいい単語を使っている
   -->
  <!-- エラー結果はすべてモーダル上で表示されるので、HTML要素はモーダルさんだけ -->
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
    // su-modalに渡すオプション的オブジェクト
    // #thinking 他のところでもそうだが、modalに渡すオブジェクトの名前に統一性を持たせたい by jflute (2022/06/06)
    this.option = {
      closable: true,
      buttons: [{
        text: 'Close',
        default: true
      }]
    }

    // observable.trigger()で呼ばれるコールバック、指定された値で実際にモーダルを表示する
    // #thinking observableって便利だけど、アプリ全体で気軽に使いまくると名前バッティングとか管理が大変そう by jflute (2022/06/06)
    observable.on('result', content => {
      this.sizeStyle = content.modalSize
      this.option.header = content.header
      this.refs.modal.messages = content.messages
      this.refs.modal.show()
    })
  </script>
</result-view>
