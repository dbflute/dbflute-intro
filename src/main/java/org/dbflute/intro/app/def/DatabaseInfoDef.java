/*
 * Copyright 2014-2015 the original author or authors.
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
package org.dbflute.intro.app.def;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jflute
 * @author p1us2er0
 */
public enum DatabaseInfoDef {

    /** mysql. */
    MySQL("mysql", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/xxx", "", false, false, false, false),
    /** oracle. */
    Oracle("oracle", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@localhost:1521:xxx", "xxx", true, true, false, true),
    /** db2. */
    DB2("db2", "com.ibm.db2.jcc.DB2Driver", "jdbc:db2://localhost:50000/xxx", "xxx", true, true, false, false),
    /** mssql. */
    SQLServer("mssql", "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "jdbc:sqlserver://localhost:1433;DatabaseName=xxx;", "dbo", true, true, false, false),
    /** postgresql. */
    PostgreSQL("postgresql", "org.postgresql.Driver", "jdbc:postgresql://localhost:5432/xxx", "public", true, false, false, false),
    /** h2. */
    H2("h2", "org.h2.Driver", "jdbc:h2:file:xxx", "PUBLIC", true, false, false, false),
    /** derby. */
    Derby("derby", "org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:xxx;create=true", "xxx", true, true, false, false);

    // ===================================================================================
    //                                                                 Definition Accessor
    //                                                                 ===================

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private static final Map<String, DatabaseInfoDef> _codeValueMap = new HashMap<String, DatabaseInfoDef>();
    private String databaseName;
    private String driverName;
    private String urlTemplate;
    private String defultSchema;
    private boolean needSchema;
    private boolean needJdbcDriverJar;
    private boolean upperSchema;
    private boolean assistInputUser;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    private DatabaseInfoDef(String databaseName, String driverName, String urlTemplate, String defultSchema,
            boolean needSchema, boolean needJdbcDriverJar, boolean upperSchema, boolean assistInputUser) {
        this.databaseName = databaseName;
        this.driverName = driverName;
        this.urlTemplate = urlTemplate;
        this.defultSchema = defultSchema;
        this.needSchema = needSchema;
        this.needJdbcDriverJar = needJdbcDriverJar;
        this.assistInputUser = assistInputUser;
    }

    static {
        for (DatabaseInfoDef value : values()) {
            _codeValueMap.put(value.getDatabaseName().toLowerCase(), value);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getDatabaseName() {
        return databaseName;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public String getDefultSchema() {
        return defultSchema;
    }

    public boolean isNeedJdbcDriverJar() {
        return needJdbcDriverJar;
    }

    public boolean isNeedSchema() {
        return needSchema;
    }

    public boolean isUpperSchema() {
        return upperSchema;
    }

    public boolean isAssistInputUser() {
        return assistInputUser;
    }

    public static DatabaseInfoDef codeOf(Object code) {
        if (code == null) {
            return null;
        }
        if (code instanceof DatabaseInfoDef) {
            return (DatabaseInfoDef) code;
        }
        return _codeValueMap.get(code.toString().toLowerCase());
    }
}
