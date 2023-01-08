var genType = 'remoteapi';
var srcPathList = [];

/**
 * process.
 * @param {Request[]} requestList - requestList (NotNull)
 */
function process(requestList) {
    for each (var request in requestList) {
        if (!request.isResourceTypeSwagger()) {
            continue;
        }
        manager.info('...Generating ' + genType + ': ' + request.requestName);
        try {
            request.enableOutputDirectory();
            manager.makeDirectory(request.generateDirPath);
            var optionMap = request.optionMap;
            scriptEngine.eval('load("./freegen/' + genType + '/' + 'RemoteApiRule.js");');
            if (optionMap.ruleJsPath && optionMap.ruleJsPath != '') {
                // load application rule settings if exists
                scriptEngine.eval('load("' + optionMap.ruleJsPath + '");');
            }
            processHull(request);
        } catch (e) {
            var message = '/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\n';
            message += 'Error in ' + genType + ' generation.\n';
            message += '\n';
            message += '[Advice]\n';
            message += '1. Upgrade version of dbflute engine to 1.1.6 or later.\n';
            message += '2. If you can not upgrade the version, you may be able to avoid errors by chang setting.\n';
            message += '3. Still if you do not improve it, there is a possibility of a bug.\n';
            message += '_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/\n';
            print(message);
            throw e;
        }
    }
    for each (var request in requestList) {
        if (!request.isResourceTypeSwagger()) {
            continue;
        }
        var optionMap = request.optionMap;
        scriptEngine.eval('load("./freegen/' + genType + '/' + 'RemoteApiRule.js");');
        if (optionMap.ruleJsPath && optionMap.ruleJsPath != '') {
            // load application rule settings if exists
            scriptEngine.eval('load("' + optionMap.ruleJsPath + '");');
        }
        var rule = scriptEngine.get('remoteApiRule');
        var schema = scriptEngine.invokeMethod(rule, 'schema', request);
        var schemaPackage = scriptEngine.invokeMethod(rule, 'schemaPackage', schema);
        var package = request.package + '.' + schemaPackage;
        clean(rule, request, package.replace(/\./g, '/'), srcPathList);
        clean(rule, request, '../resources/' + genType + '/di', srcPathList);
    }
}

/**
 * process hull.
 * @param {Request} request - request (NotNull)
 */
