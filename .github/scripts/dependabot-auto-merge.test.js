// .github/scripts/dependabot-auto-merge.js の単体テスト。
// 追加依存なしで動く Node 標準テストランナーを使用。
//   実行: node --test .github/scripts/
//
// テスト名の接頭辞 (A1, B2, C3, ...) はケースの分類 + 連番。
//   A: isFrontendPr(filePaths)   — /frontend 判定 (変更ファイルパス)
//   B: detectUpdateType(title)   — semver の更新種別 (major/minor/patch/null) 判定
//   C: run(...)                  — 全体オーケストレーション (モック octokit で分岐を検証)
//   D: 補助挙動                  — deleteRef 失敗時のフォールバックなど
//   E: dry-run モード            — 読み取りは行うが merge/close/deleteRef を実行しない

const test = require('node:test');
const assert = require('node:assert/strict');

const run = require('./dependabot-auto-merge.js');
const { isFrontendPr, detectUpdateType } = run;

// --- A. isFrontendPr(filePaths) — /frontend 判定 (変更ファイルのパスで判定) ---
test('isFrontendPr', async (t) => {
  await t.test('A1: 全て frontend/ 配下 -> true', () => {
    assert.equal(isFrontendPr(['frontend/package.json', 'frontend/package-lock.json']), true);
  });
  await t.test('A2: ルートの manifest -> false', () => {
    assert.equal(isFrontendPr(['package.json', 'package-lock.json']), false);
  });
  await t.test('A3: frontend とルートが混在 -> false (every)', () => {
    assert.equal(isFrontendPr(['frontend/package.json', 'package.json']), false);
  });
  await t.test('A4: 変更ファイルなし -> false', () => {
    assert.equal(isFrontendPr([]), false);
  });
  await t.test('A5: frontend で始まる別ディレクトリ -> false', () => {
    assert.equal(isFrontendPr(['frontend-tools/x.json']), false);
  });
});

// --- B. detectUpdateType(title) — semver 判定 ---
test('detectUpdateType', async (t) => {
  await t.test('B1: minor', () => {
    assert.equal(detectUpdateType('bump x from 3.14.1 to 3.15.2 in /frontend'), 'minor');
  });
  await t.test('B2: patch', () => {
    assert.equal(detectUpdateType('bump x from 3.3.11 to 3.3.18'), 'patch');
  });
  await t.test('B3: major', () => {
    assert.equal(detectUpdateType('bump x from 7.17.12 to 8.0.0'), 'major');
  });
  await t.test('B4: 0.x のマイナー (数値どおり minor)', () => {
    assert.equal(detectUpdateType('bump axios from 0.32.0 to 0.33.0'), 'minor');
  });
  await t.test('B5: group 更新 -> null', () => {
    assert.equal(detectUpdateType('bump uuid, webpack-cli and webpack-dev-server in /frontend'), null);
  });
  await t.test('B6: プレリリース付き -> patch', () => {
    assert.equal(detectUpdateType('bump x from 1.2.3-beta.1 to 1.2.4'), 'patch');
  });
});

// --- C/D. run(...) — 全体オーケストレーション (モック octokit) ---

// PR ごとの変更ファイルと CI ステータスを設定できるモックを組み立てる。
const CLOSE_MSG = 'テスト用クローズメッセージ';

function buildMock({ prs, filesByPr = {}, ciBySha = {}, throwOnMerge = new Set() }) {
  const actions = [];
  const commentBodies = [];
  const github = {
    paginate: async (fn, opts) => {
      if (fn === github.rest.pulls.list) return prs;
      if (fn === github.rest.pulls.listFiles) {
        return (filesByPr[opts.pull_number] ?? []).map((filename) => ({ filename }));
      }
      throw new Error('unexpected paginate call');
    },
    rest: {
      pulls: {
        list: Symbol('pulls.list'),
        listFiles: Symbol('pulls.listFiles'),
        merge: async ({ pull_number }) => {
          if (throwOnMerge.has(pull_number)) throw new Error('merge boom');
          actions.push(`merge #${pull_number}`);
        },
        update: async ({ pull_number, state }) => {
          actions.push(`update #${pull_number} ${state}`);
        },
      },
      issues: {
        createComment: async ({ issue_number, body }) => {
          actions.push(`comment #${issue_number}`);
          commentBodies.push(body);
        },
      },
      repos: {
        getCombinedStatusForRef: async ({ ref }) => ({
          data: {
            statuses: [{ context: 'ci/circleci: test_and_build', state: ciBySha[ref] ?? 'missing' }],
          },
        }),
      },
      git: {
        deleteRef: async ({ ref }) => {
          actions.push(`deleteRef ${ref}`);
        },
      },
    },
  };
  const context = { repo: { owner: 'dbflute', repo: 'dbflute-intro' } };
  const warnings = [];
  const core = {
    info: () => {},
    warning: (m) => warnings.push(m),
    startGroup: () => {},
    endGroup: () => {},
  };
  return { github, context, core, actions, warnings, commentBodies };
}

const dbPr = (number, { title, ref, sha, login = 'dependabot[bot]' }) => ({
  number,
  title,
  head: { ref, sha },
  user: { login },
});

