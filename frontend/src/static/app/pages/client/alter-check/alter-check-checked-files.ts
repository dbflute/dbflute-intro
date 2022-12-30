import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

import Raw from '../../../components/common/raw.riot'
import { AlterFile } from './types'

interface Props {
  checkedFiles: AlterFile[]
}

interface AlterCheckCheckedFiles extends IntroRiotComponent<Props, never> {
  clickFileName(file: AlterFile): void
}

export default withIntroTypes<AlterCheckCheckedFiles>({
  components: {
    Raw,
  },
  /**
   * ファイルの表示・非表示を切り替えます
   * @param file クリックされたファイルのオブジェクト (NotNull)
   */
  clickFileName(file: AlterFile) {
    file.show = !file.show
    this.update()
  },
})