function processHull(request) {
    var rule = scriptEngine.get('remoteApiRule');

    // load java property type mapping
    var typeMap = scriptEngine.invokeMethod(rule, 'typeMap');

    // schema name is from part of FreeGen request name e.g. RemoteApiSeaLand => SeaLand
    var schema = scriptEngine.invokeMethod(rule, 'schema', request);
    var schemaPackage = scriptEngine.invokeMethod(rule, 'schemaPackage', schema);

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // swagger.json's key
    // _/_/_/_/_/_/_/_/_/_/
    var optionMap = request.optionMap;
    var pathMap = optionMap.jsonMap['paths'];
    var definitionMap = optionMap.jsonMap['definitions'];

    var apiList = [];
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // loop paths's elements, per one API URL: e.g. /sea/land
    // _/_/_/_/_/_/_/_/_/_/
    for (var pathKey in pathMap) {
        var path = pathMap[pathKey];
        var commonParameters = pathMap[pathKey].parameters;
        for (var methodKey in pathMap[pathKey]) {
            var method = path[methodKey];
            var api = new java.util.LinkedHashMap();
            api.schema = schema;
            api.package = request.package;
            api.url = pathKey;
            api.httpMethod = methodKey;
            api.consumes = method.consumes || optionMap.jsonMap['consumes'];
            api.produces = method.produces || optionMap.jsonMap['produces'];
            api.methodBean = method;
            if (commonParameters && !commonParameters.equals(api.methodBean)) {
                if (api.methodBean.parameters) {
                    api.methodBean.parameters.addAll(commonParameters);
                } else {
                    api.methodBean.parameters = commonParameters;
                }
            }
            if (scriptEngine.invokeMethod(rule, 'target', api)) {
                api.url = scriptEngine.invokeMethod(rule, 'url', api);
                apiList.push(api);
            }
        }
    }
    for (var apiIndex in apiList) {
        var api = apiList[apiIndex];
        for (var comparisonApiIndex in apiList) {
            var comparisonApi = apiList[comparisonApiIndex];
            if (!api.equals(comparisonApi) && api.url === comparisonApi.url) {
                api.multipleHttpMethod = true;
                break;
            }
        }
    }

    var remoteApiBeanList = [];
    var exBehaviorMap = new java.util.LinkedHashMap();
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // loop api
    // _/_/_/_/_/_/_/_/_/_/
    for (apiIndex in apiList) {
        var api = apiList[apiIndex];
        // +------------------------+
        // |                        |
        // | Generate 'Param' beans |
        // |                        |
        // +------------------------+
        var pathVariables = new java.util.LinkedHashMap();
        var queryProperties = new java.util.LinkedHashMap();
        var formDataProperties = new java.util.LinkedHashMap();
        var bodyProperties = new java.util.LinkedHashMap();
        var paramBean = new java.util.LinkedHashMap();
        var paramBeanArray = false;

        // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
        // loop parameters's elements, e.g. in, name, description, required, schema, ...
        // _/_/_/_/_/_/_/_/_/_/
        for (parameterIndex in api.methodBean['parameters']) {
            var parameter = api.methodBean['parameters'][parameterIndex];
            if (parameter.in === 'path') {
                pathVariables[parameter.name] = parameter;
            } else if (parameter.in === 'query') {
                queryProperties[parameter.name] = parameter;
            } else if (parameter.in === 'formData') {
                formDataProperties[parameter.name] = parameter;
            } else if (parameter.in === 'body') {
                var definitionKey = '';
                if (parameter.schema.type && parameter.schema.type === 'array') {
                    definitionKey = java.net.URLDecoder.decode(parameter.schema.items['$ref'].replace('#/definitions/', ''), "UTF-8");
                } else if (parameter.schema['$ref']) {
                    definitionKey = java.net.URLDecoder.decode(parameter.schema['$ref'].replace('#/definitions/', ''), "UTF-8");
                }
                bodyProperties = definitionMap[scriptEngine.invokeMethod(rule, 'definitionKey', definitionKey)].properties;
                for (bodyPropertyKey in bodyProperties) {
                    var required = definitionMap[scriptEngine.invokeMethod(rule, 'definitionKey', definitionKey)].required;
                    if (required) {
                        bodyProperties[bodyPropertyKey].required = required.contains(bodyPropertyKey);
                    }
                }
                var remoteApiBean = createBean(rule, 'param', api, bodyProperties, definitionMap, definitionKey);
                remoteApiBean.in = api.consumes && api.consumes.indexOf('application/xml') !== -1 ? 'xml' : 'json';
                paramBeanArray = parameter.schema.type === 'array';
                paramBean = remoteApiBean;
                remoteApiBeanList.push(paramBean);
            }
        }

        if (bodyProperties.isEmpty()) {
            if (!queryProperties.isEmpty()) {
                var remoteApiBean = createBean(rule, 'param', api, queryProperties, definitionMap);
                remoteApiBean.in = 'query';
                paramBean = remoteApiBean;
                remoteApiBeanList.push(paramBean);
            }
            if (!formDataProperties.isEmpty()) {
                var remoteApiBean = createBean(rule, 'param', api, formDataProperties, definitionMap);
                remoteApiBean.in = 'formData';
                paramBean = remoteApiBean;
                remoteApiBeanList.push(paramBean);
            }
        }

        // +-------------------------+
        // |                         |
        // | Generate 'Return' beans |
        // |                         |
        // +-------------------------+
        var returnProperties = new java.util.LinkedHashMap();
        var returnBean = new java.util.LinkedHashMap();
        var returnBeanArray = false;

        // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
        // loop responses's elements, e.g. in, name, description, required, schema, ...
        // _/_/_/_/_/_/_/_/_/_/
        for (responseKey in api.methodBean['responses']) {
            var response = api.methodBean['responses'][responseKey];
            // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
            // only one return bean is targeted.
            // _/_/_/_/_/_/_/_/_/_/
            if (!returnBean.className && response.schema) {
                var array = response.schema.type === 'array';
                var responseSchema = array ? response.schema.items : response.schema;

                if (typeMap[responseSchema.format]) {
                    returnBean.className = typeMap[responseSchema.format];
                    returnBean.array = array;
                } else if (typeMap[responseSchema.type]) {
                    returnBean.className = typeMap[responseSchema.type];
                    returnBean.array = array;
                } else {
                    var definitionKey = java.net.URLDecoder.decode(responseSchema['$ref'].replace('#/definitions/', ''), "UTF-8");
                    definitionKey = scriptEngine.invokeMethod(rule, 'definitionKey', definitionKey);
                    var definition = definitionMap[definitionKey];
                    if (definition) {
                        var returnProperties = definition.properties;
                        if (!returnProperties.isEmpty()) {
                            for (returnPropertyKey in returnProperties) {
                                var returnPropertyValue = returnProperties[returnPropertyKey];
                                var required = definitionMap[scriptEngine.invokeMethod(rule, 'definitionKey', definitionKey)].required;
                                if (required) {
                                    returnPropertyValue.required = required.contains(returnPropertyKey);
                                }
                            }
                            var remoteApiBean = createBean(rule, 'return', api, returnProperties, definitionMap, definitionKey);
                            returnBeanArray = array;
                            returnBean = remoteApiBean;
                            remoteApiBeanList.push(returnBean);
                        }
                    }
                }
                // #typescript api.produces may be null so check it
                returnBean.in = (api.produces && api.produces.indexOf('application/xml') !== -1) ? 'xml' : 'json';
            }
        }
        keepBehavior(rule, api, pathVariables, paramBean, paramBeanArray, returnBean, returnBeanArray, exBehaviorMap);
    }

    if (rule['beanExtendsDefinitionGeneration']) {
        for each (var remoteApiBean in remoteApiBeanList) {
            if (!remoteApiBean.definitionKey) {
                continue;
            }
            for each (var comparisonRemoteApiBean in remoteApiBeanList) {
                if (remoteApiBean !== comparisonRemoteApiBean
                        && remoteApiBean.definitionKey === comparisonRemoteApiBean.definitionKey
                        && remoteApiBean.in === comparisonRemoteApiBean.in) {
    
                    var definitionRemoteApiBean = new java.util.LinkedHashMap();
                    definitionRemoteApiBean.api = null;
                    var schemaPackage = scriptEngine.invokeMethod(rule, 'schemaPackage', api.schema);
                    var subPackage = scriptEngine.invokeMethod(rule, 'beanExtendsDefinitionSubPackage', request, remoteApiBean.definitionKey);
                    var package = api.package + '.' + schemaPackage;
                    if (subPackage) {
                        package = package + '.' + subPackage;
                    }
                    definitionRemoteApiBean.package = package;
                    definitionRemoteApiBean.className = scriptEngine.invokeMethod(rule, 'beanExtendsDefinitionClassName', request, remoteApiBean.definitionKey);
                    definitionRemoteApiBean.extendsClass = remoteApiBean.extendsClass;
                    definitionRemoteApiBean.implementsClasses = remoteApiBean.implementsClasses;
                    definitionRemoteApiBean.properties = remoteApiBean.properties;
                    definitionRemoteApiBean.beanPurposeType = remoteApiBean.definitionKey;
                    definitionRemoteApiBean.remoteApiExp = 'definition';
                    definitionRemoteApiBean.definitionMap = definitionMap;
                    definitionRemoteApiBean.in = remoteApiBean.in;
    
                    remoteApiBeanList.push(definitionRemoteApiBean);
    
                    remoteApiBean.extendsClass = definitionRemoteApiBean.package + '.' + definitionRemoteApiBean.className;
                    remoteApiBean.implementsClasses = null;
                    remoteApiBean.properties = [];
                    remoteApiBean.definitionRemoteApiBean = definitionRemoteApiBean;
                    break;
                 }
            }
        }
    }

    processBean(rule, remoteApiBeanList);
    // #typescript suppress behavior
    //processBhv(rule, request, exBehaviorMap);
    processDoc(rule, request, exBehaviorMap);
}

