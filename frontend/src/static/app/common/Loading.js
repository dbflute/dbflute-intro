import observable from 'riot-observable'
const loading$ = observable()

let loadingCounter = 0

export function initLoading(callback) {
  loading$.on('loading', visible => {
    if (visible) {
      loadingCounter++
    } else {
      loadingCounter--
    }
    callback(loadingCounter > 0)
  })
}

export function setLoading(onOff) {
  loading$.trigger('loading', onOff)
}
