
-- =======================================================================================
--                                                                                   Basic
--                                                                                   =====
-- #df:begin#
create procedure SP_NO_PARAMETER()
begin
end;
-- #df:end#

-- #df:begin#
create procedure SP_IN_OUT_PARAMETER(
      in v_in_varchar varchar(32)
    , out v_out_varchar varchar(32)
    , inout v_inout_varchar varchar(32)
)
begin
  set v_out_varchar = v_inout_varchar;
  set v_inout_varchar = v_in_varchar;
end;
-- #df:end#

-- #df:begin#
create procedure SP_VARIOUS_TYPE_PARAMETER(
      in v_in_varchar varchar(32)
    , out v_out_varchar varchar(32)
    , out v_out_char char(3)
    , in v_in_text text
    , out v_out_text text
    , in vv_in_numeric_integer numeric(5, 0)
    , in vv_in_numeric_bigint numeric(12, 0)
    , in vv_in_numeric_decimal numeric(5, 3)
    , out vv_out_decimal decimal
    , out vv_out_integer integer
    , inout vv_inout_integer integer
    , out vv_out_bigint bigint
    , inout vv_inout_bigint bigint
    , in vvv_in_date date
    , out vvv_out_datetime datetime
)
begin
  set v_out_varchar = v_in_varchar;
  set v_out_char = 'qux';
  set v_out_text = v_in_text;
  set vv_out_decimal = 987.654;
  set vv_out_bigint = vv_inout_integer;
  set vv_inout_integer = vv_inout_bigint;
  set vv_inout_bigint = vv_out_integer;
  set vv_out_integer = 6789;
  set vvv_out_datetime = current_timestamp;
end;
-- #df:end#

-- =======================================================================================
--                                                                        Return ResultSet
--                                                                        ================
-- #df:begin#
create procedure SP_RETURN_RESULT_SET()
begin
  select MEMBER_ID, MEMBER_NAME, BIRTHDATE, FORMALIZED_DATETIME, MEMBER_STATUS_CODE
    from MEMBER;
end;
-- #df:end#

-- #df:begin#
create procedure SP_RETURN_RESULT_SET_MORE()
begin
  select MEMBER_ID, MEMBER_NAME, BIRTHDATE, FORMALIZED_DATETIME, MEMBER_STATUS_CODE
    from MEMBER;
  select * from MEMBER_STATUS;
end;
-- #df:end#

-- #df:begin#
create procedure SP_RETURN_RESULT_SET_WITH(
      in v_in_char char(3)
    , out v_out_varchar varchar(32)
    , inout v_inout_varchar varchar(32)
)
begin
  set v_out_varchar = 'qux';
  set v_inout_varchar = 'quux';
  select MEMBER_ID, MEMBER_NAME, BIRTHDATE, FORMALIZED_DATETIME, MEMBER_STATUS_CODE
    from MEMBER
   where MEMBER_STATUS_CODE = v_in_char;
  select * from MEMBER_STATUS
   where MEMBER_STATUS_CODE = v_in_char;
end;
-- #df:end#

-- =======================================================================================
--                                                                             Transaction
--                                                                             ===========
-- #df:begin#
-- test for being called from Sql2Entity and Application Execution
create procedure SP_TRANSACTION_INHERIT()
begin
  delete from MEMBER_LOGIN;
end;
-- #df:end#
