
-- =======================================================================================
--                                                                         Colorful Schema 
--                                                                         ===============
-- /= = = = = = = = = = = = = = = = = = = = = =
-- for the test of only one-to-one table
-- = = = = = = = = = =/
create table WHITE_ONLY_ONE_TO_ONE_FROM(
    FROM_ID BIGINT AUTO_INCREMENT NOT NULL,
    FROM_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (FROM_ID)
);

create table WHITE_ONLY_ONE_TO_ONE_TO(
    TO_ID BIGINT AUTO_INCREMENT NOT NULL,
    TO_NAME VARCHAR(200) NOT NULL,
    FROM_ID BIGINT NOT NULL,
    PRIMARY KEY (TO_ID),
    UNIQUE (FROM_ID)
);

alter table WHITE_ONLY_ONE_TO_ONE_TO add constraint FK_WHITE_ONLY_ONE_TO_ONE_TO_FROM
	foreign key (FROM_ID)
	REFERENCES WHITE_ONLY_ONE_TO_ONE_FROM (FROM_ID);

-- /= = = = = = = = = = = = = = = = = = = = = =
-- for the test of over relation, referrer over
-- = = = = = = = = = =/
create table WHITE_PURCHASE_REFERRER(
    PURCHASE_REFERRER_ID BIGINT AUTO_INCREMENT NOT NULL,
    PURCHASE_REFERRER_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (PURCHASE_REFERRER_ID)
);

alter table WHITE_PURCHASE_REFERRER add constraint FK_WHITE_PURCHASE_REFERRER
	foreign key (PURCHASE_REFERRER_ID)
	REFERENCES PURCHASE (PURCHASE_ID);

-- /= = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of over relation, split multiple FK
-- = = = = = = = = = =/
create table WHITE_SPLIT_MULTIPLE_FK_BASE(
    BASE_ID BIGINT NOT NULL,
    FIRST_ID INTEGER NOT NULL,
    NEXT_ID BIGINT NOT NULL,
    SPLIT_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (BASE_ID)
);

create table WHITE_SPLIT_MULTIPLE_FK_NEXT(
    NEXT_ID BIGINT NOT NULL,
    SECOND_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (NEXT_ID)
);

create table WHITE_SPLIT_MULTIPLE_FK_REF(
    FIRST_ID INTEGER NOT NULL,
    SECOND_CODE CHAR(3) NOT NULL,
    REF_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (FIRST_ID, SECOND_CODE)
);

create table WHITE_SPLIT_MULTIPLE_FK_CHILD(
    CHILD_ID BIGINT NOT NULL,
    BASE_ID BIGINT NOT NULL,
    CHILD_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (CHILD_ID)
);

alter table WHITE_SPLIT_MULTIPLE_FK_BASE add constraint FK_WHITE_SPLIT_MULTIPLE_FK_NEXT
	foreign key (NEXT_ID)
	REFERENCES WHITE_SPLIT_MULTIPLE_FK_NEXT (NEXT_ID) ;

alter table WHITE_SPLIT_MULTIPLE_FK_CHILD add constraint FK_WHITE_SPLIT_MULTIPLE_FK_BASE
	foreign key (BASE_ID)
	REFERENCES WHITE_SPLIT_MULTIPLE_FK_BASE (BASE_ID) ;

-- /= = = = = = = = = = = = = = = = = =
-- for the test of implicit reverse FK
-- = = = = = = = = = =/
create table WHITE_IMPLICIT_REVERSE_FK(
    WHITE_IMPLICIT_REVERSE_FK_ID INTEGER AUTO_INCREMENT NOT NULL,
    WHITE_IMPLICIT_REVERSE_FK_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (WHITE_IMPLICIT_REVERSE_FK_ID)
);

create table WHITE_IMPLICIT_REVERSE_FK_REF(
    WHITE_IMPLICIT_REVERSE_FK_REF_ID INTEGER AUTO_INCREMENT NOT NULL,
    WHITE_IMPLICIT_REVERSE_FK_ID INTEGER NOT NULL,
    VALID_BEGIN_DATE DATE NOT NULL,
    VALID_END_DATE DATE NOT NULL,
    PRIMARY KEY (WHITE_IMPLICIT_REVERSE_FK_REF_ID),
    UNIQUE (WHITE_IMPLICIT_REVERSE_FK_ID, VALID_BEGIN_DATE)
);

create table WHITE_IMPLICIT_REVERSE_FK_SUPPRESS(
    WHITE_IMPLICIT_REVERSE_FK_SUPPRESS_ID INTEGER AUTO_INCREMENT NOT NULL,
    WHITE_IMPLICIT_REVERSE_FK_ID INTEGER NOT NULL,
    VALID_BEGIN_DATE DATE NOT NULL,
    VALID_END_DATE DATE NOT NULL,
    PRIMARY KEY (WHITE_IMPLICIT_REVERSE_FK_SUPPRESS_ID),
    UNIQUE (WHITE_IMPLICIT_REVERSE_FK_ID, VALID_BEGIN_DATE)
);

-- /= = = = = = = = = = = = = = = = = = = =
-- for the test of compound primary key
-- = = = = = = = = = =/
create table WHITE_COMPOUND_REFERRED_NORMALLY (
	REFERRED_ID INTEGER NOT NULL,
	REFERRED_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (REFERRED_ID)
) ;

