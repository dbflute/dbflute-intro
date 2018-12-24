package org.dbflute.intro.app.model.client.document;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        //@formatter:on

        ThemeType(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String code;
        public String description;

        public static ThemeType valueByCode(String code) {
            return Arrays.stream(ThemeType.values())
                    .filter(themeType -> themeType.code.equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Illegal code : " + code));
        }
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
    public List<String> statementList;

    public static SchemaPolicyColumnMap noSettingInstance() {
        final List<Theme> themeList = Arrays.stream(ThemeType.values()).map(type -> new Theme(type, false)).collect(Collectors.toList());
        return new SchemaPolicyColumnMap(themeList, Collections.emptyList());
    }

    public SchemaPolicyColumnMap(List<Theme> themeList, List<String> statementList) {
        this.themeList = themeList;
        this.statementList = statementList;
    }
}
