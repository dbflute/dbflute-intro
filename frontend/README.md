# 構成

## 主要ライブラリ
ライブラリ名| 概要                                                              |参考ページ
---|-----------------------------------------------------------------|---
riot | フロントエンドフレームワーク                                                  | https://riot.js.org/ja/
jest | テスト実装用ライブラリ                                                     | https://jestjs.io/ja/
eslint | Lint用ライブラリ                                                      | https://eslint.org/
babel | 古いブラウザなどにも対応したjsファイルを書き出すためのトランスパイラ                             | https://babeljs.io/
webpack | 最終的なjsファイルをビルドするバンドラ。ローカル開発時のサーバー起動も担う                          | https://webpack.js.org/
husky | Gitのpre commit処理にlint, formatを実行するためのライブラリ                      | https://typicode.github.io/husky/#/
lint-staged | GitのstagedにあるファイルのみにLintを実行するためのライブラリ. huskyと合わせて利用することを想定している。 | https://github.com/okonet/lint-staged#readme

## パッケージ構成

```
├── __tests__                テストファイル用ディレクトリ. このディレクトリに配置したファイルがテストとして実行される
├── .eslintrc.json           ESLintの設定ファイル
├── .node-version            nodeのバージョンを定義するファイル
├── babel.config.js          babelの設定ファイル
├── jest.config.js           jestの設定ファイル
├── jest.riot-transformer.js .riotファイルをjest用のjsに変換するファイル
├── jest.setup-test.js       jestでテストケースごとに実行されるsetup処理を記述するファイル
├── package-lock.json        package.jsonによって実際にinstallされた依存関係を示すファイル
├── package.json             npmにおける利用ライブラリやコマンドなどの設定ファイル
├── src                      アプリを構築するファイル郡のディレクトリ
│   ├── @types               TypeScriptで型補完を効かせるための定義ファイル群（d.ts）
│   └── xxx                  TODO: 変わりそうなので後ほど追記
├── tsconfig.json            TypeScriptの設定ファイル
└── webpack.config.js        webpackの設定ファイル
```


# セットアップ
## ローカル環境の設定

```shell
# 1. frontendディレクトリに移動
cd frontend

# 2. (最初だけ) node のバージョンを .node-version に合わせて変更する
#    手順は各々のnodeバージョン管理方法に合わせてで良い
#    まだ何もインストールしていない場合は https://github.com/nodenv/nodenv がおすすめ

# 3. (最初とpackage.jsonを更新した時だけ) ライブラリインストール
npm install

# 4. フロントサーバー起動 
npm start
```

## IDEの設定
### VSCode
下記のプラグインをインストールする

プラグイン名|URL|用途
---|---|---
Riot-Tag|https://marketplace.visualstudio.com/items?itemName=crisward.riot-tag|.riotタグのシンタックスハイライトなど。ただし、メンテされてない模様...
Prettier - Code formatter|https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode|コードフォーマッター

ファイル保存時に自動でformatがかかるようになっている


### IntelliJ
下記のプラグインをインストールする

プラグイン名|URL|用途
---|---|---
Riot.js|https://plugins.jetbrains.com/plugin/12748-riot-js|.riotタグのシンタックスハイライトなど。ただし、メンテされてない模様...
Prettier|https://plugins.jetbrains.com/plugin/10456-prettier|コードフォーマッター

下記の設定をしておくと便利

- opt + ⌘ + L でフォーマットした際にprettierによるフォーマットを実行するようにする
  - `Preferences > Languages & Frameworks > JavaScript > Prettier` にて `On 'Reformat Code' action` をONにする

# Riotコンポーネントの実装方法
## .riotファイル と .tsファイル の雛形を用意する
1. 実装したいディレクトリに .riotファイルと.tsファイルを作成する
```
例: welcomeタグを作成する場合
├── src
│   └── ...
│     ├── welcome.riot 
│     └── welcome.ts 
```
2. タグコメントごとのルールにしたがって .tsファイル に `interface` を定義する
 
タグコメント|定義するもの
---|---
Definition|一度初期化されたら変わることのないプロパティ（state に含める必要がないプロパティ）
Event Handler|HTMLタグのイベント属性から呼び出される関数. 原則、prefixはDOMイベント名に合わせること（onchange、onclick、...）
Private|TypeScriptファイル内からのみ呼び出されるヘルパー関数など

```ts
// 例: welcome.ts
import { IntroRiotComponent } from '../shared/app.types';

// NOTE: IntroRiotComponent を extends する (riotの組み込み関数やIntro用のpluginをTypeScriptで呼び出せるようにするために必要)
interface Welcome extends IntroRiotComponent {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultDatabaseCode: string
  // ...

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onchangeDatabase: (databaseCode: any) => void
  // ...

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  convertClassificationsForUI: (classifications: IntroClassificationsResult) => any
  // ...
}
```
3. タグコメントごとのルールにしたがって、.tsファイルにRiotコンポーネントを実装し、`export default` で公開する

