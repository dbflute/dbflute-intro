'use strict';

import riot from 'riot'
import route from 'riot-route'
import FFetchWrapper from './common/FFetchWrapper'
import 'semantic-ui-riot'

import i18n from 'riot-i18nlet'
import i18n_ja from '../assets/i18n/locale-ja.json'
import i18n_en from '../assets/i18n/locale-en.json'

import ToastMixin from './mixin/ToastMixin';

import './main/main.tag'
import './client/create.tag'
import './client/client.tag'
import './client/client-menu.tag'
import './client/latest-result.tag'
import './client/result-modal.tag'
import './client/client-content/ex-documents.tag'
import './client/client-content/ex-schema-sync-check.tag'
import './client/client-content/ex-replace-schema.tag'
import './client/client-content/ex-alter-check.tag'
import './client/client-content/alter-check/alter-check-checked.tag'
import './client/client-content/alter-check/alter-check-checked-files.tag'
import './client/client-content/alter-check/alter-check-checked-zip.tag'
import './client/client-content/alter-check/alter-check-unrelease-dir.tag'
import './client/client-content/alter-check/alter-check-form.tag'
import './client/client-content/alter-check/alter-check-fix-form.tag'
import './client/client-content/alter-check/alter-check-begin-form.tag'
import './client/client-content/ex-schema-policy-check.tag'
import './client/client-content/schema-policy-check/schema-policy-check-statement-form-wrapper.tag'
import './client/client-content/schema-policy-check/schema-policy-check-statement-form.tag'
import './client/client-content/schema-policy-check/schema-policy-check-statement-form-expected.tag'
import './client/client-content/schema-policy-check/schema-policy-check-statement-form-expected-field.tag'
import './client/client-content/schema-policy-check/schema-policy-check-statement-form-docuement-link.tag'
import './client/client-content/st-database-info.tag'
import './client/client-content/fl-logs.tag'
import './welcome/welcome.tag'
import './common/result-view.tag'
import './common/common-menu.tag'
import './common/i18n.tag'
import './common/loading.tag'
import './common/raw.tag'
import './components/menu/menu-home.tag'
import './error/404.tag'

global.route = route;
global.observable = riot.observable();
riot.mount('*')
riot.mixin(ToastMixin);

/**
 * ffetch for API access
 */
global.ffetch = new FFetchWrapper();


//===================================================================================
//                                                                     Error Handling
//                                                                     ==============
// see IntroApiFailureHook.java for failure response
ffetch.errors.subscribe(response => {
  let header = null;
  let messages = null;
  // #thinking improvement: does it need to reload screen when status=0, 401? (implemented until 0.2.x)
  //let reload = false;
  let validationError = false;
  if (response.status === 0) {
    messages = ['Cannot access the server, retry later'];
  }
  // #hope refactor: extract to method
  if (response.status === 400) {
    header = '400 Bad Request';
    // #hope improvement: formal validation error handling
    if (response.data.failureType) { // basically here (unified JSON if 400)
      header = header + ': ' + response.data.failureType;
      validationError = response.data.failureType === 'VALIDATION_ERROR';
    }
    if (response.data.messages) { // basically here (unified JSON if 400)
      var messageList = new Array();
      for (var key in response.data.messages) {
        for (var i in response.data.messages[key]) {
          var message = response.data.messages[key][i];
          if (key.match(/List/)) {
            if (key.match(/[0-9]/)) {
              var newKey = '';
              var splitList = key.split(/[0-9]/);
              for (var i in splitList) {
                newKey += splitList[i].replace(/[0-9]/, '');
              }
              key = newKey;
            } else {
              key += '[]';
            }
          }
          if (key.lastIndexOf('.')) {
            key = key.substring(key.lastIndexOf('.') + 1);
          }
          if (key === '_global') { // don't use key if global
            messageList.push(message + '\r\n');
          } else {
            const label = i18n.i(`LABEL_${key}`);
            const symbol = (label === '') ? '' : '：';
            messageList.push(`${label}${symbol}${message}\r\n`);
          }
        }
      }
      messages = messageList;
    } else {
      messages = Array.isArray(response.data) ? response.data : [response.data];
    }
  } else if (response.status === 401) {
    header = '401 Not Authorized';
  } else if (response.status === 403) {
    header = '403 Forbidden';
  } else if (response.status >= 500) {
    header = '500 Server Error';
    messages = Array.isArray(response.data) ? response.data : [response.data];
  }
  if (header != null || messages != null) {
  const modalSize = validationError ? 'small' : 'large';
    observable.trigger('result', { header, messages, modalSize })
  }
});


// ===================================================================================
//                                                               Routing (URL mapping)
//                                                               =====================
route(() => {
  riot.mount('side-menu', 'common-menu')
  riot.mount('content', 'main')
})
route('', () => {
  riot.mount('side-menu', 'common-menu')
  riot.mount('content', 'main')
})

// client entry URL from main page
// e.g. #client/maihamadb/ to #client/maihamadb/execute/documents
route('client/*', projectName => {
  const opts = { projectName, clientMenuType: 'execute', clientMenuName: 'documents' }
  riot.mount('side-menu', 'client-menu', opts)
  riot.mount('content', 'client', opts)
})

// client basic page as /{projectName}/{menuType}/{menuName}
// e.g. #client/maihamadb/execute/documents
route('client/*/*/*', (projectName, clientMenuType, clientMenuName) => {
  const opts = { projectName, clientMenuType, clientMenuName }
  riot.mount('side-menu', 'client-menu', opts)
  riot.mount('content', 'client', opts)
})

route('create', () => {
  riot.mount('side-menu', 'common-menu')
  riot.mount('content', 'create')
})
route('welcome', () => {
  riot.mount('side-menu', 'common-menu')
  riot.mount('content', 'welcome')
})
route('/*', () => {
  riot.mount('content', 'error404')
})

route.start(true)


// ===================================================================================
//                                                                Internationalization
//                                                                ====================
i18n.init({
  defaultLangage: 'ja'
})
i18n.loads({
  "en": i18n_en,
  "ja": i18n_ja
})
