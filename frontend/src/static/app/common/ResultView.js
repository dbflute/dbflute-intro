import observable from 'riot-observable'
export const resultModal$ = observable()

export function initResultModal(callback) {
  resultModal$.on('result', content => {
    callback(content)
  })
}

export function setResult(content) {
  resultModal$.trigger('result', content)
}
