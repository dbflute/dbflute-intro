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
package org.dbflute.intro.mylasta.action;

import org.dbflute.intro.mylasta.action.DbfluteLabels;
import org.lastaflute.web.ruts.message.ActionMessage;

/**
 * The keys for message.
 * @author FreeGen
 */
public class DbfluteMessages extends DbfluteLabels {

    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    /** The key of the message: データが不正です。 */
    public static final String CONSTRAINTS_AssertFalse_MESSAGE = "{constraints.AssertFalse.message}";

    /** The key of the message: データが不正です。 */
    public static final String CONSTRAINTS_AssertTrue_MESSAGE = "{constraints.AssertTrue.message}";

    /** The key of the message: {value}${inclusive == true ? '以下' : '未満'}で入力してください。 */
    public static final String CONSTRAINTS_DecimalMax_MESSAGE = "{constraints.DecimalMax.message}";

    /** The key of the message: {value}${inclusive == true ? '以上' : 'より大きな値'}で入力してください。 */
    public static final String CONSTRAINTS_DecimalMin_MESSAGE = "{constraints.DecimalMin.message}";

    /** The key of the message: numeric value out of bounds (&lt;{integer} digits&gt;.&lt;{fraction} digits&gt; expected) */
    public static final String CONSTRAINTS_Digits_MESSAGE = "{constraints.Digits.message}";

    /** The key of the message: 未来日付を入れてください。 */
    public static final String CONSTRAINTS_Future_MESSAGE = "{constraints.Future.message}";

    /** The key of the message: {value}以下で入力してください。 */
    public static final String CONSTRAINTS_Max_MESSAGE = "{constraints.Max.message}";

    /** The key of the message: {value}以上で入力してください。 */
    public static final String CONSTRAINTS_Min_MESSAGE = "{constraints.Min.message}";

    /** The key of the message: 入力してください。 */
    public static final String CONSTRAINTS_NotNull_MESSAGE = "{constraints.NotNull.message}";

    /** The key of the message: 入力しないでください。 */
    public static final String CONSTRAINTS_Null_MESSAGE = "{constraints.Null.message}";

    /** The key of the message: 過去日付を入れてください。 */
    public static final String CONSTRAINTS_Past_MESSAGE = "{constraints.Past.message}";

    /** The key of the message: 正規表現"{regexp}"に一致させてください。 */
    public static final String CONSTRAINTS_Pattern_MESSAGE = "{constraints.Pattern.message}";

    /** The key of the message: {min}〜{max}文字で入力してください。 */
    public static final String CONSTRAINTS_Size_MESSAGE = "{constraints.Size.message}";

    /** The key of the message: クレジットカード番号の形式で入力してください。 */
    public static final String CONSTRAINTS_CreditCardNumber_MESSAGE = "{constraints.CreditCardNumber.message}";

    /** The key of the message: invalid {type} barcode */
    public static final String CONSTRAINTS_EAN_MESSAGE = "{constraints.EAN.message}";

    /** The key of the message: メールアドレスの形式で入力してください。 */
    public static final String CONSTRAINTS_Email_MESSAGE = "{constraints.Email.message}";

    /** The key of the message: {min}〜{max}で入力してください。 */
    public static final String CONSTRAINTS_Length_MESSAGE = "{constraints.Length.message}";

    /** The key of the message: The check digit for ${value} is invalid, Luhn Modulo 10 checksum failed */
    public static final String CONSTRAINTS_LuhnCheck_MESSAGE = "{constraints.LuhnCheck.message}";

    /** The key of the message: The check digit for ${value} is invalid, Modulo 10 checksum failed */
    public static final String CONSTRAINTS_Mod10Check_MESSAGE = "{constraints.Mod10Check.message}";

    /** The key of the message: The check digit for ${value} is invalid, Modulo 11 checksum failed */
    public static final String CONSTRAINTS_Mod11Check_MESSAGE = "{constraints.Mod11Check.message}";

    /** The key of the message: The check digit for ${value} is invalid, ${modType} checksum failed */
    public static final String CONSTRAINTS_ModCheck_MESSAGE = "{constraints.ModCheck.message}";

