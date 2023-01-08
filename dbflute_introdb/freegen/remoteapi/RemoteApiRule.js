// Based on ECMAScript5. Because Nashorn of java 8 is ECMAScript5.
// =======================================================================================
//                                                                              Definition
//                                                                              ==========
/**
 * Request Type.
 * @typedef {Object} Request
 */

/**
 * API Type.
 * @typedef {Object} Api
 * @property {string} api.schema - Schema.
 * @property {string} api.url - URL.
 * @property {string} api.httpMethod - HttpMethod.
 * @property {string[]} api.consumes - Consumes.
 * @property {string[]} api.produces - Produces.
 */

/**
 * PathVariable Type.
 * @typedef {Object} PathVariable
 * @property {string} pathVariable.name - Name.
 * @property {string} pathVariable.in - In.
 * @property {string} pathVariable.required - Required.(optional)
 * @property {string} pathVariable.description - Description.(optional)
 * @property {string} pathVariable.type - Type.(optional)
 * @property {string} pathVariable.format - Format.(optional)
 * @property {string} pathVariable.default - Default.(optional)
 * @property {Items} pathVariable.items - Items.(optional)
 * @property {Schema} pathVariable.schema - Schema.(optional)
 * @property {string} pathVariable.enum - Enum.(optional)
 */

/**
 * Property Type.
 * @typedef {Object} Property
 * @property {string} property.name - Name.
 * @property {string} property.required - Required.(optional)
 * @property {string} property.description - Description.(optional)
 * @property {string} property.type - Type.(optional)
 * @property {string} property.format - Format.(optional)
 * @property {string} property.default - Default.(optional)
 * @property {Items} property.items - Items.(optional)
 * @property {Schema} property.schema - Schema.(optional)
 * @property {string} property.enum - Enum.(optional)
 */

