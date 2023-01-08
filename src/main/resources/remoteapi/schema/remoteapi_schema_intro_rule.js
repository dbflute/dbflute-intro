// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// RemoteApiGen your rule settings as ECMAScript5 (related to RemoteApiRule.js in freegen)
// _/_/_/_/_/_/_/_/_/_/

// ======================================================================================
//                                                                                  Base
//                                                                                 ======
// @Override
remoteApiRule.target = function(api) { // you can select generated API 
    return true;
}


// ======================================================================================
//                                                                               Behavior
//                                                                               ========



// =======================================================================================
//                                                                            Param/Return
//                                                                            ============



// =======================================================================================
//                                                                                  Option
//                                                                                  ======
// @Override
remoteApiRule.typeMap = function() {
    var typeMap = baseRule.typeMap();
    typeMap['object'] = 'any';
    typeMap['int32'] = 'number';
    typeMap['int64'] = 'number';
    typeMap['float'] = 'number';
    typeMap['double'] = 'number';
    typeMap['string'] = 'string';
    typeMap['byte'] = 'any';
    typeMap['binary'] = 'any';
    typeMap['file'] = 'any';
    typeMap['boolean'] = 'boolean';
    typeMap['date'] = 'string';
    typeMap['date-time'] = 'string';
    typeMap['array'] = 'Array';
    typeMap[''] = 'string';
    return typeMap;
}

// name and type mapping for e.g. classification
var manualMappingClassMap = {
};

// @Override
remoteApiRule.pathVariableManualMappingClass = function(api, pathVariable) {
    return manualMappingClassMap[pathVariable.name];
}

// @Override
remoteApiRule.beanPropertyManualMappingClass = function(api, beanClassName, property) {
    return manualMappingClassMap[property.name];
}
