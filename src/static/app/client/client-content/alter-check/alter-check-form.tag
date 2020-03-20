<alter-check-form>
  <div class="ui segment">
    <div class="ui two column very relaxed stackable grid">
      <div class="column">
        <alter-check-begin-form projectname="{ opts.projectname }" updatehandler="{ opts.updatehandler }" />
      </div>
      <div class="column">
        <alter-check-fix-form projectname="{ opts.projectname }" updatehandler="{ opts.updatehandler }" />
      </div>
    </div>
    <div class="ui vertical divider">
      Or
    </div>
  </div>
</alter-check-form>
