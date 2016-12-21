'use strict';

/**
 * Client Settings Controller
 */
angular.module('dbflute-intro')
  .controller('ClientSettingsCtrl', function ($scope, $window, $state, $stateParams, ApiFactory) {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    $scope.client = null; // model of current client
    $scope.projectName = $stateParams.projectName;

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============

    $scope.prepareCurrentProject = function(projectName) {
      ApiFactory.clientSettings(projectName).then(function(response) {
        $scope.client = response.data;
      });
    };

    // ===================================================================================
    //                                                                       Optional Menu
    //                                                                       =============
    $scope.dfprop = function(client) {
      ApiFactory.dfporpBeanList(client).then(function (response) {
        $scope.dfpropBeanList = response.data;
      });
    };
    $scope.playsql = function(client) {
      ApiFactory.playsqlBeanList(client).then(function (response) {
        $scope.playsqlBeanList = response.data;
      });
    };
    $scope.log = function(client) {
      ApiFactory.logBeanList(client).then(function (response) {
        $scope.logBeanList = response.data;
      });
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.prepareCurrentProject($scope.projectName);
  });
