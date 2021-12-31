<schema-policy-check-statement-list>
  <div ref="statements" class="ui divided items segment">
    <div class="statement item" each="{ statement in opts.statements }">
      <div class="statement content">
        <div class="header" if="{ !isIncludeComment(statement) }">
          { statement }
        </div>
        <div class="header" if="{ isIncludeComment(statement) }">
          { extractStatement(statement) }
        </div>
        <div if="{ isIncludeComment(statement) }">
          <span class="frm">&#61&gt;{ extractComment(statement) }</span>
        </div>
      </div>
      <i class="statement delete link icon" onclick="{ props.deleteStatement.bind(this, props.mapType, statement) }"/>
    </div>
  </div>

  <script>
    import Sortable from 'sortablejs'
    import _ApiFactory from '../../../common/factory/ApiFactory.js'

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

    this.on('mount', () => {
      Sortable.create(self.refs.statements, {
        onEnd: (event) => {
          sortSchemaPolicy(event.oldIndex, event.newIndex)
    }
      })
    })

    this.on('update', () => {
      this.state = {
        statements: self.opts.statements
      }
    })

    this.sortSchemaPolicy = (fromIndex, toIndex) => {
      ApiFactory.sortSchemapolicyStatement(self.props.clientName, {
        mapType: self.props.mapType,
        fromIndex,
        toIndex
      })
    }

    this.isIncludeComment = (statement) => {
      return statement.includes('=>')
    }

    this.extractStatement = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return statement
      }
      const splitStatements = statement.split('=>')
      return splitStatements[0]
    }

    this.extractComment = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return ''
      }
      const splitStatements = statement.split('=>')
      return splitStatements[1]
    }
  </script>
</schema-policy-check-statement-list>
