update MEMBER_SECURITY set LOGIN_PASSWORD = sha1(LOGIN_PASSWORD);
update MEMBER set UPDATE_USER = hex(aes_encrypt(UPDATE_USER, 'himitsu'));
update WHITE_GEARED_CIPHER set CIPHER_VARCHAR = hex(aes_encrypt(CIPHER_VARCHAR, 'himitsu'));
update WHITE_GEARED_CIPHER set CIPHER_INTEGER = hex(aes_encrypt(CIPHER_INTEGER, 'himitsu'));
update WHITE_GEARED_CIPHER set CIPHER_DATE = hex(aes_encrypt(CIPHER_DATE, 'himitsu'));
update WHITE_GEARED_CIPHER set CIPHER_DATETIME = hex(aes_encrypt(CIPHER_DATETIME, 'himitsu'));
