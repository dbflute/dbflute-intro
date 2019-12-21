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
package org.dbflute.intro.app.web.dfprop;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 */
public class DfpropRegisterSchemaPolicyStatementBody {
    public static class Condition {
        @NotNull
        public String operator;
        @NotEmpty
        public List<String> values;
    }

    public static class Expected {
        @NotNull
        public String operator;
        @NotEmpty
        public List<String> values;
    }

    @NotNull
    public String type;

    @Required
    @NotBlank
    public String subject;

    @Valid
    public Condition condition;

    @Valid
    public Expected expected;

    @Required
    @NotBlank
    public String errorMessage;
}
