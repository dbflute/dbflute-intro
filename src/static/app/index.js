'use strict';

/**
 * onError
 */
window.onerror = function (message, url, lineNumber) {
    alert("Sorry...script error");
    return false;
};

/**
 * Module for ???
 */
angular.module('dbflute-intro', [
        'ngAnimate',
        'ngCookies',
        'ngTouch',
        'ngSanitize',
        'ui.router',
        'ui.bootstrap',
        'ngStorage',
        'pascalprecht.translate']);

/**
 * Module for exceptionHandler
 */
angular.module('dbflute-intro').factory("$exceptionHandler", function ($log) {
    return function (exception, cause) {
        $log.error(exception);
        alert("Sorry...script error");
    };
});

/**
 * Module for URL mapping
 */
angular.module('dbflute-intro').config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('home', { url: '/', templateUrl: 'app/main/main.html', controller: 'MainCtrl'})
        .state('client', { url: '/client/:projectName', templateUrl: 'app/client/client.html', controller: 'ClientCtrl'})
        .state('create', { url: '/create', templateUrl: 'app/client/create.html', controller: 'ClientCreateCtrl'})
        .state('welcome', { url: '/welcome', templateUrl: 'app/welcome/welcome.html', controller: 'WelcomeCtrl'})
        ;

    $urlRouterProvider.otherwise('/');
});

/**
 * Module for i18n
 */
angular.module('dbflute-intro').config(function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: 'assets/i18n/locale-',
        suffix: '.json'
    });

    $translateProvider.useSanitizeValueStrategy(null);

    $translateProvider.preferredLanguage('ja');
    $translateProvider.fallbackLanguage('en');
    $translateProvider.useMissingTranslationHandlerLog();
    // TODO jflute intro: index.js what is this? (useLocalStorage())
//    $translateProvider.useLocalStorage();
});

/**
 * Module for httpProvider
 */
angular.module('dbflute-intro').config(function($httpProvider) {
    $httpProvider.interceptors.push(
            ['$q', '$rootScope', '$injector',
            function($q, $rootScope, $injector) {

        var dialog = null;
        return {
            responseError: function(response) {
                $rootScope.messages = null;
                var reload = false;
                if(response.status === 0) {
                    $rootScope.messages = ['Cannot access the server, retry later'];
                    reload = true;
                }
                // TODO jflute intro: index.js extract to method
                if (response.status === 400) {
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
                                            newKey += splitList[i].replace(/[0-9]/,'');
                                        }
                                        key = newKey;
                                    } else {
                                        key += '[]';
                                    }
                                }
                                if (key.lastIndexOf('.')) {
                                    key = key.substring(key.lastIndexOf('.') + 1);
                                }
                                var propertyName = "LABEL_" + key; // related to json property e.g. LABEL_databaseCode
                                var $translate = $injector.get('$translate')
                                var symbol = ($translate.instant(propertyName) === '') ? '' : '：';
                                messageList.push($translate.instant(propertyName) + symbol + message + '\r\n');
                            }
                        }
                        $rootScope.messages = messageList;
                    } else {
                        $rootScope.messages = angular.isArray(response.data) ? response.data : [response.data];
                    }
                } else if (response.status === 401) {
                    $rootScope.messages = ['401 Not Authorized'];
                    reload = true;
                } else if (response.status === 403) {
                    $rootScope.messages = ['403 Forbidden'];
                } else if (response.status >= 500) {
                    $rootScope.messages = angular.isArray(response.data) ? response.data : [response.data];
                    $rootScope.messages.unshift('500 Server Error');
                }
                if (!dialog && $rootScope.messages) {
                	// TODO jflute intro: what is resultView.html?
                    dialog = $injector.get('$uibModal').open({templateUrl: 'resultView.html', scope: $rootScope});
                    dialog.result.then(function () {
                            dialog = null;
                            if (reload) {
                                window.location.reload(reload);
                            }
                        }, function () {
                            dialog = null;
                            if (reload) {
                                window.location.reload(reload);
                            }
                        });
                }
                return $q.reject(response);
            }
        }
    }]);
});
