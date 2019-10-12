package org.dbflute.intro.bizfw.util;

/**
 * parameter assertion util.
 * @author cabos
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
        AssertNotNullException(String message) {
            super(message);
        }
    }

    /**
     * Assert Not null and Empty.
     * @param params parameters (Not Null & Empty)
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
        AssertNotEmptyException(String message) {
            super(message);
        }
    }
}
