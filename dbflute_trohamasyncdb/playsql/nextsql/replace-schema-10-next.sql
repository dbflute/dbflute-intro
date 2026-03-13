
CREATE TABLE NEXT_SCHEMA_PRODUCT
(
	PRODUCT_ID BIGINT AUTO_INCREMENT NOT NULL,
	PRODUCT_NAME VARCHAR(200) NOT NULL,
	COLUMN_EXCEPT_TEST INTEGER,
	COLUMN_EXCEPT_GEN_ONLY_TEST INTEGER,
	REGISTER_DATETIME DATETIME NOT NULL,
	REGISTER_USER VARCHAR(200) NOT NULL,
	REGISTER_PROCESS VARCHAR(200) NOT NULL,
	UPDATE_DATETIME DATETIME NOT NULL,
	UPDATE_USER VARCHAR(200) NOT NULL,
	UPDATE_PROCESS VARCHAR(200) NOT NULL,
	PRIMARY KEY (PRODUCT_ID)
) COMMENT='隣の商品:' ;

-- #df:begin#
create procedure SP_NEXT_NO_PARAMETER()
begin
end;
-- #df:end#

-- #df:begin#
create procedure SP_NEXT_IN_OUT_PARAMETER(
      in v_in_varchar varchar(32)
    , out v_out_varchar varchar(32)
    , inout v_inout_varchar varchar(32)
)
begin
  set v_out_varchar = 'ddd';
  set v_inout_varchar = 'eee';
end;
-- #df:end#
