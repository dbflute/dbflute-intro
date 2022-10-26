import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import Raw from '../../components/common/raw.riot'
import { api } from '../../api/api'

interface Props {
  projectName: string
  task: string
}

interface State {
  latestResult: {
    loaded: boolean
    header: {
      text: string
      show: boolean
    }
    show: boolean
  }
}

interface LatestResult extends IntroRiotComponent<Props, State> {
  success: {
    title: string
    message: string | null
  }
  failure: {
    title: string
    message: string | null
    link: {
      message: string | null
      clickAction: () => void
    }
  }

  updateLatestResult(): void
  toggleLatestResult(): void
}

export default withIntroTypes<LatestResult>({
  components: {
    Raw,
  },
  state: {
    latestResult: {
      loaded: false,
      header: {
        text: 'Execution Result',
        show: true,
      },
      show: false,
    },
  },
  success: {
    title: 'Result: Success',
    message: null,
  },
  failure: {
    title: 'Result: Failure',
    message: null,
    link: {
      message: null,
      clickAction: null,
    },
  },

  onMounted() {
    this.updateLatestResult()
  },

  updateLatestResult() {
    api.latestResult(this.props.projectName, this.props.task).then((response) => {
      if (response.fileName) {
        this.state.latestResult = Object.assign(this.state.latestResult, {
          success: response.fileName.includes('success'),
          content: response.content,
          show: false,
          loaded: true,
        })
      }
      this.update()
    })
  },

  toggleLatestResult() {
    this.state.latestResult.show = !this.state.latestResult.show
    this.update()
  },
})
