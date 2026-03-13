
create table MEMBER(
    MEMBER_ID INTEGER AUTO_INCREMENT NOT NULL COMMENT '会員ID: 会員を識別するID。連番として基本的に自動採番される。
（会員IDだけに限らず）採番方法はDBMSによって変わる。',
    MEMBER_NAME VARCHAR(180) NOT NULL COMMENT '会員名称: 会員のフルネームの名称。',
    MEMBER_ACCOUNT VARCHAR(50) NOT NULL COMMENT '会員アカウント: 会員がログイン時に利用するアカウントNO。',
    MEMBER_STATUS_CODE CHAR(3) NOT NULL COMMENT '会員ステータスコード',
    FORMALIZED_DATETIME DATETIME COMMENT '正式会員日時: 会員が正式に確定した日時。一度確定したら更新されない。
仮会員のときはnull。',
    BIRTHDATE DATE COMMENT '生年月日: 必須項目ではないので、このデータがない会員もいる。',
    REGISTER_DATETIME DATETIME NOT NULL COMMENT '登録日時: レコードが登録された日時。共通カラムの一つ。',
    REGISTER_USER VARCHAR(200) NOT NULL COMMENT '登録ユーザ: レコードを登録したユーザ。共通カラムの一つ。',
    UPDATE_DATETIME DATETIME NOT NULL COMMENT '更新日時: レコードが（最後に）更新された日時。共通カラムの一つ。',
    UPDATE_USER VARCHAR(200) NOT NULL COMMENT '更新ユーザ: レコードを更新したユーザ。',
    VERSION_NO BIGINT NOT NULL COMMENT 'バージョンNO: レコードのバージョンを示すNO。
更新回数と等しく、主に排他制御のために利用される。',
    PRIMARY KEY (MEMBER_ID),
    UNIQUE (MEMBER_ACCOUNT)
) COMMENT='会員: 会員のプロフィールやアカウントなどの基本情報を保持する。
基本的に物理削除はなく、退会したらステータスが退会会員になる。
ライフサイクルやカテゴリの違う会員情報は、one-to-oneなどの関連テーブルにて。';


create table MEMBER_ADDRESS(
    MEMBER_ADDRESS_ID INTEGER AUTO_INCREMENT NOT NULL COMMENT '会員住所ID: 会員住所を識別するID。
履歴分も含むテーブルなので、これ自体はFKではない。',
    MEMBER_ID INTEGER NOT NULL COMMENT '会員ID: 会員を参照するID。
履歴分を含むため、これだけではユニークにはならない。',
    VALID_BEGIN_DATE DATE NOT NULL COMMENT '有効開始日: 一つの有効期間の開始を示す日付。
前の有効終了日の次の日の値が格納される。',
    VALID_END_DATE DATE NOT NULL COMMENT '有効終了日: 有効期間の終了日。
次の有効開始日の一日前の値が格納される。
ただし、次の有効期間がない場合は 9999/12/31 となる。',
    ADDRESS VARCHAR(200) NOT NULL COMMENT '住所: まるごと住所',
    REGION_ID INTEGER NOT NULL COMMENT '地域ID: 地域を参照するID。
ここでは特に住所の内容と連動しているわけではない。',
    REGISTER_DATETIME DATETIME NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATETIME NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    VERSION_NO BIGINT NOT NULL,
    PRIMARY KEY (MEMBER_ADDRESS_ID),
    UNIQUE (MEMBER_ID, VALID_BEGIN_DATE)
) COMMENT='会員住所情報: 会員の住所に関する情報。
同時に有効期間ごとに履歴管理されている。';


create table MEMBER_LOGIN(
    MEMBER_LOGIN_ID BIGINT AUTO_INCREMENT NOT NULL COMMENT '会員ログインID',
    MEMBER_ID INTEGER NOT NULL COMMENT '会員ID',
    LOGIN_DATETIME DATETIME NOT NULL COMMENT 'ログイン日時: ログインした瞬間の日時。',
    MOBILE_LOGIN_FLG INTEGER NOT NULL COMMENT 'モバイルログインフラグ: モバイル機器からのログインか否か。',
    LOGIN_MEMBER_STATUS_CODE CHAR(3) NOT NULL COMMENT 'ログイン会員ステータスコード: ログイン時の会員ステータス',
    PRIMARY KEY (MEMBER_LOGIN_ID),
    UNIQUE (MEMBER_ID, LOGIN_DATETIME)
) COMMENT='会員ログイン情報: ログインするたびに登録されるログイン履歴。';


