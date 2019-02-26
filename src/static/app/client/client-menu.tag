<client-menu>
  <div class="ui inverted vertical menu">
    <div class="header item">Execute</div>
    <a class="{ 'active': isActiveItem(ClientMenuMode.EX_DOCUMENTS) } item"
      onclick="{ clickItem.bind(this, ClientMenuMode.EX_DOCUMENTS) }">
      Documents
    </a>
    <a class="{ 'active': isActiveItem(ClientMenuMode.EX_SCHEMA_SYNC_CHECK) } item"
       onclick="{ clickItem.bind(this, ClientMenuMode.EX_SCHEMA_SYNC_CHECK) }">
      Schema Sync Check
    </a>
    <a class="{ 'active': isActiveItem(ClientMenuMode.EX_REPLACE_SCHEMA) } item"
      onclick="{ clickItem.bind(this, ClientMenuMode.EX_REPLACE_SCHEMA) }">
      Replace Schema
    </a>
    <a class="{ 'active': isActiveItem(ClientMenuMode.EX_ALTER_CHECK) } item"
      onclick="{ clickItem.bind(this, ClientMenuMode.EX_ALTER_CHECK) }">
      Alter Check
    </a>
    <a class="{ 'active': isActiveItem(ClientMenuMode.EX_SCHEMA_POLICY_CHECK) } item"
      onclick="{ clickItem.bind(this, ClientMenuMode.EX_SCHEMA_POLICY_CHECK) }">
      Schema Policy Check
    </a>
    <div class="header item">Settings</div>
    <a class="{ 'active': isActiveItem(ClientMenuMode.SET_SCHEMA_POLICY_CHECK) } item"
      onclick="{ clickItem.bind(this, ClientMenuMode.SET_SCHEMA_POLICY_CHECK) }">
      Schema Policy
    </a>
  </div>
  <script>
    import ClientMenuMode from '../common/ClientMenuMode.js'
    this.ClientMenuMode = ClientMenuMode

    this.isActiveItem = (itemName) => {
      return itemName === this.opts.activeItem
    }

    this.clickItem = (clientMenuMode, e) => {
      this.parent.update({ clientMenuMode })
    }
  </script>
</client-menu>
