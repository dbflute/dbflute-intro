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

        // ===============================================================================
        //                                                                          Client
        //                                                                          ======
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
        dfporpBeanList: function(clientBody) {
            return $http({
                method : 'POST',
                url : 'api/dfprop/' + clientBody.projectName + '/list'
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
        schemaHtml: function(client) {
            return $http({
                method: 'POST',
                url: 'api/document/' + client.projectName + '/schemahtml/'
            });
        },
        historyHtml: function(client) {
            return $http({
                method: 'POST',
                url: 'api/document/' + client.projectName + '/historyhtml/'
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
    };
});
