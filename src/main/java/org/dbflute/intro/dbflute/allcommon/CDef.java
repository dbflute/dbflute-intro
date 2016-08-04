/*
 * Copyright 2014-2016 the original author or authors.
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
package org.dbflute.intro.dbflute.allcommon;

import java.util.*;

import org.dbflute.jdbc.Classification;
import org.dbflute.jdbc.ClassificationCodeType;
import org.dbflute.jdbc.ClassificationMeta;
import org.dbflute.jdbc.ClassificationUndefinedHandlingType;

/**
 * The definition of classification.
 * @author DBFlute(AutoGenerator)
 */
public interface CDef extends Classification {

    /** The empty array for no sisters. */
    String[] EMPTY_SISTERS = new String[]{};

    /** The empty map for no sub-items. */
    @SuppressWarnings("unchecked")
    Map<String, Object> EMPTY_SUB_ITEM_MAP = (Map<String, Object>)Collections.EMPTY_MAP;

    /**
     * general boolean classification for every flg-column
     */
    public enum Flg implements CDef {
        /** Checked: means yes */
        True("1", "Checked", new String[] {"true"})
        ,
        /** Unchecked: means no */
        False("0", "Unchecked", new String[] {"false"})
        ;
        private static final Map<String, Flg> _codeValueMap = new HashMap<String, Flg>();
        static {
            for (Flg value : values()) {
                _codeValueMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeValueMap.put(sister.toLowerCase(), value); }
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private Flg(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return EMPTY_SUB_ITEM_MAP; }
        public ClassificationMeta meta() { return CDef.DefMeta.Flg; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static Flg codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof Flg) { return (Flg)code; }
            return _codeValueMap.get(code.toString().toLowerCase());
        }

        /**
         * Get the classification by the name (also called 'value' in ENUM world).
         * @param name The string of name, which is case-sensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the name. (NullAllowed: if not found, returns null)
         */
        public static Flg nameOf(String name) {
            if (name == null) { return null; }
            try { return valueOf(name); } catch (RuntimeException ignored) { return null; }
        }

        /**
         * Get the list of all classification elements. (returns new copied list)
         * @return The snapshot list of all classification elements. (NotNull)
         */
        public static List<Flg> listAll() {
            return new ArrayList<Flg>(Arrays.asList(values()));
        }

        /**
         * Get the list of classification elements in the specified group. (returns new copied list) <br>
         * @param groupName The string of group name, which is case-sensitive. (NullAllowed: if null, returns empty list)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if the group is not found)
         */
        public static List<Flg> groupOf(String groupName) {
            return new ArrayList<Flg>(4);
        }

        @Override public String toString() { return code(); }
    }

    /**
     * databases DBFlute cau use
     */
    public enum TargetDatabase implements CDef {
        /** MySQL */
        MySQL("mysql", "MySQL", EMPTY_SISTERS)
        ,
        /** PostgreSQL */
        PostgreSQL("postgresql", "PostgreSQL", EMPTY_SISTERS)
        ,
        /** Oracle */
        Oracle("oracle", "Oracle", EMPTY_SISTERS)
        ,
        /** DB2 */
        Db2("db2", "DB2", EMPTY_SISTERS)
        ,
        /** SQLServer */
        SQLServer("sqlserver", "SQLServer", EMPTY_SISTERS)
        ,
        /** H2 Database */
        H2Database("h2", "H2 Database", EMPTY_SISTERS)
        ,
        /** Apache Derby */
        ApacheDerby("derby", "Apache Derby", EMPTY_SISTERS)
        ;
        private static final Map<String, TargetDatabase> _codeValueMap = new HashMap<String, TargetDatabase>();
        static {
            for (TargetDatabase value : values()) {
                _codeValueMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeValueMap.put(sister.toLowerCase(), value); }
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private TargetDatabase(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return EMPTY_SUB_ITEM_MAP; }
        public ClassificationMeta meta() { return CDef.DefMeta.TargetDatabase; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TargetDatabase codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TargetDatabase) { return (TargetDatabase)code; }
            return _codeValueMap.get(code.toString().toLowerCase());
        }

        /**
         * Get the classification by the name (also called 'value' in ENUM world).
         * @param name The string of name, which is case-sensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the name. (NullAllowed: if not found, returns null)
         */
        public static TargetDatabase nameOf(String name) {
            if (name == null) { return null; }
            try { return valueOf(name); } catch (RuntimeException ignored) { return null; }
        }

