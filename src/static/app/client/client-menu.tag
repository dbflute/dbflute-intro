<client-menu>
  <div class="ui left fixed small inverted vertical menu">
    <menu-home></menu-home>
    <div class="item">
      <div class="header">Execute</div>
      <div class="ui tiny inverted vertical menu">
        <a class="{ 'active': isActiveItem('execute', 'documents') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'documents') }">
          Documents
        </a>
        <a class="{ 'active': isActiveItem('execute', 'schema-policy-check') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'schema-policy-check') }">
          Schema Policy Check
        </a>
        <a class="{ 'active': isActiveItem('execute', 'schema-sync-check') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'schema-sync-check') }">
          Schema Sync Check
        </a>
        <a class="{ 'active': isActiveItem('execute', 'replace-schema') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'replace-schema') }">
          Replace Schema
        </a>
        <a class="{ 'active': isActiveItem('execute', 'alter-check') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'alter-check') }">
          Alter Check
        </a>
      </div>
    </div>
    <div class="item">
      <div class="header">General Settings</div>
      <div class="ui tiny inverted vertical menu">
        <a class="{ 'active': isActiveItem('settings', 'database-info') } item"
           onclick="{ goToMenuItem.bind(this, 'settings', 'database-info') }">
          Database Info
        </a>
      </div>
    </div>
    <div class="item">
      <div class="header">Files</div>
      <div class="ui tiny inverted vertical menu">
        <a class="{ 'active': isActiveItem('files', 'logs') } item"
           onclick="{ goToMenuItem.bind(this, 'files', 'logs') }">
          Logs
        </a>
      </div>
    </div>
  </div>
  <style>
    .ui.tiny.inverted.vertical.menu {
      margin-left: 0px;
    }
  </style>
  <script>
    this.isActiveItem = (menuType, menuName) => {
      return menuType === this.opts.clientMenuType && menuName === this.opts.clientMenuName
    }
    this.goToMenuItem = (menuType, menuName) => {
      route(`client/${this.opts.projectName}/${menuType}/${menuName}`)
    }
  </script>
</client-menu>
