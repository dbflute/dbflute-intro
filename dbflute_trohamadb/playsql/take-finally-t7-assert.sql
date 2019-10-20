
-- =======================================================================================
--                                                                     Business Constraint
--                                                                     ===================
-- #df:assertListZero#
-- formalized members should have their own formalized date-times
select MEMBER_ID, MEMBER_NAME, MEMBER_STATUS_CODE, FORMALIZED_DATETIME
  from `MEMBER`
 where MEMBER_STATUS_CODE = 'FML'
   and FORMALIZED_DATETIME is null
;

-- #df:assertListZero#
-- withdrawal members should have their own withdrawal informations
select mb.MEMBER_ID, mb.MEMBER_NAME, mb.MEMBER_STATUS_CODE
  from `MEMBER` mb
 where mb.MEMBER_STATUS_CODE = 'WDL'
   and not exists (select wdl.MEMBER_ID
                     from MEMBER_WITHDRAWAL wdl
                    where wdl.MEMBER_ID = mb.MEMBER_ID
       )
;


-- =======================================================================================
--                                                                     TestData Constraint
--                                                                     ===================
-- #df:assertListZero#
select mb.MEMBER_ID as MEMBER_ID
     , count(mb.MEMBER_ID) as SELECTED_COUNT
     , min(address.MEMBER_ADDRESS_ID) as MIN_ADDRESS_ID
     , max(address.MEMBER_ADDRESS_ID) as MAX_ADDRESS_ID
     , CURRENT_DATE as TARGET_DATE
  from `MEMBER` mb
    left outer join MEMBER_ADDRESS address
      on mb.MEMBER_ID = address.MEMBER_ID
     and address.VALID_BEGIN_DATE <= CURRENT_DATE
     and address.VALID_END_DATE >= CURRENT_DATE
 group by mb.MEMBER_ID
   having count(mb.MEMBER_ID) > 1 
;

-- #df:assertListExists#
select mb.MEMBER_ID, mb.MEMBER_NAME
  from `MEMBER` mb
 where mb.BIRTHDATE is null
;

-- #df:assertListZero#
select selfref.cnt
  from (select count(*) as cnt
          from WHITE_SELF_REFERENCE
       ) selfref
 where selfref.cnt <> 6
;

-- #df:assertListZero#
select bin.cnt
  from (select count(*) as cnt
          from WHITE_BINARY
       ) bin
 where bin.cnt <> 3
;

-- #df:assertListZero#
select delimiter.cnt
  from (select count(*) as cnt
          from WHITE_DELIMITER
       ) delimiter
 where delimiter.cnt <> 15
;
