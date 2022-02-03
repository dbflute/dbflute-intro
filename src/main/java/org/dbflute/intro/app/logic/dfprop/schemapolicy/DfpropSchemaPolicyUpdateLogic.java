/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.app.logic.dfprop.schemapolicy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.schemapolicy.file.DfpropSchemaPolicyFileReplaceLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyStatement;
import org.dbflute.util.DfCollectionUtil;

/**
 * The logic for updating SchemaPolicy dfprop.
 * @author deco
 * @author subaru
 * @author hakiba
 * @author cabos
 * @author jflute
 * @since 0.5.0 split from DfpropInfoLogic (2021/06/24 Thursday at roppongi japanese)
 */
public class DfpropSchemaPolicyUpdateLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropSchemaPolicyReadLogic dfpropSchemaPolicyReadLogic;
    @Resource
    private DfpropSchemaPolicyFileReplaceLogic dfpropSchemaPolicyFileReplaceLogic;

    // ===================================================================================
    //                                                                          Update Map
    //                                                                          ==========
    public void updateSchemaPolicyMap(String projectName, SchemaPolicyMap inputSchemaPolicyMap) {
        File schemaPolicyFile = dfpropSchemaPolicyReadLogic.findSchemaPolicyFile(projectName);
        doUpdateSchemaPolicyMap(schemaPolicyFile, inputSchemaPolicyMap);
    }

    // UnitTestのエントリポイントになっているので、dfpropファイルを受け取って汎用的にしている
    protected void doUpdateSchemaPolicyMap(File dfpropFile, SchemaPolicyMap input) {
        dfpropSchemaPolicyFileReplaceLogic.replaceSchemaPolicyMapWithInput(dfpropFile, input);
    }

    // ===================================================================================
    //                                                                           Statement
    //                                                                           =========
    // -----------------------------------------------------
    //                                    Register Statement
    //                                    ------------------
    public String registerSchemaPolicyStatement(String projectName, SchemaPolicyStatement statement) {
        File schemaPolicyFile = dfpropSchemaPolicyReadLogic.findSchemaPolicyFile(projectName);

        SchemaPolicyMap schemaPolicyMap = dfpropSchemaPolicyReadLogic.findSchemaPolicyMap(projectName);
        String builtStatement = statement.buildStatement();
        addStatementToMap(statement.mapType, builtStatement, schemaPolicyMap);

        // こちらは自前マージしているようなものなので、Directlyの方を使う
        dfpropSchemaPolicyFileReplaceLogic.replaceSchemaPolicyMapDirectly(schemaPolicyFile, schemaPolicyMap);
        return builtStatement;
    }

    private void addStatementToMap(String mapType, String addedStatement, SchemaPolicyMap schemaPolicyMap) {
        if ("tableMap".equals(mapType)) {
            List<String> statements = new ArrayList<>(schemaPolicyMap.tableMap.statementList);
            statements.add(addedStatement);
            schemaPolicyMap.tableMap.statementList = statements;
        } else if ("columnMap".equals(mapType)) {
            List<String> statements = new ArrayList<>(schemaPolicyMap.columnMap.statementList);
            statements.add(addedStatement);
            schemaPolicyMap.columnMap.statementList = statements;
        }
    }

    // -----------------------------------------------------
    //                                      Delete Statement
    //                                      ----------------
    public void deleteSchemaPolicyStatement(String projectName, String mapType, String statement) {
        File schemaPolicyFile = dfpropSchemaPolicyReadLogic.findSchemaPolicyFile(projectName);

        SchemaPolicyMap schemaPolicyMap = dfpropSchemaPolicyReadLogic.findSchemaPolicyMap(projectName);
        removeStatementFromMap(mapType, statement, schemaPolicyMap);

        // Registerと同様にDirectlyの方を使う
        dfpropSchemaPolicyFileReplaceLogic.replaceSchemaPolicyMapDirectly(schemaPolicyFile, schemaPolicyMap);
    }

    private void removeStatementFromMap(String mapType, String removedStatement, SchemaPolicyMap schemaPolicyMap) {
        if ("tableMap".equals(mapType)) {
            schemaPolicyMap.tableMap.statementList =
                    schemaPolicyMap.tableMap.statementList.stream().filter(st -> !st.equals(removedStatement)).collect(Collectors.toList());
        } else if ("columnMap".equals(mapType)) {
            schemaPolicyMap.columnMap.statementList = schemaPolicyMap.columnMap.statementList.stream()
                    .filter(st -> !st.equals(removedStatement))
                    .collect(Collectors.toList());
        }
    }

    // ===================================================================================
    //                                                                      Sort Statement
    //                                                                      ==============
    public void moveSchemaPolicyStatement(String projectName, String mapType, Integer fromIndex, Integer toIndex) {
        SchemaPolicyMap schemaPolicyMap = dfpropSchemaPolicyReadLogic.findSchemaPolicyMap(projectName);
        if ("tableMap".equals(mapType)) {
            schemaPolicyMap.tableMap.statementList = moveStatements(schemaPolicyMap.tableMap.statementList, fromIndex, toIndex);
        } else if ("columnMap".equals(mapType)) {
            schemaPolicyMap.columnMap.statementList =
                    moveStatements(schemaPolicyMap.columnMap.statementList, fromIndex, toIndex);
        }
        File schemaPolicyMapFile = dfpropSchemaPolicyReadLogic.findSchemaPolicyFile(projectName);
        dfpropSchemaPolicyFileReplaceLogic.replaceSchemaPolicyMapDirectly(schemaPolicyMapFile, schemaPolicyMap);
    }

    private List<String> moveStatements(List<String> baseStatements, Integer fromIndex, Integer toIndex) {
        // 並び替え元、先のindexが一致 または 並び替え元 or 先のいずれかのindexが存在しない場合は何もしないで終了
        if (Objects.equals(fromIndex, toIndex)
                || baseStatements.size() < fromIndex
                || baseStatements.size() < toIndex) {
            return baseStatements;
        }
        return DfCollectionUtil.moveElementToIndex(baseStatements, fromIndex, toIndex);
    }
}
