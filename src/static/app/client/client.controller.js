'use strict';

/**
 * Main Controller
 */
angular.module('dbflute-intro')
        .controller('ClientCtrl', function ($scope, $window, $uibModal, $state, $stateParams, ApiFactory) {

    $scope.projectName = $stateParams.projectName;
    $scope.versions = []; // engine versions
    $scope.configuration = {}; // intro configuration
    $scope.client = null; // model of current client
    $scope.syncSchemaSetting = {};

    // ===================================================================================
    //                                                                          Basic Data
    //                                                                          ==========
    $scope.engineVersions = function(version) {
        ApiFactory.engineVersions().then(function(response) {
            $scope.versions = response.data;
        });
    };

    $scope.configuration = function() {
        ApiFactory.configuration().then(function(response) {
            $scope.configuration = response.data;
        });
    };

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    $scope.prepareCurrentProject = function(projectName) {
        ApiFactory.clientOperation(projectName).then(function(response) {
            $scope.client = response.data;
        });
    };

    // ===================================================================================
    //                                                                            Document
    //                                                                            ========
    $scope.openSchemaHTML = function(client) {
        $window.open($scope.configuration['apiServerUrl'] + 'document/' + client.projectName + '/schemahtml/');
    };

    $scope.openHistoryHTML = function(client) {
        $window.open($scope.configuration['apiServerUrl'] + 'document/' + client.projectName + '/historyhtml/');
    };

    // ===================================================================================
    //                                                                               Task
    //                                                                              ======
    $scope.task = function(task) {
        var modalInstance = $uibModal.open({
            templateUrl:"generating.html",
            backdrop:"static",keyboard:false
        });

        ApiFactory.task($scope.projectName, task).then(function (success) {
            modalInstance.close();
            $scope.prepareCurrentProject($scope.projectName);
        }, function (error) {
            modalInstance.close();
        });
    };

    $scope.schemaSyncCheck = function() {
        var modalInstance = $uibModal.open({
            templateUrl:"checking.html",
            backdrop:"static",keyboard:false
        });
        ApiFactory.task($scope.projectName, 'schemaSyncCheck').then(function (success) {
            modalInstance.close();
        }, function (error) {
            modalInstance.close();
            $window.open($scope.configuration['apiServerUrl'] + 'document/' + $scope.projectName + '/synccheckresulthtml/');
        });
    };
    // ===================================================================================
    //                                                                     SchemaSyncCheck
    //                                                                     ===============
    $scope.syncSchemaSetting = function(projectName) {
        ApiFactory.syncSchema(projectName).then(function (response) {
            $scope.syncSchemaSetting = response.data;
        });
    };

    $scope.editSyncSchema = function() {
        var modalParam = {
            projectName: $scope.projectName,
            syncSchemaSetting: $scope.syncSchemaSetting
        };
        $uibModal.open({
            templateUrl: "app/client/schema-sync-check.html",
            controller: "SchemaSyncCheckSettingController",
            resolve: {
                modalParam : function () {
                    return modalParam;
                }
            }
        });
    };


    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.engineVersions();
    $scope.prepareCurrentProject($scope.projectName);
    $scope.configuration();
    $scope.syncSchemaSetting($scope.projectName);
});

/**
 * Schema Sync Check Controller
 */
angular.module('dbflute-intro').controller('SchemaSyncCheckSettingController',
        function($scope, $uibModalInstance, modalParam, ApiFactory) {
    'use strict';

    $scope.projectName = modalParam.projectName;
    $scope.syncSchemaSettingData = modalParam.syncSchemaSetting;

    $scope.editSyncSchema = function() {
        ApiFactory.editSyncSchema(modalParam.projectName, $scope.syncSchemaSettingData).then(function(response) {
            $uibModalInstance.close();
        });
    };
});
