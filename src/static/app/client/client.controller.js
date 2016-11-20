'use strict';

/**
 * Main Controller
 */
angular.module('dbflute-intro')
        .controller('ClientCtrl', function ($scope, $window, $state, $stateParams, ApiFactory) {
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
        $window.open($scope.configuration['apiServerUrl'] + '/document/' + client.projectName + '/schemahtml/');
    };

    $scope.openHistoryHTML = function(client) {
        $window.open($scope.configuration['apiServerUrl'] + '/document/' + client.projectName + '/historyhtml/');
    };

    // ===================================================================================
    //                                                                               Task
    //                                                                              ======
    $scope.task = function(client, task) {
        $window.open('api/task/execute/' + client.projectName + '/' + task);
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.engineVersions();
    $scope.prepareCurrentProject($scope.projectName);
    $scope.configuration();
});
