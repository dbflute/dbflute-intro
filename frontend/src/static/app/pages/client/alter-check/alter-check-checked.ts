import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import AlterCheckCheckedFiles from './alter-check-checked-files.riot'
import { AlterZipState } from './types'

interface AlterCheckCheckedProps {
  checkedzip: AlterZipState
  unreleaseddir: AlterZipState
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
   * @return {boolean} true:存在する, false:zipファイルが存在しない or ファイルが0件
   */
  existsCheckedFiles(): boolean {
    const checkedZip = this.props.checkedzip
    return checkedZip !== undefined && checkedZip.checkedFiles !== undefined && checkedZip.checkedFiles.length > 0
  },

  /**
   * 未リリースのAlterDDLが存在するかを判定する
   * @return {boolean} true:存在する, false:未リリースDDL用ディレクトリが存在しない or ファイルが0件
   */
  existsUnreleasedFiles(): boolean {
    const unreleasedDir = this.props.unreleaseddir
    return unreleasedDir !== undefined && unreleasedDir.checkedFiles !== undefined && unreleasedDir.checkedFiles.length > 0
  },
})