    /** The key of the message: 入力してください。 */
    public static final String CONSTRAINTS_NotBlank_MESSAGE = "{constraints.NotBlank.message}";

    /** The key of the message: 入力してください。 */
    public static final String CONSTRAINTS_NotEmpty_MESSAGE = "{constraints.NotEmpty.message}";

    /** The key of the message: スクリプト"{script}"による評価が不正です。 */
    public static final String CONSTRAINTS_ParametersScriptAssert_MESSAGE = "{constraints.ParametersScriptAssert.message}";

    /** The key of the message: {min}〜{max}で入力してください。 */
    public static final String CONSTRAINTS_Range_MESSAGE = "{constraints.Range.message}";

    /** The key of the message: 安全ではないHTMLが含まれています。 */
    public static final String CONSTRAINTS_SafeHtml_MESSAGE = "{constraints.SafeHtml.message}";

    /** The key of the message: スクリプト"{script}"による評価が不正です。 */
    public static final String CONSTRAINTS_ScriptAssert_MESSAGE = "{constraints.ScriptAssert.message}";

    /** The key of the message: URLの形式入力してください。 */
    public static final String CONSTRAINTS_URL_MESSAGE = "{constraints.URL.message}";

    /** The key of the message: 入力してください。 */
    public static final String CONSTRAINTS_Required_MESSAGE = "{constraints.Required.message}";

    /** The key of the message: {propertyType}で入力してください。 */
    public static final String CONSTRAINTS_TypeAny_MESSAGE = "{constraints.TypeAny.message}";

    /** The key of the message: {value}人以上を入力してください。 */
    public static final String APP_VALIDATOR_MinPeopleNum_MESSAGE = "{app.validator.MinPeopleNum.message}";

    /** The key of the message: {value}人以内で入力してください。 */
    public static final String APP_VALIDATOR_MaxPeopleNum_MESSAGE = "{app.validator.MaxPeopleNum.message}";

    /** The key of the message: {min}文字以上で入力してください。 */
    public static final String APP_VALIDATOR_MinSize_MESSAGE = "{app.validator.MinSize.message}";

    /** The key of the message: {max}文字以内で入力してください。 */
    public static final String APP_VALIDATOR_MaxSize_MESSAGE = "{app.validator.MaxSize.message}";

    /** The key of the message: {min}つ以上で入力してください。 */
    public static final String APP_VALIDATOR_MinSizeCollection_MESSAGE = "{app.validator.MinSizeCollection.message}";

    /** The key of the message: {max}つ以内で入力してください。 */
    public static final String APP_VALIDATOR_MaxSizeCollection_MESSAGE = "{app.validator.MaxSizeCollection.message}";

    /** The key of the message: {equals}文字で入力してください。 */
    public static final String APP_VALIDATOR_EqualsSize_MESSAGE = "{app.validator.EqualsSize.message}";

    /** The key of the message: カタカナで入力してください。 */
    public static final String APP_VALIDATOR_Katakana_MESSAGE = "{app.validator.Katakana.message}";

    /** The key of the message: 電話番号を正しい形式で入力してください[ハイフン付きで入力してください]。 */
    public static final String APP_VALIDATOR_Tel_MESSAGE = "{app.validator.Tel.message}";

    /** The key of the message: 郵便番号の形式[XXX-XXXX]で入力してください。 */
    public static final String APP_VALIDATOR_ZipCode_MESSAGE = "{app.validator.ZipCode.message}";

    /** The key of the message: 英数字混合で入力してください。 */
    public static final String APP_VALIDATOR_AlphaNumMix_MESSAGE = "{app.validator.AlphaNumMix.message}";

    /** The key of the message: 半角数字、半角英字(大文字)、半角カナ、一部記号のみで入力してください。 */
    public static final String APP_VALIDATOR_BankAccountName_MESSAGE = "{app.validator.BankAccountName.message}";

    /** The key of the message: パスワードとパスワード(確認)が一致してません。 */
    public static final String APP_VALIDATOR_Password_Mismatch_MESSAGE = "{app.validator.Password.Mismatch.message}";

