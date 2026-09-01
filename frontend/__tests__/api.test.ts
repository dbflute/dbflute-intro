import { extractMessages } from '../src/static/app/api/api'
import { isTaskLogSuccess } from '../src/static/app/api/task-log'

describe('extractMessages', () => {
  test.each([
    [
      'IntroのUnifiedFailureBean形式のmessagesが返されたとき、メッセージを配列へ正規化できること',
      { messages: { name: ['必須です'], email: ['形式が不正です'] } },
      ['必須です', '形式が不正です'],
    ],
    [
      'messages内に想定外のobjectがあるとき、JSON文字列として正規化できること',
      { messages: { detail: { code: 'INVALID_REQUEST' } } },
      ['{"code":"INVALID_REQUEST"}'],
    ],
    ['配列が返されたとき、各要素をメッセージとして正規化できること', ['エラー1', 'エラー2'], ['エラー1', 'エラー2']],
    ['plain textが返されたとき、配列へ正規化できること', 'Gateway Timeout', ['Gateway Timeout']],
    ['想定外のobjectが返されたとき、JSON文字列として正規化できること', { detail: 'unexpected' }, ['{"detail":"unexpected"}']],
    ['dataがないとき、フォールバックメッセージを返すこと', undefined, ['fallback message']],
  ])('%s', (_name, data, expected) => {
    expect(extractMessages(data, 'fallback message')).toEqual(expected)
  })
})

describe('ログファイル名によるタスク成否判定', () => {
  test.each([
    ['successを含むログファイル名なら成功と判定すること', 'dbflute_intro_doc_success_20260510.log', true],
    ['failureを含むログファイル名なら失敗と判定すること', 'dbflute_intro_doc_failure_20260510.log', false],
  ])('%s', (_name, fileName, expected) => {
    expect(isTaskLogSuccess(fileName)).toBe(expected)
  })
})
