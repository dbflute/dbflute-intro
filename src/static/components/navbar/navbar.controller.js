'use strict';

angular.module('dbflute-intro').controller('NavbarCtrl', function ($scope, $translate) {
    $scope.date = new Date();
    $scope.changeLanguage = function (langKey) {
        $translate.use(langKey);
    };
});