    /** The key of the message: 店舗表示時は席のみ手数料またはコースのいずれかは必須です。 */
    public static final String APP_VALIDATOR_ShopAnyRequired_MESSAGE = "{app.validator.ShopAnyRequired.message}";

    /** The key of the message: 催行日前の予約で使用されているため、非表示にはできません。 */
    public static final String APP_VALIDATOR_ShopNotHide_MESSAGE = "{app.validator.ShopNotHide.message}";

    /** The key of the message: 営業時間の曜日が重複しています。 */
    public static final String APP_VALIDATOR_WeekCd_DUPLICATE_MESSAGE = "{app.validator.WeekCd.duplicate.message}";

    /** The key of the message: コースに指定されている人数と予約人数が一致してません。 */
    public static final String APP_VALIDATOR_ReservePeopleNum_MESSAGE = "{app.validator.ReservePeopleNum.message}";

    /** The key of the message: 予約ステータス[予約確定]の場合、予約店舗を入れる必要があります。 */
    public static final String APP_VALIDATOR_ReserveStatusCdReserveFix_MESSAGE = "{app.validator.ReserveStatusCdReserveFix.message}";

    /** The key of the message: 予約店舗候補に同じ店舗は選択できません。 */
    public static final String APP_VALIDATOR_ReserveShopCandidate_DUPLICATE_MESSAGE = "{app.validator.ReserveShopCandidate.duplicate.message}";

    /** The key of the message: 希望候補またはNG候補以外のコースは全て選択してください。 */
    public static final String APP_VALIDATOR_ReserveShopCandidate_REQUIRED_MESSAGE = "{app.validator.ReserveShopCandidate.required.message}";

    /** The key of the message: 同じ候補番号は選択できません。 */
    public static final String APP_VALIDATOR_Priority_DUPLICATE_MESSAGE = "{app.validator.Priority.duplicate.message}";

    /** The key of the message: 予約確定または空席あり状態では、席のみまたはコースのいずれかは必須です。 */
    public static final String APP_VALIDATOR_SeatOnlyAndCourse_MESSAGE = "{app.validator.SeatOnlyAndCourse.message}";

    /** The key of the message: {0}では席のみ予約はできません。 */
    public static final String APP_VALIDATOR_SeatOnlyReserve_MESSAGE = "{app.validator.SeatOnlyReserve.message}";

    /** The key of the message: 予約変更時は、通知メッセージは必須です。 */
    public static final String APP_VALIDATOR_NoticeMessage_MESSAGE = "{app.validator.NoticeMessage.message}";

    /** The key of the message: 選択したメール・Fax送信タイプと予約データの整合性がとれてません。(予約ステータスが不正、店舗FAX番号がないなど) */
    public static final String APP_VALIDATOR_ReserveLogTypeCd_MESSAGE = "{app.validator.ReserveLogTypeCd.message}";

    /** The key of the message: 選択した店舗は無効です。選択店舗の表示フラグを確認してください。 */
    public static final String APP_VALIDATOR_ReserveShopData_INVALID_MESSAGE = "{app.validator.ReserveShopData.invalid.message}";

    /** The key of the message: 選択したコース(料金)は無効です。選択コースの表示フラグまたは有効期間を確認してください。 */
    public static final String APP_VALIDATOR_ReserveCourseData_INVALID_MESSAGE = "{app.validator.ReserveCourseData.invalid.message}";

    /** The key of the message: 選択したコースはご指定の開催日時に終了しています。コースを変更してください。 */
    public static final String APP_VALIDATOR_ReserveCourseEffectiveTime_OUTSIDE_MESSAGE = "{app.validator.ReserveCourseEffectiveTime.outside.message}";

    /** The key of the message: 店舗情報が更新された可能性があります。お手数ですが、始めからやり直してください。 */
    public static final String APP_VALIDATOR_ReserveShopCandidate_ENTRY_FAILED_MESSAGE = "{app.validator.ReserveShopCandidate.entry.failed.message}";