create table MEMBER_SECURITY(
    MEMBER_ID INTEGER NOT NULL COMMENT '会員ID: そのまま one-to-one を構成するためのFKとなる。',
    LOGIN_PASSWORD VARCHAR(50) NOT NULL COMMENT 'ログインパスワード: ログイン時に利用するパスワード。
本当は良くないが、Exampleなのでひとまず暗号化していない。',
    REMINDER_QUESTION VARCHAR(50) NOT NULL COMMENT 'リマインダ質問: パスワードを忘れた際のリマインダ機能における質問の内容。',
    REMINDER_ANSWER VARCHAR(50) NOT NULL COMMENT 'リマインダ回答: パスワードを忘れた際のリマインダ機能における質問の答え。',
    REMINDER_USE_COUNT INTEGER NOT NULL COMMENT 'リマインダ利用回数: リマインダを利用した回数。
多いと忘れっぽい会員と言えるが、
そんなことを知ってもしょうがない。',
    REGISTER_DATETIME DATETIME NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATETIME NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    VERSION_NO BIGINT NOT NULL,
    PRIMARY KEY (MEMBER_ID)
) COMMENT='会員セキュリティ情報: 会員とは one-to-one で、会員一人につき必ず一つのセキュリティ情報がある';


create table MEMBER_SERVICE(
    MEMBER_SERVICE_ID INTEGER AUTO_INCREMENT NOT NULL COMMENT '会員サービスID: 独立した主キーとなるが、実質的に会員IDとは one-to-one である。',
    MEMBER_ID INTEGER NOT NULL COMMENT '会員ID: 会員を参照するID。ユニークなので、会員とは one-to-one の関係に。',
    SERVICE_POINT_COUNT INTEGER NOT NULL COMMENT 'サービスポイント数: 会員が現在利用できるサービスポイントの数。
基本的に、購入時には増えてポイントを使ったら減る。',
    SERVICE_RANK_CODE CHAR(3) NOT NULL COMMENT 'サービスランクコード: サービスランクを参照するコード。
どんなランクがあるのかドキドキですね。',
    REGISTER_DATETIME DATETIME NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATETIME NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    VERSION_NO BIGINT NOT NULL,
    PRIMARY KEY (MEMBER_SERVICE_ID),
    UNIQUE (MEMBER_ID)
) COMMENT='会員サービス: 会員のサービス情報（ポイントサービスなど）。';


create table MEMBER_WITHDRAWAL(
    MEMBER_ID INTEGER NOT NULL,
    WITHDRAWAL_REASON_CODE CHAR(3) COMMENT '退会理由コード: 退会した定型理由を参照するコード。
何も言わずに退会する会員もいるので必須項目ではない。',
    WITHDRAWAL_REASON_INPUT_TEXT TEXT COMMENT '退会理由入力テキスト: 会員がフリーテキストで入力できる退会理由。
もう言いたいこと言ってもらう感じ。',
    WITHDRAWAL_DATETIME DATETIME NOT NULL COMMENT '退会日時: 退会した瞬間の日時。
正式会員日時と違い、こっちはone-to-oneの別テーブルで。',
    REGISTER_DATETIME DATETIME NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATETIME NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    PRIMARY KEY (MEMBER_ID)
) COMMENT='会員退会情報: 退会会員の退会に関する詳細な情報。
退会会員のみデータが存在する。';


create table PURCHASE(
    PURCHASE_ID BIGINT AUTO_INCREMENT NOT NULL COMMENT '購入ID: 連番',
    MEMBER_ID INTEGER NOT NULL COMMENT '会員ID: 会員を参照するID。
購入を識別する自然キー（複合ユニーク制約）の筆頭要素。',
    PRODUCT_ID INTEGER NOT NULL COMMENT '商品ID: 商品を参照するID。',
    PURCHASE_DATETIME DATETIME NOT NULL COMMENT '購入日時: 購入した瞬間の日時。',
    PURCHASE_COUNT INTEGER NOT NULL COMMENT '購入数量: 購入した商品の（一回の購入における）数量。',
    PURCHASE_PRICE INTEGER NOT NULL COMMENT '購入価格: 購入によって実際に会員が支払った（支払う予定の）価格。
基本は商品の定価に購入数量を掛けたものになるが、
ポイント利用や割引があったりと必ずしもそうはならない。',
    PAYMENT_COMPLETE_FLG INTEGER NOT NULL COMMENT '支払完了フラグ: この購入に関しての支払いが完了しているか否か。',
    REGISTER_DATETIME DATETIME NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATETIME NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    VERSION_NO BIGINT NOT NULL,
    PRIMARY KEY (PURCHASE_ID),
    UNIQUE (MEMBER_ID, PRODUCT_ID, PURCHASE_DATETIME),
    KEY (MEMBER_ID),
    KEY (PRODUCT_ID)
) COMMENT='購入: 一つの商品に対する一回の購入を表現する。
一回の購入で一つの商品を複数個買うこともある。' ;


