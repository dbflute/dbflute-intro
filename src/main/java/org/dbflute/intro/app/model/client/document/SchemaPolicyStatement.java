/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.app.model.client.document;

import java.util.List;

/**
 * @author hakiba
 */
public class SchemaPolicyStatement {
    public enum Operator {
        AND, OR;

        public String joinName() {
            return " " + name().toLowerCase() + " ";
        }
    }

    public static class Condition {
        public Operator operator;
        public List<String> values;

        public Condition(String operator, List<String> values) {
            this.operator = Operator.valueOf(operator.toUpperCase());
            this.values = values;
        }
    }

    public static class Expected {
        public Operator operator;
        public List<String> values;

        public Expected(String operator, List<String> values) {
            this.operator = Operator.valueOf(operator.toUpperCase());
            this.values = values;
        }
    }

    public String mapType;
    public String subject;
    public Condition condition;
    public Expected expected;
    public String errorMessage;

    public SchemaPolicyStatement(String mapType, String subject, Condition condition, Expected expected, String errorMessage) {
        this.mapType = mapType;
        this.subject = subject;
        this.condition = condition;
        this.expected = expected;
        this.errorMessage = errorMessage;
    }

    public String buildStatement() {
        StringBuilder builder = new StringBuilder("if ");
        builder.append(subject);
        builder.append(" is ");
        String condStr = String.join(condition.operator.joinName(), condition.values);
        builder.append(condStr);
        builder.append(" then ");
        builder.append(String.join(expected.operator.joinName(), expected.values));
        builder.append(" => ");
        builder.append(errorMessage);
        return builder.toString();
    }
}