create table WHITE_COMPOUND_REFERRED_PRIMARY (
	REFERRED_ID INTEGER NOT NULL,
	REFERRED_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (REFERRED_ID)
) ;

create table WHITE_COMPOUND_PK (
	PK_FIRST_ID INTEGER NOT NULL,
	PK_SECOND_ID INTEGER NOT NULL,
	PK_NAME VARCHAR(200) NOT NULL,
	REFERRED_ID INTEGER NOT NULL,
	PRIMARY KEY (PK_FIRST_ID, PK_SECOND_ID),
	UNIQUE (PK_SECOND_ID, REFERRED_ID),
	UNIQUE (PK_SECOND_ID, PK_FIRST_ID)
) ;

create table WHITE_COMPOUND_PK_REF (
	MULTIPLE_FIRST_ID INTEGER NOT NULL,
	MULTIPLE_SECOND_ID INTEGER NOT NULL,
	REF_FIRST_ID INTEGER NOT NULL,
	REF_SECOND_ID INTEGER NOT NULL,
	REF_NAME VARCHAR(50) NOT NULL,
	PRIMARY KEY (MULTIPLE_FIRST_ID, MULTIPLE_SECOND_ID)
) ;

alter table WHITE_COMPOUND_PK_REF add constraint FK_WHITE_COMPOUND_PK_REF
	foreign key (REF_FIRST_ID, REF_SECOND_ID)
	REFERENCES WHITE_COMPOUND_PK (PK_FIRST_ID, PK_SECOND_ID) ;

-- for the test of multiple compound key
create table WHITE_COMPOUND_PK_REF_NEST (
	COMPOUND_PK_REF_NEST_ID INTEGER NOT NULL PRIMARY KEY,
	FOO_MULTIPLE_ID INTEGER NOT NULL,
	BAR_MULTIPLE_ID INTEGER NOT NULL,
	QUX_MULTIPLE_ID INTEGER NOT NULL,
	NEST_NAME VARCHAR(50) NOT NULL
) ;

alter table WHITE_COMPOUND_PK_REF_NEST add constraint FK_WHITE_COMPOUND_PK_REF_NEST_FOO_BAR
	foreign key (FOO_MULTIPLE_ID, BAR_MULTIPLE_ID)
	REFERENCES WHITE_COMPOUND_PK_REF (MULTIPLE_FIRST_ID, MULTIPLE_SECOND_ID) ;

alter table WHITE_COMPOUND_PK_REF_NEST add constraint FK_WHITE_COMPOUND_PK_REF_NEST_BAR_QUX
	foreign key (BAR_MULTIPLE_ID, QUX_MULTIPLE_ID)
	REFERENCES WHITE_COMPOUND_PK_REF (MULTIPLE_FIRST_ID, MULTIPLE_SECOND_ID) ;

-- for the test of correlated fixed condition
create table WHITE_COMPOUND_PK_REF_MANY (
	MULTIPLE_FIRST_ID INTEGER NOT NULL,
	MULTIPLE_SECOND_ID INTEGER NOT NULL,
	REF_MANY_FIRST_ID INTEGER NOT NULL,
	REF_MANY_SECOND_ID INTEGER NOT NULL,
	REF_MANY_CODE CHAR(3) NOT NULL,
	REF_MANY_NAME VARCHAR(50) NOT NULL,
	REF_MANY_DATETIME DATETIME NOT NULL,
	PRIMARY KEY (MULTIPLE_FIRST_ID, MULTIPLE_SECOND_ID)
) ;

create table WHITE_COMPOUND_PK_WRONG_ORDER (
	FIRST_ID INTEGER NOT NULL,
	SECOND_ID INTEGER NOT NULL,
	THIRD_ID INTEGER NOT NULL,
	WRONG_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (SECOND_ID, THIRD_ID, FIRST_ID)
) ;

-- /= = = = = = = = = = = = = = = 
-- for the test of unique-key FK
-- = = = = = = = = = =/
create table WHITE_UQ_FK (
	UQ_FK_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	UQ_FK_CODE CHAR(3) NOT NULL,
	UNIQUE (UQ_FK_CODE)
) ;

create table WHITE_UQ_FK_REF (
	UQ_FK_REF_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	FK_TO_PK_ID NUMERIC(16) NOT NULL,
	FK_TO_UQ_CODE CHAR(3) NOT NULL,
	COMPOUND_UQ_FIRST_CODE CHAR(3) NOT NULL,
	COMPOUND_UQ_SECOND_CODE CHAR(3) NOT NULL,
	UNIQUE (COMPOUND_UQ_FIRST_CODE, COMPOUND_UQ_SECOND_CODE)
) ;

alter table WHITE_UQ_FK_REF add constraint FK_WHITE_UQ_FK_REF_PK
	foreign key (FK_TO_PK_ID) references WHITE_UQ_FK (UQ_FK_ID) ;

alter table WHITE_UQ_FK_REF add constraint FK_WHITE_UQ_FK_REF_UQ
	foreign key (FK_TO_UQ_CODE) references WHITE_UQ_FK (UQ_FK_CODE) ;

-- for the test of compound unique key
-- and the test of same name key
create table WHITE_UQ_FK_REF_NEST (
	UQ_FK_REF_NEST_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	COMPOUND_UQ_FIRST_CODE CHAR(3) NOT NULL,
	COMPOUND_UQ_SECOND_CODE CHAR(3) NOT NULL
) ;

