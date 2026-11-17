// done jflute 定数名expectedSubjectItems が ifの主語(Subject)と少々紛らわしいので... (2026/11/17)
// expectedCandidateItems (Expectedの候補項目) とかぼかした方が無難かも。(nonIsも含まれているわけだし)
// 本来ifのSubjectとthen(Expected)のSubjectって対照的なんだけど、UI的にはifの主語の方を強くSubjectとして打ち出しているというのもあり。
// あーいやでも、tsの方見ると onChangeSubjectVerb() とかもう (Expectedの) Subject って前面に押し出してるから、
// これはこのままの方がいいか。っていうコメントを残すことでおしまいにする。
/**
 * Expected (thenのtheme) の主語の候補項目。(isなし表現も含む)
 * 現時点(2026/11/17)ではlabelとvalueは一致するが、内部制御変更の可能性のために区別している。
 * typeは "isあり表現" と "isなし表現" の区別。
 */
export const expectedSubjectItems = [
  {
    label: '',
    value: null,
    type: 'nonIs',
  },
  {
    label: 'bad',
    value: 'bad',
    type: 'nonIs',
  },
  {
    label: 'hasPK',
    value: 'hasPK',
    type: 'nonIs',
  },
  {
    label: 'upperCaseBasis',
    value: 'upperCaseBasis',
    type: 'nonIs',
  },
  {
    label: 'lowerCaseBasis',
    value: 'lowerCaseBasis',
    type: 'nonIs',
  },
  {
    label: 'identityIfPureIDPK',
    value: 'identityIfPureIDPK',
    type: 'nonIs',
  },
  {
    label: 'sequenceIfPureIDPK',
    value: 'sequenceIfPureIDPK',
    type: 'nonIs',
  },
  {
    label: 'hasCommonColumn',
    value: 'hasCommonColumn',
    type: 'nonIs',
  },
  {
    label: 'hasAlias',
    value: 'hasAlias',
    type: 'nonIs',
  },
  {
    label: 'hasComment',
    value: 'hasComment',
    type: 'nonIs',
  },
  {
    label: 'tableName is',
    value: 'tableName is',
    type: 'hasIs',
  },
  {
    label: 'alias is',
    value: 'alias is',
    type: 'hasIs',
  },
  {
    label: 'comment is',
    value: 'comment is',
    type: 'hasIs',
  },
  {
    label: 'pkName is',
    value: 'pkName is',
    type: 'hasIs',
  },
  {
    label: 'fkName is',
    value: 'fkName is',
    type: 'hasIs',
  },
  {
    label: 'uniqueName is',
    value: 'uniqueName is',
    type: 'hasIs',
  },
  {
    label: 'indexName is',
    value: 'indexName is',
    type: 'hasIs',
  },
  {
    label: 'pk_columnName is',
    value: 'pk_columnName is',
    type: 'hasIs',
  },
  {
    label: 'pk_dbType is',
    value: 'pk_dbType is',
    type: 'hasIs',
  },
  {
    label: 'pk_size is',
    value: 'pk_size is',
    type: 'hasIs',
  },
  {
    label: 'pk_dbType_with_size is',
    value: 'pk_dbType_with_size is',
    type: 'hasIs',
  },
]
