'use strict';

/**
 * Client Create Controller
 */
angular.module('dbflute-intro')
    .controller('ClientCreateCtrl', function ($scope, $window, $state, ApiFactory) {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    $scope.classificationMap = {}; // e.g. targetDatabase
    $scope.client = {
        create: true,
        mainSchemaSettings: {},
        schemaSyncCheckMap: {},
        dbfluteVersion: '',
        jdbcDriver: null
    }; // model of current client
    $scope.needsJdbcDriver = false;
    $scope.oRMapperOptionsFlg = false;
    $scope.option = {testConnection: true};
    $scope.versions = [];

    // ===================================================================================
    //                                                                      Initial Method
    //                                                                      ==============
    $scope.findClassifications = function () {
        ApiFactory.classifications().then(function (response) {
            $scope.classificationMap = response.data;
        });
    };
    $scope.engineVersions = function () {
        ApiFactory.engineVersions().then(function (response) {
            $scope.versions = response.data;
        });
    };
    $scope.setLatestEngineVerison = function (client) {
        ApiFactory.engineLatest().then(function (response) {
          client.dbfluteVersion = response.data.latestReleaseVersion;
        })
    };

    // ===================================================================================
    //                                                                        Event Method
    //                                                                        ============
    $scope.openORMapperOptions = function () {
        $scope.oRMapperOptionsFlg = !$scope.oRMapperOptionsFlg;
    };
    $scope.changeDatabase = function (client) {
      var database = $scope.classificationMap['targetDatabaseMap'][client.databaseCode];
      // switch showing JDBCDriver select form
      $scope.needsJdbcDriver = !database.embeddedJar;
      // initialize JDBC Driver
      client.jdbcDriver = null;
      client.jdbcDriverFqcn = database.driverName;
      client.mainSchemaSettings.url = database.urlTemplate;
      client.mainSchemaSettings.schema = database.defaultSchema;
    };
    $scope.create = function (client, testConnection) {
        ApiFactory.createClient(client, testConnection).then(function (response) {
            $state.go('home');
        });
    };
    $scope.changeFile = function(files) {
      var file = files[0];
      var reader = new FileReader();
      reader.onload = (function() {
        return function() {
          // encode base64
          var result = window.btoa(reader.result);
          $scope.client.jdbcDriver = {fileName: null, data: null};
          $scope.client.jdbcDriver.fileName = file.name;
          $scope.client.jdbcDriver.data = result;
        };
      }(file));
      if (file) {
        reader.readAsBinaryString(file);
      }
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.findClassifications();
    $scope.engineVersions();
    $scope.setLatestEngineVerison($scope.client);
});
