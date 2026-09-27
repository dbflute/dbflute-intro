import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

import Raw from '../../../components/common/raw.riot'
import { AlterFile } from './types'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  /** チェック済みのAlterDDL */
  checkedFiles: AlterFile[]
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface AlterCheckCheckedFiles extends IntroRiotComponent<Props, never> {
  /**
   * ファイルの表示・非表示を切り替えます
   * @param file クリックされたファイルのオブジェクト
   */
  clickFileName(file: AlterFile): void
}

export default withIntroTypes<AlterCheckCheckedFiles>({
  components: {
    Raw,
  },
  clickFileName(file: AlterFile) {
    file.show = !file.show
    this.update()
  },
})
