import * as riot from 'riot'
import App from './app.riot'
import 'semantic-ui-riot';

const mountApp = riot.component(App);

mountApp(document.getElementById('root'));
