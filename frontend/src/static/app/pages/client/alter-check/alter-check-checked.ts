import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import AlterCheckCheckedFiles from './alter-check-checked-files.riot'
import { AlterDir, AlterZip } from './types'

interface AlterCheckCheckedProps {
  /** チェック済みのAlterDDL zip */
  checkedZip: AlterZip
  /** 未リリースチェック済みのAlterDDL */
  unreleasedDir: AlterDir
}

interface AlterCheckChecked extends IntroRiotComponent<AlterCheckCheckedProps, never> {
  existsCheckedFiles(): boolean
  existsUnreleasedFiles(): boolean
}

export default withIntroTypes<AlterCheckChecked>({
  components: {
    AlterCheckCheckedFiles,
  },
  /**
   * checked zipにAlterDDLが存在するかを判定する
   * @return {boolean} true:存在する, false:ファイルが0件
   */
  existsCheckedFiles(): boolean {
    return this.props.checkedZip.checkedFiles.length > 0
  },

  /**
   * 未リリースのAlterDDLが存在するかを判定する
   * @return {boolean} true:存在する, false:ファイルが0件
   */
  existsUnreleasedFiles(): boolean {
    return this.props.unreleasedDir.checkedFiles.length > 0
  },
})