タグコメント|定義するもの
---|---
Definition|interfaceで定義したDefinitionの実値
Lifecycle|riot標準のライフサイクル関数（必要に応じて）
Event Handler|interfaceで定義したEvent Handlerの実装
Private|interfaceで定義したPrivate関数の実装

```ts
// 例: welcome.ts
import { IntroRiotComponent, withIntroTypes } from '../shared/app.types';

interface Welcome extends IntroRiotComponent {
  // ...
}

// NOTE: withIntroTypes関数で先に定義したinterfaceのRiotコンポーネントを実装する
//       引数にRiotコンポーネントオブジェクトを定義することでTypeScriptの補完が効くようになる
export default withIntroTypes<Welcome>({
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultDatabaseCode: 'mysql',
  // ...

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    // ...
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onchangeDatabase(databaseCode: any) {
      // ...
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  convertClassificationsForUI(classifications: IntroClassificationsResult) {
      // ...
  }
})
```
4. .riotファイルに HTMLタグの定義 と RiotComponentのimport&export を実装する
```html
<!-- 例: welcome.riot -->
<welcome>
  <h2>...</h2>
  <div>
    <div>...</div>
    ...
  </div>
  
  <script>
    // NOTE: 先に定義した .ts ファイルをimportし、そのままexportする
    import welcome from './welcome'
    export default welcome
  </script>
</welcome>
```

## state を利用する
1. Riotコンポーネントにstateプロパティを定義する
```ts
// 例: welcome.ts
export default withIntroTypes<Welcome>({
  state: {
    oRMapperOptionsFlg: false,
  }
})
```
2. HTMLから呼び出す
```html
<!-- 例: welcome.riot -->
<welcome>
  ...
  <div class="required field" hidden={ !state.oRMapperOptionsFlg }>
  ...
</welcome>
```

stateの更新の仕方などは[こちら](https://riot.js.org/ja/documentation/#%E7%8A%B6%E6%85%8B)を参照 

## 他のRiotコンポーネントを利用する
1. .tsファイルに利用するコンポーネントをimportする
```ts
// 例: welcome.ts
import i18n from '../common/i18n.riot';

export default withIntroTypes<Welcome>({
  components: { i18n }, // NOTE: components: { i18n: i18n }, と定義しているのと同義
})
```
2. HTMLから呼び出す
```html
<!-- 例: welcome.riot -->
<welcome>
  ...
  <!-- パターン1: Riotコンポーネントをそのまま使う -->
  <i18n>LABEL_databaseCode</i18n>
  <!-- パターン2: is属性を使うことで標準的なHTMLタグをRiotコンポーネントにすることもできる  -->
  <!-- 参照: https://riot.js.org/ja/documentation/#%E3%82%B3%E3%83%B3%E3%83%9D%E3%83%BC%E3%83%8D%E3%83%B3%E3%83%88%E3%81%A8%E3%81%97%E3%81%A6%E3%81%AE-html-%E8%A6%81%E7%B4%A0 -->
  <label is="i18n">LABEL_databaseCode</label>
  ...
</welcome>
```

## semantic-ui-riotを利用する
1. interface `SemanticUiRiotPlugin` に関数定義を追加する
```ts
export interface SemanticUiRiotPlugin {
  suLoading(b: boolean): void
}
```
2. Riotコンポーネントから呼び出す
```ts
// 例: welcome.ts
export default withIntroTypes<Welcome>({
  onclickCreate() {
    // NOTE: thisで呼び出すことができる
    this.suLoading(true)
  },
})
```

※ 現状、semantic-ui-riot は TypeScript用の型定義(d.tsファイル)が存在していないので利用する semantic-ui-riot 関数に合わせて定義する必要がある

## plugin（共通関数）を利用する
1. interface `DBFluteIntroPlugin` に関数定義を追加する
```ts
export interface DBFluteIntroPlugin {
  successToast: (input: { title: string | undefined, message: string | undefined}) => void
}
```
2. 定義した関数を実装する
```ts
export interface DBFluteIntroPlugin {
  successToast: (input: { title: string | undefined, message: string | undefined}) => void
}

const dbflutePlugin: DBFluteIntroPlugin = {
  successToast({ title = undefined, message = undefined }: { title: string | undefined, message: string | undefined}) {
      // ...
  }
}
```
3. Riotコンポーネントから呼び出す
```ts
// 例: welcome.ts
export default withIntroTypes<Welcome>({
  showToast(projectName: string) {
    // NOTE: thisで呼び出すことができる
    this.successToast({ title: 'xxx', message: 'yyy' })
  },
})

```


# テストの実装方法
TODO
