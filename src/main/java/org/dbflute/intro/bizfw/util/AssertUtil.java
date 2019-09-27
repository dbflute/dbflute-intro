package org.dbflute.intro.bizfw.util;

/**
 * parameter assertion util.
 * @author cabos
 */
public class AssertUtil {

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
        AssertNotNullException(String message) {
            super(message);
        }
    }
}