    /** The key of the message: CSVファイルのカラム数が規定値と一致してません。 */
    public static final String APP_VALIDATOR_CourseCsvDataSize_MISMATCH_MESSAGE = "{app.validator.CourseCsvDataSize.mismatch.message}";

    /** The key of the message: 画像名に対する画像ファイルが存在しません。 */
    public static final String APP_VALIDATOR_ImageData_NOT_FOUND_MESSAGE = "{app.validator.ImageData.not.found.message}";

    /** The key of the message: [true/false]または[0/1]で入力してください。 */
    public static final String APP_VALIDATOR_XTypeBoolean_MESSAGE = "{app.validator.XTypeBoolean.message}";

    /** The key of the message: フォーマットが不正です。 */
    public static final String APP_VALIDATOR_LocalTimeFormat_MESSAGE = "{app.validator.LocalTimeFormat.message}";

    /** The key of the message: フォーマットが不正です。 */
    public static final String APP_VALIDATOR_LocalDateTimeFormat_MESSAGE = "{app.validator.LocalDateTimeFormat.message}";

    /** The key of the message: 値が不正です。正しい値を入力してください。 */
    public static final String APP_CONVERTER_Valid_MESSAGE = "{app.converter.Valid.message}";

    /** The key of the message: 数字を入力してください。 */
    public static final String APP_CONVERTER_Number_MESSAGE = "{app.converter.Number.message}";

    /** The key of the message: 正しい日付を入力してください。 */
    public static final String APP_CONVERTER_Date_MESSAGE = "{app.converter.Date.message}";

    /** The key of the message: 正しい時間を入力してください。 */
    public static final String APP_CONVERTER_Time_MESSAGE = "{app.converter.Time.message}";

    /** The key of the message: 正しい日時を入力してください。 */
    public static final String APP_CONVERTER_DateTime_MESSAGE = "{app.converter.DateTime.message}";

    /** The key of the message: input number for {0} */
    public static final String ERRORS_NUMBER = "{errors.number}";

    /** The key of the message: same value is selected in {0} */
    public static final String ERRORS_SAME_VALUE = "{errors.same.value}";

    /** The key of the message: greater than {0} */
    public static final String ERRORS_GREATER_THAN = "{errors.greater.than}";

    /** The key of the message: less than {0} */
    public static final String ERRORS_LESS_THAN = "{errors.less.than}";

    /** The key of the message: greater equals {0} */
    public static final String ERRORS_GREATER_EQUALS = "{errors.greater.equals}";

    /** The key of the message: {0}つ以内で入力してください。 */
    public static final String ERRORS_LESS_EQUALS = "{errors.less.equals}";

    /** The key of the message: input {0} at least one */
    public static final String ERRORS_REQUIRED_AT_LEAST_ONE = "{errors.required.at.least.one}";

    /** The key of the message: input either {0} or {1} */
    public static final String ERRORS_REQUIRED_OR = "{errors.required.or}";

    /** The key of the message: Uploading failed, because actual size {0} bytes exceeded limit size {1} bytes. */
    public static final String ERRORS_UPLOAD_SIZE = "{errors.upload.size}";

    /** The key of the message: メールアドレスかパスワードが間違っています。 */
    public static final String ERRORS_EMPTY_LOGIN = "{errors.empty.login}";

    /** The key of the message: すでに存在します。 */
    public static final String ERRORS_ALREADY_EXISTS = "{errors.already.exists}";

    /** The key of the message: ログインに失敗しました。 */
    public static final String ERRORS_LOGIN_FAILURE = "{errors.login.failure}";

    /** The key of the message: retry because of illegal transition */
    public static final String ERRORS_APP_ILLEGAL_TRANSITION = "{errors.app.illegal.transition}";

    /** The key of the message: 削除されています。 */
    public static final String ERRORS_APP_DB_ALREADY_DELETED = "{errors.app.db.already.deleted}";

    /** The key of the message: 更新されています。 */
    public static final String ERRORS_APP_DB_ALREADY_UPDATED = "{errors.app.db.already.updated}";

