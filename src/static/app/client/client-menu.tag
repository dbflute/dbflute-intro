<client-menu>
  <div class="ui left fixed small inverted vertical menu">
    <a href="/" class="header item">
      <span class="glyphicon glyphicon-home"></span>
      DBFlute Intro
    </a>
    <div class="item">
      <div class="header">Execute</div>
      <div class="inverted menu">
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
      </div>
    </div>
    <div class="item">
      <div class="header">General Settings</div>
      <div class="inverted menu">
        <a class="{ 'active': isActiveItem('settings', 'database-info') } item"
           href="./#operate/{ opts.projectName }/settings/database-info">
          Database Info
        </a>
        <a class="{ 'active': isActiveItem('settings', 'schema-policy') } item"
           href="./#operate/{ opts.projectName }/settings/schema-policy">
          Schema Policy
        </a>
      </div>
    </div>
    <div class="item">
      <div class="header">Files</div>
      <div class="inverted menu">
        <a class="{ 'active': isActiveItem('files', 'logs') } item"
           href="./#operate/{ opts.projectName }/files/logs">
          Logs
        </a>
      </div>
    </div>
  </div>
  <script>
    this.isActiveItem = (menuType, menuName) => {
      return menuType === opts.clientMenuType && menuName == opts.clientMenuName
    }
  </script>
</client-menu>
