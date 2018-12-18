package org.dbflute.intro.app.model.client.document;

import java.util.List;

/**
 * @author hakiba
 */
public class SchemaPolicyColumnMap {

    public enum ThemeType {
        //@formatter:off
        UpperCaseBasis("upperCaseBasis", "Table name is capitalized (determined by name on SQL)."),
        LowerCaseBasis("lowerCaseBasis", "Table name is lowercase (determined by name on SQL)."),
        HasAlias("hasAlias", "There is an alias (premise assuming that part of DB comment is recognized as an alias)."),
        HasComment("hasComment", "There is a comment (except for the alias part).");

        //@formatter:off

        ThemeType(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String code;
        public String description;
    }

    public static class Theme {
        public ThemeType type;
        public boolean isActive;

        public Theme(ThemeType type, boolean isActive) {
            this.type = type;
            this.isActive = isActive;
        }
    }

    public List<Theme> themeList;

    public SchemaPolicyColumnMap(List<Theme> themeList) {
        this.themeList = themeList;
    }
}
