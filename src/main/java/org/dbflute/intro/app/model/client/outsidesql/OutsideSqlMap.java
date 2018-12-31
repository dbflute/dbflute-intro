/*
 * Copyright 2014-2019 the original author or authors.
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
package org.dbflute.intro.app.model.client.outsidesql;

/**
 * @author p1us2er0
 * @author jflute
 */
public class OutsideSqlMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private boolean generateProcedureParameterBean;
    private String procedureSynonymHandlingType; // #later should be CDef by jflute

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
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
