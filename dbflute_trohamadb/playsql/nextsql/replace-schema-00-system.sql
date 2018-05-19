-- #df:changeUser(system)#
-- #df:checkUser()#
create database /*$dfprop.mainCatalog*/;

-- #df:reviveUser()#
-- #df:checkUser()#
grant all privileges on /*$dfprop.mainCatalog*/.*
  to /*$dfprop.mainUser*/@localhost identified by '/*$dfprop.mainPassword*/';

flush privileges;