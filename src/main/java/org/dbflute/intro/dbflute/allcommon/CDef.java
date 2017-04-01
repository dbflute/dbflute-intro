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

import org.dbflute.exception.ClassificationNotFoundException;
import org.dbflute.jdbc.Classification;
import org.dbflute.jdbc.ClassificationCodeType;
import org.dbflute.jdbc.ClassificationMeta;
import org.dbflute.jdbc.ClassificationUndefinedHandlingType;
import org.dbflute.optional.OptionalThing;
import static org.dbflute.util.DfTypeUtil.emptyStrings;

/**
 * The definition of classification.
 * @author DBFlute(AutoGenerator)
 */
public interface CDef extends Classification {

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
        private static final Map<String, Flg> _codeClsMap = new HashMap<String, Flg>();
        private static final Map<String, Flg> _nameClsMap = new HashMap<String, Flg>();
        static {
            for (Flg value : values()) {
                _codeClsMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeClsMap.put(sister.toLowerCase(), value); }
                _nameClsMap.put(value.name().toLowerCase(), value);
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private Flg(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return Collections.emptyMap(); }
        public ClassificationMeta meta() { return CDef.DefMeta.Flg; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification of the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns empty)
         * @return The optional classification corresponding to the code. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<Flg> of(Object code) {
            if (code == null) { return OptionalThing.ofNullable(null, () -> { throw new ClassificationNotFoundException("null code specified"); }); }
            if (code instanceof Flg) { return OptionalThing.of((Flg)code); }
            if (code instanceof OptionalThing<?>) { return of(((OptionalThing<?>)code).orElse(null)); }
            return OptionalThing.ofNullable(_codeClsMap.get(code.toString().toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification code: " + code);
            });
        }

        /**
         * Find the classification by the name. (CaseInsensitive)
         * @param name The string of name, which is case-insensitive. (NotNull)
         * @return The optional classification corresponding to the name. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<Flg> byName(String name) {
            if (name == null) { throw new IllegalArgumentException("The argument 'name' should not be null."); }
            return OptionalThing.ofNullable(_nameClsMap.get(name.toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification name: " + name);
            });
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use of(code).</span> <br>
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static Flg codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof Flg) { return (Flg)code; }
            return _codeClsMap.get(code.toString().toLowerCase());
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use byName(name).</span> <br>
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
         * @param groupName The string of group name, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if not found, throws exception)
         */
        public static List<Flg> listByGroup(String groupName) {
            if (groupName == null) { throw new IllegalArgumentException("The argument 'groupName' should not be null."); }
            throw new ClassificationNotFoundException("Unknown classification group: Flg." + groupName);
        }

        /**
         * Get the list of classification elements corresponding to the specified codes. (returns new copied list) <br>
         * @param codeList The list of plain code, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the code list. (NotNull, EmptyAllowed: when empty specified)
         */
        public static List<Flg> listOf(Collection<String> codeList) {
            if (codeList == null) { throw new IllegalArgumentException("The argument 'codeList' should not be null."); }
            List<Flg> clsList = new ArrayList<Flg>(codeList.size());
            for (String code : codeList) { clsList.add(of(code).get()); }
            return clsList;
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
        MySQL("mysql", "MySQL", emptyStrings())
        ,
        /** PostgreSQL */
        PostgreSQL("postgresql", "PostgreSQL", emptyStrings())
        ,
        /** Oracle */
        Oracle("oracle", "Oracle", emptyStrings())
        ,
        /** DB2 */
        Db2("db2", "DB2", emptyStrings())
        ,
        /** SQLServer */
        SQLServer("sqlserver", "SQLServer", emptyStrings())
        ,
        /** H2 Database */
        H2Database("h2", "H2 Database", emptyStrings())
        ,
        /** Apache Derby */
        ApacheDerby("derby", "Apache Derby", emptyStrings())
        ;
        private static final Map<String, TargetDatabase> _codeClsMap = new HashMap<String, TargetDatabase>();
        private static final Map<String, TargetDatabase> _nameClsMap = new HashMap<String, TargetDatabase>();
        static {
            for (TargetDatabase value : values()) {
                _codeClsMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeClsMap.put(sister.toLowerCase(), value); }
                _nameClsMap.put(value.name().toLowerCase(), value);
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private TargetDatabase(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return Collections.emptyMap(); }
        public ClassificationMeta meta() { return CDef.DefMeta.TargetDatabase; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification of the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns empty)
         * @return The optional classification corresponding to the code. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TargetDatabase> of(Object code) {
            if (code == null) { return OptionalThing.ofNullable(null, () -> { throw new ClassificationNotFoundException("null code specified"); }); }
            if (code instanceof TargetDatabase) { return OptionalThing.of((TargetDatabase)code); }
            if (code instanceof OptionalThing<?>) { return of(((OptionalThing<?>)code).orElse(null)); }
            return OptionalThing.ofNullable(_codeClsMap.get(code.toString().toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification code: " + code);
            });
        }

        /**
         * Find the classification by the name. (CaseInsensitive)
         * @param name The string of name, which is case-insensitive. (NotNull)
         * @return The optional classification corresponding to the name. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TargetDatabase> byName(String name) {
            if (name == null) { throw new IllegalArgumentException("The argument 'name' should not be null."); }
            return OptionalThing.ofNullable(_nameClsMap.get(name.toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification name: " + name);
            });
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use of(code).</span> <br>
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TargetDatabase codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TargetDatabase) { return (TargetDatabase)code; }
            return _codeClsMap.get(code.toString().toLowerCase());
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use byName(name).</span> <br>
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
         * @param groupName The string of group name, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if not found, throws exception)
         */
        public static List<TargetDatabase> listByGroup(String groupName) {
            if (groupName == null) { throw new IllegalArgumentException("The argument 'groupName' should not be null."); }
            throw new ClassificationNotFoundException("Unknown classification group: TargetDatabase." + groupName);
        }

        /**
         * Get the list of classification elements corresponding to the specified codes. (returns new copied list) <br>
         * @param codeList The list of plain code, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the code list. (NotNull, EmptyAllowed: when empty specified)
         */
        public static List<TargetDatabase> listOf(Collection<String> codeList) {
            if (codeList == null) { throw new IllegalArgumentException("The argument 'codeList' should not be null."); }
            List<TargetDatabase> clsList = new ArrayList<TargetDatabase>(codeList.size());
            for (String code : codeList) { clsList.add(of(code).get()); }
            return clsList;
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
        Java("java", "Java", emptyStrings())
        ,
        /** C# */
        C("csharp", "C#", emptyStrings())
        ,
        /** Scala */
        Scala("scala", "Scala", emptyStrings())
        ;
        private static final Map<String, TargetLanguage> _codeClsMap = new HashMap<String, TargetLanguage>();
        private static final Map<String, TargetLanguage> _nameClsMap = new HashMap<String, TargetLanguage>();
        static {
            for (TargetLanguage value : values()) {
                _codeClsMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeClsMap.put(sister.toLowerCase(), value); }
                _nameClsMap.put(value.name().toLowerCase(), value);
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private TargetLanguage(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return Collections.emptyMap(); }
        public ClassificationMeta meta() { return CDef.DefMeta.TargetLanguage; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification of the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns empty)
         * @return The optional classification corresponding to the code. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TargetLanguage> of(Object code) {
            if (code == null) { return OptionalThing.ofNullable(null, () -> { throw new ClassificationNotFoundException("null code specified"); }); }
            if (code instanceof TargetLanguage) { return OptionalThing.of((TargetLanguage)code); }
            if (code instanceof OptionalThing<?>) { return of(((OptionalThing<?>)code).orElse(null)); }
            return OptionalThing.ofNullable(_codeClsMap.get(code.toString().toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification code: " + code);
            });
        }

        /**
         * Find the classification by the name. (CaseInsensitive)
         * @param name The string of name, which is case-insensitive. (NotNull)
         * @return The optional classification corresponding to the name. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TargetLanguage> byName(String name) {
            if (name == null) { throw new IllegalArgumentException("The argument 'name' should not be null."); }
            return OptionalThing.ofNullable(_nameClsMap.get(name.toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification name: " + name);
            });
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use of(code).</span> <br>
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TargetLanguage codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TargetLanguage) { return (TargetLanguage)code; }
            return _codeClsMap.get(code.toString().toLowerCase());
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use byName(name).</span> <br>
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
         * @param groupName The string of group name, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if not found, throws exception)
         */
        public static List<TargetLanguage> listByGroup(String groupName) {
            if (groupName == null) { throw new IllegalArgumentException("The argument 'groupName' should not be null."); }
            throw new ClassificationNotFoundException("Unknown classification group: TargetLanguage." + groupName);
        }

        /**
         * Get the list of classification elements corresponding to the specified codes. (returns new copied list) <br>
         * @param codeList The list of plain code, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the code list. (NotNull, EmptyAllowed: when empty specified)
         */
        public static List<TargetLanguage> listOf(Collection<String> codeList) {
            if (codeList == null) { throw new IllegalArgumentException("The argument 'codeList' should not be null."); }
            List<TargetLanguage> clsList = new ArrayList<TargetLanguage>(codeList.size());
            for (String code : codeList) { clsList.add(of(code).get()); }
            return clsList;
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
        LastaDi("lasta_di", "Lasta Di", emptyStrings())
        ,
        /** Spring Framework */
        SpringFramework("spring", "Spring Framework", emptyStrings())
        ,
        /** Google Guice */
        GoogleGuice("guice", "Google Guice", emptyStrings())
        ,
        /** Seasar (S2Container) */
        SeasarS2Container("seasar", "Seasar (S2Container)", emptyStrings())
        ,
        /** CDI */
        Cdi("cdi", "CDI", emptyStrings())
        ;
        private static final Map<String, TargetContainer> _codeClsMap = new HashMap<String, TargetContainer>();
        private static final Map<String, TargetContainer> _nameClsMap = new HashMap<String, TargetContainer>();
        static {
            for (TargetContainer value : values()) {
                _codeClsMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeClsMap.put(sister.toLowerCase(), value); }
                _nameClsMap.put(value.name().toLowerCase(), value);
            }
        }
        private String _code; private String _alias; private Set<String> _sisterSet;
        private TargetContainer(String code, String alias, String[] sisters)
        { _code = code; _alias = alias; _sisterSet = Collections.unmodifiableSet(new LinkedHashSet<String>(Arrays.asList(sisters))); }
        public String code() { return _code; } public String alias() { return _alias; }
        public Set<String> sisterSet() { return _sisterSet; }
        public Map<String, Object> subItemMap() { return Collections.emptyMap(); }
        public ClassificationMeta meta() { return CDef.DefMeta.TargetContainer; }

        public boolean inGroup(String groupName) {
            return false;
        }

        /**
         * Get the classification of the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns empty)
         * @return The optional classification corresponding to the code. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TargetContainer> of(Object code) {
            if (code == null) { return OptionalThing.ofNullable(null, () -> { throw new ClassificationNotFoundException("null code specified"); }); }
            if (code instanceof TargetContainer) { return OptionalThing.of((TargetContainer)code); }
            if (code instanceof OptionalThing<?>) { return of(((OptionalThing<?>)code).orElse(null)); }
            return OptionalThing.ofNullable(_codeClsMap.get(code.toString().toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification code: " + code);
            });
        }

        /**
         * Find the classification by the name. (CaseInsensitive)
         * @param name The string of name, which is case-insensitive. (NotNull)
         * @return The optional classification corresponding to the name. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TargetContainer> byName(String name) {
            if (name == null) { throw new IllegalArgumentException("The argument 'name' should not be null."); }
            return OptionalThing.ofNullable(_nameClsMap.get(name.toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification name: " + name);
            });
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use of(code).</span> <br>
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TargetContainer codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TargetContainer) { return (TargetContainer)code; }
            return _codeClsMap.get(code.toString().toLowerCase());
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use byName(name).</span> <br>
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
         * @param groupName The string of group name, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if not found, throws exception)
         */
        public static List<TargetContainer> listByGroup(String groupName) {
            if (groupName == null) { throw new IllegalArgumentException("The argument 'groupName' should not be null."); }
            throw new ClassificationNotFoundException("Unknown classification group: TargetContainer." + groupName);
        }

        /**
         * Get the list of classification elements corresponding to the specified codes. (returns new copied list) <br>
         * @param codeList The list of plain code, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the code list. (NotNull, EmptyAllowed: when empty specified)
         */
        public static List<TargetContainer> listOf(Collection<String> codeList) {
            if (codeList == null) { throw new IllegalArgumentException("The argument 'codeList' should not be null."); }
            List<TargetContainer> clsList = new ArrayList<TargetContainer>(codeList.size());
            for (String code : codeList) { clsList.add(of(code).get()); }
            return clsList;
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
        JDBC("jdbc", "JDBC", emptyStrings())
        ,
        /** Doc */
        Doc("doc", "Doc", emptyStrings())
        ,
        /** Generate */
        Generate("generate", "Generate", emptyStrings())
        ,
        /** OutsideSqlTest */
        OutsideSqlTest("outside_sql_test", "OutsideSqlTest", emptyStrings())
        ,
        /** Sql2Entity */
        Sql2Entity("sql2entity", "Sql2Entity", emptyStrings())
        ,
        /** ReplaceSchema */
        ReplaceSchema("replace_schema", "ReplaceSchema", emptyStrings())
        ,
        /** LoadDataReverse */
        LoadDataReverse("load_data_reverse", "LoadDataReverse", emptyStrings())
        ,
        /** SchemaSyncCheck */
        SchemaSyncCheck("schema_sync_check", "SchemaSyncCheck", emptyStrings())
        ;
        private static final Map<String, TaskType> _codeClsMap = new HashMap<String, TaskType>();
        private static final Map<String, TaskType> _nameClsMap = new HashMap<String, TaskType>();
        static {
            for (TaskType value : values()) {
                _codeClsMap.put(value.code().toLowerCase(), value);
                for (String sister : value.sisterSet()) { _codeClsMap.put(sister.toLowerCase(), value); }
                _nameClsMap.put(value.name().toLowerCase(), value);
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
         * Get the classification of the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns empty)
         * @return The optional classification corresponding to the code. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TaskType> of(Object code) {
            if (code == null) { return OptionalThing.ofNullable(null, () -> { throw new ClassificationNotFoundException("null code specified"); }); }
            if (code instanceof TaskType) { return OptionalThing.of((TaskType)code); }
            if (code instanceof OptionalThing<?>) { return of(((OptionalThing<?>)code).orElse(null)); }
            return OptionalThing.ofNullable(_codeClsMap.get(code.toString().toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification code: " + code);
            });
        }

        /**
         * Find the classification by the name. (CaseInsensitive)
         * @param name The string of name, which is case-insensitive. (NotNull)
         * @return The optional classification corresponding to the name. (NotNull, EmptyAllowed: if not found, returns empty)
         */
        public static OptionalThing<TaskType> byName(String name) {
            if (name == null) { throw new IllegalArgumentException("The argument 'name' should not be null."); }
            return OptionalThing.ofNullable(_nameClsMap.get(name.toLowerCase()), () ->{
                throw new ClassificationNotFoundException("Unknown classification name: " + name);
            });
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use of(code).</span> <br>
         * Get the classification by the code. (CaseInsensitive)
         * @param code The value of code, which is case-insensitive. (NullAllowed: if null, returns null)
         * @return The instance of the corresponding classification to the code. (NullAllowed: if not found, returns null)
         */
        public static TaskType codeOf(Object code) {
            if (code == null) { return null; }
            if (code instanceof TaskType) { return (TaskType)code; }
            return _codeClsMap.get(code.toString().toLowerCase());
        }

        /**
         * <span style="color: #AD4747; font-size: 120%">Old style so use byName(name).</span> <br>
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
         * @param groupName The string of group name, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the group. (NotNull, EmptyAllowed: if not found, throws exception)
         */
        public static List<TaskType> listByGroup(String groupName) {
            if (groupName == null) { throw new IllegalArgumentException("The argument 'groupName' should not be null."); }
            throw new ClassificationNotFoundException("Unknown classification group: TaskType." + groupName);
        }

        /**
         * Get the list of classification elements corresponding to the specified codes. (returns new copied list) <br>
         * @param codeList The list of plain code, which is case-insensitive. (NotNull)
         * @return The snapshot list of classification elements in the code list. (NotNull, EmptyAllowed: when empty specified)
         */
        public static List<TaskType> listOf(Collection<String> codeList) {
            if (codeList == null) { throw new IllegalArgumentException("The argument 'codeList' should not be null."); }
            List<TaskType> clsList = new ArrayList<TaskType>(codeList.size());
            for (String code : codeList) { clsList.add(of(code).get()); }
            return clsList;
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

        public OptionalThing<? extends Classification> of(Object code) {
            if (Flg.name().equals(name())) { return CDef.Flg.of(code); }
            if (TargetDatabase.name().equals(name())) { return CDef.TargetDatabase.of(code); }
            if (TargetLanguage.name().equals(name())) { return CDef.TargetLanguage.of(code); }
            if (TargetContainer.name().equals(name())) { return CDef.TargetContainer.of(code); }
            if (TaskType.name().equals(name())) { return CDef.TaskType.of(code); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public OptionalThing<? extends Classification> byName(String name) {
            if (Flg.name().equals(name())) { return CDef.Flg.byName(name); }
            if (TargetDatabase.name().equals(name())) { return CDef.TargetDatabase.byName(name); }
            if (TargetLanguage.name().equals(name())) { return CDef.TargetLanguage.byName(name); }
            if (TargetContainer.name().equals(name())) { return CDef.TargetContainer.byName(name); }
            if (TaskType.name().equals(name())) { return CDef.TaskType.byName(name); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public Classification codeOf(Object code) { // null if not found, old style so use classificationOf(code)
            if (Flg.name().equals(name())) { return CDef.Flg.codeOf(code); }
            if (TargetDatabase.name().equals(name())) { return CDef.TargetDatabase.codeOf(code); }
            if (TargetLanguage.name().equals(name())) { return CDef.TargetLanguage.codeOf(code); }
            if (TargetContainer.name().equals(name())) { return CDef.TargetContainer.codeOf(code); }
            if (TaskType.name().equals(name())) { return CDef.TaskType.codeOf(code); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public Classification nameOf(String name) { // null if not found, old style so use classificationByName(name)
            if (Flg.name().equals(name())) { return CDef.Flg.valueOf(name); }
            if (TargetDatabase.name().equals(name())) { return CDef.TargetDatabase.valueOf(name); }
            if (TargetLanguage.name().equals(name())) { return CDef.TargetLanguage.valueOf(name); }
            if (TargetContainer.name().equals(name())) { return CDef.TargetContainer.valueOf(name); }
            if (TaskType.name().equals(name())) { return CDef.TaskType.valueOf(name); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> listAll() {
            if (Flg.name().equals(name())) { return toClsList(CDef.Flg.listAll()); }
            if (TargetDatabase.name().equals(name())) { return toClsList(CDef.TargetDatabase.listAll()); }
            if (TargetLanguage.name().equals(name())) { return toClsList(CDef.TargetLanguage.listAll()); }
            if (TargetContainer.name().equals(name())) { return toClsList(CDef.TargetContainer.listAll()); }
            if (TaskType.name().equals(name())) { return toClsList(CDef.TaskType.listAll()); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> listByGroup(String groupName) { // exception if not found
            if (Flg.name().equals(name())) { return toClsList(CDef.Flg.listByGroup(groupName)); }
            if (TargetDatabase.name().equals(name())) { return toClsList(CDef.TargetDatabase.listByGroup(groupName)); }
            if (TargetLanguage.name().equals(name())) { return toClsList(CDef.TargetLanguage.listByGroup(groupName)); }
            if (TargetContainer.name().equals(name())) { return toClsList(CDef.TargetContainer.listByGroup(groupName)); }
            if (TaskType.name().equals(name())) { return toClsList(CDef.TaskType.listByGroup(groupName)); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> listOf(Collection<String> codeList) {
            if (Flg.name().equals(name())) { return toClsList(CDef.Flg.listOf(codeList)); }
            if (TargetDatabase.name().equals(name())) { return toClsList(CDef.TargetDatabase.listOf(codeList)); }
            if (TargetLanguage.name().equals(name())) { return toClsList(CDef.TargetLanguage.listOf(codeList)); }
            if (TargetContainer.name().equals(name())) { return toClsList(CDef.TargetContainer.listOf(codeList)); }
            if (TaskType.name().equals(name())) { return toClsList(CDef.TaskType.listOf(codeList)); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> groupOf(String groupName) { // old style
            if (Flg.name().equals(name())) { return toClsList(CDef.Flg.groupOf(groupName)); }
            if (TargetDatabase.name().equals(name())) { return toClsList(CDef.TargetDatabase.groupOf(groupName)); }
            if (TargetLanguage.name().equals(name())) { return toClsList(CDef.TargetLanguage.groupOf(groupName)); }
            if (TargetContainer.name().equals(name())) { return toClsList(CDef.TargetContainer.groupOf(groupName)); }
            if (TaskType.name().equals(name())) { return toClsList(CDef.TaskType.groupOf(groupName)); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        @SuppressWarnings("unchecked")
        private List<Classification> toClsList(List<?> clsList) {
            return (List<Classification>)clsList;
        }

        public ClassificationCodeType codeType() {
            if (Flg.name().equals(name())) { return ClassificationCodeType.Number; }
            if (TargetDatabase.name().equals(name())) { return ClassificationCodeType.String; }
            if (TargetLanguage.name().equals(name())) { return ClassificationCodeType.String; }
            if (TargetContainer.name().equals(name())) { return ClassificationCodeType.String; }
            if (TaskType.name().equals(name())) { return ClassificationCodeType.String; }
            return ClassificationCodeType.String; // as default
        }

        public ClassificationUndefinedHandlingType undefinedHandlingType() {
            if (Flg.name().equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if (TargetDatabase.name().equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if (TargetLanguage.name().equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if (TargetContainer.name().equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            if (TaskType.name().equals(name())) { return ClassificationUndefinedHandlingType.EXCEPTION; }
            return ClassificationUndefinedHandlingType.LOGGING; // as default
        }

        public static OptionalThing<CDef.DefMeta> find(String classificationName) { // instead of valueOf()
            if (classificationName == null) { throw new IllegalArgumentException("The argument 'classificationName' should not be null."); }
            if (Flg.name().equalsIgnoreCase(classificationName)) { return OptionalThing.of(CDef.DefMeta.Flg); }
            if (TargetDatabase.name().equalsIgnoreCase(classificationName)) { return OptionalThing.of(CDef.DefMeta.TargetDatabase); }
            if (TargetLanguage.name().equalsIgnoreCase(classificationName)) { return OptionalThing.of(CDef.DefMeta.TargetLanguage); }
            if (TargetContainer.name().equalsIgnoreCase(classificationName)) { return OptionalThing.of(CDef.DefMeta.TargetContainer); }
            if (TaskType.name().equalsIgnoreCase(classificationName)) { return OptionalThing.of(CDef.DefMeta.TaskType); }
            return OptionalThing.ofNullable(null, () -> {
                throw new ClassificationNotFoundException("Unknown classification: " + classificationName);
            });
        }

        public static CDef.DefMeta meta(String classificationName) { // old style so use find(name)
            if (classificationName == null) { throw new IllegalArgumentException("The argument 'classificationName' should not be null."); }
            if (Flg.name().equalsIgnoreCase(classificationName)) { return CDef.DefMeta.Flg; }
            if (TargetDatabase.name().equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TargetDatabase; }
            if (TargetLanguage.name().equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TargetLanguage; }
            if (TargetContainer.name().equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TargetContainer; }
            if (TaskType.name().equalsIgnoreCase(classificationName)) { return CDef.DefMeta.TaskType; }
            throw new IllegalStateException("Unknown classification: " + classificationName);
        }

        @SuppressWarnings("unused")
        private String[] xinternalEmptyString() {
            return emptyStrings(); // to suppress 'unused' warning of import statement
        }
    }
}
