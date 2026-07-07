/*
 * Copyright 2014-2026 the original author or authors.
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
package org.dbflute.intro.app.web.dfprop.schemapolicy;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.lastaflute.web.validation.Required;

/**
 * SchemaPolicyのdfpropを編集するリクエストBody。 <br>
 * 更新対象の項目だけ設定する。(サーバーサイド側で既存dfpropとマージされる)
 * @author hakiba
 * @author jflute
 */
public class DfpropSchemaPolicyEditBody {

    @Required
    @Valid
    public WholeMap wholeMap;

    @Required
    @Valid
    public TableMap tableMap;

    @Required
    @Valid
    public ColumnMap columnMap;

    public static class WholeMap {

        @NotNull
        @Valid
        public List<Theme> themeList;
    }

    // #for_now jflute 現時点ではthemeのみ更新。将来的にはstatementも更新できるようにしたい (2026/07/07)
    public static class TableMap {

        @NotNull
        @Valid
        public List<Theme> themeList;
    }

    public static class ColumnMap {

        @NotNull
        @Valid
        public List<Theme> themeList;
    }

    public static class Theme {

        // #thinking jflute uniqueTableAliasなどthemeの項目名をなんて呼ぶかDBFluteとしても決まってない。 (2026/07/07)
        // まあ、themeNameかな？少なくとも typeCode だとピンと来ないので修正したいところだが、フロントエンドも一緒に修正が必要。
        @Required
        public String typeCode;

        @Required
        public Boolean isActive;
    }
}
