import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

import Raw from '../../../components/common/raw.riot'
import { AlterDDLFile } from './types-alter'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  /** チェック済みのAlterDDL */
  checkedDDLFiles: AlterDDLFile[]
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
  clickFileName(ddlFile: AlterDDLFile): void
}

export default withIntroTypes<AlterCheckCheckedFiles>({
  components: {
    Raw,
  },
  clickFileName(ddlFile: AlterDDLFile) {
    ddlFile.show = !ddlFile.show
    this.update()
  },
})
