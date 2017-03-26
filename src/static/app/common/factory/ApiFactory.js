'use strict';
angular.module('dbflute-intro').factory('ApiFactory',
        function ($rootScope, $http) {
    'use strict';

    return {
        // ===============================================================================
        //                                                                           Intro
        //                                                                           =====
        manifest: function() {
            return $http({
                method : 'POST',
                url : 'api/intro/manifest'
            });
        },
        classifications: function() {
        	return $http({
        		method : 'POST',
        		url : 'api/intro/classifications'
        	});
        },
        configuration: function () {
            return $http({
                method: 'POST',
                url: 'api/intro/configuration'
            });
        },

        // ===============================================================================
        //                                                                         Welcome
        //                                                                         =======

        createWelcomeClient: function(client, testConnection) {
          return $http({
            method : 'POST',
            url : 'api/welcome/create',
            data : {client: client, testConnection: testConnection}
          });
        },

        // ===============================================================================
        //                                                                          Client
        //                                                                          ======
        clientList: function() {
            return $http({
                method : 'POST',
                url : 'api/client/list'
            });
        },
        clientOperation: function(projectName) {
            return $http({
                method : 'POST',
                url : 'api/client/operation/' + projectName
            });
        },
        createClient: function(client, testConnection) {
            return $http({
                method : 'POST',
                url : 'api/client/create/' + client.projectName,
                data : {client: client, testConnection: testConnection}
            });
        },
        updateClient: function(clientBody, testConnection) {
            return $http({
                method : 'POST',
                url : 'api/client/edit/' + clientBody.projectName,
                data : {clientBody: clientBody, testConnection: testConnection}
            });
        },
        removeClient: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/client/delete/' + clientBody.project
            });
        },
        settings: function (projectName) {
          return $http({
            method : 'POST',
            url : 'api/settings/' + projectName
          })
        },
        updateSettings: function(clientBody) {
          return $http({
            method : 'POST',
            url : 'api/settings/edit/' + clientBody.projectName,
            data : {client: clientBody}
          });
        },
        dfporpBeanList: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/dfprop/' + clientBody.projectName + '/list'
            });
        },
        syncSchema: function(projectName) {
            return $http({
                method: 'POST',
                url : 'api/dfprop/' + projectName + '/syncschema'
            });
        },
        editSyncSchema: function(projectName, syncSchemaSettingData) {
            return $http({
                method: 'POST',
                url: 'api/dfprop/' + projectName + '/syncschema/edit',
                data: {
                    url: syncSchemaSettingData.url,
                    schema: syncSchemaSettingData.schema,
                    user: syncSchemaSettingData.user,
                    password: syncSchemaSettingData.password,
                    isSuppressCraftDiff: syncSchemaSettingData.isSuppressCraftDiff
                }
            });
        },
        document: function(projectName) {
            return $http({
                method: 'POST',
                url : 'api/dfprop/' + projectName + '/document'
            });
        },
        editDocument: function(projectName, documentSetting) {
            return $http({
                method: 'POST',
                url: 'api/dfprop/' + projectName + '/document/edit',
              data: {
                upperCaseBasic: documentSetting.upperCaseBasic,
                aliasDelimiterInDbComment: documentSetting.aliasDelimiterInDbComment,
                dbCommentOnAliasBasis: documentSetting.dbCommentOnAliasBasis
              }
            });
        },
        playsqlBeanList: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/playsql/' + clientBody.projectName + '/list'
            });
        },
        logBeanList: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/log/' + clientBody.projectName + '/list'
            });
        },

        // ===============================================================================
        //                                                                          Engine
        //                                                                          ======
        engineLatest: function() {
            return $http({
                method : 'POST',
                url : 'api/engine/latest'
            });
        },
        engineVersions: function() {
            return $http({
                method : 'POST',
                url : 'api/engine/versions'
            });
        },
        // needs trailing slash if URL parameter contains dot
        downloadEngine: function(params) {
            return $http({
                method : 'POST',
                url : 'api/engine/download/' + params.version + '/'
            });
        },
        removeEngine: function(params) {
            return $http({
                method : 'POST',
                url : 'api/engine/remove/' + params.version + '/'
            });
        },

        // ===============================================================================
        //                                                                            Task
        //                                                                            ====
        task: function(projectName, task) {
            return $http({
                method: 'POST',
                url: 'api/task/execute/' + projectName + '/' + task
            });
        }
    };
});
