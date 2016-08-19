'use strict';

angular.module('dbflute-intro')
        .controller('MainCtrl', function ($scope, $window, $uibModal, ApiFactory) {

    //  Bean -> Body
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

    $scope.classifications = function() {
        ApiFactory.classifications().then(function(response) {
            $scope.classificationMap = response.data;
        });
    };

    $scope.findClientBeanList = function() {
        ApiFactory.clientBeanList().then(function(response) {
            $scope.clientBeanList = response.data;
        });
     };

    $scope.createClient = function() {
        $scope.editFlg = true;
        $scope.clientBean = {create: true, mainSchemaSettings: {}, systemUserSettings: {}, schemaSyncCheckMap: {}, optionBean: {}};
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
                if ($scope.clientBean.projectName == clientBean.projectName) {
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
        $window.open('api/document/' + clientBean.projectName+ '/schemahtml/');
    };

    $scope.openHistoryHTML = function(clientBean) {
      $window.open('api/document/' + clientBean.projectName+ '/historyhtml/');
    };

    $scope.task = function(clientBean, task) {
        $window.open('api/task/execute/' + clientBean.projectName + '/' + task);
    };

    $scope.dfprop = function(clientBean) {
        ApiFactory.dfporpBeanList(convertParam(clientBean)).then(function (response) {
            $scope.dfpropBeanList = response.data;
        })
    };
    $scope.playsql = function(clientBean) {
        ApiFactory.playsqlBeanList(convertParam(clientBean)).then(function (response) {
            $scope.playsqlBeanList = response.data;
        })
    };
    $scope.log = function(clientBean) {
        ApiFactory.logBeanList(convertParam(clientBean)).then(function (response) {
            $scope.logBeanList = response.data;
        })
    };
    $scope.changeDatabase = function(clientBean) {
    	clientBean.jdbcDriverFqcn = clientBean.driverName;
        var database = $scope.classificationMap["targetDatabaseMap"][clientBean.targetDatabase];
        clientBean.jdbcDriverFqcn = database.driverName;
        clientBean.mainSchemaSettings.url = database.urlTemplate;
        clientBean.mainSchemaSettings.schema =  database.defaultSchema;
    };

    $scope.downloadModal = function() {
        var downloadInstance = $uibModal.open({
            templateUrl: 'app/main/download.html',
            controller: 'DownloadInstanceController',
            resolve: {
                engineLatest: function() {
                    return ApiFactory.engineLatest();
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
    $scope.classifications();
    $scope.findClientBeanList();
});

angular.module('dbflute-intro').controller('DownloadInstanceController',
        function($scope, $uibModalInstance, engineLatest, ApiFactory) {
    'use strict';

    var engineLatestData = engineLatest.data;
    var latestReleaseVersion = engineLatestData.latestReleaseVersion;
    var latestSnapshotVersion = engineLatestData.latestSnapshotVersion;

    $scope.downloading = false;
    $scope.currentBranch = 'compatible11x';
    $scope.latestVersion = latestReleaseVersion;
    $scope.specifiedVersion = latestReleaseVersion;
    $scope.dbfluteEngine = {version: latestReleaseVersion};

    $scope.version = null;

    $scope.downloadEngine = function() {
        $scope.downloading = true;

        if ($scope.specifiedVersion !== null) {
            $scope.dbfluteEngine.version = $scope.specifiedVersion;
        }
        ApiFactory.downloadEngine($scope.dbfluteEngine).then(function(response) {
            $scope.downloading = false;

            ApiFactory.engineVersions().then(function(response) {
                $uibModalInstance.close(response.data);
            });
        });
    };
});
