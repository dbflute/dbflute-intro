package org.dbflute.intro.app.logic.intro;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cabos
 */
public class IntroSystemPropertyLogic {

    protected static final String DECOMMENT_SERVER_KEY = "intro.decomment.server";
    protected static final Map<String, String> SYSTEM_PROPERTY_MAP = new HashMap<>();

    public boolean isDecommentServerKey() {
        if (SYSTEM_PROPERTY_MAP.containsKey(DECOMMENT_SERVER_KEY)) {
            return Boolean.valueOf(SYSTEM_PROPERTY_MAP.getOrDefault(DECOMMENT_SERVER_KEY, String.valueOf(Boolean.FALSE)));
        }
        String property = System.getProperty(DECOMMENT_SERVER_KEY, String.valueOf(Boolean.FALSE));
        SYSTEM_PROPERTY_MAP.put(DECOMMENT_SERVER_KEY, property);
        return Boolean.valueOf(property);
    }
}
