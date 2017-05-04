/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.mylasta.direction.sponsor.introdb;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbflute.Entity;
import org.dbflute.bhv.BehaviorSelector;
import org.dbflute.bhv.BehaviorWritable;
import org.dbflute.dbmeta.DBMeta;
import org.dbflute.helper.filesystem.FileTextIO;
import org.dbflute.helper.token.file.FileToken;
import org.dbflute.intro.dbflute.allcommon.DBMetaInstanceHandler;
import org.dbflute.intro.dbflute.exbhv.ClsTargetContainerBhv;
import org.dbflute.intro.dbflute.exbhv.ClsTargetDatabaseBhv;
import org.dbflute.intro.dbflute.exbhv.ClsTargetLanguageBhv;
import org.dbflute.util.DfResourceUtil;
import org.lastaflute.core.util.ContainerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jflute
 */
public class IntroDBInitializer {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Logger logger = LoggerFactory.getLogger(IntroDBInitializer.class);
    private static final String SCHEMA_SQL_PATH = "introdb/create-schema.sql";
    private static final String TSV_BASE_PATH = "introdb/data/common";

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    public void initializeIntroDB() {
        createSchema();
        registerData();
    }

    // ===================================================================================
    //                                                                       Create Schema
    //                                                                       =============
    protected void createSchema() {
        final String schemaSqlPath = SCHEMA_SQL_PATH;
        final String sqls = readCreateSchemaSql(schemaSqlPath);
        logger.debug("...Creating IntroDB: {}", schemaSqlPath);
        final DataSource dataSource = ContainerUtil.getComponent(DataSource.class);
        Connection conn = null;
        Statement st = null;
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
            for (DBMeta dbmeta : DBMetaInstanceHandler.getUnmodifiableDBMetaMap().values()) {
                st.execute("drop table " + dbmeta.getTableSqlName() + " if exists");
            }
            st.execute(sqls);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to execute the DDL: " + schemaSqlPath, e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    protected String readCreateSchemaSql(String schemaSqlPath) {
        final InputStream ins = DfResourceUtil.getResourceStream(schemaSqlPath);
        if (ins == null) {
            throw new IllegalStateException("Not found the create-schema SQL: " + schemaSqlPath);
        }
        return new FileTextIO().encodeAsUTF8().read(ins); // semi-colon separated SQLs
    }

    // ===================================================================================
    //                                                                       Register Data
    //                                                                       =============
    protected void registerData() {
        // #hope get file list and resolve table by file name by jfulte (2016/08/12)
        final BehaviorSelector selector = ContainerUtil.getComponent(BehaviorSelector.class);
        registerTsv(selector, ClsTargetDatabaseBhv.class, "10-CLS_TARGET_DATABASE.tsv");
        registerTsv(selector, ClsTargetLanguageBhv.class, "12-CLS_TARGET_LANGUAGE.tsv");
        registerTsv(selector, ClsTargetContainerBhv.class, "14-CLS_TARGET_CONTAINER.tsv");
    }

    protected void registerTsv(BehaviorSelector selector, Class<? extends BehaviorWritable> bhvType, String tsvName) {
        final String tsvPath = TSV_BASE_PATH + "/" + tsvName;
        final InputStream ins = DfResourceUtil.getResourceStream(tsvPath);
        if (ins == null) {
            throw new IllegalStateException("Not found the TSV: " + tsvPath);
        }
        try {
            logger.debug("...Registering TSV: {}", tsvName);
            final BehaviorWritable writableBhv = selector.select(bhvType);
            final DBMeta dbmeta = writableBhv.asDBMeta();
            final List<Entity> entityList = new ArrayList<Entity>();
            new FileToken().tokenize(ins, resource -> {
                final Map<String, String> columnValueMap = resource.toColumnValueMap();
                final Entity entity = dbmeta.newEntity();
                dbmeta.acceptAllColumnMap(entity, columnValueMap);
                entityList.add(entity);
            }, op -> op.delimitateByTab().encodeAsUTF8().handleEmptyAsNull());
            writableBhv.lumpCreate(entityList, null);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to register the TSV: " + tsvName, e);
        }
    }
}
