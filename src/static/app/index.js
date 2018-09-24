'use strict';

// /**
//  * onError
//  */
// window.onerror = function (message, url, lineNumber) {
//     alert("Sorry...script error");
//     return false;
// };

// /**
//  * Module for ???
//  */
// angular.module('dbflute-intro', [
//         'ngAnimate',
//         'ngCookies',
//         'ngTouch',
//         'ngSanitize',
//         'ui.router',
//         'ui.bootstrap',
//         'ngStorage',
//         'pascalprecht.translate',
//         'ngFlash']);

// /**
//  * Module for exceptionHandler
//  */
// angular.module('dbflute-intro').factory("$exceptionHandler", function ($log) {
//     return function (exception, cause) {
//         $log.error(exception);
//         alert("Sorry...script error");
//     };
// });

// /**
//  * NetworkError Modal
//  */
// angular.module('dbflute-intro').controller('NetworkErrorController', function($scope, $uibModalInstance, ApiFactory, modalParam) {
//   'use strict';
//   $scope.config = modalParam;
//   $scope.useSystemProxies = false;

//   $scope.retry = function() {
//     ApiFactory.retry(this.config.method, this.config.url, this.config.data, this.useSystemProxies);
//     $uibModalInstance.close();
//   };
// });

import riot from 'riot'
import route from 'riot-route'
import FFetchWrapper from './common/FFetchWrapper'
import 'semantic-ui-riot'

import i18n from 'riot-i18nlet'
import i18n_ja from '../assets/i18n/locale-ja.json'
import i18n_en from '../assets/i18n/locale-en.json'

import './main/main.tag'
import './settings/settings.tag'
import './client/create.tag'
import './client/client.tag'
import './welcome/welcome.tag'
import './common/result-view.tag'
import './common/navbar.tag'
import './common/i18n.tag'
import './common/loading.tag'
import './error/404.tag'

global.route = route;
global.observable = riot.observable();
riot.mount('*')

/**
 * ffetch for API access
 */
global.ffetch = new FFetchWrapper();

/**
 * Error Handling
 * (see IntroApiFailureHook.java for failure response)
 */
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

/**
 * URL mapping
 */
route(() => {
  riot.mount('content', 'main')
})
route('', () => {
  riot.mount('content', 'main')
})
route('operate/*', projectName => {
  riot.mount('content', 'client', { projectName })
})
route('create', () => {
  riot.mount('content', 'create')
})
route('settings/*', projectName => {
  riot.mount('content', 'settings', { projectName })
})
route('welcome', () => {
  riot.mount('content', 'welcome')
})
route('/*', () => {
  riot.mount('content', 'error404')
})
route.start(true)

/**
 * i18n
 */
i18n.init({
  defaultLangage: 'ja'
})
i18n.loads({
  "en": i18n_en,
  "ja": i18n_ja
})
