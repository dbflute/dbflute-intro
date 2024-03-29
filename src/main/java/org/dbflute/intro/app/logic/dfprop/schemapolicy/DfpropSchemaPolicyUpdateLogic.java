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

import static org.dbflute.intro.mylasta.appcls.AppCDef.SubjectableMapType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.schemapolicy.file.DfpropSchemaPolicyFileReplaceLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyStatement;
import org.dbflute.intro.bizfw.tellfailure.SchemaPolicyStatementOutOfIndexException;
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
        addStatementToMap(statement.mapTypeAsCls(), builtStatement, schemaPolicyMap);

        // こちらは自前マージしているようなものなので、Directlyの方を使う
        dfpropSchemaPolicyFileReplaceLogic.replaceSchemaPolicyMapDirectly(schemaPolicyFile, schemaPolicyMap);
        return builtStatement;
    }

    private void addStatementToMap(SubjectableMapType mapType, String addedStatement, SchemaPolicyMap schemaPolicyMap) {
        if (SubjectableMapType.Table.equals(mapType)) {
            List<String> statements = new ArrayList<>(schemaPolicyMap.tableMap.statementList);
            statements.add(addedStatement);
            schemaPolicyMap.tableMap.statementList = statements;
        } else if (SubjectableMapType.Column.equals(mapType)) {
            List<String> statements = new ArrayList<>(schemaPolicyMap.columnMap.statementList);
            statements.add(addedStatement);
            schemaPolicyMap.columnMap.statementList = statements;
        }
    }

    // -----------------------------------------------------
    //                                      Delete Statement
    //                                      ----------------
    public void deleteSchemaPolicyStatement(String projectName, SubjectableMapType mapType, String statement) {
        File schemaPolicyFile = dfpropSchemaPolicyReadLogic.findSchemaPolicyFile(projectName);

        SchemaPolicyMap schemaPolicyMap = dfpropSchemaPolicyReadLogic.findSchemaPolicyMap(projectName);
        removeStatementFromMap(mapType, statement, schemaPolicyMap);

        // Registerと同様にDirectlyの方を使う
        dfpropSchemaPolicyFileReplaceLogic.replaceSchemaPolicyMapDirectly(schemaPolicyFile, schemaPolicyMap);
    }

    private void removeStatementFromMap(SubjectableMapType mapType, String removedStatement, SchemaPolicyMap schemaPolicyMap) {
        if (SubjectableMapType.Table.equals(mapType)) {
            schemaPolicyMap.tableMap.statementList =
                    schemaPolicyMap.tableMap.statementList.stream().filter(st -> !st.equals(removedStatement)).collect(Collectors.toList());
        } else if (SubjectableMapType.Column.equals(mapType)) {
            schemaPolicyMap.columnMap.statementList = schemaPolicyMap.columnMap.statementList.stream()
                    .filter(st -> !st.equals(removedStatement))
                    .collect(Collectors.toList());
        }
    }

    // ===================================================================================
    //                                                                      Sort Statement
    //                                                                      ==============
    public void moveSchemaPolicyStatement(String projectName, SubjectableMapType mapType, Integer fromIndex, Integer toIndex) {
        SchemaPolicyMap schemaPolicyMap = dfpropSchemaPolicyReadLogic.findSchemaPolicyMap(projectName);
        if (SubjectableMapType.Table.equals(mapType)) {
            schemaPolicyMap.tableMap.statementList = moveStatements(schemaPolicyMap.tableMap.statementList, fromIndex, toIndex);
        } else if (SubjectableMapType.Column.equals(mapType)) {
            schemaPolicyMap.columnMap.statementList = moveStatements(schemaPolicyMap.columnMap.statementList, fromIndex, toIndex);
        }
        File schemaPolicyMapFile = dfpropSchemaPolicyReadLogic.findSchemaPolicyFile(projectName);
        dfpropSchemaPolicyFileReplaceLogic.replaceSchemaPolicyMapDirectly(schemaPolicyMapFile, schemaPolicyMap);
    }

    private List<String> moveStatements(List<String> baseStatements, Integer fromIndex, Integer toIndex) {
        if (baseStatements.size() < fromIndex || baseStatements.size() < toIndex) {
            throw new SchemaPolicyStatementOutOfIndexException("The index of the statement is out of range",
                    "statement size is " + baseStatements.size() + ", but fromIndex=" + fromIndex + ", toIndex=" + toIndex);
        }
        return DfCollectionUtil.moveElementToIndex(baseStatements, fromIndex, toIndex);
    }
}
