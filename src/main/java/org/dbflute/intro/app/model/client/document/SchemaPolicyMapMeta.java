package org.dbflute.intro.app.model.client.document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hakiba
 */
public class SchemaPolicyMapMeta {
    public static class WholeMapMeta {
        public ThemeListMeta themeListMeta = new ThemeListMeta();
    }

    public static class ThemeListMeta {
        public List<String> originalThemeCodeList = new ArrayList<>();
    }

    public WholeMapMeta wholeMapMeta = new WholeMapMeta();
}
