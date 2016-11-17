'use strict';

/**
* Welcome Controller
*/
angular.module('dbflute-intro')
        .controller('WelcomeCtrl', function ($scope, $window, $uibModal, $state, $stateParams, ApiFactory) {

    // TODO hakiba implement as you like it by jflute (2016/08/22)
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // << Entry Page >>
    // *Project Name   // e.g. maihamadb
    // *DBMS           // change event for other items
    // +O/R Mapper Options:
    //   *Language     // default: Java
    //   *Container    // default: Lasta Di
    //   *Package Base // default: org.docksidestage.dbflute
    // *JDBC Driver    // auto-input by DBMS
    // *URL            // template-input by DBMS
    // Schema          // required according as DBMS
    // *User
    // Password
    // JDBC Driver Path // required according as DBMS
    // *checkbox: Use the existing database // true then test connection
    //
    // {Create Project!}
    //  o test connection // if fails, validation error
    //  o download latest version
    //  o create client
    // _/_/_/_/_/_/_/_/_/_/

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    $scope.classificationMap = {}; // e.g. targetDatabase
    $scope.client = {
        create: true,
        mainSchemaSettings: {},
        schemaSyncCheckMap: {},
        dbfluteVersion: ""
    }; // model of current client
    $scope.oRMapperOptionsFlg = false;
    $scope.option = {testConnection: true};

    // ===================================================================================
    //                                                                      Initial Method
    //                                                                      ==============
    $scope.findClassifications = function () {
        ApiFactory.classifications().then(function (response) {
            $scope.classificationMap = response.data;
        });
    };
    $scope.registerEngineLatest = function() {
        ApiFactory.engineLatest().then(function (response) {
            $scope.client.dbfluteVersion = response.data.latestReleaseVersion;
        })
    };

    // ===================================================================================
    //                                                                        Event Method
    //                                                                        ============
    $scope.openORMapperOptions = function() {
        $scope.oRMapperOptionsFlg = !$scope.oRMapperOptionsFlg;
    };
    $scope.changeDatabase = function (client) {
    	client.jdbcDriverFqcn = client.driverName;
    	var database = $scope.classificationMap["targetDatabaseMap"][client.databaseCode];
    	client.jdbcDriverFqcn = database.driverName;
    	client.mainSchemaSettings.url = database.urlTemplate;
    	client.mainSchemaSettings.schema = database.defaultSchema;
    };
    $scope.create = function (client, testConnection) {
      var modalInstance = $uibModal.open({
        templateUrl:"progress.html",
        backdrop:"static",keyboard:false
      });
      ApiFactory.createWelcomeClient(client, testConnection).then(function (response) {
        modalInstance.close();
        $state.go('client', { projectName: client.projectName });
      });
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.findClassifications();
    $scope.registerEngineLatest();
});
