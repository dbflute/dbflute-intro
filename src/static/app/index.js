'use strict';
window.onerror = function (message, url, lineNumber) {
    alert("画面の処理中にエラーが発生しました。");
    return false;
};

angular.module('dbflute-intro', [
        'ngAnimate',
        'ngCookies',
        'ngTouch',
        'ngSanitize',
        'ui.router',
        'ui.bootstrap',
        'ngStorage',
        'pascalprecht.translate']);

angular.module('dbflute-intro').factory("$exceptionHandler", function ($log) {
    return function (exception, cause) {
        $log.error(exception);
        alert("画面の処理中にエラーが発生しました。");
    };
});

angular.module('dbflute-intro').config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('home', {
            url: '/',
            templateUrl: 'app/main/main.html',
            controller: 'MainCtrl'
        }).state('client', {
            url: '/client',
            templateUrl: 'app/client/client.html',
          controller: 'ClientCtrl'
        });

    $urlRouterProvider.otherwise('/');
});

angular.module('dbflute-intro').config(function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
        prefix: 'assets/i18n/locale-',
        suffix: '.json'
    });

    $translateProvider.useSanitizeValueStrategy(null);

    $translateProvider.preferredLanguage('ja');
    $translateProvider.fallbackLanguage('en');
    $translateProvider.useMissingTranslationHandlerLog();
//    $translateProvider.useLocalStorage();
});

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
                    $rootScope.messages = ['エラーが発生しました。サーバが停止しています。しばらくたってからアクセスしてください。'];
                    reload = true;
                }

                if (response.status === 400) {
                    if (response.data.messages) {
                        var messageList = new Array();
                        var convertCamelToSnake = function(key) {
                            return key.replace(/([A-Z])/g,
                                function(s) {
                                    return '_' + s.charAt(0).toLowerCase();
                                }
                            );
                        };
                        for (var key in response.data.messages) {
                            for (var i in response.data.messages[key]) {
                                var message = response.data.messages[key][i];

                                // リストを含む場合の処理
                                if (key.match(/List/)) {
                                    // indexを含むリストの場合
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

                                // TODO
                                if (key.lastIndexOf('.')) {
                                    key = key.substring(key.lastIndexOf('.') + 1);
                                }
                                var propertyName = convertCamelToSnake(key).toUpperCase();
                                propertyName = "LABEL_" + propertyName;
                                var $translate = $injector.get('$translate')
                                var symbol = ($translate.instant(propertyName) === '') ? '' : '：';
                                messageList.push($translate.instant(propertyName) + symbol + message + '\r\n');
                            }
                        }
                        $rootScope.messages = messageList;

                    } else {
                        $rootScope.messages = angular.isArray(response.data) ? response.data : [response.data];
                    }
                }

                if (response.status === 401) {
                    $rootScope.messages = ['セッションが切れました。再度認証してください。'];
                    reload = true;
                }

                if (response.status === 403) {
                    $rootScope.messages = ['権限がありません。'];
                }

                if (response.status === 500) {
                    $rootScope.messages = angular.isArray(response.data) ? response.data : [response.data];
                    $rootScope.messages.unshift('エラーが発生しました。');
                }

                if (!dialog && $rootScope.messages) {
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
