const ToastMixin = {
  /**
   * 成功時のトーストを表示する
   * - 使用できるパラメータは[semantic-ui-riotのtoast]{@link https://semantic-ui-riot.web.app/addon/toast}を参照
   * @param {string|null} title タイトル（未設定の場合は省略される）
   * @param {string|null} message メッセージ（未設定の場合は省略される）
   */
  successToast({title= null, message= null}) {
    this.suToast({
      title, // keyと渡す変数名が同じため省略する ("title: title,"としているのと同じ)
      message, // keyと渡す変数名が同じため省略する ("message: message,"としているのと同じ)
      class: 'pink positive'
    })
  }
}

export default ToastMixin
