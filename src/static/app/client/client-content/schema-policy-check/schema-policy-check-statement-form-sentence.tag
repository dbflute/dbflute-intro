<schema-policy-check-statement-form-sentence>
  <div class="grouped fields required">

    <label>Sentence</label>
    <p>
      <schema-policy-check-statement-form-docuement-link formtype="{ props.formType }" />
      /
      <a onclick="{ showSample }">sample</a>
    </p>

    <div if="{ state.showSample }" class="ui info message">
      <div class="ui two column grid">
        <div class="column">
          <h5>Format</h5>
          <div class="ui segment">
            <span class="variable">SUBJECT</span> is (not) <span class="variable">VALUE</span>
          </div>
          <p>or</p>
          <div class="ui segment">
            <span class="variable">THEME</span>
          </div>
        </div>
        <div class="column">
          <h5>Sample</h5>
          <div class="ui inverted segment">
            <div class="comment">// FK constraint name must be FK_[tableName]_...</div>
            <div><span class="variable">fkName</span> is <span class="variable">prefix:FK_$$table$$</span></div>
            <div class="comment">// column DB type must be VARCHAR (only for column)</div>
            <div><span class="variable">dbType</span> is <span class="variable">VARCHAR</span></div>
            <div class="ui divider"></div>
            <div><span class="variable">hasPK</span> <span class="comment">// must has PK constraint</span></div>
            <div><span class="variable">notNull</span> <span class="comment">// must has NotNull constraint</span></div>
          </div>
          <a href="http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#example" target="_blank">more sample</a>
        </div>
      </div>
    </div>

    <schema-policy-check-statement-form-sentence-field />

    <div class="ui grid">
      <div class="four wide right floated column">
        <i class="plus link icon" style="float: right" onclick="{ handleFieldAdd }" />
        <div class="inline fields" style="float: right" show="{ needsCondition() }">
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="expected-mode" ref="isAnd" checked="checked" onchange="{ handleConditionChange }">
              <label>and</label>
            </div>
          </div>
          <div class="field">
            <div class="ui radio checkbox">
              <input type="radio" name="expected-mode" ref="isOr" onchange="{ handleConditionChange }">
              <label>or</label>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</schema-policy-check-statement-form-sentence>
