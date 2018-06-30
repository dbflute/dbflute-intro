/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.web.document.decomment;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.app.logic.intro.IntroSystemLogic;
import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

// done cabos rename DecommentSaveBody by jflute (2017/08/10)

/**
 * @author cabos
 * @author deco
 */
public class DecommentSaveBody {

    // done cabos system parameter, use (groups = ClientError.class) _lavc... by jflute (2017/08/10)
    // done cabos javadoc and add e.g. (above annotation) by jflute (2017/09/28)
    // done cabos delete merged property by jflute (2017/11/11)

    /**
     * table name
     * e.g. "MEMBER"
     */
    @Required(groups = ClientError.class)
    public String tableName;

    /**
     * column name
     * The decomment target may be TABLE so null allowed.
     * e.g. "MEMBER_NAME"
     */
    public String columnName;

    /**
     * decomment target type
     * e.g. COLUMN
     */
    @Required(groups = ClientError.class)
    public DfDecoMapPieceTargetType targetType;

    /**
     * inputted column comment on the schema.html
     * e.g. "this column's value is always null ...??  DO NOT BE SILLY!!!" (NullAllowed)
     */
    @Required
    public String decomment;

    /**
     * column comment on table definition
     * The comments on database may be blank so null allowed.
     * e.g. "this column's value allowed null?" (NullAllowed)
     */
    public String databaseComment;

    /**
     * column comment version
     * The comment version will update when the decomment.dfprop file is picked up.
     * e.g. 2
     */
    @Required(groups = ClientError.class)
    public Long commentVersion;

    /**
     * the input author
     * Only used when run as decomment server {@link IntroSystemLogic#isDecommentServer()}
     * e.g. "cabos"
     */
    public String inputAuthor;

    /**
     * the list of ancestor authors
     * Current piece author is derived by server at first decomment so empty allowed.
     * e.g. ["cabos", "hakiba", "deco"]  (EmptyAllowed)
     */
    @NotNull(groups = ClientError.class)
    public List<String> authors;

    /**
     * the list of previous piece code
     * Current piece code is derived by server at first decomment so empty allowed.
     * e.g. ["FE893L1"]  (EmptyAllowed)
     */
    @NotNull(groups = ClientError.class)
    public List<String> previousPieces;
}
