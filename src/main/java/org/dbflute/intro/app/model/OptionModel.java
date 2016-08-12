/*
 * Copyright 2014-2016 the original author or authors.
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
package org.dbflute.intro.app.model;

/**
 * @author p1us2er0
 * @author jflute
 */
public class OptionModel {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                           documentMap
    //                                           -----------
    private boolean dbCommentOnAliasBasis;
    private String aliasDelimiterInDbComment;
    private boolean checkColumnDefOrderDiff;
    private boolean checkDbCommentDiff;
    private boolean checkProcedureDiff;

    // -----------------------------------------------------
    //                                         outsideSqlMap
    //                                         -------------
    private boolean generateProcedureParameterBean;
    private String procedureSynonymHandlingType;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    // -----------------------------------------------------
    //                                           documentMap
    //                                           -----------
    public boolean isDbCommentOnAliasBasis() {
        return dbCommentOnAliasBasis;
    }

    public void setDbCommentOnAliasBasis(boolean isDbCommentOnAliasBasis) {
        this.dbCommentOnAliasBasis = isDbCommentOnAliasBasis;
    }

    public String getAliasDelimiterInDbComment() {
        return aliasDelimiterInDbComment;
    }

    public void setAliasDelimiterInDbComment(String aliasDelimiterInDbComment) {
        this.aliasDelimiterInDbComment = aliasDelimiterInDbComment;
    }

    public boolean isCheckColumnDefOrderDiff() {
        return checkColumnDefOrderDiff;
    }

    public void setCheckColumnDefOrderDiff(boolean isCheckColumnDefOrderDiff) {
        this.checkColumnDefOrderDiff = isCheckColumnDefOrderDiff;
    }

    public boolean isCheckDbCommentDiff() {
        return checkDbCommentDiff;
    }

    public void setCheckDbCommentDiff(boolean isCheckDbCommentDiff) {
        this.checkDbCommentDiff = isCheckDbCommentDiff;
    }

    public boolean isCheckProcedureDiff() {
        return checkProcedureDiff;
    }

    public void setCheckProcedureDiff(boolean isCheckProcedureDiff) {
        this.checkProcedureDiff = isCheckProcedureDiff;
    }

    // -----------------------------------------------------
    //                                         outsideSqlMap
    //                                         -------------
    public boolean isGenerateProcedureParameterBean() {
        return generateProcedureParameterBean;
    }

    public void setGenerateProcedureParameterBean(boolean isGenerateProcedureParameterBean) {
        this.generateProcedureParameterBean = isGenerateProcedureParameterBean;
    }

    public String getProcedureSynonymHandlingType() {
        return procedureSynonymHandlingType;
    }

    public void setProcedureSynonymHandlingType(String procedureSynonymHandlingType) {
        this.procedureSynonymHandlingType = procedureSynonymHandlingType;
    }
}
