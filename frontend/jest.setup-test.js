// noinspection NodeCoreCodingAssistance
// riotの内部でwindow.cryptoが使用されるが、jestはデフォルトでcrypto関数を用意していないのでnodeのライブラリで関数定義しておく
const nodeCrypto = require('crypto')
window.crypto = {
  getRandomValues: (array) => {
    return nodeCrypto.randomFillSync(array)
  }
}

// index.htmlに合わせて、riotアプリのエントリーポイントとなる要素を予めdocument.bodyに仕込んでおく
const root = document.createElement('div')
root.setAttribute('id', 'root')
document.body.appendChild(root)