        /**
         * Get the list of all classification elements. (returns new copied list)
         * @return The snapshot list of all classification elements. (NotNull)
         */
        public static List<TargetDatabase> listAll() {
            return new ArrayList<TargetDatabase>(Arrays.asList(values()));
        }

        /**
         * Get the list of classification elements in the specified group. (returns new copied list) <br>
         * @param groupName The string of group name, which is case-sensitive. (NullAllowed: if null, returns empty list)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if the group is not found)
         */
        public static List<TargetDatabase> groupOf(String groupName) {
            return new ArrayList<TargetDatabase>(4);
        }

        @Override public String toString() { return code(); }
    }

    /**
     * databases DBFlute cau use
     */
    public enum TargetLanguage implements CDef {
        /** Java */
        Java("java", "Java", EMPTY_SISTERS)
        ,
        /** C# */
        C("csharp", "C#", EMPTY_SISTERS)
        ,
        /** Scala */
        Scala("scala", "Scala", EMPTY_SISTERS)
        ;
        private static final Map<String, TargetLanguage> _codeValueMap = new HashMap<String, TargetLanguage>();
        static {
            for (TargetLanguage value : values()) {
                _codeValueMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeValueMap.put(sister.toLowerCase(), value); }
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private TargetLanguage(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return EMPTY_SUB_ITEM_MAP; }
        public ClassificationMeta meta() { return CDef.DefMeta.TargetLanguage; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TargetLanguage codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TargetLanguage) { return (TargetLanguage)code; }
            return _codeValueMap.get(code.toString().toLowerCase());
        }

        /**
         * Get the classification by the name (also called 'value' in ENUM world).
         * @param name The string of name, which is case-sensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the name. (NullAllowed: if not found, returns null)
         */
        public static TargetLanguage nameOf(String name) {
            if (name == null) { return null; }
            try { return valueOf(name); } catch (RuntimeException ignored) { return null; }
        }

        /**
         * Get the list of all classification elements. (returns new copied list)
         * @return The snapshot list of all classification elements. (NotNull)
         */
        public static List<TargetLanguage> listAll() {
            return new ArrayList<TargetLanguage>(Arrays.asList(values()));
        }

        /**
         * Get the list of classification elements in the specified group. (returns new copied list) <br>
         * @param groupName The string of group name, which is case-sensitive. (NullAllowed: if null, returns empty list)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if the group is not found)
         */
        public static List<TargetLanguage> groupOf(String groupName) {
            return new ArrayList<TargetLanguage>(4);
        }

        @Override public String toString() { return code(); }
    }

    /**
     * containers DBFlute cau use
     */
    public enum TargetContainer implements CDef {
        /** Lasta Di */
        LastaDi("lasta_di", "Lasta Di", EMPTY_SISTERS)
        ,
        /** Spring Framework */
        SpringFramework("spring", "Spring Framework", EMPTY_SISTERS)
        ,
        /** Google Guice */
        GoogleGuice("guice", "Google Guice", EMPTY_SISTERS)
        ,
        /** Seasar (S2Container) */
        SeasarS2Container("seasar", "Seasar (S2Container)", EMPTY_SISTERS)
        ,
        /** CDI */
        Cdi("cdi", "CDI", EMPTY_SISTERS)
        ;
        private static final Map<String, TargetContainer> _codeValueMap = new HashMap<String, TargetContainer>();
        static {
            for (TargetContainer value : values()) {
                _codeValueMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeValueMap.put(sister.toLowerCase(), value); }
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private TargetContainer(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return EMPTY_SUB_ITEM_MAP; }
        public ClassificationMeta meta() { return CDef.DefMeta.TargetContainer; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TargetContainer codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TargetContainer) { return (TargetContainer)code; }
            return _codeValueMap.get(code.toString().toLowerCase());
        }

        /**
         * Get the classification by the name (also called 'value' in ENUM world).
         * @param name The string of name, which is case-sensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the name. (NullAllowed: if not found, returns null)
         */
        public static TargetContainer nameOf(String name) {
            if (name == null) { return null; }
            try { return valueOf(name); } catch (RuntimeException ignored) { return null; }
        }

        /**
         * Get the list of all classification elements. (returns new copied list)
         * @return The snapshot list of all classification elements. (NotNull)
         */
        public static List<TargetContainer> listAll() {
            return new ArrayList<TargetContainer>(Arrays.asList(values()));
        }

