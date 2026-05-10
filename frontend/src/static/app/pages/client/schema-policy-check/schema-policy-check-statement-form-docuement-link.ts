import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'

type StatementMapType = 'tableMap' | 'columnMap'

interface Props {
  /** 対象とするマップ種別 (tableMap / columnMap) */
  formType: StatementMapType
}

interface SchemaPolicyCheckStatementFormDocuementLink extends IntroRiotComponent<Props, never> {
  /** ドキュメントの遷移先URLを返す。 */
  buildDocumentUrl(): string
}

/** マップ種別ごとのExpected項目ドキュメントURL。 */
const DOCUMENT_URL: Record<StatementMapType, string> = {
  tableMap: 'http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementthentheme',
  columnMap: 'http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementthentheme',
}

export default withIntroTypes<SchemaPolicyCheckStatementFormDocuementLink>({
  buildDocumentUrl() {
    return DOCUMENT_URL[this.props.formType]
  },
})