alter table WHITE_UQ_FK_REF_NEST add constraint FK_WHITE_UQ_FK_REF_NEST_UQ
	foreign key (COMPOUND_UQ_FIRST_CODE, COMPOUND_UQ_SECOND_CODE)
	REFERENCES WHITE_UQ_FK_REF (COMPOUND_UQ_FIRST_CODE, COMPOUND_UQ_SECOND_CODE) ;

create table WHITE_UQ_FK_WITHOUT_PK (
	UQ_FK_CODE CHAR(3) NOT NULL,
	UQ_FK_NAME VARCHAR(64) NOT NULL,
	UNIQUE (UQ_FK_CODE)
) ;

create table WHITE_UQ_FK_WITHOUT_PK_REF (
	UQ_FK_REF_ID NUMERIC(16) NOT NULL,
	FK_TO_UQ_CODE CHAR(3) NOT NULL
) ;

alter table WHITE_UQ_FK_WITHOUT_PK_REF add constraint FK_WHITE_UQ_FK_WITHOUT_PK_REF
	foreign key (FK_TO_UQ_CODE) references WHITE_UQ_FK_WITHOUT_PK (UQ_FK_CODE) ;

-- /= = = = = = = = = = = = = = = = = = = = = = 
-- for the test of unique key as classification
-- = = = = = = = = = =/
create table WHITE_UQ_CLASSIFICATION (
	UQ_CLS_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	UQ_CLS_CODE CHAR(3) NOT NULL,
	UNIQUE (UQ_CLS_CODE)
) ;

create table WHITE_UQ_CLASSIFICATION_FLG (
	UQ_CLS_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	UQ_CLS_FLG INTEGER NOT NULL,
	UNIQUE (UQ_CLS_FLG)
) ;

create table WHITE_UQ_CLASSIFICATION_FLG_BIT (
	UQ_CLS_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	UQ_CLS_NAME VARCHAR(30) NOT NULL,
	UQ_CLS_BIT_FLG BIT NOT NULL,
	UNIQUE (UQ_CLS_NAME, UQ_CLS_BIT_FLG)
) ;

create table WHITE_UQ_CLASSIFICATION_FLG_PART (
	UQ_CLS_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	UQ_CLS_NAME VARCHAR(30) NOT NULL,
	UQ_CLS_FLG INTEGER NOT NULL,
	UNIQUE (UQ_CLS_NAME, UQ_CLS_FLG)
) ;

-- /= = = = = = = = = = = = = = = = = = 
-- for the test of no-primary-key table
-- = = = = = = = = = =/
create table WHITE_NO_PK (
	NO_PK_ID NUMERIC(16) NOT NULL,
	NO_PK_NAME VARCHAR(32),
	NO_PK_INTEGER INTEGER
) ;

create table WHITE_NO_PK_COMMON_COLUMN (
	NO_PK_ID NUMERIC(16) NOT NULL,
	NO_PK_NAME VARCHAR(32),
	NO_PK_INTEGER INTEGER,
    REGISTER_DATETIME DATETIME NOT NULL COMMENT '登録日時: レコードが登録された日時。共通カラムの一つ。',
    REGISTER_USER VARCHAR(200) NOT NULL COMMENT '登録ユーザ: レコードを登録したユーザ。共通カラムの一つ。',
    UPDATE_DATETIME DATETIME NOT NULL COMMENT '更新日時: レコードが（最後に）更新された日時。共通カラムの一つ。',
    UPDATE_USER VARCHAR(200) NOT NULL COMMENT '更新ユーザ: レコードを更新したユーザ。'
) ;

CREATE VIEW WHITE_NO_PK_RELATION as
select product.PRODUCT_ID
     , product.PRODUCT_NAME
     , product.PRODUCT_HANDLE_CODE
     , product.PRODUCT_STATUS_CODE
     , (select max(purchase.PURCHASE_DATETIME)
          from PURCHASE purchase
         where purchase.PRODUCT_ID = product.PRODUCT_ID
       ) as LATEST_PURCHASE_DATETIME
  from PRODUCT product
;

-- /= = = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of variant relation (biz-many-to-one)
-- = = = = = = = = = =/
create table WHITE_VARIANT_RELATION_MASTER_FOO(
    MASTER_FOO_ID BIGINT NOT NULL,
    MASTER_FOO_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (MASTER_FOO_ID)
);

create table WHITE_VARIANT_RELATION_MASTER_BAR(
    MASTER_BAR_ID BIGINT NOT NULL,
    MASTER_BAR_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (MASTER_BAR_ID)
);

create table WHITE_VARIANT_RELATION_MASTER_QUX(
    MASTER_QUX_ID BIGINT NOT NULL,
    MASTER_QUX_NAME VARCHAR(200) NOT NULL,
    QUX_TYPE_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (MASTER_QUX_ID)
);

create table WHITE_VARIANT_RELATION_MASTER_CORGE(
    MASTER_CORGE_ID BIGINT NOT NULL,
    MASTER_CORGE_NAME VARCHAR(200) NOT NULL,
    CORGE_TYPE_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (MASTER_CORGE_ID)
);

create table WHITE_VARIANT_RELATION_REFERRER(
    REFERRER_ID BIGINT NOT NULL,
    VARIANT_MASTER_ID BIGINT NOT NULL,
    MASTER_TYPE_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (REFERRER_ID)
);

