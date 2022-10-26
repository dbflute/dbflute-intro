import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

import Raw from '../../../components/common/raw.riot'

interface AlterCheckCheckedFilesProps {
  checkedfiles: any[]
}

interface AlterCheckCheckedFiles extends IntroRiotComponent<AlterCheckCheckedFilesProps> {
  clickFileName(file: any): void
}

export default withIntroTypes<AlterCheckCheckedFiles>({
  components: {
    Raw,
  },
  /**
   * ファイルの表示・非表示を切り替えます
   * @param file クリックされたファイルのオブジェクト (NotNull)
   */
  clickFileName(file) {
    file.show = !file.show
  },
})
