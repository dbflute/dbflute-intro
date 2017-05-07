'use strict';

/**
 * Settings Controller
 */
angular.module('dbflute-intro')
  .controller('SettingsCtrl', function ($scope, $window, $state, $stateParams, Flash, ApiFactory) {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    $scope.client = null; // model of current client
    $scope.projectName = $stateParams.projectName;

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============

    $scope.prepareCurrentProject = function(projectName) {
      ApiFactory.settings(projectName).then(function(response) {
        $scope.client = response.data;
      });
    };
    $scope.edit = function(client) {
      ApiFactory.updateSettings(client).then(function() {
        // success
        $scope.successAlert();
      }, function() {
        // failure
        $scope.failureAlert();
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
    //                                                                             Message
    //                                                                             =======
    $scope.successAlert = function() {
      var message = '<strong>Success!</strong> DB Info was updated properly.';
      var id = Flash.create('success', message, 3000, {class: 'custom-class', id: 'custom-id'}, true);
    };
    $scope.failureAlert = function() {
      var message = "<strong>Failure!</strong> DB Info was can't updated properly.";
      var id = Flash.create('danger', message, 3000, {class: 'custom-class', id: 'custom-id'}, true);
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.prepareCurrentProject($scope.projectName);
  });