create table WHITE_VARIANT_RELATION_REFERRER_REF(
    REF_ID BIGINT NOT NULL,
    REFERRER_ID BIGINT NOT NULL,
    PRIMARY KEY (REF_ID)
);

alter table WHITE_VARIANT_RELATION_REFERRER_REF add constraint FK_WHITE_VARIANT_RELATION_REFERRER_REF
	foreign key (REFERRER_ID) references WHITE_VARIANT_RELATION_REFERRER (REFERRER_ID) ;

create table WHITE_VARIANT_RELATION_LOCAL_PK_REFERRER(
    REVERSEFK_SUPPRESSED_ID BIGINT NOT NULL,
    MASTER_TYPE_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (REVERSEFK_SUPPRESSED_ID)
);

-- /= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of all-in-one table classification (biz-many-to-one)
-- = = = = = = = = = =/
create table WHITE_ALL_IN_ONE_CLS_CATEGORY (
	CLS_CATEGORY_CODE CHAR(3) NOT NULL,
	CLS_CATEGORY_NAME VARCHAR(20) NOT NULL,
	DESCRIPTION VARCHAR(200) NOT NULL,
	PRIMARY KEY (CLS_CATEGORY_CODE)
) ;

create table WHITE_ALL_IN_ONE_CLS_ELEMENT (
	CLS_CATEGORY_CODE CHAR(3) NOT NULL,
	CLS_ELEMENT_CODE CHAR(3) NOT NULL,
	CLS_ELEMENT_NAME VARCHAR(20) NOT NULL,
	ATTRIBUTE_EXP TEXT NOT NULL,
	PRIMARY KEY (CLS_CATEGORY_CODE, CLS_ELEMENT_CODE)
) ;

alter table WHITE_ALL_IN_ONE_CLS_ELEMENT add constraint FK_WHITE_ALL_IN_ONE_CLS_ELEMENT_CATEGORY
	foreign key (CLS_CATEGORY_CODE)
	REFERENCES WHITE_ALL_IN_ONE_CLS_CATEGORY (CLS_CATEGORY_CODE) ;

-- also for the test of no implicit reverse FK
create table WHITE_ALL_IN_ONE_CLS_COMPOUND_PK_REF (
	FOO_CODE CHAR(3) NOT NULL,
	BAR_CODE CHAR(3) NOT NULL,
	QUX_CODE CHAR(3) NOT NULL,
	PRIMARY KEY (FOO_CODE, BAR_CODE, QUX_CODE)
) ;

