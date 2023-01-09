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
// _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
// o 先頭の "Remote" は除去する (無くても良いかなと、シンプルな方を優先)
// o JavaScriptとJavaで直感的に行き来できるように、Java側のクラス名に合わせる
// _/_/_/_/_/_/_/_/_/_/
// @Override
remoteApiRule.paramClassName = function(api, detail) {
    var baseName = this.beanClassName(api, detail).replace('Remote', ''); // 固定prefixなし
    return baseName + 'Body'; // Paramじゃなくて
}

// @Override
remoteApiRule.returnClassName = function(api, detail) {
    var baseName = this.beanClassName(api, detail).replace('Remote', ''); // 固定prefixなし
    return baseName + 'Result'; // Paramじゃなくて
}



// =======================================================================================
//                                                                                  Option
//                                                                                  ======
// すべてのマッピングをTypeScriptの型に合わせる
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

// 区分値クラスも自動生成するようにしたら、ここで関連付けをしていく
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
