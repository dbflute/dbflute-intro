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

import riotI18nlet from 'riot-i18nlet'
import i18n_ja from '../assets/i18n/locale-ja.json'
import i18n_en from '../assets/i18n/locale-en.json'

import './main/main.tag'
import './client/create.tag'
import './common/result-view.tag'
import './common/navbar.tag'
import './common/i18n.tag'

global.route = route;
global.observable = riot.observable();
riot.mount('*')

/**
 * ffetch
 */
global.ffetch = new FFetchWrapper();
ffetch.errors.subscribe(response => {
  let header = null;
  let messages = null;
  let reload = false;
  if (response.status === 0) {
    messages = ['Cannot access the server, retry later'];
    reload = true;
  }
  // TODO jflute intro: index.js extract to method
  if (response.status === 400) {
    header = '400 Bad Request'
    // TODO jflute intro: index.js validation error handling
    if (response.data.messages) {
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
            const label = riotI18nlet.i(`LABEL_${key}`);
            const symbol = (label === '') ? '' : '：';
            messageList.push(`${label}${symbol}${message}\r\n`);
          }
        }
      }
      messages = messageList;
    } else {
      messages = angular.isArray(response.data) ? response.data : [response.data];
    }
  } else if (response.status === 401) {
    header = '401 Not Authorized';
    reload = true;
  } else if (response.status === 403) {
    header = '403 Forbidden';
  } else if (response.status >= 500) {
    header = '500 Server Error';
    messages = angular.isArray(response.data) ? response.data : [response.data];
  }
  observable.trigger('result', { header, messages })
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
  riot.mount('content', 'create', { projectName })
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
route.start(true)

/**
 * i18n
 */
riotI18nlet.init({
  defaultLangage: 'ja'
})
riotI18nlet.loads({
  "en": i18n_en,
  "ja": i18n_ja
})