create table PRODUCT(
    PRODUCT_ID INTEGER AUTO_INCREMENT NOT NULL COMMENT '商品ID',
    PRODUCT_NAME VARCHAR(50) NOT NULL COMMENT '商品名称',
    PRODUCT_HANDLE_CODE VARCHAR(100) NOT NULL COMMENT '商品ハンドルコード: 商品を識別する業務上のコード。',
    PRODUCT_CATEGORY_CODE CHAR(3) NOT NULL,
    PRODUCT_STATUS_CODE CHAR(3) NOT NULL,
    REGULAR_PRICE INTEGER NOT NULL COMMENT '定価: 特に割引などがない場合の販売価格',
    REGISTER_DATETIME DATETIME NOT NULL,
    REGISTER_USER VARCHAR(200) NOT NULL,
    UPDATE_DATETIME DATETIME NOT NULL,
    UPDATE_USER VARCHAR(200) NOT NULL,
    VERSION_NO BIGINT NOT NULL,
    PRIMARY KEY (PRODUCT_ID),
    UNIQUE (PRODUCT_HANDLE_CODE)
) COMMENT='商品: 会員へ販売する商品のマスタ。
販売可能なものだけでなく、生産中止や販売中止などの商品も含まれる。';


create table PRODUCT_CATEGORY(
    PRODUCT_CATEGORY_CODE CHAR(3) NOT NULL COMMENT '商品カテゴリコード',
    PRODUCT_CATEGORY_NAME VARCHAR(50) NOT NULL COMMENT '商品カテゴリ名称',
    PARENT_CATEGORY_CODE CHAR(3) COMMENT '親カテゴリコード: 最上位の場合はデータなし。',
    PRIMARY KEY (PRODUCT_CATEGORY_CODE)
) COMMENT='商品カテゴリ: 商品のカテゴリを表現するマスタ。
自己参照の階層になっている。';


create table PRODUCT_STATUS(
    PRODUCT_STATUS_CODE CHAR(3) NOT NULL COMMENT '商品ステータスコード',
    PRODUCT_STATUS_NAME VARCHAR(50) NOT NULL COMMENT '商品ステータス名称',
    DISPLAY_ORDER INTEGER NOT NULL COMMENT '表示順',
    PRIMARY KEY (PRODUCT_STATUS_CODE),
    UNIQUE (DISPLAY_ORDER)
) COMMENT='商品ステータス';


create table REGION(
    REGION_ID INTEGER NOT NULL COMMENT '地域ID',
    REGION_NAME VARCHAR(50) NOT NULL COMMENT '地域名称',
    PRIMARY KEY (REGION_ID)
) COMMENT='地域: 主に会員の住所に対応する地域。
かなりざっくりした感じではある。';


create table SERVICE_RANK(
    SERVICE_RANK_CODE CHAR(3) NOT NULL COMMENT 'サービスランクコード: サービスランクを識別するコード。',
    SERVICE_RANK_NAME VARCHAR(50) NOT NULL COMMENT 'サービスランク名称: サービスランクの名称。
（ゴールドとかプラチナとか基本的には威厳のある名前）',
    SERVICE_POINT_INCIDENCE NUMERIC(5, 3) NOT NULL COMMENT 'サービスポイント発生率: 購入ごとのサービスポイントの発生率。
購入価格にこの値をかけた数が発生ポイント。',
    NEW_ACCEPTABLE_FLG INTEGER NOT NULL COMMENT '新規受け入れ可能フラグ: このランクへの新規受け入れができるかどうか。',
    DESCRIPTION VARCHAR(200) NOT NULL,
    DISPLAY_ORDER INTEGER NOT NULL,
    PRIMARY KEY (SERVICE_RANK_CODE),
    UNIQUE (DISPLAY_ORDER)
) COMMENT='サービスランク: 会員のサービスレベルを表現するランク。
（ゴールドとかプラチナとか）';


create table MEMBER_STATUS(
    MEMBER_STATUS_CODE CHAR(3) NOT NULL COMMENT '会員ステータスコード: 会員ステータスを識別するコード。',
    MEMBER_STATUS_NAME VARCHAR(50) NOT NULL COMMENT '会員ステータス名称',
    DESCRIPTION VARCHAR(200) NOT NULL COMMENT '説明: 会員ステータスそれぞれの説明。
気の利いた説明があるとディベロッパーがとても助かる。',
    DISPLAY_ORDER INTEGER NOT NULL COMMENT '表示順: UI上のステータスの表示順を示すNO。
並べるときは、このカラムに対して昇順のソート条件にする。',
    PRIMARY KEY (MEMBER_STATUS_CODE),
    UNIQUE (DISPLAY_ORDER)
) COMMENT='会員ステータス';


