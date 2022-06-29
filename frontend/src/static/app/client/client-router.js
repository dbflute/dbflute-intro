import { getCurrentRoute, router } from '@riotjs/route'

export function getCurrentClientRoute() {
  const currentRoute = getCurrentRoute()
  const paths = currentRoute.split('/')
  const params = paths.length === 4 ? {
    projectName: paths[1],
    clientMenuType: paths[2],
    clientMenuName: paths[3]
  } : {}
  return { path: currentRoute, params }
}

/**
 * クライアント画面のURL（ルーティング）の定義
 * @type { Routes }
 */
export const routes = Object.freeze({
  executeDocuments: {
    path: 'client/:projectName/execute/documents',
    open: (projectName) => {
      router.push(`client/${projectName}/execute/documents`)
    }
  },
  executeSchemaSyncCheck: {
    path: 'client/:projectName/execute/schema-sync-check',
    open: (projectName) => {
      router.push(`client/${projectName}/execute/schema-sync-check`)
    }
  },
  executeReplaceSchema: {
    path: 'client/:projectName/execute/replace-schema',
    open: (projectName) => {
      router.push(`client/${projectName}/execute/replace-schema`)
    }
  },
  executeAlterCheck: {
    path: 'client/:projectName/execute/alter-check',
    open: (projectName) => {
      router.push(`client/${projectName}/execute/alter-check`)
    }
  },
  executeSchemaPolicyCheck: {
    path: 'client/:projectName/execute/schema-policy-check',
    open: (projectName) => {
      router.push(`client/${projectName}/execute/schema-policy-check`)
    }
  },
  settingsDatabaseInfo: {
    path: 'client/:projectName/settings/database-info',
    open: (projectName) => {
      router.push(`client/${projectName}/settings/database-info`)
    }
  },
  filesLogs: {
    path: 'client/:projectName/files/logs',
    open: (projectName) => {
      router.push(`client/${projectName}/files/logs`)
    }
  },
})
