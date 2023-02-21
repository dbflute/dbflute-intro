export interface SemanticUiRiotPlugin {
  suLoading(b: boolean): void
  suConfirm(message: string): Promise<void>
}
