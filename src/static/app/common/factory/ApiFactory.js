export default class ApiFactory {
    // ===============================================================================
    //                                                                           Intro
    //                                                                           =====
    manifest() {
        return http({
            method: 'POST',
            url: 'api/intro/manifest'
        });
    }
    classifications() {
        return http({
            method: 'POST',
            url: 'api/intro/classifications'
        });
    }
    configuration() {
        return http({
            method: 'POST',
            url: 'api/intro/configuration'
        });
    }

    // ===============================================================================
    //                                                                         Welcome
    //                                                                         =======

    createWelcomeClient(client, testConnection) {
        return http({
            method: 'POST',
            url: 'api/welcome/create',
            data: { client: client, testConnection: testConnection }
        });
    }

    // ===============================================================================
    //                                                                          Client
    //                                                                          ======
    clientList() {
        return http({
            method: 'POST',
            url: 'api/client/list'
        });
    }
    clientOperation(projectName) {
        return http({
            method: 'POST',
            url: 'api/client/operation/' + projectName
        });
    }
    createClient(client, testConnection) {
        return http({
            method: 'POST',
            url: 'api/client/create/' + client.projectName,
            data: { client: client, testConnection: testConnection }
        });
    }
    updateClient(clientBody, testConnection) {
        return http({
            method: 'POST',
            url: 'api/client/edit/' + clientBody.projectName,
            data: { clientBody: clientBody, testConnection: testConnection }
        });
    }
    removeClient(clientBody) {
        return http({
            method: 'POST',
            url: 'api/client/delete/' + clientBody.project
        });
    }
    settings(projectName) {
        return http({
            method: 'POST',
            url: 'api/settings/' + projectName
        })
    }
    updateSettings(clientBody) {
        return http({
            method: 'POST',
            url: 'api/settings/edit/' + clientBody.projectName,
            data: { client: clientBody }
        });
    }
    dfporpBeanList(clientBody) {
        return http({
            method: 'POST',
            url: 'api/dfprop/' + clientBody.projectName + '/list'
        });
    }
    syncSchema(projectName) {
        return http({
            method: 'POST',
            url: 'api/dfprop/' + projectName + '/syncschema'
        });
    }
    editSyncSchema(projectName, syncSchemaSettingData) {
        return http({
            method: 'POST',
            url: 'api/dfprop/' + projectName + '/syncschema/edit',
            data: {
                url: syncSchemaSettingData.url,
                schema: syncSchemaSettingData.schema,
                user: syncSchemaSettingData.user,
                password: syncSchemaSettingData.password,
                isSuppressCraftDiff: syncSchemaSettingData.isSuppressCraftDiff || false // need not null
            }
        });
    }
    document(projectName) {
        return http({
            method: 'POST',
            url: 'api/dfprop/' + projectName + '/document'
        });
    }
    editDocument(projectName, documentSetting) {
        return http({
            method: 'POST',
            url: 'api/dfprop/' + projectName + '/document/edit',
            data: {
                upperCaseBasic: documentSetting.upperCaseBasic,
                aliasDelimiterInDbComment: documentSetting.aliasDelimiterInDbComment,
                dbCommentOnAliasBasis: documentSetting.dbCommentOnAliasBasis
            }
        });
    }
    playsqlBeanList(clientBody) {
        return http({
            method: 'POST',
            url: 'api/playsql/' + clientBody.projectName + '/list'
        });
    }
    logBeanList(clientBody) {
        return http({
            method: 'POST',
            url: 'api/log/' + clientBody.projectName + '/list'
        });
    }

    // ===============================================================================
    //                                                                          Engine
    //                                                                          ======
    engineLatest() {
        return http({
            method: 'POST',
            url: 'api/engine/latest'
        });
    }
    engineVersions() {
        return http({
            method: 'POST',
            url: 'api/engine/versions'
        });
    }
    // needs trailing slash if URL parameter contains dot
    downloadEngine(params) {
        return http({
            method: 'POST',
            url: 'api/engine/download/' + params.version + '/'
        });
    }
    removeEngine(params) {
        return http({
            method: 'POST',
            url: 'api/engine/remove/' + params.version + '/'
        });
    }

    // ===============================================================================
    //                                                                            Task
    //                                                                            ====
    task(projectName, task) {
        return http({
            method: 'POST',
            url: 'api/task/execute/' + projectName + '/' + task
        });
    }
    // ===============================================================================
    //                                                                           Retry
    //                                                                           =====
    retry(method, url, data, useSystemProxies) {
        data = data || {};
        data['useSystemProxies'] = useSystemProxies;
        return http({
            method: method,
            url: url,
            data: data
        });
    }
}

const http = function (request) {
    return fetch(request.url, {
        method: request.method,
        body: toJson(request.data),
    }).then(response => {
        if (!response.ok) {
            return response.json().then(json => {
                throw new ApiError(response.status, json)
            })
        }
        return response.json()
    })
}

const toJson = function (value) {
    return JSON.stringify(value, (k, v) => {
        if (v === '') {
            return null
        }
        return v
    })
}