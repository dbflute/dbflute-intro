import { AutobindObjectMethods, RiotComponent, RiotComponentFactoryFunction } from 'riot'
import { DBFluteIntroPlugin } from './app-plugin'
import { SemanticUiRiotPlugin } from '../../@types/semantic-ui-riot'

export interface IntroRiotComponent extends RiotComponent, DBFluteIntroPlugin, SemanticUiRiotPlugin {}

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
  ComponentFactory = RiotComponentFactoryFunction<AutobindObjectMethods<IntroRiotComponentWithoutInternals<Component>, Component>>
>(fn: ComponentFactory): () => Component
export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentFactory = RiotComponentFactoryFunction<
    AutobindObjectMethods<IntroRiotComponentWithoutInternalsAndInitialState<Component>, Component>
  >
>(fn: ComponentFactory): () => Component
export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentObjectWithInitialState = IntroRiotComponentWithoutInternals<Component>
>(component: AutobindObjectMethods<ComponentObjectWithInitialState, Component>): Component
export function withIntroTypes<
  Component extends IntroRiotComponent,
  ComponentObjectWithoutInitialState = IntroRiotComponentWithoutInternalsAndInitialState<Component>
>(component: AutobindObjectMethods<ComponentObjectWithoutInitialState, Component>): Component {
  return component as any
}
