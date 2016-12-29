'use strict';

/**
 * Main Controller
 */
angular.module('dbflute-intro')
        .controller('ClientCtrl', function ($scope, $window, $uibModal, $state, $stateParams, ApiFactory) {
    // TODO deco remove unused functions
    //  Bean -> Body
    var convertParam = function(param) {
        return param;
    };

    $scope.projectName = $stateParams.projectName;
    $scope.versions = []; // engine versions
    $scope.configuration = {}; // intro configuration
    $scope.client = null; // model of current client

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
            templateUrl:"progress.html",
            backdrop:"static",keyboard:false
        });

        ApiFactory.task($scope.projectName, task).then(function (success) {
            modalInstance.close();
            $scope.prepareCurrentProject($scope.projectName);
        }, function (error) {
            modalInstance.close();
        });
    };
    // ===================================================================================
    //                                                                     SchemaSyncCheck
    //                                                                     ===============
    $scope.schemaSyncCheck = function() {
        var modalInstance = $uibModal.open({
            templateUrl: "app/client/schema-sync-check.html",
            controller: "SchemaSyncCheckSettingController",
            resolve: {
                syncSchemaSetting: function() {
                    return ApiFactory.syncSchema($scope.projectName);
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
});

/**
 * Schema Sync Check Controller
 */
angular.module('dbflute-intro').controller('SchemaSyncCheckSettingController',
        function($scope, $uibModalInstance, syncSchemaSetting, ApiFactory) {
    'use strict';

    $scope.syncSchemaSettingData = syncSchemaSetting.data;

    $scope.checkSchema = function() {
        // TODO add check schema by deco
        console.log("update schema");
    };
});
