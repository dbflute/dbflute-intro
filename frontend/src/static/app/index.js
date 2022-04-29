import * as riot from 'riot'
import App from './app.riot'
import 'semantic-ui-riot'
import './plugin/i18n'
import introPlugin from './plugin/IntroPlugin'

// 全てのComponentで共通的に利用する処理を登録する
riot.install(introPlugin)

// index.htmlのid=rootタグにapp.riotをマウントすることでriotアプリを起動する
const mountApp = riot.component(App)
mountApp(document.getElementById('root'))
