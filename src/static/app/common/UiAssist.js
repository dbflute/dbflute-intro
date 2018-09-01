export default class UiAssist {
  setBlankItem(itemList) {
    itemList.unshift({ value: null, label: '' })
  }
}
