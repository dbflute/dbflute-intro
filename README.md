#DBFlute Intro

##DBFlute Introとは

DBFluteの導入(セットアップ)ツールです。

[DBFlute公式サイトのDBFlute Introページ](http://dbflute.seasar.org/ja/manual/function/helper/intro/index.html)

####DBFlute Introダウンロード

* [dbflute-intro.jar](http://p1us2er0.github.io/dbflute-intro/download/dbflute-intro.jar) クロスプラットフォーム版(Windows, Mac, Linux)

* [dbflute-intro.zip](http://p1us2er0.github.io/dbflute-intro/download/dbflute-intro.zip) Windows実行ファイル版

 ※zip形式になっているので要解凍

##DBFlute Introのコンセプト

Java開発者以外にも使いやすいように。

→Javaが実行できる環境は必要ですが、EclipseやMavenの知識は不要です。

DBFluteのO/Rマッパー以外の機能を手軽に利用できるように。

(O/Rマッパーとして使わなくても(使えなくても)便利な機能がたくさんあります。)

    SchemaHTML ○
    HistoryHTML ○
    SchemaSyncCheck ○
    CraftDiff △
    LoadDataReverse○
    ReplaceSchema △
    PropertiesHTML
    AlterCheck
    FreeGen

※◯がついている機能がDBFlute Introで簡単に利用できます。

##デモ

###1 . ダウンロード

1-1. DBFlute Intro本体

<http://p1us2er0.github.io/dbflute-intro/download/dbflute-intro.jar>

1-2. デモに使用するDB

<https://www.seasar.org/svn/dbflute/trunk/dbflute-basic-example/src/main/resources/exampledb/exampledb.h2.db>

※参考 ER図 <http://dbflute.seasar.org/ja/view/exampledb/index.html?goto=1>

1-3. フォルダ配置
デモフォルダを作って、1-1、1-2のファイルを配置。

     dbflute-intro-demo
     ├dbflute-intro.jar
     └db
       ├exampledb.h2.db
       ├exampledb_st.h2.db ・・・ 1-2のファイルをコピー
       └exampledb_it.h2.db ・・・ 1-2のファイルをコピー

###2 . DBFlute Introのセットアップ

2-1. DBFlute Introの実行

    java -jar dbflute-intro.jar

※macの場合はjarダブルクリックでもOK。

(新しいOSでセキュリティ警告が出るため、初回は右クリックから選択して実行。)

2-2. DBFluteクライアントのダウンロード

バージョンを入力して、「OK」を押下。

※デフォルトは最新バージョンになっています。

2.3. 初期設定(管理したいDBごとに1回)

    DB名 = exampledb (任意の名前)
    RDMS = H2 (RDBの種類)
    URL =  jdbc:h2:file:../db/exampledb (DB接続URL。RDBごとに記述が異なります。)
    スキーマ = PUBLIC (スキーマ名。スキーマの概念がない場合は、空)
    ユーザ = sa (DBの接続ユーザ名)
    パスワード = 空 (パスワード)
    Jdbcドライバのパス = 空 (H2の場合はDBFluteクライアントにバンドルされているため不要)
    DBFluteバージョン = 1.0.4.B(ダンロードしているDBFluteクライアントの任意のバージョン)

他のDB環境の+を押下。

  「it」と入力。

    URL = jdbc:h2:file:../db/exampledb_it (DB接続URL。RDBごとに記述が異なります。)
    スキーマ = PUBLIC (スキーマ名。スキーマの概念がない場合は、空)
    ユーザ = sa (DBの接続ユーザ名)
    パスワード = 空 (パスワード)

他のDB環境の+を押下。

「st」と入力。

    URL = jdbc:h2:file:../db/exampledb_st (DB接続URL。RDBごとに記述が異なります。)
    スキーマ = PUBLIC (スキーマ名。スキーマの概念がない場合は、空)
    ユーザ = sa (DBの接続ユーザ名)
    パスワード = 空 (パスワード)

「作成」を押下。

###3 . 情報取得、参照

3-1. DB情報取得、参照。

「ドキュメント作成」を押下。

「テーブル定義を開く」を押下して、htmlを確認。

「DB変更履歴を開く」を押下して、htmlを確認。

※現時点では差分がないため、参照できません。

3-2. 別環境のDBとDDLレベルの差分確認。

「スキーマの差分チェック」を押下して、「it」を選択。

「差分チェック結果を開く」を押下して、 「it」を選択して、htmlを確認。

※現時点では差分がないため、参照できません。

###4 . DB情報変更後にDB情報取得、参照。

4-1. DBに接続する。

exampledb.h2.dbに任意の方法で接続。

4-2. DDL発行。

    ALTER TABLE PUBLIC.MEMBER ADD TEST_COLUMN VARCHAR(10);
    CREATE TABLE TEST_TABLE(TEST_ID INTEGER IDENTITY NOT NULL PRIMARY KEY);

4-3. DB情報取得、参照。

「ドキュメント作成」を押下。

「テーブル定義を開く」を押下して、htmlを確認。

「DB変更履歴を開く」を押下して、htmlを確認。

4-4. 別環境のDBとDDLレベルの差分確認。

「スキーマの差分チェック」を押下して、「it」を選択。

「差分チェック結果を開く」を押下して、 「it」を選択して、htmlを確認。

4-5. DDLを発行して、差分をなくす。

exampledb_it.h2.dbに任意の方法で接続して、4-2のDDLを発行。

4-6. 別環境のDBとDDLレベルの差分確認。

「スキーマの差分チェック」を押下して、「it」を選択。

「差分チェック結果を開く」を押下して、 「it」を選択して、htmlを確認。

※現時点では差分がないため、参照できません。

###5 . 別環境のDBとDMLレベルの差分確認。

5-1. 前提知識

<http://dbflute.seasar.org/ja/manual/function/genbafit/projectfit/craftdiff/>

5-2. DML差分のための準備

以下のフォルダに、「craftdiff」フォルダを作成。

dbflute-intro-demo/dbflute-intro/dbflute_exampledb/schema

「craftdiff」フォルダに「craft-schema.sql」を作成。

    -- #df:assertEquals(MemberStatus)#
    select MEMBER_STATUS_CODE as KEY, MEMBER_STATUS_NAME, DISPLAY_ORDER
      from MEMBER_STATUS
     order by KEY
    ;

5-3. DB情報取得、参照。

「ドキュメント作成」を押下。

「DB変更履歴を開く」を押下して、htmlを確認。

5-4. 別環境のDBとDMLレベルの差分確認。

「スキーマの差分チェック」を押下して、「it」を選択。

「差分チェック結果を開く」を押下して、 「it」を選択して、htmlを確認。

## developer

```
## init
# git clone
git clone https://github.com/p1us2er0/dbflute-intro.git

# refresh app
cd dbflute-intro
./gradlew refresh

## run
# build
cd dbflute-intro
./gradlew build

# run
cd dbflute-intro
java -jar build/libs/dbflute-intro.war

## during development
cd dbflute-intro
./gradlew run
./gradlew gulp_serve
```

## URL list

```
# dbflute intro
api/intro/manifest

# dbflute engine
api/engine/publicProperties
api/engine/versions
api/engine/download/{version}

# dbflute client
api/client/list
api/client/detail/{project}
api/client/add
api/client/remove/{project}
api/client/update
api/client/task/{project}/{task}
api/client/schemahtml
api/client/historyhtml
```

end.
