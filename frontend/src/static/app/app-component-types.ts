// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// 各画面のcomponentは、app-component-types.ts を import する。
//
// e.g. welcome.ts
//  import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
//  ...
//  interface Welcome extends IntroRiotComponent<never, State> { ...
//  export default withIntroTypes<Welcome>({ ...
//
// 各画面は、Riot+Intro+各画面スタイルのビッグな interface を準備して、
// その interface実装として画面の詳細な振る舞いを定義して、
// それを引数の取る withIntroTypes()関数を export default する。
// (この withIntroTypes() は誰が呼ぶのかな？riotの仕組みで呼ばれる？)
//
// withIntroTypes()は、riot.withTypes()の代わり
// コア API - riot.withTypes | Riot.js: https://riot.js.org/ja/api/#riotwithtypes
// _/_/_/_/_/_/_/_/

import { AutobindObjectMethods, RiotComponent, RiotComponentFactoryFunction } from 'riot'
import { DBFluteIntroPlugin } from './app-plugin'
import { SemanticUiRiotPlugin } from '../../@types/semantic-ui-riot'

// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// 各画面のcomponentが、継承するinterface component。
//
// 階層構造:
//  RiotComponent, DBFluteIntroPlugin, SemanticUiRiotPlugin
//   |-interface IntroRiotComponent   // これ
//       |-e.g. interface Welcome     // extends これ
// _/_/_/_/_/_/_/_/
// any は後から解決されるもの!?なのでここではピンポイントでlintを無効化
// eslint-disable-next-line @typescript-eslint/no-explicit-any
export interface IntroRiotComponent<Props = any, State = any>
  extends RiotComponent<Props, State>, DBFluteIntroPlugin, SemanticUiRiotPlugin {}

export type IntroRiotComponentWithoutInternals<Component extends IntroRiotComponent> = Omit<
  Component,
  'props' | 'root' | 'name' | 'slots' | 'mount' | 'update' | 'unmount' | '$' | '$$' | keyof DBFluteIntroPlugin | keyof SemanticUiRiotPlugin
>
export type IntroRiotComponentWithoutInternalsAndInitialState<Component extends IntroRiotComponent> = Omit<
  IntroRiotComponentWithoutInternals<Component>,
  'state'
>

export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentFactory = RiotComponentFactoryFunction<AutobindObjectMethods<IntroRiotComponentWithoutInternals<Component>, Component>>,
>(fn: ComponentFactory): () => Component
export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentFactory = RiotComponentFactoryFunction<
    AutobindObjectMethods<IntroRiotComponentWithoutInternalsAndInitialState<Component>, Component>
  >,
>(fn: ComponentFactory): () => Component
export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentObjectWithInitialState = IntroRiotComponentWithoutInternals<Component>,
>(component: AutobindObjectMethods<ComponentObjectWithInitialState, Component>): Component
export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentObjectWithoutInitialState = IntroRiotComponentWithoutInternalsAndInitialState<Component>,
>(component: AutobindObjectMethods<ComponentObjectWithoutInitialState, Component>): Component
export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentObjectWithoutInitialState = IntroRiotComponentWithoutInternalsAndInitialState<Component>,
>(component: AutobindObjectMethods<ComponentObjectWithoutInitialState, Component>): Component {
  return component as any
}
