<main>
  <navbar />
  <div class="container">
    <div class="row">
      <div class="col-sm-3">
        <div>
          <h2>DBFlute Client</h2>
          <input type="button" class="btn btn-primary" value="Create" onclick="{ goToClientCreate }" />
        </div>
        <div class="container">
          <table class="table">
            <thead>
              <tr>
                <!-- <th>{ 'LABEL_projectName' | translate:translationData }</th>
                                    <th>{ 'LABEL_databaseCode' | translate:translationData }</th>
                                    <th>{ 'LABEL_languageCode' | translate:translationData }</th>
                                    <th>{ 'LABEL_containerCode' | translate:translationData }</th> -->
              </tr>
            </thead>
            <tbody class="list-group">
              <tr each="{client in clientList}">
                <td>
                  <a href="" class="list-group-item" onclick="{ goToClient.bind(this, client) }">{ client.projectName }</a>
                </td>
                <td>{ client.databaseCode }</td>
                <td>{ client.languageCode }</td>
                <td>{ client.containerCode }</td>
                <td>
                  <input type="button" class="btn btn-primary" value="Settings" onclick="{ goToSettings.bind(this, client) }" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div>
          <h2>DBFlute Engine</h2>
          <button type="button" class="btn btn-primary" onclick="{ downloadModal }">
            <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>Download
          </button>
        </div>
        <div class="container">
          <table class="table">
            <thead>
              <tr>
                <!-- <th>{ 'LABEL_engineVersion' | translate:translationData }</th> -->
                <th></th>
              </tr>
            </thead>
            <tbody class="list-group">
              <tr each="{version in versions}">
                <td>
                  <span class="list-group-item">{ version }</span>
                </td>
                <td>
                  <input type="button" class="btn btn-primary pull-right" value="Remove" onclick="{ removeEngine.bind(this,version) }" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
    <h3>
      <small class="text-info">System Info</small>
    </h3>
    <div class="row">
      <div class="col-sm-6 col-md-4" each="{ manifest }">
        <small class="text-info">{ key } = { value }</small>
      </div>
    </div>
  </div>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
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

    this.classifications = function () {
      ApiFactory.classifications().then(function (json) {
        self.classificationMap = json
        self.update()
      })
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareClientList = function () {
      ApiFactory.clientList().then(function (json) {
        // if (json.length > 0) 
        {
          self.clientList = json
          // } else {
          //   $state.go('welcome') // if no client show welcome page
          self.update()
        }
      })
    }

    this.goToClient = function (client) {
      //   $state.go('operate', { projectName: client.projectName })
    }

    this.goToClientCreate = function () {
      //   $state.go('create')
    }

    this.goToSettings = function (client) {
      //   $state.go('settings', { client: client, projectName: client.projectName })
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
      this.classifications()
      this.prepareClientList()
    })
  </script>
</main>