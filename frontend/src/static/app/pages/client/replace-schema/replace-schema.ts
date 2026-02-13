// import で特定のtsファイルからinterfaceとfunctionをimport。ファイル内で使えるようにする。
// {}で特定のinterfaceやfunctionを指定できる。
// tsにおけるinterfaceとtypeの違いは？
// 型エイリアスは既存の型や型の組み合わせに新しい名前をつける機能
// typeは型エイリアス
// type型では以下ができる
// プリミティブ型に名前をつける
// プリミティブ型は最小単位。オブジェクト型はプリミティブ型を組み合わせた集合体
// UNION型

// ./は現在のディレクトリ
// ../は1つ上の階層
// ../../は2つ上の階層

// ロジックをimport
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { TaskExecuteStatus } from '../task-execute-modal'
import { api } from '../../../api/api'

// sqlにハイライトをつけるための設定
import Prism from 'prismjs'
//jsファイルをminで軽量化してimport
import 'prismjs/components/prism-sql.min'
import 'prismjs/themes/prism.css'
//カスタムコンポーネントをimport
import LatestResult from '../latest-result.riot'
import TaskExecuteModal from '../task-execute-modal.riot'
import ReplaceSchema from './replace-schema'

type PlaysqlDropdownItem = {
  label: string
  value?: string // value =string | undifined と同じ。stringは任意という意味
}

type ReplaceSchemaLatestResultState = {
  success: boolean
  content: string
}

interface Props {
  projectName: string
}

interface State {
  settings?: DfpropSettingsResult
  playsqlDropDownItems: PlaysqlDropdownItem[]
  executeStatus: TaskExecuteStatus
  executeResultMessage?: string
  latestResult?: ReplaceSchemaLatestResultState
}

// IntroRiotComponentを継承
interface ReplaceSchema extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onMounted(): void
  onclickOpenDataDir(): void
  onclickReplaceSchemaTask(): void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareSettings(projectName: string): Promise<void>
  preparePlaysql(projectName: string): void
  prepareComponents(projectName: string): void
}

// defaultつけると他ファイルでimportするときに好きな名前でimportできる
// defaultは1ファイルに最大１つ
// export function メソッド名<
//   型A (中身のデータ),
//   型B (組み立てガイド) = 型Aを元に自動計算された複雑な型
// >(組み立て関数): () => 型A
// (組み立て関数): 型Aだとメソッド実行実行時に型Aを返す
// (組み立て関数): () =>型Aだとメソッド実行時に関数（引数なしで戻り値型A）を返す
// riot.jsのconst componentAPI = callOrAssign(exports) || {};
//  function callOrAssign(source) {
//     return isFunction(source) ? source.prototype && source.prototype.constructor ? new source() : source() : source;
//   }
// ここで引数なしで実行される
// ComponentFactoryはComponentが決まれば一意に決まるので、１つ目の型Componentだけ明示する
export default withIntroTypes<ReplaceSchema>({
  components: {
    LatestResult,
    TaskExecuteModal,
  },

  state: {
    settings: undefined,
    playsqlDropDownItems: [{ label: '-', value: undefined }],
    executeStatus: 'None',
  },
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted(): void {
    this.prepareSettings(this.props.projectName)
    this.preparePlaysql(this.props.projectName)
    this.prepareComponents(this.props.projectName)
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onclickOpenDataDir(): void {
    api.openDataDir(this.props.projectName)
  },

  async onclickReplaceSchemaTask(): Promise<void> {
    await this.suConfirm('Are you sure to execute Replace Schema task?')
    this.update({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
    try {
      const data = await api.task(this.props.projectName, 'replaceSchema')
      const message = data.success ? 'Success' : 'Failure'
      this.update({ executeStatus: 'Completed', executeResultMessage: message })
    } catch (e) {
      this.update({ executeStatus: 'None' })
    }
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  async prepareSettings(projectName: string): Promise<void> {
    const settings = await api.settings(projectName)
    this.update({
      settings: settings,
    })
  },

  async preparePlaysql(projectName: string): Promise<void> {
    const playsqlListResults = await api.playsqlBeanList(projectName)
    const playsqlDropDownItems = playsqlListResults.map((obj) => ({
      label: obj.fileName,
      value: `<span style="display: none;">${obj.fileName}</span>` + Prism.highlight(obj.content || '', Prism.languages.sql, 'sql'),
    }))
    this.update({
      playsqlDropDownItems: this.state.playsqlDropDownItems.concat(playsqlDropDownItems),
    })
  },

  async prepareComponents(projectName: string): Promise<void> {
    const body = await api.latestResult(projectName, 'replaceSchema')
    if (body) {
      const latestResult = {
        success: body.fileName.includes('success'),
        content: body.content,
      }
      this.update({ latestResult: latestResult })
    }
  },
})
