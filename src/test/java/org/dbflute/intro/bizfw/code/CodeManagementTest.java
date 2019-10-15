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
package org.dbflute.intro.bizfw.code;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.utflute.core.PlainTestCase;
import org.dbflute.util.Srl;
import org.lastaflute.web.Execute;

/**
 * @author cabos
 * @author jflute
 */
public class CodeManagementTest extends PlainTestCase {

    private static final List<String> EDITABLE_METHOD_NAME =
            Arrays.asList("edit", "update", "create", "delete", "download", "remove", "execute", "prepare");

    public void test_decomment_annotation_check() {
        policeStoryOfJavaClassChase((srcFile, clazz) -> {
            for (Method method : clazz.getMethods()) {
                final Execute execute = method.getAnnotation(Execute.class);
                final String methodName = method.getName();
                if (execute != null) {
                    if (EDITABLE_METHOD_NAME.stream().anyMatch(name -> methodName.toLowerCase().contains(name))) {
                        final NotAvailableDecommentServer methodAnnotation = method.getAnnotation(NotAvailableDecommentServer.class);
                        final NotAvailableDecommentServer classAnnotation = clazz.getAnnotation(NotAvailableDecommentServer.class);
                        if (methodAnnotation == null && classAnnotation == null) {
                            String msg = clazz.getName() + "#" + methodName + " doesn't have NotAvailableDecommentServer annotation.\n"
                                    + "This method is editable method.";
                            throw new IllegalStateException(msg);
                        }
                    }
                }
            }
        });
    }

    public void test_decomment_decoMapFile_dependency() {
        policeStoryOfJavaClassChase((srcFile, clazz) -> {
            if (clazz.getName().startsWith("org.dbflute.infra")) { // e.g. DfDecoMapFile
                log("...Checking infra class: {}", clazz.getName());
                readLine(srcFile, "UTF-8", line -> {
                    if (line.startsWith("import ")) {
                        String refName = Srl.rtrim(Srl.substringFirstRear(line, "import "), ";");
                        if (refName.startsWith("org.dbflute.intro")) {
                            String msg = "Cannot refer to intro classes: " + clazz.getName() + ", " + refName;
                            throw new IllegalStateException(msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * Logic package check.
     * See details in http://dbflute.seasar.org/ja/lastaflute/howto/impldesign/beandesign.html#insidebean .
     */
    public void test_notExistsInvalidClassNameInLogicPackage() {
        policeStoryOfJavaClassChase((srcFile, clazz) -> {
            if (clazz.getName().startsWith("org.dbflute.intro.app.logic")) {
                log("...Checking logic class: {}", clazz.getName());
                if (!Srl.endsWith(clazz.getName(), "Logic", "Return", "Param", "Exception")) {
                    String msg = "Class in logic package should ends with \"Logic\", \"Return\", \"Param\" or \"Exception\" .\n"
                            + "class name : " + clazz.getName();
                    // TODO cabos create class of code management test (2019-10-15)
                    throw new IllegalStateException(msg);
                }
            }
        });
    }

    /**
     * DBFlute does not provide service that needs auth.
     * Don't use AllowAnyoneAccess annotation.
     */
    public void test_notUseAllowAnyoneAccess() {
        policeStoryOfJavaClassChase((srcFile, clazz) -> {
            if (clazz.getName().startsWith("org.dbflute.intro.web")) {
                log("...Checking logic class: {}", clazz.getName());
                readLine(srcFile, "UTF-8", line -> {
                    if (line.contains("@AllowAnyoneAccess")) {
                        String msg = "Enable all access, remove annotation. class name : " + clazz.getName();
                        throw new IllegalStateException(msg);
                    }
                });
            }
        });
    }
}