/**
 * Keep information of bean.
 * @param {Rule} rule - rrule. (NotNull)
 * @param {string} beanPurposeType - The bean role type. e.g. param, return (NotNull)
 * @param {Api} api - The information of api. (NotNull)
 * @param {Properties} properties - The information of property for the bean. (NotNull)
 * @param {DefinitionMap} definitionMap - The information of definition map. (NotNull)
 * @param {string} definitionKey - definition key (NotNull)
 */
function createBean(rule, beanPurposeType, api, properties, definitionMap, definitionKey) {
    var schemaPackage = scriptEngine.invokeMethod(rule, 'schemaPackage', api.schema);
    var subPackage = scriptEngine.invokeMethod(rule, 'beanSubPackage', api);
    var package = api.package + '.' + schemaPackage;
    if (subPackage) {
        package = package + '.' + subPackage;
    }

    var remoteApiBean = new java.util.LinkedHashMap();
    remoteApiBean.api = api;
    remoteApiBean.package = package;
    remoteApiBean.className = scriptEngine.invokeMethod(rule, beanPurposeType + 'ClassName', api, false);
    remoteApiBean.definitionKey = definitionKey;
    remoteApiBean.extendsClass = scriptEngine.invokeMethod(rule, beanPurposeType + 'ExtendsClass', api, properties);
    remoteApiBean.implementsClasses = scriptEngine.invokeMethod(rule, beanPurposeType + 'ImplementsClasses', api, properties);
    remoteApiBean.properties = properties;
    remoteApiBean.beanPurposeType = beanPurposeType;
    remoteApiBean.remoteApiExp = api.httpMethod.toUpperCase() + ' ' + api.url;
    remoteApiBean.definitionMap = definitionMap;
    return remoteApiBean;
}

