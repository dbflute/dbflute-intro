<schema-policy-check-statement-list>
  <ui-list items="{ opts.statements }" onsorted="{ sortStatement }" sortable="true" options="{ prepareListOptions() }">
    <div class="content">
      <div class="header" if="{ !props.options.isIncludeComment(item) }">
        { item }
      </div>
      <div class="header" if="{ props.options.isIncludeComment(item) }">
        { props.options.extractStatement(item) }
      </div>
      <div if="{ props.options.isIncludeComment(item) }">
        <span class="frm">&#61&gt;{ props.options.extractComment(item) }</span>
      </div>
    </div>
    <i class="delete link icon" onclick="{ props.options.deleteStatement.bind(this, props.options.mapType, item) }"/>
  </ui-list>

  <style>
    ui-list .delete.link.icon {
      display: none;
    }

    ui-list .item:hover .delete.link.icon {
      display: inline-block;
    }
  </style>

  <script>
    import _ApiFactory from '../../../common/factory/ApiFactory.js'

    // =======================================================================================
    //                                                                              Properties
    //                                                                              ==========
    const ApiFactory = new _ApiFactory()

    let self = this

    self.props = {
      clientName: self.opts.clientname,
      mapType: self.opts.maptype,
      deleteStatement: self.opts.deletestatement,
    }

    self.state = {
      statements: self.opts.statements
    }

    // =======================================================================================
    //                                                                              Life Cycle
    //                                                                              ==========
    this.on('update', () => {
      this.state = {
        statements: self.opts.statements
      }
    })

    // =======================================================================================
    //                                                                               Functions
    //                                                                               =========
    /**
     * ステートメントを並び替えます
     * @param fromIndex {number} - 並び替え元のステートメントの並び順
     * @param toIndex {number} - 並び替え先のステートメントの並び順
     */
    this.sortStatement = (fromIndex, toIndex) => {
      if (fromIndex === toIndex) {
        return
      }
      ApiFactory.sortSchemapolicyStatement(self.props.clientName, {
        mapType: self.props.mapType,
        fromIndex,
        toIndex
      })
    }

    /**
     * ステートメント文にコメントを含んでいるかを判定します
     * @param statement {string} ステートメント文
     * @return {boolean} true:コメントを含んでいる, false:含んでいない
     */
    this.isIncludeComment = (statement) => {
      return statement.includes('=>')
    }

    /**
     * ステートメント文からコメントを除去します
     * @param statement {string} ステートメント文
     * @return {string} コメントを除去したステートメント文
     */
    this.extractStatement = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return statement
      }
      const splitStatements = statement.split('=>')
      return splitStatements[0]
    }

    /**
     * ステートメント文からコメントを抽出します
     * @param statement {string} ステートメント文
     * @return {string} ステートメント文中のコメント
     */
    this.extractComment = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return ''
      }
      const splitStatements = statement.split('=>')
      return splitStatements[1]
    }

    /**
     * リストに渡すオプションを構築します
     * @return {any} リストに渡すオプション
     */
    this.prepareListOptions = () => {
      return {
        isIncludeComment: self.isIncludeComment,
        extractStatement: self.extractStatement,
        extractComment: self.extractComment,
        deleteStatement: self.props.deleteStatement,
        mapType: self.props.mapType
      }
    }
  </script>
</schema-policy-check-statement-list>
