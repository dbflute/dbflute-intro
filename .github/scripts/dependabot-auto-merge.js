// Dependabot の PR を日次でまとめて処理するスクリプト。
// .github/workflows/dependabot-auto-merge.yml から actions/github-script 経由で呼ばれる。
//
// 処理ルール (Issue #649):
//   - /frontend 配下でない PR              -> クローズ
//   - /frontend 配下 かつ major アップデート -> 何もしない (手動レビュー)
//   - /frontend 配下 かつ minor/patch:
//       - CircleCI (test_and_build) が success -> マージ
//       - それ以外 (failure/pending/欠如)       -> 何もしない
//   - バージョンを一意に解釈できない PR (複数依存の group 更新など) -> 何もしない

// CircleCI がコミットステータスとして出すコンテキスト名
const CI_CONTEXT = 'ci/circleci: test_and_build';

/**
 * /frontend 配下の PR かどうかを、変更ファイルのパスで判定する。
 * - Dependabot PR はどれか 1 ディレクトリの manifest だけを変更するため、変更ファイルが全て frontend/ 配下なら frontend の PR とみなす
 * - frontend: ["frontend/package.json", "frontend/package-lock.json"] / ルート: ["package.json", "package-lock.json"]
 * @param filePaths PR で変更されたファイルパスの配列 (NotNull)
 * @return {boolean} 全ファイルが frontend/ 配下なら true (変更ファイルが空なら false)
 */
const isFrontendPr = (filePaths) =>
  filePaths.length > 0 && filePaths.every((p) => p.startsWith('frontend/'));

/**
 * PR タイトルの "from A.B.C to D.E.F" から semver の更新種別を判定する。
 * @param title Dependabot の PR タイトル (NotNull)
 * @return {'major'|'minor'|'patch'|null} 更新種別。一意に解釈できなければ null (複数依存の group 更新など)
 */
const detectUpdateType = (title) => {
  const m = title.match(/from (\d+)\.(\d+)\.(\d+)\S* to (\d+)\.(\d+)\.(\d+)/);
  if (!m) return null;
  const [, oldMajor, oldMinor, , newMajor, newMinor] = m;
  if (oldMajor !== newMajor) return 'major';
  if (oldMinor !== newMinor) return 'minor';
  return 'patch';
};

/**
 * open な Dependabot PR を列挙し、1 件ずつ判定してマージ / クローズ / 何もしない のいずれかを行う。
 * - actions/github-script から `await run({ github, context, core, closeComment })` の形で呼ばれる
 * @param github actions/github-script が渡す octokit クライアント (NotNull)
 * @param context actions/github-script が渡す実行コンテキスト (NotNull)
 * @param core actions/github-script が渡す @actions/core ツールキット (NotNull)
 * @param closeComment 対象外 PR をクローズする際に投稿するコメント本文 (NotNull: workflow から渡す)
 * @param dryRun true の場合、読み取りは行うがマージ/クローズ/ブランチ削除は実行せずログのみ出す (NullAllowed: 既定 false)
 * @return {Promise<void>}
 */
module.exports = async ({ github, context, core, closeComment, dryRun = false }) => {
  const { owner, repo } = context.repo;
  if (dryRun) core.info('*** DRY RUN mode: no merge/close/delete will be performed ***');

  /**
   * ブランチを削除する。削除に失敗しても致命的にはせず info ログに留める。
   * @param ref 削除対象のブランチ名 (NotNull)
   * @return {Promise<void>}
   */
  const deleteBranch = async (ref) => {
    try {
      await github.rest.git.deleteRef({ owner, repo, ref: `heads/${ref}` });
    } catch (e) {
      core.info(`branch delete skipped (${ref}): ${e.message}`);
    }
  };

  /**
   * 対象コミットの CircleCI ステータスを取得する。
   * @param sha 対象コミットの SHA (NotNull)
   * @return {Promise<string>} CI_CONTEXT の state (success / failure / pending など、無ければ 'missing')
   */
  const getCiState = async (sha) => {
    const { data } = await github.rest.repos.getCombinedStatusForRef({ owner, repo, ref: sha });
    return data.statuses.find((s) => s.context === CI_CONTEXT)?.state ?? 'missing';
  };

  /**
   * PR にクローズ理由をコメントしてクローズし、ブランチを削除する。
   * @param num PR 番号 (NotNull)
   * @param branch PR のヘッドブランチ名 (NotNull)
   * @return {Promise<void>}
   */
  const closePr = async (num, branch) => {
    if (dryRun) {
      core.info(`[dry-run] would comment & close #${num}, then delete branch ${branch}`);
      return;
    }
    await github.rest.issues.createComment({
      owner,
      repo,
      issue_number: num,
      body: closeComment,
    });
    await github.rest.pulls.update({ owner, repo, pull_number: num, state: 'closed' });
    await deleteBranch(branch);
  };

  /**
   * PR をマージ (merge commit) し、ブランチを削除する。
   * @param num PR 番号 (NotNull)
   * @param branch PR のヘッドブランチ名 (NotNull)
   * @return {Promise<void>}
   */
  const mergePr = async (num, branch) => {
    if (dryRun) {
      core.info(`[dry-run] would merge #${num}, then delete branch ${branch}`);
      return;
    }
    await github.rest.pulls.merge({ owner, repo, pull_number: num, merge_method: 'merge' });
    await deleteBranch(branch);
  };

  /**
   * PR 1 件を判定して マージ / クローズ / 何もしない のいずれかを行う。
   * @param pr pulls.list が返す PR オブジェクト (NotNull)
   * @return {Promise<void>}
   */
  const processPr = async (pr) => {
    const { number: num, title } = pr;
    const branch = pr.head.ref;

    const files = await github.paginate(github.rest.pulls.listFiles, {
      owner,
      repo,
      pull_number: num,
      per_page: 100,
    });
    const filePaths = files.map((f) => f.filename);

    if (!isFrontendPr(filePaths)) {
      core.info('Not under /frontend -> close');
      await closePr(num, branch);
      return;
    }

    const updateType = detectUpdateType(title);
    if (!updateType) {
      core.info('Cannot parse a single semver bump (group update?) -> skip');
      return;
    }
    core.info(`update_type=${updateType}`);
    if (updateType === 'major') {
      core.info('Major update -> leave for manual review');
      return;
    }

    const ciState = await getCiState(pr.head.sha);
    core.info(`ci_state(${CI_CONTEXT})=${ciState}`);
    if (ciState !== 'success') {
      core.info('CI is not success -> skip (not merging)');
      return;
    }

    core.info(`CI success + ${updateType} -> merge`);
    await mergePr(num, branch);
  };

  const prs = await github.paginate(github.rest.pulls.list, {
    owner,
    repo,
    state: 'open',
    per_page: 100,
  });

  for (const pr of prs) {
    if (pr.user?.login !== 'dependabot[bot]') continue;
    core.startGroup(`PR #${pr.number} - ${pr.title}`);
    try {
      await processPr(pr);
    } catch (e) {
      core.warning(`PR #${pr.number}: ${e.message}`);
    } finally {
      core.endGroup();
    }
  }
};

// 単体テスト用に純粋関数もエクスポートしておく
module.exports.isFrontendPr = isFrontendPr;
module.exports.detectUpdateType = detectUpdateType;