/**
 * Keep information of behavior.
 * @param {Rule} rule - rule. (NotNull)
 * @param {Api} api - The information of api. (NotNull)
 * @param {PathVariables} pathVariables - The array of path variables. (NotNull)
 * @param {ParamBean} paramBean - The information of param bean. (NotNull)
 * @param {boolean} paramBeanArray - true if param bean is array. (NotNull)
 * @param {ReturnBean} returnBean - The information of return bean. (NotNull)
 * @param {boolean} returnBeanArray - true if return bean is array. (NotNull)
 * @param {ExBehaviorMap} exBehaviorMap - The map of behavior information. (NotNull)
 */
function keepBehavior(rule, api, pathVariables, paramBean, paramBeanArray, returnBean, returnBeanArray, exBehaviorMap) {
    var schemaPackage = scriptEngine.invokeMethod(rule, 'schemaPackage', api.schema);
    var subPackage = scriptEngine.invokeMethod(rule, 'behaviorSubPackage', api);
    var package = api.package + '.' + schemaPackage;
    if (subPackage) {
        package = package + '.' + subPackage;
    }
    var className = scriptEngine.invokeMethod(rule, 'exBehaviorClassName', api);
    if (!exBehaviorMap[package + '.' + className]) {
        var exBehavior = new java.util.LinkedHashMap();
        exBehavior.package = package;
        exBehavior.className = className;
        exBehavior.remoteApiExp = subPackage;
        exBehaviorMap[package + '.' + className] = exBehavior;

        var bsBehavior = new java.util.LinkedHashMap();
        bsBehavior.package = package;
        bsBehavior.className = scriptEngine.invokeMethod(rule, 'bsBehaviorClassName', api);
        bsBehavior.remoteApiExp = subPackage;
        bsBehavior.methodList = [];
        exBehavior.bsBehavior = bsBehavior;
    }
    exBehaviorMap[package + '.' + className].bsBehavior.methodList.push({ 'api': api, 'pathVariables': pathVariables, 'paramBean': paramBean, 'paramBeanArray': paramBeanArray, 'returnBean': returnBean, 'returnBeanArray': returnBeanArray });
}

// #typescript changed extensions to '.ts'
/**
 * Process bean. (generating class)
 * @param {Rule} rule - rule. (NotNull)
 * @param {RemoteApiBeanList} remoteApiBeanList - remoteApiBeanList. (NotNull)
 */
