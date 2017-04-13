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
    $scope.documentSettings = {};
    $scope.classificationMap = {}; // e.g. targetDatabase

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

    $scope.findClassifications = function () {
        ApiFactory.classifications().then(function (response) {
            $scope.classificationMap = response.data;
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

    $scope.openSyncCheckResultHTML = function(client) {
        $window.open($scope.configuration['apiServerUrl'] + 'document/' + client.projectName + '/synccheckresulthtml/');
    };

    $scope.editDocumentSettings = function() {
      var modalParam = {
          projectName: $scope.projectName,
          documentSettings: $scope.documentSettings
      };
      $uibModal.open({
          templateUrl: "app/client/document-settings.html",
          controller: "DocumentSettingsController",
          resolve: {
              modalParam : function () {
                  return modalParam;
              }
          }
      });
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

    // ===================================================================================
    //                                                                     SchemaSyncCheck
    //                                                                     ===============
    $scope.schemaSyncCheck = function() {
        var modalInstance = $uibModal.open({
            templateUrl:"checking.html",
            backdrop:"static",keyboard:false
        });

        ApiFactory.task($scope.projectName, 'schemaSyncCheck').then(function (response) {
            modalInstance.close();
            if (response.data.success) {
                $uibModal.open({
                    templateUrl:"success.html"
                });
            } else {
                $scope.prepareCurrentProject($scope.projectName);
            }
        }, function (error) {
            modalInstance.close();
        });
    };

    $scope.syncSchemaSetting = function(projectName) {
        ApiFactory.syncSchema(projectName).then(function (response) {
            $scope.syncSchemaSetting = response.data;
        });
    };

    $scope.documentSettings = function(projectName) {
        ApiFactory.document(projectName).then(function(response) {
            $scope.documentSettings = response.data;
        });
    };

    $scope.editSyncSchema = function() {
        var database = $scope.classificationMap['targetDatabaseMap'][$scope.client.databaseCode];
        var modalParam = {
            projectName: $scope.projectName,
            syncSchemaSetting: $scope.syncSchemaSetting,
            defaultSyncUrl: database.urlTemplate
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

    $scope.canCheckSchemaSetting = function() {
        return $scope.syncSchemaSetting.url != null && $scope.syncSchemaSetting.user != null;
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.engineVersions();
    $scope.prepareCurrentProject($scope.projectName);
    $scope.configuration();
    $scope.syncSchemaSetting($scope.projectName);
    $scope.documentSettings($scope.projectName);
    $scope.findClassifications();
});

/**
 * Schema Sync Check Controller
 */
angular.module('dbflute-intro').controller('SchemaSyncCheckSettingController',
        function($scope, $uibModalInstance, modalParam, ApiFactory) {
    'use strict';

    $scope.projectName = modalParam.projectName;
    $scope.syncSchemaSettingData = modalParam.syncSchemaSetting;
    $scope.defaultSyncUrl = modalParam.defaultSyncUrl;

    // ===================================================================================
    //                                                                     SchemaSyncCheck
    //                                                                     ===============
    $scope.doEditSyncSchema = function() {
        ApiFactory.editSyncSchema(modalParam.projectName, $scope.syncSchemaSettingData).then(function(response) {
            $uibModalInstance.close();
        });
    };

    $scope.prepareSyncSchemaUrl = function() {
        $scope.syncSchemaSettingData.url = $scope.syncSchemaSettingData.url || $scope.defaultSyncUrl;
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.prepareSyncSchemaUrl();
});

/**
 * Document Controller
 */
angular.module('dbflute-intro').controller('DocumentSettingsController',
        function($scope, $uibModalInstance, modalParam, ApiFactory) {
    'use strict';

    $scope.projectName = modalParam.projectName;
    $scope.documentSettings = modalParam.documentSettings;

    // ===================================================================================
    //                                                                            Document
    //                                                                            ========
    $scope.doEditDocumentSettings = function () {
        ApiFactory.editDocument(modalParam.projectName, $scope.documentSettings).then(function(response) {
             $uibModalInstance.close();
        });
    };
});