    /** The key of the message: すでに登録されています。 */
    public static final String ERRORS_APP_DB_ALREADY_EXISTS = "{errors.app.db.already.exists}";

    /** The key of the message: システムエラーが発生しました。 */
    public static final String ERRORS_APP_SYSTEM_ERROR = "{errors.app.system.error}";

    /** The key of the message: システムエラーが発生しました。 */
    public static final String ERRORS_APP_DATABASE_CONNECTION = "{errors.app.database.connection}";

    /**
     * Add the created action message for the key 'constraints.AssertFalse.message' with parameters.
     * <pre>
     * message: データが不正です。
     * comment: ---------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsAssertFalseMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_AssertFalse_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.AssertTrue.message' with parameters.
     * <pre>
     * message: データが不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsAssertTrueMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_AssertTrue_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.DecimalMax.message' with parameters.
     * <pre>
     * message: {value}${inclusive == true ? '以下' : '未満'}で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsDecimalMaxMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_DecimalMax_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.DecimalMin.message' with parameters.
     * <pre>
     * message: {value}${inclusive == true ? '以上' : 'より大きな値'}で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsDecimalMinMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_DecimalMin_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Digits.message' with parameters.
     * <pre>
     * message: numeric value out of bounds (&lt;{integer} digits&gt;.&lt;{fraction} digits&gt; expected)
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param integer The parameter integer for message. (NotNull)
     * @param fraction The parameter fraction for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsDigitsMessage(String property, String integer, String fraction) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Digits_MESSAGE, integer, fraction));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Future.message' with parameters.
     * <pre>
     * message: 未来日付を入れてください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsFutureMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Future_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Max.message' with parameters.
     * <pre>
     * message: {value}以下で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsMaxMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Max_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Min.message' with parameters.
     * <pre>
     * message: {value}以上で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsMinMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Min_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.NotNull.message' with parameters.
     * <pre>
     * message: 入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsNotNullMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_NotNull_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Null.message' with parameters.
     * <pre>
     * message: 入力しないでください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsNullMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Null_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Past.message' with parameters.
     * <pre>
     * message: 過去日付を入れてください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsPastMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Past_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Pattern.message' with parameters.
     * <pre>
     * message: 正規表現"{regexp}"に一致させてください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param regexp The parameter regexp for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsPatternMessage(String property, String regexp) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Pattern_MESSAGE, regexp));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Size.message' with parameters.
     * <pre>
     * message: {min}〜{max}文字で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsSizeMessage(String property, String min, String max) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Size_MESSAGE, min, max));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.CreditCardNumber.message' with parameters.
     * <pre>
     * message: クレジットカード番号の形式で入力してください。
     * comment: -------------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsCreditCardNumberMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_CreditCardNumber_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.EAN.message' with parameters.
     * <pre>
     * message: invalid {type} barcode
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param type The parameter type for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsEanMessage(String property, String type) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_EAN_MESSAGE, type));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Email.message' with parameters.
     * <pre>
     * message: メールアドレスの形式で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsEmailMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Email_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Length.message' with parameters.
     * <pre>
     * message: {min}〜{max}で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsLengthMessage(String property, String min, String max) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Length_MESSAGE, min, max));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.LuhnCheck.message' with parameters.
     * <pre>
     * message: The check digit for ${value} is invalid, Luhn Modulo 10 checksum failed
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsLuhnCheckMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_LuhnCheck_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Mod10Check.message' with parameters.
     * <pre>
     * message: The check digit for ${value} is invalid, Modulo 10 checksum failed
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsMod10CheckMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Mod10Check_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Mod11Check.message' with parameters.
     * <pre>
     * message: The check digit for ${value} is invalid, Modulo 11 checksum failed
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsMod11CheckMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Mod11Check_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.ModCheck.message' with parameters.
     * <pre>
     * message: The check digit for ${value} is invalid, ${modType} checksum failed
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @param modType The parameter modType for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsModCheckMessage(String property, String value, String modType) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_ModCheck_MESSAGE, value, modType));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.NotBlank.message' with parameters.
     * <pre>
     * message: 入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsNotBlankMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_NotBlank_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.NotEmpty.message' with parameters.
     * <pre>
     * message: 入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsNotEmptyMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_NotEmpty_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.ParametersScriptAssert.message' with parameters.
     * <pre>
     * message: スクリプト"{script}"による評価が不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param script The parameter script for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsParametersScriptAssertMessage(String property, String script) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_ParametersScriptAssert_MESSAGE, script));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Range.message' with parameters.
     * <pre>
     * message: {min}〜{max}で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsRangeMessage(String property, String min, String max) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Range_MESSAGE, min, max));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.SafeHtml.message' with parameters.
     * <pre>
     * message: 安全ではないHTMLが含まれています。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsSafeHtmlMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_SafeHtml_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.ScriptAssert.message' with parameters.
     * <pre>
     * message: スクリプト"{script}"による評価が不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param script The parameter script for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsScriptAssertMessage(String property, String script) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_ScriptAssert_MESSAGE, script));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.URL.message' with parameters.
     * <pre>
     * message: URLの形式入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsUrlMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_URL_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.Required.message' with parameters.
     * <pre>
     * message: 入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsRequiredMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_Required_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'constraints.TypeAny.message' with parameters.
     * <pre>
     * message: {propertyType}で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param propertyType The parameter propertyType for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addConstraintsTypeAnyMessage(String property, String propertyType) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(CONSTRAINTS_TypeAny_MESSAGE, propertyType));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.MinPeopleNum.message' with parameters.
     * <pre>
     * message: {value}人以上を入力してください。
     * comment: -------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorMinPeopleNumMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_MinPeopleNum_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.MaxPeopleNum.message' with parameters.
     * <pre>
     * message: {value}人以内で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param value The parameter value for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorMaxPeopleNumMessage(String property, String value) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_MaxPeopleNum_MESSAGE, value));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.MinSize.message' with parameters.
     * <pre>
     * message: {min}文字以上で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorMinSizeMessage(String property, String min) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_MinSize_MESSAGE, min));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.MaxSize.message' with parameters.
     * <pre>
     * message: {max}文字以内で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorMaxSizeMessage(String property, String max) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_MaxSize_MESSAGE, max));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.MinSizeCollection.message' with parameters.
     * <pre>
     * message: {min}つ以上で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param min The parameter min for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorMinSizeCollectionMessage(String property, String min) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_MinSizeCollection_MESSAGE, min));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.MaxSizeCollection.message' with parameters.
     * <pre>
     * message: {max}つ以内で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param max The parameter max for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorMaxSizeCollectionMessage(String property, String max) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_MaxSizeCollection_MESSAGE, max));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.EqualsSize.message' with parameters.
     * <pre>
     * message: {equals}文字で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param equals The parameter equals for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorEqualsSizeMessage(String property, String equals) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_EqualsSize_MESSAGE, equals));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.Katakana.message' with parameters.
     * <pre>
     * message: カタカナで入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorKatakanaMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_Katakana_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.Tel.message' with parameters.
     * <pre>
     * message: 電話番号を正しい形式で入力してください[ハイフン付きで入力してください]。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorTelMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_Tel_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ZipCode.message' with parameters.
     * <pre>
     * message: 郵便番号の形式[XXX-XXXX]で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorZipCodeMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ZipCode_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.AlphaNumMix.message' with parameters.
     * <pre>
     * message: 英数字混合で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorAlphaNumMixMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_AlphaNumMix_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.BankAccountName.message' with parameters.
     * <pre>
     * message: 半角数字、半角英字(大文字)、半角カナ、一部記号のみで入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorBankAccountNameMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_BankAccountName_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.Password.Mismatch.message' with parameters.
     * <pre>
     * message: パスワードとパスワード(確認)が一致してません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorPasswordMismatchMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_Password_Mismatch_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ShopAnyRequired.message' with parameters.
     * <pre>
     * message: 店舗表示時は席のみ手数料またはコースのいずれかは必須です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorShopAnyRequiredMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ShopAnyRequired_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ShopNotHide.message' with parameters.
     * <pre>
     * message: 催行日前の予約で使用されているため、非表示にはできません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorShopNotHideMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ShopNotHide_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.WeekCd.duplicate.message' with parameters.
     * <pre>
     * message: 営業時間の曜日が重複しています。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorWeekCdDuplicateMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_WeekCd_DUPLICATE_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReservePeopleNum.message' with parameters.
     * <pre>
     * message: コースに指定されている人数と予約人数が一致してません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReservePeopleNumMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReservePeopleNum_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveStatusCdReserveFix.message' with parameters.
     * <pre>
     * message: 予約ステータス[予約確定]の場合、予約店舗を入れる必要があります。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveStatusCdReserveFixMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveStatusCdReserveFix_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveShopCandidate.duplicate.message' with parameters.
     * <pre>
     * message: 予約店舗候補に同じ店舗は選択できません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveShopCandidateDuplicateMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveShopCandidate_DUPLICATE_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveShopCandidate.required.message' with parameters.
     * <pre>
     * message: 希望候補またはNG候補以外のコースは全て選択してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveShopCandidateRequiredMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveShopCandidate_REQUIRED_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.Priority.duplicate.message' with parameters.
     * <pre>
     * message: 同じ候補番号は選択できません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorPriorityDuplicateMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_Priority_DUPLICATE_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.SeatOnlyAndCourse.message' with parameters.
     * <pre>
     * message: 予約確定または空席あり状態では、席のみまたはコースのいずれかは必須です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorSeatOnlyAndCourseMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_SeatOnlyAndCourse_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.SeatOnlyReserve.message' with parameters.
     * <pre>
     * message: {0}では席のみ予約はできません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorSeatOnlyReserveMessage(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_SeatOnlyReserve_MESSAGE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.NoticeMessage.message' with parameters.
     * <pre>
     * message: 予約変更時は、通知メッセージは必須です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorNoticeMessageMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_NoticeMessage_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveLogTypeCd.message' with parameters.
     * <pre>
     * message: 選択したメール・Fax送信タイプと予約データの整合性がとれてません。(予約ステータスが不正、店舗FAX番号がないなど)
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveLogTypeCdMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveLogTypeCd_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveShopData.invalid.message' with parameters.
     * <pre>
     * message: 選択した店舗は無効です。選択店舗の表示フラグを確認してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveShopDataInvalidMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveShopData_INVALID_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveCourseData.invalid.message' with parameters.
     * <pre>
     * message: 選択したコース(料金)は無効です。選択コースの表示フラグまたは有効期間を確認してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveCourseDataInvalidMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveCourseData_INVALID_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveCourseEffectiveTime.outside.message' with parameters.
     * <pre>
     * message: 選択したコースはご指定の開催日時に終了しています。コースを変更してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveCourseEffectiveTimeOutsideMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveCourseEffectiveTime_OUTSIDE_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ReserveShopCandidate.entry.failed.message' with parameters.
     * <pre>
     * message: 店舗情報が更新された可能性があります。お手数ですが、始めからやり直してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorReserveShopCandidateEntryFailedMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ReserveShopCandidate_ENTRY_FAILED_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.CourseCsvDataSize.mismatch.message' with parameters.
     * <pre>
     * message: CSVファイルのカラム数が規定値と一致してません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorCourseCsvDataSizeMismatchMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_CourseCsvDataSize_MISMATCH_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.ImageData.not.found.message' with parameters.
     * <pre>
     * message: 画像名に対する画像ファイルが存在しません。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorImageDataNotFoundMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_ImageData_NOT_FOUND_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.XTypeBoolean.message' with parameters.
     * <pre>
     * message: [true/false]または[0/1]で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorXTypeBooleanMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_XTypeBoolean_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.LocalTimeFormat.message' with parameters.
     * <pre>
     * message: フォーマットが不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorLocalTimeFormatMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_LocalTimeFormat_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.validator.LocalDateTimeFormat.message' with parameters.
     * <pre>
     * message: フォーマットが不正です。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppValidatorLocalDateTimeFormatMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_VALIDATOR_LocalDateTimeFormat_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.converter.Valid.message' with parameters.
     * <pre>
     * message: 値が不正です。正しい値を入力してください。
     * comment: -------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppConverterValidMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_CONVERTER_Valid_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.converter.Number.message' with parameters.
     * <pre>
     * message: 数字を入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppConverterNumberMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_CONVERTER_Number_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.converter.Date.message' with parameters.
     * <pre>
     * message: 正しい日付を入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppConverterDateMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_CONVERTER_Date_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.converter.Time.message' with parameters.
     * <pre>
     * message: 正しい時間を入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppConverterTimeMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_CONVERTER_Time_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'app.converter.DateTime.message' with parameters.
     * <pre>
     * message: 正しい日時を入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addAppConverterDateTimeMessage(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(APP_CONVERTER_DateTime_MESSAGE));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.number' with parameters.
     * <pre>
     * message: input number for {0}
     * comment: -------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsNumber(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_NUMBER, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.same.value' with parameters.
     * <pre>
     * message: same value is selected in {0}
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsSameValue(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_SAME_VALUE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.greater.than' with parameters.
     * <pre>
     * message: greater than {0}
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsGreaterThan(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_GREATER_THAN, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.less.than' with parameters.
     * <pre>
     * message: less than {0}
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsLessThan(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_LESS_THAN, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.greater.equals' with parameters.
     * <pre>
     * message: greater equals {0}
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsGreaterEquals(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_GREATER_EQUALS, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.less.equals' with parameters.
     * <pre>
     * message: {0}つ以内で入力してください。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsLessEquals(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_LESS_EQUALS, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.required.at.least.one' with parameters.
     * <pre>
     * message: input {0} at least one
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsRequiredAtLeastOne(String property, String arg0) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_REQUIRED_AT_LEAST_ONE, arg0));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.required.or' with parameters.
     * <pre>
     * message: input either {0} or {1}
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @param arg1 The parameter arg1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsRequiredOr(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_REQUIRED_OR, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.upload.size' with parameters.
     * <pre>
     * message: Uploading failed, because actual size {0} bytes exceeded limit size {1} bytes.
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @param arg0 The parameter arg0 for message. (NotNull)
     * @param arg1 The parameter arg1 for message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsUploadSize(String property, String arg0, String arg1) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_UPLOAD_SIZE, arg0, arg1));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.empty.login' with parameters.
     * <pre>
     * message: メールアドレスかパスワードが間違っています。
     * comment: ----------------
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsEmptyLogin(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_EMPTY_LOGIN));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.already.exists' with parameters.
     * <pre>
     * message: すでに存在します。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAlreadyExists(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_ALREADY_EXISTS));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.login.failure' with parameters.
     * <pre>
     * message: ログインに失敗しました。
     * comment: - - - - - - - - - -/
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsLoginFailure(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_LOGIN_FAILURE));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.illegal.transition' with parameters.
     * <pre>
     * message: retry because of illegal transition
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppIllegalTransition(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_ILLEGAL_TRANSITION));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.db.already.deleted' with parameters.
     * <pre>
     * message: 削除されています。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppDbAlreadyDeleted(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_DB_ALREADY_DELETED));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.db.already.updated' with parameters.
     * <pre>
     * message: 更新されています。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppDbAlreadyUpdated(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_DB_ALREADY_UPDATED));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.db.already.exists' with parameters.
     * <pre>
     * message: すでに登録されています。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppDbAlreadyExists(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_DB_ALREADY_EXISTS));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.system.error' with parameters.
     * <pre>
     * message: システムエラーが発生しました。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppSystemError(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_SYSTEM_ERROR));
        return this;
    }

    /**
     * Add the created action message for the key 'errors.app.database.connection' with parameters.
     * <pre>
     * message: システムエラーが発生しました。
     * </pre>
     * @param property The property name for the message. (NotNull)
     * @return this. (NotNull)
     */
    public DbfluteMessages addErrorsAppDatabaseConnection(String property) {
        assertPropertyNotNull(property);
        add(property, new ActionMessage(ERRORS_APP_DATABASE_CONNECTION));
        return this;
    }
}
