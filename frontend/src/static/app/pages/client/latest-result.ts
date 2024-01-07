import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import Raw from '../../components/common/raw.riot'

interface Props {
  projectName: string
  task: string
  success: boolean
  showHeader: boolean
  headerTitle?: string
  resultTitle: string
  resultMessage?: string
  content?: string
  linkTitle?: string
  onClickLink?: () => void
}

interface State {
  loaded: boolean
  showContent: boolean
}

interface LatestResult extends IntroRiotComponent<Props, State> {
  onMounted(): void
  toggleLatestResult(): void
}

export default withIntroTypes<LatestResult>({
  components: {
    Raw,
  },
  state: {
    loaded: false,
    showContent: false,
  },
  onMounted() {
    this.state.loaded = true
    this.update()
  },
  toggleLatestResult() {
    this.state.showContent = !this.state.showContent
    this.update()
  },
})