test('run: 各シナリオで正しいアクションを行う', async () => {
  const prs = [
    dbPr(1, { title: 'bump a from 1.0.0 to 1.0.1', ref: 'db/a', sha: 'sha1' }), // C1 frontend patch, CI success -> merge
    dbPr(2, { title: 'bump b from 1.0.0 to 2.0.0', ref: 'db/b', sha: 'sha2' }), // C2 frontend major -> nothing
    dbPr(3, { title: 'bump c from 1.0.0 to 1.1.0', ref: 'db/c', sha: 'sha3' }), // C3 non-frontend -> close
    dbPr(4, { title: 'bump d from 1.0.0 to 1.1.0', ref: 'db/d', sha: 'sha4' }), // C4 frontend minor, CI failure -> nothing
    dbPr(5, { title: 'bump e from 1.0.0 to 1.1.0', ref: 'db/e', sha: 'sha5' }), // C5 frontend minor, CI missing -> nothing
    dbPr(6, { title: 'bump uuid and webpack in /frontend', ref: 'db/f', sha: 'sha6' }), // C6 group -> nothing
    dbPr(7, { title: 'human pr', ref: 'feature/x', sha: 'sha7', login: 'someone' }), // C7 not dependabot -> skip
    dbPr(8, { title: 'bump g from 1.0.0 to 1.0.1', ref: 'db/g', sha: 'sha8' }), // C9 frontend title but root files -> close
  ];
  const filesByPr = {
    1: ['frontend/package.json'],
    2: ['frontend/package.json'],
    3: ['package.json'],
    4: ['frontend/package.json'],
    5: ['frontend/package.json'],
    6: ['frontend/package.json'],
    8: ['package.json'], // frontend っぽいタイトルでも変更ファイルはルート
  };
  const ciBySha = { sha1: 'success', sha2: 'success', sha4: 'failure', sha5: 'missing', sha6: 'success' };

  const { github, context, core, actions, warnings, commentBodies } = buildMock({ prs, filesByPr, ciBySha });
  await run({ github, context, core, closeComment: CLOSE_MSG });

  assert.deepEqual(actions, [
    'merge #1',
    'deleteRef heads/db/a',
    'comment #3',
    'update #3 closed',
    'deleteRef heads/db/c',
    'comment #8',
    'update #8 closed',
    'deleteRef heads/db/g',
  ]);
  assert.equal(warnings.length, 0);
  // クローズ時のコメント本文は外部から渡した closeComment がそのまま使われる
  assert.deepEqual(commentBodies, [CLOSE_MSG, CLOSE_MSG]);
});

test('C8: 1件で例外が出ても後続 PR の処理は継続する', async () => {
  const prs = [
    dbPr(1, { title: 'bump a from 1.0.0 to 1.0.1', ref: 'db/a', sha: 'sha1' }), // merge で例外
    dbPr(2, { title: 'bump b from 1.0.0 to 1.0.1', ref: 'db/b', sha: 'sha2' }), // 正常に merge
  ];
  const filesByPr = { 1: ['frontend/package.json'], 2: ['frontend/package.json'] };
  const ciBySha = { sha1: 'success', sha2: 'success' };

  const { github, context, core, actions, warnings } = buildMock({
    prs,
    filesByPr,
    ciBySha,
    throwOnMerge: new Set([1]),
  });
  await run({ github, context, core, closeComment: CLOSE_MSG });

  // #1 は warning、#2 は正常にマージされる
  assert.equal(warnings.length, 1);
  assert.match(warnings[0], /#1/);
  assert.deepEqual(actions, ['merge #2', 'deleteRef heads/db/b']);
});

test('D1: deleteRef が失敗してもマージ自体は成功扱い (例外を握りつぶす)', async () => {
  const prs = [dbPr(1, { title: 'bump a from 1.0.0 to 1.0.1', ref: 'db/a', sha: 'sha1' })];
  const { github, context, core, actions, warnings } = buildMock({
    prs,
    filesByPr: { 1: ['frontend/package.json'] },
    ciBySha: { sha1: 'success' },
  });
  github.rest.git.deleteRef = async () => {
    throw new Error('ref not found');
  };

  await run({ github, context, core, closeComment: CLOSE_MSG });

  assert.deepEqual(actions, ['merge #1']); // merge は行われ、deleteRef の例外で全体が落ちない
  assert.equal(warnings.length, 0);
});

test('E: dry-run では merge/close/deleteRef を一切行わない', async () => {
  const prs = [
    dbPr(1, { title: 'bump a from 1.0.0 to 1.0.1', ref: 'db/a', sha: 'sha1' }), // 通常なら merge
    dbPr(2, { title: 'bump c from 1.0.0 to 1.1.0', ref: 'db/c', sha: 'sha2' }), // 通常なら close
  ];
  const filesByPr = { 1: ['frontend/package.json'], 2: ['package.json'] };
  const ciBySha = { sha1: 'success' };

  const { github, context, core, actions, warnings, commentBodies } = buildMock({ prs, filesByPr, ciBySha });
  await run({ github, context, core, closeComment: CLOSE_MSG, dryRun: true });

  assert.deepEqual(actions, []); // 変更系 API は一切呼ばれない
  assert.deepEqual(commentBodies, []); // クローズコメントも投稿しない
  assert.equal(warnings.length, 0);
});
