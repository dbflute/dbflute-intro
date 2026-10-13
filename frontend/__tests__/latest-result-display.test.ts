import * as riot from 'riot'

import LatestResult from '../src/static/app/pages/client/latest-result.riot'

type MountedComponent = {
  container: HTMLElement
  unmount: () => void
}

function mountLatestResult(props: { success: boolean; resultTitle: string; content: string }): MountedComponent {
  const container = document.createElement('div')
  document.body.appendChild(container)
  const mounted = riot.component(LatestResult)(container, {
    projectName: 'maihamadb',
    task: 'replaceSchema',
    showHeader: false,
    ...props,
  })
  return {
    container,
    unmount: () => {
      mounted.unmount()
      container.remove()
    },
  }
}

describe('LatestResultの表示', () => {
  test.each([
    ['成功', true, 'Execution Result: Success', '.ui.positive.message'],
    ['失敗', false, 'Execution Result: Failure', '.ui.negative.message'],
  ])('%sの場合は結果種別とタイトルを表示する', (_name, success, resultTitle, selector) => {
    const mounted = mountLatestResult({ success, resultTitle, content: 'task log' })

    const result = mounted.container.querySelector(selector)
    expect(result).not.toBeNull()
    expect(result?.querySelector('.latest-result-title')?.textContent).toContain(resultTitle)

    mounted.unmount()
  })
})
