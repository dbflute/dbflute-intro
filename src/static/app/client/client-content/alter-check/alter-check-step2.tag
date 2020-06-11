<alter-check-step2>
  <section show="{ isEditing() }">
    <h4 class="ui header">Step2. Execute AlterCheck</h4>

    <h5 class="ui header">Open Editing Alter SQL Files</h5>
    <alter-check-editting-files
      editingsqls="{ opts.editingsqls }"
      preparedfilename="{ opts.preparedfilename }"
      projectname="{ opts.projectname }" />

    <h5 class="ui header">Executor</h5>
    <alter-check-executor
      projectname="{ opts.projectname }"
    />

    <h5 class="ui header" show="{ showResult() }">Latest AlterCheck Result</h5>
    <button class="ui button blue" show="{ showLink() }" onclick="{ openAlterCheckResultHTML }">
      <i class="linkify icon" />Open Check Result HTML
    </button>
    <div class="latest-result">
      <latest-result />
    </div>
  </section>
  <script>
    const self = this

    this.showResult = () => {
      return self.opts.hasaltercheckresulthtml || (self.latestResult != null && self.latestResult.loaded)
    }

    this.showLink = () => {
      return self.opts.hasaltercheckresulthtml
    }

    this.openAlterCheckResultHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/altercheckresulthtml/')
    }
  </script>
</alter-check-step2>
