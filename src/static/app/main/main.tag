<main>
  <div>
    <h2>DBFlute Client</h2>
    <input type="button" class="ui button primary" value="Create" onclick="{ goToClientCreate }" />
  </div>
  <table class="ui table">
    <thead>
      <tr>
        <th data-is="i18n">LABEL_projectName</th>
        <th data-is="i18n">LABEL_databaseCode</th>
        <th data-is="i18n">LABEL_languageCode</th>
        <th data-is="i18n">LABEL_containerCode</th>
        <th></th>
      </tr>
    </thead>
    <tbody class="list-group">
      <tr each="{client in clientList}">
        <td>
          <a onclick="{ goToClient.bind(this, client) }">{ client.projectName }</a>
        </td>
        <td>{ client.databaseCode }</td>
        <td>{ client.languageCode }</td>
        <td>{ client.containerCode }</td>
        <td class="right aligned">
          <input type="button" class="ui button primary" value="Settings" onclick="{ goToSettings.bind(this, client) }" />
        </td>
      </tr>
    </tbody>
  </table>

  <h2>DBFlute Engine</h2>
  <button type="button" class="ui button primary" onclick="{ downloadModal }">
    <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>Download
  </button>

  <table class="ui table">
    <thead>
      <tr>
        <th data-is="i18n">LABEL_engineVersion</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <tr each="{version in versions}">
        <td>{ version }</td>
        <td class="right aligned">
          <input type="button" class="ui button primary" value="Remove" onclick="{ removeEngine.bind(this,version) }" />
        </td>
      </tr>
    </tbody>
  </table>
  <h3>
    <small class="text-info">System Info</small>
  </h3>
  <div class="ui stackable four column grid">
    <div class="column" each="{ manifest }">
      <small>{ key } = { value }</small>
    </div>
  </div>

  <style>
    table+h2,
    table+h3 {
      margin-top: 3rem;
    }
  </style>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    import route from 'riot-route'

    const ApiFactory = new _ApiFactory()

    const self = this
    this.manifest = {} // intro manifest
    this.versions = [] // engine versions
    this.configuration = {} // intro configuration
    this.clientList = [] // existing clients

    // ===================================================================================
    //                                                                          Basic Data
    //                                                                          ==========
    this.manifest = function () {
      ApiFactory.manifest().then(function (json) {
        self.manifest = json
        self.update()
      })
    }

    this.engineVersions = function () {
      ApiFactory.engineVersions().then(function (json) {
        self.versions = json
        self.update()
      })
    }

    this.configuration = function () {
      ApiFactory.configuration().then(function (json) {
        self.configuration = json
        self.update()
      })
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareClientList = function () {
      ApiFactory.clientList().then(function (json) {
        if (json.length > 0) {
          self.clientList = json
          self.update()
        } else {
          //   $state.go('welcome') // if no client show welcome page
        }
      })
    }

    this.goToClient = function (client) {
      route('operate/' + client.projectName)
    }

    this.goToClientCreate = function () {
      route('create')
    }

    this.goToSettings = function (client) {
      route(`settings/${client.projectName}`)
    }

    // ===================================================================================
    //                                                                              Engine
    //                                                                              ======
    this.downloadModal = function () {
      //   let downloadInstance = $uibModal.open({
      //     templateUrl: 'app/main/download.html',
      //     controller: 'DownloadInstanceController',
      //     resolve: {
      //       engineLatest: function () {
      //         return ApiFactory.engineLatest()
      //       }
      //     }
      //   })

      //   downloadInstance.result.then(function (versions) {
      //     this.versions = versions
      //   })
    }

    this.removeEngine = function (version) {
      let params = { version: version }
      ApiFactory.removeEngine(params).then(function () {
        this.engineVersions()
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.manifest()
      this.engineVersions()
      this.configuration()
      this.prepareClientList()
    })
  </script>
</main>
