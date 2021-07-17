<schema-policy-check-statement-form-docuement-link>
  <span>
    <a class="help link" href="{ buildDocumentUrl() }" target="_blank">document</a>
  </span>

  <script>
    const self = this
    const documentUrl = {
      table : 'http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementthentheme',
      column : 'http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementthentheme'
    }

    self.props = {
      formType: self.opts.formtype,
    }

    self.buildDocumentUrl = () => {
      const formType = self.props.formType
      if (formType === 'tableMap') {
        return documentUrl.table
      } else {
        return documentUrl.column
      }
    }
  </script>
</schema-policy-check-statement-form-docuement-link>
