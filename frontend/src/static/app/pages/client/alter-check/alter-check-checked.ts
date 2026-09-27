import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import AlterCheckCheckedFiles from './alter-check-checked-files.riot'
import { AlterDir, AlterZip } from './types'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface AlterCheckCheckedProps {
  /** チェック済みのAlterDDL zip */
  checkedZip: AlterZip
  /** 未リリースチェック済みのAlterDDL */
  unreleasedDir: AlterDir
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface AlterCheckChecked extends IntroRiotComponent<AlterCheckCheckedProps, never> {
  // #thinking existsCheckedFiles() → existsCheckedZip() ってzipを入れた方が直感的かも by jflute (2026/09/27)
  // unreleasedの方も checkedFiles と言うプロパティは共通なので。
  /**
   * checked zipにAlterDDLが存在するかを判定する
   * @return true:存在する, false:ファイルが0件
   */
  existsCheckedFiles(): boolean

  /**
   * 未リリースのAlterDDLが存在するかを判定する
   * @return true:存在する, false:ファイルが0件
   */
  existsUnreleasedFiles(): boolean
}

export default withIntroTypes<AlterCheckChecked>({
  components: {
    AlterCheckCheckedFiles,
  },
  existsCheckedFiles(): boolean {
    return this.props.checkedZip.checkedFiles.length > 0
  },
  existsUnreleasedFiles(): boolean {
    return this.props.unreleasedDir.checkedFiles.length > 0
  },
})