        /**
         * Get the list of classification elements in the specified group. (returns new copied list) <br>
         * @param groupName The string of group name, which is case-sensitive. (NullAllowed: if null, returns empty list)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if the group is not found)
         */
        public static List<TargetContainer> groupOf(String groupName) {
            return new ArrayList<TargetContainer>(4);
        }

        @Override public String toString() { return code(); }
    }

    /**
     * DBFlute tasks e.g. jdbc, doc
     */
    public enum TaskType implements CDef {
        /** JDBC */
        JDBC("jdbc", "JDBC", EMPTY_SISTERS)
        ,
        /** Doc */
        Doc("doc", "Doc", EMPTY_SISTERS)
        ,
        /** Generate */
        Generate("generate", "Generate", EMPTY_SISTERS)
        ,
        /** OutsideSqlTest */
        OutsideSqlTest("outside_sql_test", "OutsideSqlTest", EMPTY_SISTERS)
        ,
        /** Sql2Entity */
        Sql2Entity("sql2entity", "Sql2Entity", EMPTY_SISTERS)
        ,
        /** ReplaceSchema */
        ReplaceSchema("replace_schema", "ReplaceSchema", EMPTY_SISTERS)
        ,
        /** LoadDataReverse */
        LoadDataReverse("load_data_reverse", "LoadDataReverse", EMPTY_SISTERS)
        ,
        /** SchemaSyncCheck */
        SchemaSyncCheck("schema_sync_check", "SchemaSyncCheck", EMPTY_SISTERS)
        ;
        private static final Map<String, TaskType> _codeValueMap = new HashMap<String, TaskType>();
        static {
            for (TaskType value : values()) {
                _codeValueMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeValueMap.put(sister.toLowerCase(), value); }
            }
        }
        private static final Map<String, Map<String, Object>> _subItemMapMap = new HashMap<String, Map<String, Object>>();
        static {
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "jdbc");
                _subItemMapMap.put(JDBC.code(), Collections.unmodifiableMap(subItemMap));
            }
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "doc");
                _subItemMapMap.put(Doc.code(), Collections.unmodifiableMap(subItemMap));
            }
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "generate");
                _subItemMapMap.put(Generate.code(), Collections.unmodifiableMap(subItemMap));
            }
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "outside-sql-test");
                _subItemMapMap.put(OutsideSqlTest.code(), Collections.unmodifiableMap(subItemMap));
            }
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "sql2entity");
                _subItemMapMap.put(Sql2Entity.code(), Collections.unmodifiableMap(subItemMap));
            }
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "replace-schema");
                _subItemMapMap.put(ReplaceSchema.code(), Collections.unmodifiableMap(subItemMap));
            }
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "loadDataReverse");
                _subItemMapMap.put(LoadDataReverse.code(), Collections.unmodifiableMap(subItemMap));
            }
            {
                Map<String, Object> subItemMap = new HashMap<String, Object>();
                subItemMap.put("manageArg", "schema-sync-check");
                _subItemMapMap.put(SchemaSyncCheck.code(), Collections.unmodifiableMap(subItemMap));
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private TaskType(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return _subItemMapMap.get(code()); }
        public ClassificationMeta meta() { return CDef.DefMeta.TaskType; }

        public String manageArg() {
            return (String)subItemMap().get("manageArg");
        }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TaskType codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TaskType) { return (TaskType)code; }
            return _codeValueMap.get(code.toString().toLowerCase());
        }

        /**
         * Get the classification by the name (also called 'value' in ENUM world).
         * @param name The string of name, which is case-sensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the name. (NullAllowed: if not found, returns null)
         */
        public static TaskType nameOf(String name) {
            if (name == null) { return null; }
            try { return valueOf(name); } catch (RuntimeException ignored) { return null; }
        }

        /**
         * Get the list of all classification elements. (returns new copied list)
         * @return The snapshot list of all classification elements. (NotNull)
         */
        public static List<TaskType> listAll() {
            return new ArrayList<TaskType>(Arrays.asList(values()));
        }

        /**
         * Get the list of classification elements in the specified group. (returns new copied list) <br>
         * @param groupName The string of group name, which is case-sensitive. (NullAllowed: if null, returns empty list)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if the group is not found)
         */
        public static List<TaskType> groupOf(String groupName) {
            return new ArrayList<TaskType>(4);
        }

        @Override public String toString() { return code(); }
    }

    public enum DefMeta implements ClassificationMeta {
        /** general boolean classification for every flg-column */
        Flg
        ,
        /** databases DBFlute cau use */
        TargetDatabase
        ,
        /** databases DBFlute cau use */
        TargetLanguage
        ,
        /** containers DBFlute cau use */
        TargetContainer
        ,
        /** DBFlute tasks e.g. jdbc, doc */
        TaskType
        ;
        public String classificationName() {
            return name(); // same as definition name
        }

        public Classification codeOf(Object code) {
            if ("Flg".equals(name())) { return CDef.Flg.codeOf(code); }
            if ("TargetDatabase".equals(name())) { return CDef.TargetDatabase.codeOf(code); }
            if ("TargetLanguage".equals(name())) { return CDef.TargetLanguage.codeOf(code); }
            if ("TargetContainer".equals(name())) { return CDef.TargetContainer.codeOf(code); }
            if ("TaskType".equals(name())) { return CDef.TaskType.codeOf(code); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public Classification nameOf(String name) {
            if ("Flg".equals(name())) { return CDef.Flg.valueOf(name); }
            if ("TargetDatabase".equals(name())) { return CDef.TargetDatabase.valueOf(name); }
            if ("TargetLanguage".equals(name())) { return CDef.TargetLanguage.valueOf(name); }
            if ("TargetContainer".equals(name())) { return CDef.TargetContainer.valueOf(name); }
            if ("TaskType".equals(name())) { return CDef.TaskType.valueOf(name); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> listAll() {
            if ("Flg".equals(name())) { return toClassificationList(CDef.Flg.listAll()); }
            if ("TargetDatabase".equals(name())) { return toClassificationList(CDef.TargetDatabase.listAll()); }
            if ("TargetLanguage".equals(name())) { return toClassificationList(CDef.TargetLanguage.listAll()); }
            if ("TargetContainer".equals(name())) { return toClassificationList(CDef.TargetContainer.listAll()); }
            if ("TaskType".equals(name())) { return toClassificationList(CDef.TaskType.listAll()); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> groupOf(String groupName) {
            if ("Flg".equals(name())) { return toClassificationList(CDef.Flg.groupOf(groupName)); }
            if ("TargetDatabase".equals(name())) { return toClassificationList(CDef.TargetDatabase.groupOf(groupName)); }
            if ("TargetLanguage".equals(name())) { return toClassificationList(CDef.TargetLanguage.groupOf(groupName)); }
            if ("TargetContainer".equals(name())) { return toClassificationList(CDef.TargetContainer.groupOf(groupName)); }
            if ("TaskType".equals(name())) { return toClassificationList(CDef.TaskType.groupOf(groupName)); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        @SuppressWarnings("unchecked")
        private List<Classification> toClassificationList(List<?> clsList) {
            return (List<Classification>)clsList;
        }

        public ClassificationCodeType codeType() {
            if ("Flg".equals(name())) { return ClassificationCodeType.Number; }
            if ("TargetDatabase".equals(name())) { return ClassificationCodeType.String; }
            if ("TargetLanguage".equals(name())) { return ClassificationCodeType.String; }
            if ("TargetContainer".equals(name())) { return ClassificationCodeType.String; }
            if ("TaskType".equals(name())) { return ClassificationCodeType.String; }
            return ClassificationCodeType.String; // as default
        }

        public ClassificationUndefinedHandlingType undefinedHandlingType() {
            if ("Flg".equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if ("TargetDatabase".equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if ("TargetLanguage".equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if ("TargetContainer".equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if ("TaskType".equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            return ClassificationUndefinedHandlingType.LOGGING; // as default
        }

        public static CDef.DefMeta meta(String classificationName) { // instead of valueOf()
            if (classificationName == null) { throw new IllegalArgumentException("The argument 'classificationName' should not be null."); }
            if ("Flg".equalsIgnoreCase(classificationName)) { return CDef.DefMeta.Flg; }
            if ("TargetDatabase".equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TargetDatabase; }
            if ("TargetLanguage".equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TargetLanguage; }
            if ("TargetContainer".equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TargetContainer; }
            if ("TaskType".equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TaskType; }
            throw new IllegalStateException("Unknown classification: " + classificationName);
        }
    }
}
