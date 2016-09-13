'use strict';

/**
* Welcome Controller
*/
angular.module('dbflute-intro')
        .controller('WelcomeCtrl', function ($scope, $window, $uibModal, ApiFactory) {

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

    //  Bean -> Body
    var convertParam = function(param) {
    // #later deleted
    //        var tempParam = angular.copy(param);
    //        for (var key in tempParam) {
    //            if (key.indexOf('Bean') >= 0) {
    //                var old = key;
    //                var data = tempParam[key];
    //                key = key.replace('Bean', 'Body');
    //                tempParam[key] = data;
    //                delete tempParam[old];
    //            }
    //
    //            if (tempParam[key] instanceof Array) {
    //                if (tempParam[key][0] instanceof Array || tempParam[key][0] instanceof Object) {
    //                    for (var i in tempParam[key]) {
    //                        convertParam(tempParam[key][i]);
    //                    }
    //                }
    //            } else if (tempParam[key] instanceof Object) {
    //                convertParam(tempParam[key]);
    //            }
    //        }
    //
    //        return tempParam;
    return param;
    };

    $scope.manifest = {}; // intro manifest
    $scope.versions = []; // engine versions
    $scope.classificationMap = {}; // e.g. targetDatabase
    $scope.client = {
        create: true,
        mainSchemaSettings: {},
        systemUserSettings: {},
        schemaSyncCheckMap: {},
        optionBean: {}
    }; // model of current client
    $scope.clientList = []; // existing clients
    $scope.editFlg = true;
    $scope.oRMapperOptionsFlg = false;
    $scope.option = {testConnection: true};

    // ===================================================================================
    //                                                                          Basic Data
    //                                                                          ==========
    $scope.manifest = function () {
        ApiFactory.manifest().then(function (response) {
            $scope.manifest = response.data;
        });
    };

    $scope.engineVersions = function (version) {
        ApiFactory.engineVersions().then(function (response) {
            $scope.versions = response.data;
        });
    };

    $scope.classifications = function () {
        ApiFactory.classifications().then(function (response) {
            $scope.classificationMap = response.data;
        });
    };

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    $scope.setCurrentProject = function (client) {
        $scope.client = angular.copy(client);
    };

    $scope.findClientList = function () {
        ApiFactory.clientList().then(function (response) {
            $scope.clientList = response.data;
        });
    };

    $scope.showClientInput = function () {
        $scope.editFlg = true;
        $scope.client = {
            create: true,
            mainSchemaSettings: {},
            systemUserSettings: {},
            schemaSyncCheckMap: {},
            optionBean: {}
        };
    };

    $scope.openORMapperOptions = function() {
        $scope.oRMapperOptionsFlg = !$scope.oRMapperOptionsFlg;
    };

    $scope.cancelEdit = function () {
        $scope.editFlg = false;
        if ($scope.client.create) {
            $scope.client = null;
        } else {
            for (var index in $scope.clientList) {
                var client = $scope.clientList[index];
                if ($scope.client.projectName == client.projectName) {
                    $scope.client = client;
                    break;
                }
            }
        }
    };

    $scope.create = function (client, testConnection) {
        ApiFactory.createClient(convertParam(client), testConnection).then(function (response) {
            $scope.editFlg = false;
            $scope.findClientList();
        });
    };

    $scope.update = function (client, testConnection) {
        ApiFactory.updateClient(convertParam(client), testConnection).then(function (response) {
            $scope.editFlg = false;
            $scope.findClientList();
        });
    };

    $scope.remove = function (client) {
        ApiFactory.removeClient(convertParam(client)).then(function (response) {
            $scope.editFlg = false;
            $scope.client = null;
            $scope.findClientList();
        });
    };

    $scope.changeDatabase = function (client) {
        client.jdbcDriverFqcn = client.driverName;
        var database = $scope.classificationMap["targetDatabaseMap"][client.databaseCode];
        client.jdbcDriverFqcn = database.driverName;
        client.mainSchemaSettings.url = database.urlTemplate;
        client.mainSchemaSettings.schema = database.defaultSchema;
    };

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    $scope.manifest();
    $scope.engineVersions();
    $scope.classifications();
    $scope.findClientList();
});
