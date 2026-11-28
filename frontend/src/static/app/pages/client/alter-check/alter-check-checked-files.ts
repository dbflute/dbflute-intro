import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

import Raw from '../../../components/common/raw.riot'
import { AlterDDLFile } from './types-alter'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  // 注: checkedDDLFiles みたいに変数名に大文字続き入れると、.riot側で checkedDdlFiles にしないとヒットしない。
  // #thinking jflute でも state.editingDDLFiles で参照できてるとこもあるが何が違うんだろう？ props と stateで 違う？ (2026/10/13)
  /** チェック済みのAlterDDLたち */
  checkedFiles: AlterDDLFile[]
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface AlterCheckCheckedFiles extends IntroRiotComponent<Props, never> {
  /**
   * ファイルの表示・非表示を切り替えます
   * @param checkedFile クリックされたファイルのオブジェクト
   */
  clickFileName(checkedFile: AlterDDLFile): void
}

export default withIntroTypes<AlterCheckCheckedFiles>({
  components: {
    Raw,
  },
  clickFileName(checkedFile: AlterDDLFile) {
    checkedFile.showsContent = !checkedFile.showsContent
    this.update()
  },
})
