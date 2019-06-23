<main>
  <div class="ui text container">
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
      </tr>
      </tbody>
    </table>

    <h2>DBFlute Engine</h2>
    <button type="button" class="ui button primary" onclick="{ showDownloadModal }">
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
          <input type="button" class="ui negative button" value="Remove" onclick="{ removeEngine.bind(this,version) }" />
        </td>
      </tr>
      </tbody>
    </table>
    <h3>
      <small class="text-info">System Info</small>
    </h3>
    <div class="ui stackable four column grid">
      <div class="column" each="{ key, value in manifest }">
        <small>{ value } = { key }</small>
      </div>
    </div>
  </div>

  <su-modal modal="{ downloadModal }" ref="downloadModal">
    <div class="description">
      The latest version is { opts.modal.latestVersion.latestReleaseVersion } now.
      <form class="ui form">
        <div class="required field">
          <label>Version</label>
          <input type="text" ref="version" value="{ opts.modal.latestVersion.latestReleaseVersion }">
        </div>
      </form>
    </div>
  </su-modal>

  <su-modal modal="{ processModal }" ref="processModal">
    <div class="description">
      Downloading...
    </div>
  </su-modal>

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
    this.latestVersion = {} // latest engin verion

    this.downloadModal = {
      header: 'DBFlute Engine Download',
      closable: true,
      buttons:  [
        {
          text: 'Download',
          action: 'downloadEngine'
        }
      ],
      latestVersion: {}
    }

    this.processModal = {
      closable: false
    }

    // ===================================================================================
    //                                                                          Basic Data
    //                                                                          ==========
    this.introManifest = () => {
      ApiFactory.manifest().then((json) => {
        self.manifest = json
        self.update()
      })
    }

    this.engineVersions = () => {
      ApiFactory.engineVersions().then((json) => {
        self.versions = json
        self.update()
      })
    }

    this.latestVersion = () => {
      ApiFactory.engineLatest().then((json) => {
        self.downloadModal.latestVersion = json
        self.update()
      })
    }

    this.configuration = () => {
      ApiFactory.configuration().then((json) => {
        self.configuration = json
        self.update()
      })
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareClientList = () => {
      ApiFactory.clientList().then((json) => {
        if (json.length > 0) {
          self.clientList = json
          self.update()
        } else {
          self.goToWelcome()
        }
      })
    }

    this.goToClient = (client) => {
      route('operate/' + client.projectName + '/execute/documents')
    }

    this.goToClientCreate = () => {
      route('create')
    }

    this.goToWelcome = () => {
      route('welcome')
    }

    this.goToSettings = (client) => {
      route(`settings/${client.projectName}`)
    }

    // ===================================================================================
    //                                                                              Engine
    //                                                                              ======
    this.showDownloadModal = () => {
      self.refs.downloadModal.show()
    }

    this.removeEngine = (version) => {
      let params = { version: version }
      ApiFactory.removeEngine(params).finally(() => {
        this.engineVersions()
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.introManifest()
      this.engineVersions()
      this.latestVersion()
      this.configuration()
      this.prepareClientList()

      this.refs.downloadModal.on('downloadEngine', () => {
        self.refs.processModal.show()
        let downloadModal = self.refs.downloadModal
        let version = downloadModal.refs.version.value
        ApiFactory.downloadEngine({version}).finally(() => {
          self.refs.processModal.hide()
          self.engineVersions()
        })
      })
    })
  </script>
</main>
