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
package org.dbflute.intro.app.web.task;

import org.lastaflute.web.validation.Required;

/**
 * DBFluteタスクの実行結果。
 * @author deco
 * @author jflute
 */
public class TaskExecutionResult {

    // あえて大文字BooleanにしてRequiredにすることで、自動生成typeでoptionalが外れる。
    // 自動生成ツール側でプリミティブだったら強制的にnon-optionalとしてもいいのかもだけど、
    // publicフィールドのケースだとBooleanを使った方が良いというのはあるのでこれで。
    /**
     * 業務的に実行が成功したか？失敗は、SchemaPolicyのviolationなど。<br>
     * システム的な失敗のケースは、そもそも例外がthrowされるのでHTTP statusが200ではない。
     */
    @Required
    public final Boolean success;

    public TaskExecutionResult(Boolean success) {
        this.success = success;
    }
}