var baseRule = {

    // ===================================================================================
    //                                                                               Const
    //                                                                               =====
    /** field naming. */
    FIELD_NAMING: {
        CAMEL_TO_LOWER_SNAKE: 'CAMEL_TO_LOWER_SNAKE'
    },

    // ===================================================================================
    //                                                                               Base
    //                                                                              ======
    /**
     * Return schema.
     * @param {Request} request - Request. (NotNull)
     * @return {string} schema. (NotNull)
     */
    schema: function(request) {
        return request.requestName.replace(/^RemoteApi/g, '');
    },

    /**
     * Return schema package.
     * @param {Api} api - API. (NotNull)
     * @return {string} schema package. (NotNull)
     */
    schemaPackage: function(schema) {
        return manager.decamelize(schema).replace(/_/g, '.').toLowerCase();
    },

    /**
     * Return true if target.
     * @param {Api} api - API. (NotNull)
     * @return {boolean} true if target. (NotNull)
     */
    target: function(api) {
        // p1us2er0 fixedly all target (2019/08/31)
        // previously, it was TARGET when the request body or response body was json or xml content.
        return true;
    },

    /**
     * Return filtered URL.
     * @param {Api} api - API. (NotNull)
     * @return {boolean} filtered URL. (NotNull)
     */
    url: function(api) { return api.url; },

    /**
     * Return subPackage.
     * @param {Api} api - API. (NotNull)
     * @return {string} subPackage. (NotNull)
     */
    subPackage: function(api) {
        return api.url.replace(/(_|-|^\/|\/$)/g, '').replace(/\/\{.*?\}/g, '').replace(/\..+$/g, '').replace(/\//g, '.').toLowerCase();
    },

    // ===================================================================================
    //                                                                               DiXml
    //                                                                               =====
    /**
     * Return di xml path for target container lasta di.
     * @param {string} schema - schema. (NotNull)
     * @return {string} di xml path for target container lasta di. (NotNull)
     */
    diXmlPath: function(schema, resourceFilePath) {
        return '../resources/remoteapi/di/remoteapi_' + this.schemaPackage(schema).replace(/\./g, '-') + '.xml';
    },

    /**
     * Return dicon path for target container seasar.
     * @param {string} schema - schema. (NotNull)
     * @return {string} dicon path for target container seasar. (NotNull)
     */
    diconPath: function(schema, resourceFilePath) {
        return '../resources/remoteapi/di/remoteapi_' + this.schemaPackage(schema).replace(/\./g, '-') + '.dicon';
    },

    /**
     * Return java config class name for target container spring.
     * @param {string} schema - schema. (NotNull)
     * @return {string} java config class name for target container spring. (NotNull)
     */
    javaConfigClassName: function(schema) {
        return 'Remote' + schema + 'BeansJavaConfig';
    },

    // ===================================================================================
    //                                                                            Behavior
    //                                                                            ========
    /** true for automatically generating behavior classes. */
    behaviorClassGeneration: true,
    /** true for automatically generating behavior methods. */
    behaviorMethodGeneration: true,
    /** behavior method access modifier. */
    behaviorMethodAccessModifier: 'public',
    /** framework behavior class. */
    frameworkBehaviorClass: 'org.lastaflute.remoteapi.LastaRemoteBehavior',

    /**
     * Return abstract behavior class name.
     * @param {string} schema - schema. (NotNull)
     * @return {string} abstract behavior class name. (NotNull)
     */
    abstractBehaviorClassName: function(schema) {
        return 'AbstractRemote' + schema + 'Bhv';
    },

    /**
     * Return filtered behavior subPackage.
     * @param {Api} api - API. (NotNull)
     * @return {string} filtered behavior subPackage. (NotNull)
     */
    behaviorSubPackage: function(api) {
        return this.subPackage(api).replace(/^([^.]*)\.(.+)/, '$1');
    },

    /**
     * Return bsBehavior class name.
     * @param {Api} api - API. (NotNull)
     * @return {string} bsBehavior class name. (NotNull)
     */
    bsBehaviorClassName: function(api) {
        return 'BsRemote' + api.schema + manager.initCap(manager.camelize(this.behaviorSubPackage(api).replace(/\./g, '_'))) + 'Bhv';
    },

    /**
     * Return exBehavior class name.
     * @param {Api} api - API. (NotNull)
     * @return {string} exBehavior class name. (NotNull)
     */
    exBehaviorClassName: function(api) {
        return 'Remote' + api.schema + manager.initCap(manager.camelize(this.behaviorSubPackage(api).replace(/\./g, '_'))) + 'Bhv';
    },

    /**
     * Return behavior request method name.
     * @param {Api} api - API. (NotNull)
     * @return {string} behavior request method name. (NotNull)
     */
    behaviorRequestMethodName: function(api) {
        var methodPart = manager.camelize(this.subPackage(api).replace(this.behaviorSubPackage(api), '').replace(/\./g, '_'));
        return 'request' + manager.initCap(methodPart) + (api.multipleHttpMethod ? manager.initCap(api.httpMethod): '');
    },

    /**
     * Return behavior rule method name.
     * @param {Api} api - API. (NotNull)
     * @return {string} behavior rule method name. (NotNull)
     */
    behaviorRuleMethodName: function(api) {
        var methodPart = manager.camelize(this.subPackage(api).replace(this.behaviorSubPackage(api), '').replace(/\./g, '_'));
        return 'ruleOf' + manager.initCap(methodPart) + (api.multipleHttpMethod ? manager.initCap(api.httpMethod): '');
    },

    // ===================================================================================
    //                                                                        Param/Return
    //                                                                        ============
    /**
     * Return filtered bean subPackage.
     * @param {Api} api - API. (NotNull)
     * @return {string} filtered bean subPackage. (NotNull)
     */
    beanSubPackage: function(api) {
        var package = this.subPackage(api);
        if (package === this.behaviorSubPackage(api)) {
            package += '.index';
        }
        return package;
    },

    /**
     * Return filterd definition key.
     * e.g. Filter common header pattern class.
     * @param {string} definitionKey - definition key. (NotNull)
     * @return {string} filterd definition key. (NotNull)
     */
    definitionKey: function(definitionKey) { return definitionKey; },

    /**
     * Returns definition key before filtering from the filtered definition key.
     * e.g. Restore the class of the filtered common header pattern.
     * @param {string} definitionKey - filterd definition key. (NotNull)
     * @return {string} definition key before filtering from the filtered definition key. (NotNull)
     */
    unDefinitionKey: function(definitionKey) { return definitionKey; },

    beanExtendsDefinitionGeneration: false,

    /**
     * Return filtered bean definition subPackage.
     * @param {Request} request - Request. (NotNull)
     * @param {string} definitionKey - definition key. (NotNull)
     * @return {string} filtered bean subPackage. (NotNull)
     */
    beanExtendsDefinitionSubPackage: function(request, definitionKey) {
        return 'definition';
    },

    /**
     * Return bean definition class name.
     * @param {Request} request - Request. (NotNull)
     * @param {string} definitionKey - definition key. (NotNull)
     * @return {string} bean definition class name. (NotNull)
     */
    beanExtendsDefinitionClassName: function(request, definitionKey) {
        return definitionKey.replace(/.*\./g, '').replace(/(<|>)/g, '') + 'Definition';
    },

    /**
     * Return bean class name.
     * @param {Api} api - API. (NotNull)
     * @param {boolean} detail - detail. (NotNull)
     * @return {string} bean class name. (NotNull)
     */
    beanClassName: function(api, detail) {
        var namePart = detail ? api.url.replace(/(_|-|^\/|\/$|\{|\})/g, '').replace(/\//g, '_').toLowerCase(): this.subPackage(api);
        return 'Remote' + manager.initCap(manager.camelize(namePart.replace(/\./g, '_'))) + (api.multipleHttpMethod ? manager.initCap(api.httpMethod): '');
    },

    /**
     * Return param extends class.
     * @param {Api} api - API. (NotNull)
     * @param {Object} properties - properties. (NotNull)
     * @return {string} param extends class. (NullAllowed)
     */
    paramExtendsClass: function(api, properties) {
        return null;
    },

    /**
     * Return param implements classes.
     * @param {Api} api - API. (NotNull)
     * @param {Object} properties - properties. (NotNull)
     * @return {string} param implements classes. (NullAllowed)
     */
    paramImplementsClasses: function(api, properties) {
        return null;
    },

    /**
     * Return param class name.
     * @param {Api} api - API. (NotNull)
     * @param {boolean} detail - detail. (NotNull)
     * @return {string} param class name. (NotNull)
     */
    paramClassName: function(api, detail) {
        return this.beanClassName(api, detail) + 'Param';
    },

    /**
     * Return return extends class.
     * @param {Api} api - API. (NotNull)
     * @param {Object} properties - properties. (NotNull)
     * @return {string} return extends class. (NullAllowed)
     */
    returnExtendsClass: function(api, properties) {
        return null;
    },

    /**
     * Return return implements classes.
     * @param {Api} api - API. (NotNull)
     * @param {Object} properties - properties. (NotNull)
     * @return {string} return implements classes. (NullAllowed)
     */
    returnImplementsClasses: function(api, properties) {
        return null;
    },

    /**
     * Return returnClassName.
     * @param {Api} api - API. (NotNull)
     * @param {boolean} detail - detail. (NotNull)
     * @return {string} returnClassName. (NotNull)
     */
    returnClassName: function(api, detail) {
        return this.beanClassName(api, detail) + 'Return';
    },

    /**
     * Return nest class name.
     * @param {Api} api - API. (NotNull)
     * @param {string} className - class name. (NotNull)
     * @return {string} nest class name. (NotNull)
     */
    nestClassName: function(api, className) {
        return className.replace(/(Part|Result|Model|Bean)$/, '') + 'Part';
    },

    /**
     * Return field name.
     * @param {Api} api - API. (NotNull)
     * @param {string} fieldName - field name. (NotNull)
     * @return {string} field name. (NotNull)
     */
    fieldName: function(api, bean, fieldName) {
        var fieldNaming = this.fieldNamingMapping()[bean.in];
        if (fieldNaming === this.FIELD_NAMING.CAMEL_TO_LOWER_SNAKE) {
            return manager.initUncap(manager.camelize(fieldName));
        }
        return fieldName;
    },

    // ===================================================================================
    //                                                                                 Doc
    //                                                                                 ===
    /** true for automatically generating doc. */
    docGeneration: true,

    // ===================================================================================
    //                                                                              Option
    //                                                                              ======
    /**
     * Return java import order list.
     * @return java import order list. (NotNull)
     */
    importOrderList: function() {
        return ['java', 'javax', 'junit', 'org', 'com', 'net', 'ognl', 'mockit', 'jp'];
    },

    /**
     * Return field naming mapping.
     * @return field naming mapping. (NotNull)
     */
    fieldNamingMapping: function() {
        return {
            'path': this.FIELD_NAMING.CAMEL_TO_LOWER_SNAKE,
            'query': this.FIELD_NAMING.CAMEL_TO_LOWER_SNAKE,
            'formData': this.FIELD_NAMING.CAMEL_TO_LOWER_SNAKE,
            'json': this.FIELD_NAMING.CAMEL_TO_LOWER_SNAKE,
            'xml': this.FIELD_NAMING.CAMEL_TO_LOWER_SNAKE
        };
    },

    /**
     * Return java property type mapping.
     * e.g. java.util.List -> org.eclipse.collections.api.list.ImmutableList, java.time.LocalDate -> String etc.
     * @return typeMap The map of type conversion, swagger type to java type. (NotNull)
     */
    typeMap: function() {
        return {
            'object': 'java.util.Map<String, Object>',
            'int32': 'Integer',
            'int64': 'Long',
            'float': 'Float',
            'double': 'Double',
            'string': 'String',
            'byte': 'byte[]',
            'binary': 'org.lastaflute.web.ruts.multipart.MultipartFormFile',
            'file': 'org.lastaflute.web.ruts.multipart.MultipartFormFile',
            'boolean': 'Boolean',
            'date': 'java.time.LocalDate',
            'date-time': 'java.time.LocalDateTime',
            'array': 'java.util.List',
            '': 'String'
        };
    },

    /**
     * Return path variable manual mapping class.
     * @param {Api} api - API. (NotNull)
     * @param {PathVariable} pathVariable - path variable. (NotNull)
     * @return {string} path variable manual mapping class. (NullAllowed)
     */
    pathVariableManualMappingClass: function(api, pathVariable) {
        return null;
    },

    /**
     * Return bean property manual mapping class.
     * @param {Api} api - API. (NotNull)
     * @param {string} beanClassName - bean class name. (NotNull)
     * @param {Property} property - property. (NotNull)
     * @return {string} bean property manual mapping class. (NullAllowed)
     */
    beanPropertyManualMappingClass: function(api, beanClassName, property) {
        return null;
    },

    /**
     * Return path variable manual mapping description.
     * @param {Api} api - API. (NotNull)
     * @param {PathVariable} pathVariable - path variable. (NotNull)
     * @return {string} path variable manual mapping description. (NullAllowed)
     */
    pathVariableManualMappingDescription: function(api, pathVariable) {
        return null;
    },

    /**
     * Return bean property manual mapping description.
     * @param {Api} api - API. (NotNull)
     * @param {string} beanClassName - bean class name. (NotNull)
     * @param {Property} property - property. (NotNull)
     * @return {string} bean property manual mapping description. (NullAllowed)
     */
    beanPropertyManualMappingDescription: function(api, beanClassName, property) {
        return null;
    },

    /**
     * Return delete target.
     * @param {Request} request - rquest. (NotNull)
     * @return {File} file. (NotNull)
     * @return {boolean} delete target. (NotNull)
     */
    deleteTarget: function(request, file) {
        try {
            return new java.lang.String(java.nio.file.Files.readAllBytes(file.toPath()), 'UTF-8').contains(' @author FreeGen');
        } catch (e) {
            return false;
        }
    }
};

var remoteApiRule = {};
for (var i in baseRule) {
    remoteApiRule[i] = baseRule[i];
}
