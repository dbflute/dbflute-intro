<result-modal>
  <!-- 何かの処理結果を伝えるモーダルダイアログ  (written at 2022/05/12)
    機能:
      o モーダルダイアログを表示する
      o モーダル上で任意のメッセージを伝える
      o モーダル上のボタンでモーダルを消せる
      o モーダル外のクリックでモーダルを消せる
      o 表示後すぐにEnterでモーダルを消せる

      作りの特徴:
      o 呼び出し側のtagからの引数として表示メッセージを受け取っている (当たり前のことだけど)
      o 呼び出し側のtagで関数を呼び出せばモーダルを表示できるようにしている
      o su-modalさん命よ！ https://github.com/black-trooper/semantic-ui-riot/blob/master/tags/modal/su-modal.riot
  -->
  <!-- #thinking 属性オブジェクト名とref名が同じって紛らわしくないだろうか？ by jflute (2022/05/12) -->
  <su-modal modal="{ resultModal }" class="large" ref="resultModal">
    <div class="description">
      { opts.modal.message }
    </div>
  </su-modal>

  <script>
    let self = this

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // モーダル情報を司るオブジェクト
    // 
    // [tips]
    // closable: モーダル外でクリックしたときにcloseするか？
    // buttons-default: デフォルトでフォーカスが当たってるか？(すぐにEnterで押せるか？)
    // message: モーダルダイアログで表示するメッセージ (ここではshow()のときに設定される)
    // _/_/_/_/_/_/_/_/_/_/
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

    /**
     * モーダルダイアログを指定されたメッセージで表示する。
     * 呼び出し側のtagのscriptから呼ばれる想定。ダイアログを出したい時に呼ぶ。
     * @param {string} message モーダルダイアログで表示するメッセージ (NotNull)
     */
    this.show = (message) => {
      self.resultModal.message = message
      self.refs.resultModal.show()
    }
  </script>
</result-modal>
