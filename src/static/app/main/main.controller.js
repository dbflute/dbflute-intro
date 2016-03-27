'use strict';

angular.module('dbflute-intro')
        .controller('MainCtrl', function ($scope, $window, $uibModal, ApiFactory) {

    // レスポンスの型を変換 Bean -> Body
    var convertParam = function(param) {
        var tempParam = angular.copy(param);
        for (var key in tempParam) {
            if (key.indexOf('Bean') >= 0) {
                var old = key;
                var data = tempParam[key];
                key = key.replace('Bean', 'Body');
                tempParam[key] = data;
                delete tempParam[old];
            }

            if (tempParam[key] instanceof Array) {
                if (tempParam[key][0] instanceof Array || tempParam[key][0] instanceof Object) {
                    for (var i in tempParam[key]) {
                        convertParam(tempParam[key][i]);
                    }
                }
            } else if (tempParam[key] instanceof Object) {
                convertParam(tempParam[key]);
            }
        }

        return tempParam;
    };

    $scope.manifest = {};
    $scope.publicProperties = [];
    $scope.versions = [];
    $scope.classificationMap = {};
    $scope.clientBeanList = [];
    $scope.clientBean = null;
    $scope.editFlg = false;
    $scope.option = {testConnection: true};

    $scope.manifest = function() {
        ApiFactory.manifest().then(function(response) {
            $scope.manifest = response.data;
        });
    };

    $scope.engineVersions = function(version) {
        ApiFactory.engineVersions().then(function(response) {
            $scope.versions = response.data;
        });
    };

    $scope.classification = function() {
        ApiFactory.classification().then(function(response) {
            $scope.classificationMap = response.data;
        });
    };

    $scope.findClientBeanList = function() {
        ApiFactory.clientBeanList().then(function(response) {
            $scope.clientBeanList = response.data;
        });
     };

    $scope.add = function() {
        $scope.editFlg = true;
        $scope.clientBean = {create: true, databaseBean: {}, systemUserDatabaseBean: {}, schemaSyncCheckMap: {}, optionBean: {}};
    };

    $scope.edit = function() {
        $scope.editFlg = true;
    };

    $scope.cancelEdit = function() {
        $scope.editFlg = false;
        if ($scope.clientBean.create) {
            $scope.clientBean = null;
        } else {
            for (var index in $scope.clientBeanList) {
                var clientBean = $scope.clientBeanList[index].clientBean;
                if ($scope.clientBean.project == clientBean.project) {
                    $scope.clientBean = clientBean;
                    break;
                }
            }
        }
    };

    $scope.create = function(clientBean, testConnection) {
        ApiFactory.createClient(convertParam(clientBean), testConnection).then(function(response) {
            $scope.editFlg = false;
            $scope.findClientBeanList();
        });
    };

    $scope.update = function(clientBean, testConnection) {
        ApiFactory.updateClient(convertParam(clientBean), testConnection).then(function(response) {
            $scope.editFlg = false;
            $scope.findClientBeanList();
        });
    };

    $scope.remove = function(clientBean) {
        ApiFactory.removeClient(convertParam(clientBean)).then(function(response) {
            $scope.editFlg = false;
            $scope.clientBean = null;
            $scope.findClientBeanList();
        });
    };

    $scope.openSchemaHTML = function(clientBean) {
        $window.open('api/client/schemahtml/' + clientBean.project);
    };

    $scope.openHistoryHTML = function(clientBean) {
        $window.open('api/client/historyhtml/' + clientBean.project);
    };

    $scope.task = function(clientBean, task) {
        $window.open('api/client/task/' + clientBean.project + '/' + task);
    };

    $scope.changeDatabase = function(clientBean) {
        var databaseInfoDef = $scope.classificationMap["databaseInfoDefMap"][clientBean.database];

        clientBean.jdbcDriver = databaseInfoDef.driverName;
        clientBean.databaseBean.url = databaseInfoDef.urlTemplate;
        clientBean.databaseBean.schema =  databaseInfoDef.defultSchema;
        // "needJdbcDriverJar": false,
        // "needSchema": false,
        // "upperSchema": false,
        // "assistInputUser": false,
    };

    $scope.downloadModal = function() {
        var downloadInstance = $uibModal.open({
            templateUrl: 'app/main/download.html',
            controller: 'DownloadInstanceController',
            resolve: {
              publicProperties: function() {
                  return ApiFactory.publicProperties();
              }
          }
        });

        downloadInstance.result.then(function(versions) {
            $scope.versions = versions;
        });
    };

    $scope.addSchemaSyncCheckMap = function() {
        var name = prompt("Please enter name");
        if (!name) {
            // 入力を促す
            return;
        }
        if (!name) {
            return;
        }
        $scope.clientBean.schemaSyncCheckMap[name] = {};
    }

    $scope.removeSchemaSyncCheckMap = function(name) {
        delete $scope.clientBean.schemaSyncCheckMap[name];
    }

    $scope.removeEngine = function(version) {
        var params = {version: version};
        ApiFactory.removeEngine(params).then(function(response) {
            $scope.engineVersions();
        });
    }

    $scope.setCurrentProject = function(clientBean) {
        $scope.clientBean = angular.copy(clientBean.clientBean);
    };

    $scope.manifest();
    $scope.engineVersions();
    $scope.classification();
    $scope.findClientBeanList();
});

angular.module('dbflute-intro').controller('DownloadInstanceController',
        function($scope, $uibModalInstance, publicProperties, ApiFactory) {
    'use strict';

    $scope.downloading = false;

    $scope.publicProperties = {};
    $scope.publicProperties.compatible10x = [];
    $scope.publicProperties.compatible11x = [];
    $scope.currentBranch = 'compatible11x';
    $scope.dbfluteEngine = {version: ''};

    var publicPropertiesData = publicProperties.data;
    var compatible10xRelease = publicPropertiesData['compatible10x.dbflute.latest.release.version'];
    var compatible10xSnapshot = publicPropertiesData['compatible10x.dbflute.latest.snapshot.version'];
    var compatible11xRelease = publicPropertiesData['dbflute.latest.release.version'];
    var compatible11xSnapshot = publicPropertiesData['dbflute.latest.snapshot.version'];

    $scope.publicProperties.compatible10x[0] = compatible10xRelease;
    $scope.publicProperties.compatible11x[0] = compatible11xRelease;

    if (compatible10xRelease !== compatible10xSnapshot) {
        $scope.publicProperties.compatible10x[1] = compatible10xSnapshot;
    }

    if (compatible11xRelease !== compatible11xSnapshot) {
        $scope.publicProperties.compatible11x[1] = compatible11xSnapshot;
        $scope.dbfluteEngine.version = compatible11xSnapshot;
    }

    $scope.dbfluteEngine = {
        version: $scope.publicProperties.compatible11x[0]
    };

    $scope.selectVersion = function(version) {
        $scope.dbfluteEngine.version = version;
    };

    $scope.version = null;

    $scope.downloadEngine = function() {
        $scope.downloading = true;

        if ($scope.version !== null) {
            $scope.dbfluteEngine.version = $scope.version;
        }
        ApiFactory.downloadEngine($scope.dbfluteEngine).then(function(response) {
            $scope.downloading = false;

            ApiFactory.engineVersions().then(function(response) {
                $uibModalInstance.close(response.data);
            });
        });
    };
});