create table WHITE_ALL_IN_ONE_CLS_NORMAL_COL_REF (
	CLS_REF_ID INTEGER NOT NULL,
	FOO_CODE CHAR(3) NOT NULL,
	BAR_CODE CHAR(3) NOT NULL,
	QUX_CODE CHAR(3) NOT NULL,
	PRIMARY KEY (FOO_CODE, BAR_CODE, QUX_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of columnExcept on databaseInfoMap.dfprop
-- = = = = = = = = = =/
create table WHITE_COLUMN_EXCEPT (
	EXCEPT_COLUMN_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	COLUMN_EXCEPT_TEST INTEGER -- actually NOT except
) ;

create table WHITE_COLUMN_EXCEPT_GEN_ONLY (
	GEN_ONLY_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	GEN_ONLY_NAME VARCHAR(200) NOT NULL,
	THIS_IS_GEN_ONLY VARCHAR(200)
) ;

-- /= = = = = = = = = = = = = = = = = = =
-- for the test of MyselfInScopeSubQuery
-- = = = = = = = = = =/
create table WHITE_MYSELF (
	MYSELF_ID integer NOT NULL PRIMARY KEY,
	MYSELF_NAME varchar(80) NOT NULL
) ;

create table WHITE_MYSELF_CHECK ( 
	MYSELF_CHECK_ID integer NOT NULL PRIMARY KEY,
	MYSELF_CHECK_NAME varchar(80) NOT NULL,
	MYSELF_ID integer
) ;

alter table WHITE_MYSELF_CHECK add constraint FK_WHITE_MYSELF_CHECK_SELF
	foreign key (MYSELF_ID) references WHITE_MYSELF (MYSELF_ID) ;

-- /= = = = = = = = = = = = = = = = = = 
-- for the test of self reference
-- = = = = = = = = = =/
create table WHITE_SELF_REFERENCE (
	SELF_REFERENCE_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	SELF_REFERENCE_NAME VARCHAR(200) NOT NULL,
	PARENT_ID NUMERIC(16)
) ;

alter table WHITE_SELF_REFERENCE add constraint FK_WHITE_SELF_REFERENCE_PARENT
	foreign key (PARENT_ID) references WHITE_SELF_REFERENCE (SELF_REFERENCE_ID) ;

create table WHITE_SELF_REFERENCE_REF_ONE (
	SELF_REFERENCE_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	SELF_REFERENCE_REF_ONE_NAME VARCHAR(200) NOT NULL
) ;

alter table WHITE_SELF_REFERENCE_REF_ONE add constraint FK_WHITE_SELF_REFERENCE_REF_ONE
	foreign key (SELF_REFERENCE_ID) references WHITE_SELF_REFERENCE (SELF_REFERENCE_ID) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of binary data type (e.g. image file)
-- = = = = = = = = = =/
create table WHITE_BINARY (
	BINARY_ID BIGINT AUTO_INCREMENT NOT NULL,
	BINARY_DATA BINARY,
	BLOB_DATA BLOB,
	PRIMARY KEY (BINARY_ID)
) ;

-- =======================================================================================
--                                                                        DBFlute Property
--                                                                        ================
-- /= = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of grouping map for classification
-- = = = = = = = = = =/
create table WHITE_GROUPING_REFERENCE(
    GROUPING_REFERENCE_ID BIGINT AUTO_INCREMENT NOT NULL,
    GROUPING_REFERENCE_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (GROUPING_REFERENCE_ID)
);

-- /= = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of implicit conversion foreign key
-- = = = = = = = = = =/
create table WHITE_IMPLICIT_CONV_INTEGER (
	IMPLICIT_CONV_INTEGER_ID INTEGER NOT NULL,
	IMPLICIT_CONV_NUMERIC_ID INTEGER NOT NULL,
	IMPLICIT_CONV_STRING_ID INTEGER NOT NULL,
	IMPLICIT_CONV_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (IMPLICIT_CONV_INTEGER_ID)
) ;

create table WHITE_IMPLICIT_CONV_NUMERIC (
	IMPLICIT_CONV_NUMERIC_ID NUMERIC(20, 0) NOT NULL,
	IMPLICIT_CONV_INTEGER_ID NUMERIC(20, 0) NOT NULL,
	IMPLICIT_CONV_STRING_ID NUMERIC(20, 0) NOT NULL,
	IMPLICIT_CONV_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (IMPLICIT_CONV_NUMERIC_ID)
) ;

create table WHITE_IMPLICIT_CONV_STRING (
	IMPLICIT_CONV_STRING_ID VARCHAR(10) NOT NULL,
	IMPLICIT_CONV_INTEGER_ID VARCHAR(10) NOT NULL,
	IMPLICIT_CONV_NUMERIC_ID VARCHAR(10) NOT NULL,
	IMPLICIT_CONV_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (IMPLICIT_CONV_STRING_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of suppress join/subquery foreign key
-- = = = = = = = = = =/
create table WHITE_SUPPRESS_JOIN_SQ (
	SUPPRESS_JOIN_SQ_ID INTEGER NOT NULL,
	SUPPRESS_JOIN_SQ_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (SUPPRESS_JOIN_SQ_ID)
) ;

create table WHITE_SUPPRESS_JOIN_SQ_MANY (
	MANY_ID INTEGER NOT NULL,
	MANY_NAME VARCHAR(200) NOT NULL,
	SUPPRESS_JOIN_SQ_ID INTEGER NOT NULL,
	MANY_ONE_ID INTEGER NOT NULL,
	PRIMARY KEY (MANY_ID)
) ;

create table WHITE_SUPPRESS_JOIN_SQ_MANY_ONE (
	MANY_ONE_ID INTEGER NOT NULL,
	MANY_ONE_NAME VARCHAR(200) NOT NULL,
	MANY_ONE_ONE_ID INTEGER NOT NULL,
	PRIMARY KEY (MANY_ONE_ID)
) ;

create table WHITE_SUPPRESS_JOIN_SQ_MANY_ONE_ONE (
	MANY_ONE_ONE_ID INTEGER NOT NULL,
	MANY_ONE_ONE_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (MANY_ONE_ONE_ID)
) ;

create table WHITE_SUPPRESS_JOIN_SQ_ONE (
	ONE_ID INTEGER NOT NULL,
	ONE_NAME VARCHAR(200) NOT NULL,
	SUPPRESS_JOIN_SQ_ID INTEGER NOT NULL,
	ONE_ADDI_ID INTEGER NOT NULL,
	PRIMARY KEY (ONE_ID),
	UNIQUE (SUPPRESS_JOIN_SQ_ID)
) ;

create table WHITE_SUPPRESS_JOIN_SQ_ONE_ADDI (
	ONE_ADDI_ID INTEGER NOT NULL,
	ONE_ADDI_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (ONE_ADDI_ID)
) ;

alter table WHITE_SUPPRESS_JOIN_SQ_MANY add constraint FK_WHITE_SUPPRESS_JOIN_SQ_MANY
	foreign key (SUPPRESS_JOIN_SQ_ID) references WHITE_SUPPRESS_JOIN_SQ (SUPPRESS_JOIN_SQ_ID) ;

alter table WHITE_SUPPRESS_JOIN_SQ_MANY add constraint FK_WHITE_SUPPRESS_JOIN_SQ_MANY_ONE
	foreign key (MANY_ONE_ID) references WHITE_SUPPRESS_JOIN_SQ_MANY_ONE (MANY_ONE_ID) ;

alter table WHITE_SUPPRESS_JOIN_SQ_MANY_ONE add constraint FK_WHITE_SUPPRESS_JOIN_SQ_MANY_ONE_ONE
	foreign key (MANY_ONE_ONE_ID) references WHITE_SUPPRESS_JOIN_SQ_MANY_ONE_ONE (MANY_ONE_ONE_ID) ;

alter table WHITE_SUPPRESS_JOIN_SQ_ONE add constraint FK_WHITE_SUPPRESS_JOIN_SQ_ONE
	foreign key (SUPPRESS_JOIN_SQ_ID) references WHITE_SUPPRESS_JOIN_SQ (SUPPRESS_JOIN_SQ_ID) ;

-- /= = = = = = = = = = = = = = = = = = = = =
-- for the test of table except generate-only
-- = = = = = = = = = =/
-- except completely (no getting meta data)
create table WHITE_TABLE_EXCEPT_NOMETA (
	NOMETA_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	NOMETA_NAME VARCHAR(200) NOT NULL
) ;

-- generated referred from generate-only table
create table WHITE_TABLE_EXCEPT_GEN_HEAD (
	GEN_HEAD_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	GEN_HEAD_NAME VARCHAR(200) NOT NULL,
	NOMETA_ID NUMERIC(16)
) ;

alter table WHITE_TABLE_EXCEPT_GEN_HEAD add constraint FK_WHITE_TABLE_EXCEPT_GEN_HEAD_NOMETA
	foreign key (NOMETA_ID) references WHITE_TABLE_EXCEPT_NOMETA (NOMETA_ID) ;

-- except generate-only referring generated table
create table WHITE_TABLE_EXCEPT_GEN_ONLY (
	GEN_ONLY_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	GEN_ONLY_NAME VARCHAR(200) NOT NULL,
	GEN_HEAD_ID NUMERIC(16),
	NOMETA_ID NUMERIC(16)
) ;

alter table WHITE_TABLE_EXCEPT_GEN_ONLY add constraint FK_WHITE_TABLE_EXCEPT_GEN_ONLY_HEAD
	foreign key (GEN_HEAD_ID) references WHITE_TABLE_EXCEPT_GEN_HEAD (GEN_HEAD_ID) ;

alter table WHITE_TABLE_EXCEPT_GEN_ONLY add constraint FK_WHITE_TABLE_EXCEPT_GEN_ONLY_NOMETA
	foreign key (NOMETA_ID) references WHITE_TABLE_EXCEPT_NOMETA (NOMETA_ID) ;

-- generated referring except gen-only
create table WHITE_TABLE_EXCEPT_GEN_REF (
	GEN_REF_ID NUMERIC(16) NOT NULL PRIMARY KEY,
	GEN_REF_NAME VARCHAR(200) NOT NULL,
	GEN_ONLY_ID NUMERIC(16)
) ;

alter table WHITE_TABLE_EXCEPT_GEN_REF add constraint FK_WHITE_TABLE_EXCEPT_GEN_REF_ONLY
	foreign key (GEN_ONLY_ID) references WHITE_TABLE_EXCEPT_GEN_ONLY (GEN_ONLY_ID) ;

-- /= = = = = = = = = = = = = = = = = =
-- for the test of point type-mapping
-- = = = = = = = = = =/
create table WHITE_POINT_TYPE_MAPPING (
	POINT_TYPE_MAPPING_ID NUMERIC(16) NOT NULL,
	POINT_TYPE_MAPPING_MEMBER_NAME VARCHAR(32),
	POINT_TYPE_MAPPING_PRICE_COUNT INTEGER,
	POINT_TYPE_MAPPING_SALE_DATE BIGINT,
	POINT_TYPE_MAPPING_WANTED_DATETIME DATETIME,
	PRIMARY KEY (POINT_TYPE_MAPPING_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of include query of condition-bean 
-- = = = = = = = = = =/
create table WHITE_INCLUDE_QUERY (
	INCLUDE_QUERY_ID BIGINT AUTO_INCREMENT NOT NULL,
	INCLUDE_QUERY_VARCHAR VARCHAR(100),
	INCLUDE_QUERY_INTEGER INTEGER,
	INCLUDE_QUERY_DATE DATE,
	INCLUDE_QUERY_DATETIME DATETIME,
	PRIMARY KEY (INCLUDE_QUERY_ID)
) ;

-- /= = = = = = = = = = = = = = =
-- for the test of geared cipher 
-- = = = = = = = = = =/
create table WHITE_GEARED_CIPHER (
	CIPHER_ID BIGINT AUTO_INCREMENT NOT NULL,
	CIPHER_INTEGER VARCHAR(100),
	CIPHER_VARCHAR VARCHAR(100),
	CIPHER_DATE VARCHAR(100),
	CIPHER_DATETIME VARCHAR(100),
	PRIMARY KEY (CIPHER_ID)
) ;

-- =======================================================================================
--                                                                           ReplaceSchema
--                                                                           =============
-- /= = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of ReplaceSchema delimiter data loading
-- = = = = = = = = = =/
create table WHITE_DELIMITER (
	DELIMITER_ID BIGINT AUTO_INCREMENT NOT NULL,
	NUMBER_NULLABLE INTEGER,
	STRING_CONVERTED VARCHAR(200),
	STRING_NON_CONVERTED VARCHAR(200),
	DATE_DEFAULT DATE NOT NULL,
	PRIMARY KEY (DELIMITER_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of ReplaceSchema excel data loading
-- = = = = = = = = = =/
create table WHITE_XLS_MAN (
	XLS_MAN_ID BIGINT AUTO_INCREMENT NOT NULL,
	STRING_CONVERTED VARCHAR(200),
	TIMESTAMP_CONVERTED DATETIME,
	PRIMARY KEY (XLS_MAN_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of ReplaceSchema logging for LoadData
-- = = = = = = = = = =/
create table WHITE_LOAD_DATA (
	LOAD_DATA_ID BIGINT NOT NULL,
	LOAD_DATA_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (LOAD_DATA_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of ReplaceSchema logging for LoadData
-- = = = = = = = = = =/
create table WHITE_SUPPRESS_DEF_CHECK (
	DEF_CHECK_ID BIGINT NOT NULL,
	DEF_CHECK_NAME VARCHAR(200) NOT NULL,
	PRIMARY KEY (DEF_CHECK_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of date adjustment for LoadData
-- = = = = = = = = = =/
create table WHITE_DATE_ADJUSTMENT (
	DATE_ADJUSTMENT_ID BIGINT NOT NULL,
	ADJUSTED_DATE DATE COMMENT 'adjusted',
	ADJUSTED_DATETIME DATETIME,
	ADJUSTED_TIME TIME,
	ADJUSTED_INTEGER INTEGER,
	ADJUSTED_NAMED_STRING_LONG BIGINT COMMENT 'adjusted',
	ADJUSTED_NAMED_TYPED_LONG BIGINT COMMENT 'adjusted',
	ADJUSTED_PINPOINT_STRING_LONG BIGINT COMMENT 'adjusted',
	ADJUSTED_PINPOINT_TYPED_LONG BIGINT COMMENT 'adjusted',
	ADJUSTED_PLAIN_LONG BIGINT,
	ADJUSTED_STRING VARCHAR(32),
	PRIMARY KEY (DATE_ADJUSTMENT_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of large text file
-- = = = = = = = = = =/
create table WHITE_LARGE_TEXT_FILE (
	LARGE_TEXT_FILE_ID BIGINT NOT NULL,
	LARGE_TEXT_FILE_TEXT TEXT,
	PRIMARY KEY (LARGE_TEXT_FILE_ID)
) ;

-- /= = = = = = = = = = = = = = = = = = = =
-- for the test of over relation non cache
-- = = = = = = = = = =/

create table WHITE_PERROTTA_OVER_MEMBER(
    MEMBER_ID INTEGER NOT NULL,
    MEMBER_NAME VARCHAR(180) NOT NULL,
    PRODUCT_ID INTEGER NOT NULL,
    TRACE_TYPE_CODE CHAR(3) NOT NULL,
    MACHO_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (MEMBER_ID)
);

create table WHITE_PERROTTA_OVER_MEMBER_MACHO(
    MACHO_CODE CHAR(3) NOT NULL,
    MACHO_NAME VARCHAR(200) NOT NULL,
    PRIMARY KEY (MACHO_CODE)
);

create table WHITE_PERROTTA_OVER_PRODUCT(
    PRODUCT_ID INTEGER NOT NULL,
    PRODUCT_NAME VARCHAR(50) NOT NULL,
    PRODUCT_NESTED_CODE CHAR(3) NOT NULL,
    PRIMARY KEY (PRODUCT_ID)
);

create table WHITE_PERROTTA_OVER_PRODUCT_NESTED(
    PRODUCT_NESTED_CODE CHAR(3) NOT NULL,
    PRODUCT_NESTED_NAME VARCHAR(50) NOT NULL,
    PRIMARY KEY (PRODUCT_NESTED_CODE)
);

create table WHITE_PERROTTA_OVER_TRACE(
    TRACE_ID BIGINT NOT NULL,
    PREVIOUS_PRODUCT_ID INTEGER NOT NULL,
    NEXT_PRODUCT_ID INTEGER NOT NULL,
    TRACE_TYPE_CODE CHAR(3) NOT NULL,
    UNIQUE (PREVIOUS_PRODUCT_ID, NEXT_PRODUCT_ID),
    PRIMARY KEY (TRACE_ID)
);

alter table WHITE_PERROTTA_OVER_MEMBER add constraint FK_WHITE_PERROTTA_OVER_MEMBER_PRODUCT
	foreign key (PRODUCT_ID) references WHITE_PERROTTA_OVER_PRODUCT (PRODUCT_ID) ;

alter table WHITE_PERROTTA_OVER_MEMBER add constraint FK_WHITE_PERROTTA_OVER_MEMBER_MACHO
	foreign key (MACHO_CODE) references WHITE_PERROTTA_OVER_MEMBER_MACHO (MACHO_CODE) ;

alter table WHITE_PERROTTA_OVER_PRODUCT add constraint FK_WHITE_PERROTTA_OVER_PRODUCT_NESTED
	foreign key (PRODUCT_NESTED_CODE) references WHITE_PERROTTA_OVER_PRODUCT_NESTED (PRODUCT_NESTED_CODE) ;

alter table WHITE_PERROTTA_OVER_TRACE add constraint FK_WHITE_PERROTTA_OVER_TRACE_PREVIOUS
	foreign key (PREVIOUS_PRODUCT_ID) references WHITE_PERROTTA_OVER_PRODUCT (PRODUCT_ID) ;

alter table WHITE_PERROTTA_OVER_TRACE add constraint FK_WHITE_PERROTTA_OVER_TRACE_NEXT
	foreign key (NEXT_PRODUCT_ID) references WHITE_PERROTTA_OVER_PRODUCT (PRODUCT_ID) ;


-- =======================================================================================
--                                                                           Irrgular Case
--                                                                           =============
-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of escaped dfprop
-- = = = = = = = = = =/
create table WHITE_ESCAPED_DFPROP (
	ESCAPED_DFPROP_CODE CHAR(3) NOT NULL,
	ESCAPED_DFPROP_NAME VARCHAR(20),
	PRIMARY KEY (ESCAPED_DFPROP_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of escaped java-doc comment
-- = = = = = = = = = =/
create table WHITE_ESCAPED_JAVA_DOC (
	ESCAPED_JAVA_DOC_CODE CHAR(3) NOT NULL,
	ESCAPED_JAVA_DOC_NAME VARCHAR(20),
	PRIMARY KEY (ESCAPED_JAVA_DOC_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of escaped number initial name
-- = = = = = = = = = =/
create table WHITE_ESCAPED_NUMBER_INITIAL (
	NUMBER_INITIAL_CODE CHAR(3) NOT NULL,
	NUMBER_INITIAL_NAME VARCHAR(20),
	PRIMARY KEY (NUMBER_INITIAL_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of line separator comment
-- = = = = = = = = = =/
create table WHITE_LINE_SEP_COMMENT (
	LINE_SEP_COMMENT_CODE CHAR(3) NOT NULL,
	LINE_SEP_COMMENT_NAME VARCHAR(20),
	PRIMARY KEY (LINE_SEP_COMMENT_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of no camelzing classification
-- = = = = = = = = = =/
create table WHITE_CLS_NO_CAMELIZING (
	NO_CAMELIZING_CODE VARCHAR(10) NOT NULL,
	NO_CAMELIZING_NAME VARCHAR(20),
	PRIMARY KEY (NO_CAMELIZING_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of deprecated classification element
-- = = = = = = = = = =/
create table WHITE_DEPRECATED_CLS_ELEMENT (
	DEPRECATED_CLS_ELEMENT_CODE CHAR(3) NOT NULL,
	DEPRECATED_CLS_ELEMENT_NAME VARCHAR(20),
	PRIMARY KEY (DEPRECATED_CLS_ELEMENT_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = = = = = = =
-- for the test of DB comment
-- = = = = = = = = = =/
create table WHITE_DB_COMMENT (
	DB_COMMENT_CODE CHAR(3) NOT NULL COMMENT 'Normal: this is normal comment.',
	DB_COMMENT_NAME VARCHAR(20) COMMENT 'JavaDocHeadache: e.g. /*BEGIN*/, @ @foo <br>',
	PRIMARY KEY (DB_COMMENT_CODE)
) ;

-- /= = = = = = = = = = = = = = = = = = = =
-- for the test of duplicate stiletto alias
-- = = = = = = = = = =/
create table WHITE_STILETTO_ALIAS (
	STILETTO_ALIAS_ID INTEGER NOT NULL PRIMARY KEY,
	FOO VARCHAR(200),
	FOO_0 VARCHAR(200),
	FOO_1 VARCHAR(200),
	FOO2 VARCHAR(200),
	FOO_3 VARCHAR(200),
	FOO4 VARCHAR(200),
	BAR VARCHAR(200),
	QUX VARCHAR(200)
) ;

create table WHITE_STILETTO_ALIAS_REF (
	REF_ID INTEGER NOT NULL PRIMARY KEY,
	FOO0 VARCHAR(200),
	FOO_1 VARCHAR(200),
	FOO2 VARCHAR(200),
	FOO3 VARCHAR(200),
	FOO_4 VARCHAR(200),
	BAR_0 VARCHAR(200),
	QUX_0 VARCHAR(200),
	c21 VARCHAR(200),
	c22 VARCHAR(200),
	c23 VARCHAR(200),
	STILETTO_ALIAS_ID INTEGER
) ;

alter table WHITE_STILETTO_ALIAS_REF add constraint FK_WHITE_STILETTO_ALIAS_REF
	foreign key (STILETTO_ALIAS_ID) references WHITE_STILETTO_ALIAS (STILETTO_ALIAS_ID) ;

-- /= = = = = = = = = = = = = = = = = = 
-- for the test of quoted table name
-- = = = = = = = = = =/
create table WHITE_QUOTED (
	`SELECT` INTEGER NOT NULL PRIMARY KEY,
	`FROM` VARCHAR(200)
) ;

create table WHITE_QUOTED_REF (
	`WHERE` INTEGER NOT NULL PRIMARY KEY,
	`ORDER` INTEGER
) ;

alter table WHITE_QUOTED_REF add constraint FK_WHITE_QUOTED_REF
	foreign key (`ORDER`) references WHITE_QUOTED (`SELECT`) ;

-- /= = = = = = = = = = = = = = = = = = = =
-- for the test of program reservation word
-- = = = = = = = = = =/
create table WHITE_PG_RESERV (
	CLASS integer NOT NULL PRIMARY KEY,
	`CASE` integer,
	PACKAGE integer,
	`DEFAULT` integer,
	NEW integer,
	NATIVE integer,
	VOID integer,
	PUBLIC integer,
	PROTECTED integer,
	PRIVATE integer,
	INTERFACE integer,
	ABSTRACT integer,
	FINAL integer,
	FINALLY integer,
	`RETURN` integer,
	`DOUBLE` integer,
	`FLOAT` integer,
	SHORT integer,
	TYPE char(3),
	RESERV_NAME varchar(32) NOT NULL
) ;

create table WHITE_PG_RESERV_REF (
	REF_ID integer NOT NULL PRIMARY KEY,
	CLASS integer
) ;

alter table WHITE_PG_RESERV_REF add constraint FK_WHITE_PG_RESERV_REF_CLASS
	foreign key (CLASS) references WHITE_PG_RESERV (CLASS) ;
