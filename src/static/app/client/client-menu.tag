<client-menu>
  <div class="ui inverted vertical menu">
    <div class="header item">Execute</div>
    <a class="{ 'active': isActiveItem('execute', 'documents') } item"
       href="./#operate/{ opts.projectName }/execute/documents">
      Documents
    </a>
    <a class="{ 'active': isActiveItem('execute', 'schema-sync-check') } item"
       href="./#operate/{ opts.projectName }/execute/schema-sync-check">
      Schema Sync Check
    </a>
    <a class="{ 'active': isActiveItem('execute', 'replace-schema') } item"
       href="./#operate/{ opts.projectName }/execute/replace-schema">
      Replace Schema
    </a>
    <a class="{ 'active': isActiveItem('execute', 'alter-check') } item"
       href="./#operate/{ opts.projectName }/execute/alter-check">
      Alter Check
    </a>
    <a class="{ 'active': isActiveItem('execute', 'schema-policy-check') } item"
       href="./#operate/{ opts.projectName }/execute/schema-policy-check">
      Schema Policy Check
    </a>
    <div class="header item">Settings</div>
    <a class="{ 'active': isActiveItem('settings', 'schema-policy-check') } item"
      href="./#operate/{ opts.projectName }/settings/schema-policy-check">
      Schema Policy
    </a>
  </div>
  <script>
    this.isActiveItem = (menuType, menuName) => {
      return menuType === opts.clientMenuType && menuName == opts.clientMenuName
    }
  </script>
</client-menu>