function processBean(rule, remoteApiBeanList) {
    var sortRemoteApiBeanMap = new java.util.TreeMap();
    for (remoteApiBeanListIndex in remoteApiBeanList) {
        var remoteApiBean = remoteApiBeanList[remoteApiBeanListIndex];
        var path = remoteApiBean.package.replace(/\./g, '/') +'/' + remoteApiBean.className + '.ts';
        sortRemoteApiBeanMap[path + '@@@@@' + (remoteApiBean.api ? remoteApiBean.api.url : '')] = remoteApiBean;
    }

    var uniqueRemoteApiBeanMap = {};
    for (remoteApiBeanKey in sortRemoteApiBeanMap) {
        var remoteApiBean = sortRemoteApiBeanMap[remoteApiBeanKey];
        var path = remoteApiBean.package.replace(/\./g, '/') + '/' + remoteApiBean.className + '.ts';
        if (uniqueRemoteApiBeanMap[path] && uniqueRemoteApiBeanMap[path].properties != remoteApiBean.properties) {
            print('warning duplication! try change path');
            remoteApiBean.className = scriptEngine.invokeMethod(rule, remoteApiBean.beanPurposeType + 'ClassName', remoteApiBean.api, true);
            var path = remoteApiBean.package.replace(/\./g, '/') + '/' + remoteApiBean.className + '.ts';
        }
        if (uniqueRemoteApiBeanMap[path] && uniqueRemoteApiBeanMap[path].properties != remoteApiBean.properties) {
            print('warning duplication! skip path');
        } else {
            uniqueRemoteApiBeanMap[path] = remoteApiBean;
        }
    }

    for (remoteApiBeanKey in uniqueRemoteApiBeanMap) {
        var remoteApiBean = uniqueRemoteApiBeanMap[remoteApiBeanKey];
        var path = remoteApiBean.package.replace(/\./g, '/') + '/' + remoteApiBean.className + '.ts';
        generate('./remoteapi/RemoteApiBean.vm', path, remoteApiBean, true);
    }
}

/**
 * Process behavior. (generating class)
 * Also generate DI xml.
 * @param {Rule} rule - rule. (NotNull)
 * @param {Request} request - request (NotNull)
 * @param {ExBehaviorMap} exBehaviorMap - The map of behavior information. (NotNull)
 */
function processBhv(rule, request, exBehaviorMap) {
    if (!rule['behaviorClassGeneration']) {
        return [];
    }
    var schema = scriptEngine.invokeMethod(rule, 'schema', request);
    var schemaPackage = scriptEngine.invokeMethod(rule, 'schemaPackage', schema);
    var className = scriptEngine.invokeMethod(rule, 'abstractBehaviorClassName', schema);
    var abstractBehavior = new java.util.LinkedHashMap();
    abstractBehavior.package = request.package + '.' + schemaPackage;
    abstractBehavior.className = className;
    abstractBehavior.remoteApiExp = request.requestName;
    abstractBehavior.frameworkBehaviorClass = rule['frameworkBehaviorClass'];
    var path = abstractBehavior.package.replace(/\./g, '/') + '/' + abstractBehavior.className + '.ts';
    generate('./remoteapi/RemoteApiAbstractBehavior.vm', path, abstractBehavior, false);

    for (exBehaviorKey in exBehaviorMap) {
        var exBehavior = exBehaviorMap[exBehaviorKey];

        var bsBehavior = exBehavior.bsBehavior;
        bsBehavior.abstractBehavior = abstractBehavior;
        path = bsBehavior.package.replace(/\./g, '/') + '/' + bsBehavior.className + '.ts';
        generate('./remoteapi/RemoteApiBsBehavior.vm', path, bsBehavior, true);

        path = exBehavior.package.replace(/\./g, '/') + '/' + exBehavior.className + '.ts';
        generate('./remoteapi/RemoteApiExBehavior.vm', path, exBehavior, false);
    }

    var container = new java.util.LinkedHashMap();
    container.schemaPackage = schemaPackage;
    container.exBehaviorMap = exBehaviorMap;

    if (manager.isTargetContainerLastaDi()) {
        var path = scriptEngine.invokeMethod(rule, 'diXmlPath', schema, request.resourceFile);
        generate('./remoteapi/container/lastadi/RemoteApiDiXml.vm', path, container, true);
    }
    if (manager.isTargetContainerSeasar()) {
        var path = scriptEngine.invokeMethod(rule, 'diconPath', schema, request.resourceFile);
        generate('./remoteapi/container/seasar/RemoteApiDicon.vm', path, container, true);
    }
    if (manager.isTargetContainerSpring()) {
        var javaConfigClass = new java.util.LinkedHashMap();
        javaConfigClass.package = request.package + '.' + schemaPackage;
        javaConfigClass.className = scriptEngine.invokeMethod(rule, 'javaConfigClassName', schema);;
        var path = abstractBehavior.package.replace(/\./g, '/') + '/' + javaConfigClass.className + '.ts';
        generate('./remoteapi/container/spring/RemoteApiBeansJavaConfig.vm', path, javaConfigClass, true);

        path = 'org/lastaflute/spring/LastafluteBeansJavaConfig.java';
        generate('./remoteapi/container/spring/LastafluteBeansJavaConfig.vm', path, javaConfigClass, true);
    }
}

