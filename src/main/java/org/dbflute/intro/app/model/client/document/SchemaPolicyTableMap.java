package org.dbflute.intro.app.model.client.document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hakiba
 */
public class SchemaPolicyTableMap {

    public enum ThemeType {
        //@formatter:off
        HasPK("hasPK", "All tables have PK."),
        UpperCaseBasis("upperCaseBasis", "Table name is capitalized (determined by name on SQL)."),
        LowerCaseBasis("lowerCaseBasis", "Table name is lowercase (determined by name on SQL)."),
        IdentityIfPureIDPK("identityIfPureIDPK", "An Identity is assigned to a single PK column ending with an ID that is not an FK."),
        SequenceIfPureIDPK("sequenceIfPureIDPK", "Sequences are attached to single PK columns that end with an ID that is not FK."),
        HasCommonColumn("hasCommonColumn", "Has a common column."),
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

    public static SchemaPolicyTableMap noSettingInstance() {
        final List<Theme> themeList = Arrays.stream(ThemeType.values()).map(type -> new Theme(type, false)).collect(Collectors.toList());
        return new SchemaPolicyTableMap(themeList);
    }

    public SchemaPolicyTableMap(List<Theme> themeList) {
        this.themeList = themeList;
    }
}
