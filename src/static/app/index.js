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
//  * Module for URL mapping
//  */
// angular.module('dbflute-intro').config(function ($stateProvider, $urlRouterProvider) {
//     $stateProvider
//         .state('home', { url: '/', templateUrl: 'app/main/main.html', controller: 'MainCtrl'})
//         .state('operate', { url: '/operate/:projectName', templateUrl: 'app/client/client.html', controller: 'ClientCtrl'})
//         .state('create', { url: '/create', templateUrl: 'app/client/create.html', controller: 'ClientCreateCtrl'})
//         .state('settings', { url: '/settings/:projectName', templateUrl: 'app/settings/settings.html', controller: 'SettingsCtrl'})
//         .state('welcome', { url: '/welcome', templateUrl: 'app/welcome/welcome.html', controller: 'WelcomeCtrl'})
//         ;

//     $urlRouterProvider.otherwise('/');
// });

// /**
//  * Module for i18n
//  */
// angular.module('dbflute-intro').config(function($translateProvider) {
//     $translateProvider.useStaticFilesLoader({
//         prefix: 'assets/i18n/locale-',
//         suffix: '.json'
//     });

//     $translateProvider.useSanitizeValueStrategy(null);

//     $translateProvider.preferredLanguage('ja');
//     $translateProvider.fallbackLanguage('en');
//     $translateProvider.useMissingTranslationHandlerLog();
//     // TODO jflute intro: index.js what is this? (useLocalStorage())
// //    $translateProvider.useLocalStorage();
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

import './main/main.tag'
import './client/create.tag'
import './common/result-view.tag'
import './common/navbar.tag'

global.route = route;
global.observable = riot.observable();
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
            // TODO translate
            // var propertyName = "LABEL_" + key; // related to json property e.g. LABEL_databaseCode
            // var $translate = $injector.get('$translate');
            // var symbol = ($translate.instant(propertyName) === '') ? '' : '：';
            // messageList.push($translate.instant(propertyName) + symbol + message + '\r\n');
            messageList.push(`${key} ${message}\r\n`);
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

route('', () => {
  riot.mount('content', 'main')
})

route(collection => {
  riot.mount('content', collection)
})

route.start(true)

riot.mount('*')
