<schema-policy-check-statement-form-docuement-link>
   <!-- ClientのSchemaPolicyCheckのStatement追加のExpected項目のdcumentリンク (written at 2022/02/10)
   機能:
    o tableMapとcolumnMapでそれぞれのドキュメントのリンクを出す

   作りの特徴:
    o propsでformtypeを受け取って、URLを出しわけしている。
   -->
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

    /**
     * ドキュメントのURLを返す
     * @return Expected項目のdocumentURL (NotNull)
     */
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
