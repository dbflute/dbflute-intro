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
package org.dbflute.intro.app.logic.document;

import java.util.List;

/**
 * @author cabos
 */
public class CheckedZipBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String fileName;
    private List<AlterSqlBean> checkedSqlList;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public CheckedZipBean(String fileName, List<AlterSqlBean> checkedSqlList) {
        this.fileName = fileName;
        this.checkedSqlList = checkedSqlList;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getFileName() {
        return fileName;
    }

    public List<AlterSqlBean> getCheckedSqlList() {
        return checkedSqlList;
    }
}
