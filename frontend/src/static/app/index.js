import * as riot from 'riot'
import App from './app.riot'
import 'semantic-ui-riot';
import './plugin/i18n';

const mountApp = riot.component(App);

mountApp(document.getElementById('root'));
