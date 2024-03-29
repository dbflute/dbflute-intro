-- #df:checkEnv(ut,slave)#

-- #df:changeUser(system)#
-- #df:checkUser(mainSchema)#
create database /*$dfprop.mainCatalog*/;

-- #df:reviveUser()#
-- #df:checkUser(mainUser)#
create user /*$dfprop.mainUser*/@'%' identified by '/*$dfprop.mainPassword*/';

-- #df:reviveUser()#
-- #df:checkUser(grant)#
grant all privileges on /*$dfprop.mainCatalog*/.* to /*$dfprop.mainUser*/@'%';

-- #df:reviveUser()#
-- #df:checkUser(grant)#
grant all privileges on /*$nextCatalog*/.* to /*$dfprop.mainUser*/@'%';

flush privileges;