/**
 * Process doc.
 * @param {Rule} rule - rule. (NotNull)
 * @param {Request} request - request (NotNull)
 * @param {ExBehaviorMap} exBehaviorMap - The map of behavior information. (NotNull)
 */
function processDoc(rule, request, exBehaviorMap) {
    if (!rule['docGeneration']) {
        return;
    }
    var doc = new java.util.LinkedHashMap();
    doc.schema = scriptEngine.invokeMethod(rule, 'schema', request);
    doc.schemaPackage = scriptEngine.invokeMethod(rule, 'schemaPackage', doc.schema);
    doc.exBehaviorMap = exBehaviorMap;
    var docHtml = generate('./' + genType + '/doc/RemoteApiDocHtml.vm', null, doc, true);
    var lastaDocHtmlPathList = manager.getLastaDocHtmlPathList();
    var markNaviLink = manager.getLastaDocHtmlMarkFreeGenDocNaviLink();
    var markBody = manager.getLastaDocHtmlMarkFreeGenDocBody();
    var naviLinkHtml = '    | <a href="#' + genType + '">to ' + genType + '</a>';
    var naviLinkDestinationHtml = '<span id="' + genType + '"></span>';
    for (var lastaDocHtmlPathIndex in lastaDocHtmlPathList) {
        var lastaDocHtmlPath = java.nio.file.Paths.get(lastaDocHtmlPathList[lastaDocHtmlPathIndex]);
        var lastaDocHtml = Java.type('java.lang.String').join('\n', java.nio.file.Files.readAllLines(lastaDocHtmlPath));
        if (!lastaDocHtml.contains(naviLinkHtml)) {
            lastaDocHtml = lastaDocHtml.replace(markNaviLink, naviLinkHtml + '\n' + markNaviLink);
        }
        if (!lastaDocHtml.contains(naviLinkDestinationHtml)) {
            docHtml = naviLinkDestinationHtml + docHtml;
        }
        java.nio.file.Files.write(lastaDocHtmlPath, lastaDocHtml.replace(markBody, docHtml + '\n' + markBody).getBytes('UTF-8'));
    }
}

// ===================================================================================
//                                                                              Common
//                                                                              ======
/**
 * generate file.
 * @param {string} src - src (NotNull)
 * @param {string} dest - dest (NotNull)
 * @param {map} data - data (NotNull)
 * @param {boolean} overwite - overwite (NotNull)
 */
function generate(src, dest, data, overwite) {
    if (dest === null) {
        return generator.parse(src, dest, 'data', data);
    }
    srcPathList.push(dest);
    if (!java.nio.file.Files.exists(java.nio.file.Paths.get(generator.outputPath, dest)) || overwite) {
        manager.makeDirectory(dest);
        print('generate("' + dest + '")');
        return generator.parse(src, dest, 'data', data);
    }
    return '';
}

function clean(rule, request, genDir, srcPathList) {
    var generateAbsolutePathList = [];
    for (var srcPathIndex in srcPathList) {
        generateAbsolutePathList.push(new java.io.File(generator.outputPath, srcPathList[srcPathIndex]).getAbsolutePath());
    }
    var list = listFiles(new java.io.File(generator.outputPath, genDir));
    for (var index in list) {
        var file = list[index];
        if (generateAbsolutePathList.indexOf(file.getAbsolutePath()) === -1
                && (rule == null || scriptEngine.invokeMethod(rule, 'deleteTarget', request, file))) {
            print('delete(' + file + ')');
            file.delete();
        }
    }
}

function listFiles(dir) {
    var list = [];
    var fileList = dir.listFiles();
    for (var fileIndex in fileList) {
        var file = fileList[fileIndex];
        if (file.isDirectory()) {
            list = list.concat(this.listFiles(file));
        } else if (file.isFile()) {
            list.push(file);
        }
    }
    return list;
}