create table WITHDRAWAL_REASON(
    WITHDRAWAL_REASON_CODE CHAR(3) NOT NULL COMMENT '退会理由コード',
    WITHDRAWAL_REASON_TEXT TEXT NOT NULL COMMENT '退会理由テキスト: 退会理由の内容。テキスト形式なので目いっぱい書けるが、
そうするとUI側できれいに見せるのが大変でしょうね。',
    DISPLAY_ORDER INTEGER NOT NULL COMMENT '表示順',
    PRIMARY KEY (WITHDRAWAL_REASON_CODE),
    UNIQUE (DISPLAY_ORDER)
) COMMENT='退会理由: 会員に選ばせる定型的な退会理由のマスタ。';


alter table MEMBER add constraint FK_MEMBER_MEMBER_STATUS 
    foreign key (MEMBER_STATUS_CODE) references MEMBER_STATUS (MEMBER_STATUS_CODE);

alter table MEMBER_ADDRESS add constraint FK_MEMBER_ADDRESS_MEMBER 
    foreign key (MEMBER_ID) references MEMBER (MEMBER_ID);

alter table MEMBER_ADDRESS add constraint FK_MEMBER_ADDRESS_REGION 
    foreign key (REGION_ID) references REGION (REGION_ID);

alter table MEMBER_LOGIN add constraint FK_MEMBER_LOGIN_MEMBER_STATUS 
    foreign key (LOGIN_MEMBER_STATUS_CODE) references MEMBER_STATUS (MEMBER_STATUS_CODE);

alter table MEMBER_LOGIN add constraint FK_MEMBER_LOGIN_MEMBER 
    foreign key (MEMBER_ID) references MEMBER (MEMBER_ID);

alter table MEMBER_SERVICE add constraint FK_MEMBER_SERVICE_MEMBER 
    foreign key (MEMBER_ID) references MEMBER (MEMBER_ID);

alter table MEMBER_SERVICE add constraint FK_MEMBER_SERVICE_SERVICE_RANK_CODE 
    foreign key (SERVICE_RANK_CODE) references SERVICE_RANK (SERVICE_RANK_CODE);

alter table MEMBER_SECURITY add constraint FK_MEMBER_SECURITY_MEMBER 
    foreign key (MEMBER_ID) references MEMBER (MEMBER_ID);

alter table MEMBER_WITHDRAWAL add constraint FK_MEMBER_WITHDRAWAL_MEMBER 
    foreign key (MEMBER_ID) references MEMBER (MEMBER_ID);

alter table MEMBER_WITHDRAWAL add constraint FK_MEMBER_WITHDRAWAL_WITHDRAWAL_REASON 
    foreign key (WITHDRAWAL_REASON_CODE) references WITHDRAWAL_REASON (WITHDRAWAL_REASON_CODE);

alter table PURCHASE add constraint FK_PURCHASE_MEMBER 
    foreign key (MEMBER_ID) references MEMBER (MEMBER_ID);

alter table PURCHASE add constraint FK_PURCHASE_PRODUCT 
    foreign key (PRODUCT_ID) references PRODUCT (PRODUCT_ID);

alter table PRODUCT add constraint FK_PRODUCT_PRODUCT_CATEGORY 
    foreign key (PRODUCT_CATEGORY_CODE) references PRODUCT_CATEGORY (PRODUCT_CATEGORY_CODE);

alter table PRODUCT add constraint FK_PRODUCT_PRODUCT_STATUS 
    foreign key (PRODUCT_STATUS_CODE) references PRODUCT_STATUS (PRODUCT_STATUS_CODE);

alter table PRODUCT_CATEGORY add constraint FK_PRODUCT_CATEGORY_PARENT 
    foreign key (PARENT_CATEGORY_CODE) references PRODUCT_CATEGORY (PRODUCT_CATEGORY_CODE);


create index IX_MEMBER_MEMBER_NAME ON MEMBER(MEMBER_NAME);
create index IX_MEMBER_FORMALIZED_DATETIME ON MEMBER(FORMALIZED_DATETIME);
create index IX_MEMBER_LOGIN_DATETIME ON MEMBER_LOGIN(LOGIN_DATETIME);
create index IX_MEMBER_SERVICE_POINT_COUNT ON MEMBER_SERVICE(SERVICE_POINT_COUNT);
create index IX_PURCHASE_PRODUCT_DATETIME ON PURCHASE(PRODUCT_ID, PURCHASE_DATETIME);
create index IX_PURCHASE_DATETIME_MEMBER ON PURCHASE(PURCHASE_DATETIME, MEMBER_ID);
create index IX_PURCHASE_PRICE ON PURCHASE(PURCHASE_PRICE);
create index IX_PRODUCT_PRODUCT_NAME ON PRODUCT(PRODUCT_NAME);
