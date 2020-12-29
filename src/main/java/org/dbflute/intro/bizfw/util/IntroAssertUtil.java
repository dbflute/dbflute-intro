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
package org.dbflute.intro.bizfw.util;

/**
 * parameter assertion util.
 * @author cabos
 * @author jflute
 */
public class IntroAssertUtil {

    /**
     * Assert Not null.
     * @param params parameters (NotNull)
     */
    public static void assertNotNull(Object... params) {
        if (params == null) {
            throw new AssertNotNullException("Parameter is null.");
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] != null) {
                continue;
            }
            throw new AssertNotNullException("Parameter " + i + "  is null.");
        }
    }

    public static class AssertNotNullException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        AssertNotNullException(String message) {
            super(message);
        }
    }

    /**
     * Assert Not null and Empty.
     * @param params parameters (Not Null and Empty)
     */
    public static void assertNotEmpty(String... params) {
        if (params == null) {
            throw new AssertNotNullException("Parameter is null.");
        }
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) {
                throw new AssertNotNullException("Parameter " + i + "  is null.");
            }
            if (params[i].isEmpty()) {
                throw new AssertNotEmptyException("Parameter " + i + "  is null.");
            }
        }
    }

    public static class AssertNotEmptyException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        AssertNotEmptyException(String message) {
            super(message);
        }
    }
}
