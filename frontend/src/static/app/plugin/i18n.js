import i18next from 'i18next';
import i18n_ja from '../../assets/i18n/locale-ja.json'
import i18n_en from '../../assets/i18n/locale-en.json'

/**
 * 表示テキストの国際化対応の初期化処理を行います。
 * index.jsなどで import './plugin/i18n' するだけでOKです。
 */
i18next.init({
  lng: 'ja',
  debug: false,
  resources: {
    en: {
      translation: i18n_en
    },
    ja: {
      translation: i18n_ja
    },
  }
});
