import i18next from 'i18next';
import i18n_ja from '../../assets/i18n/locale-ja.json'
import i18n_en from '../../assets/i18n/locale-en.json'

i18next.init({
  lng: 'ja',
  debug: true,
  resources: {
    en: {
      translation: i18n_en
    },
    ja: {
      translation: i18n_ja
    },
  }
});
