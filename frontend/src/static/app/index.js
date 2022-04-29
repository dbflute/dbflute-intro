import * as riot from 'riot'
import App from './app.riot'
import 'semantic-ui-riot'
import './plugin/i18n'
import introPlugin from './plugin/IntroPlugin'

const mountApp = riot.component(App)
riot.install(introPlugin)

mountApp(document.getElementById('root'))
