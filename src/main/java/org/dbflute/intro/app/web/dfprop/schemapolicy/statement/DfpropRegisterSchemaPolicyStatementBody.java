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
package org.dbflute.intro.app.web.dfprop.schemapolicy.statement;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author hakiba
 */
public class DfpropRegisterSchemaPolicyStatementBody {
    public static class ConditionPart {
        @NotNull
        public String operator;
        @NotEmpty
        public List<String> conditions;
    }

    public static class ExpectedPart {
        @NotNull
        public String operator;
        @NotEmpty
        public List<String> expected;
    }

    @NotNull
    public String type;

    @NotBlank
    public String subject;

    @Valid
    public ConditionPart condition;

    @Valid
    public ExpectedPart expected;

    @NotBlank
    public String comment;
}
