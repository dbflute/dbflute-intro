/*
 * Copyright 2014-2015 the original author or authors.
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
package org.dbflute.intro.mylasta.webcls;

import java.util.*;

import org.dbflute.jdbc.Classification;
import org.dbflute.jdbc.ClassificationCodeType;
import org.dbflute.jdbc.ClassificationMeta;
import org.dbflute.jdbc.ClassificationUndefinedHandlingType;

/**
 * The definition of web classification.
 * @author FreeGen
 */
public interface WebCDef extends Classification {

    /** The empty array for no sisters. */
    String[] EMPTY_SISTERS = new String[]{};

    /** The empty map for no sub-items. */
    @SuppressWarnings("unchecked")
    Map<String, Object> EMPTY_SUB_ITEM_MAP = (Map<String, Object>)Collections.EMPTY_MAP;

    /**
     * TargetLanguage for DBFlute
     */
    public enum TargetLanguage implements WebCDef {
        /** java: Java */
        java("java", "java", EMPTY_SISTERS)
        ,
        /** csharp: CSharp */
        csharp("csharp", "csharp", EMPTY_SISTERS)
        ,
        /** scala: Scala */
        scala("scala", "scala", EMPTY_SISTERS)
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
        public ClassificationMeta meta() { return WebCDef.DefMeta.TargetLanguage; }

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
     * TargetContainer for DBFlute
     */
    public enum TargetContainer implements WebCDef {
        /** lasta_di: Lasta Di */
        lasta_di("lasta_di", "lasta_di", EMPTY_SISTERS)
        ,
        /** spring: Spring Framework */
        spring("spring", "spring", EMPTY_SISTERS)
        ,
        /** guice: Google Guice */
        guice("guice", "guice", EMPTY_SISTERS)
        ,
        /** seasar: Seasar */
        seasar("seasar", "seasar", EMPTY_SISTERS)
        ,
        /** cdi: CDI */
        cdi("cdi", "cdi", EMPTY_SISTERS)
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
        public ClassificationMeta meta() { return WebCDef.DefMeta.TargetContainer; }

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

    public enum DefMeta implements ClassificationMeta {
        /** TargetLanguage for DBFlute */
        TargetLanguage
        ,
        /** TargetContainer for DBFlute */
        TargetContainer
        ;
        public String classificationName() {
            return name(); // same as definition name
        }

        public Classification codeOf(Object code) {
            if ("TargetLanguage".equals(name())) { return WebCDef.TargetLanguage.codeOf(code); }
            if ("TargetContainer".equals(name())) { return WebCDef.TargetContainer.codeOf(code); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public Classification nameOf(String name) {
            if ("TargetLanguage".equals(name())) { return WebCDef.TargetLanguage.valueOf(name); }
            if ("TargetContainer".equals(name())) { return WebCDef.TargetContainer.valueOf(name); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> listAll() {
            if ("TargetLanguage".equals(name())) { return toClassificationList(WebCDef.TargetLanguage.listAll()); }
            if ("TargetContainer".equals(name())) { return toClassificationList(WebCDef.TargetContainer.listAll()); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        public List<Classification> groupOf(String groupName) {
            if ("TargetLanguage".equals(name())) { return toClassificationList(WebCDef.TargetLanguage.groupOf(groupName)); }
            if ("TargetContainer".equals(name())) { return toClassificationList(WebCDef.TargetContainer.groupOf(groupName)); }
            throw new IllegalStateException("Unknown definition: " + this); // basically unreachable
        }

        @SuppressWarnings("unchecked")
        private List<Classification> toClassificationList(List<?> clsList) {
            return (List<Classification>)clsList;
        }

        public ClassificationCodeType codeType() {
            if ("TargetLanguage".equals(name())) { return ClassificationCodeType.String; }
            if ("TargetContainer".equals(name())) { return ClassificationCodeType.String; }
            return ClassificationCodeType.String; // as default
        }

        public ClassificationUndefinedHandlingType undefinedHandlingType() {
            if ("TargetLanguage".equals(name())) { return ClassificationUndefinedHandlingType.LOGGING; }
            if ("TargetContainer".equals(name())) { return ClassificationUndefinedHandlingType.LOGGING; }
            return ClassificationUndefinedHandlingType.LOGGING; // as default
        }

        public static WebCDef.DefMeta meta(String classificationName) { // instead of valueOf()
            if (classificationName == null) { throw new IllegalArgumentException("The argument 'classificationName' should not be null."); }
            throw new IllegalStateException("Unknown classification: " + classificationName);
        }
    }
}
