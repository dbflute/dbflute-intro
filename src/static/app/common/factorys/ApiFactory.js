'use strict';
angular.module('dbflute-intro').factory('ApiFactory',
        function ($rootScope, $http) {
    'use strict';

    return {
        manifest: function() {
            return $http({
                method : 'POST',
                url : 'api/intro/manifest'
            });
        },
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
        classifications: function() {
            return $http({
                method : 'POST',
                url : 'api/client/classifications'
            });
        },
        clientList: function() {
            return $http({
                method : 'POST',
                url : 'api/client/list'
            });
        },
        createClient: function(client, testConnection) {
            return $http({
                method : 'POST',
                url : 'api/client/create',
                data : {client: client, testConnection: testConnection}
            });
        },
        updateClient: function(clientBody, testConnection) {
            return $http({
                method : 'POST',
                url : 'api/client/update',
                data : {clientBody: clientBody, testConnection: testConnection}
            });
        },
        removeClient: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/client/delete/' + clientBody.project
            });
        },
        downloadEngine: function(params) {
            return $http({
                method : 'POST',
                url : 'api/engine/download/' + params.version
            });
        },
        removeEngine: function(params) {
            return $http({
                method : 'POST',
                url : 'api/engine/remove/' + params.version
            });
        },
        dfporpBeanList: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/dfprop/' + clientBody.project + '/list'
            });
        },
        playsqlBeanList: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/playsql/' + clientBody.project + '/list'
            });
        },
        logBeanList: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/log/' + clientBody.project + '/list'
            });
        },
        schemaHtml: function(client) {
            return $http({
                method: 'POST',
                url: 'api/document/' + client.projectName + '/schemahtml/'
            });
        },
    };
});
