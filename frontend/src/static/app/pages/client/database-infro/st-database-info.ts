import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
}

type DatabaseInfoForm = {
  url: string
  schema: string
  user: string
  password: string
}

interface State {
  prepared: boolean
  settings?: DfpropSettingsResult
  form: DatabaseInfoForm
}

interface StDatabaseInfo extends IntroRiotComponent<Props, State> {
  onMounted(): void
  onChangeUrl(e: InputEvent): void
  onChangeSchema(e: InputEvent): void
  onChangeUser(e: InputEvent): void
  onChangePassword(e: InputEvent): void
  editClient(): Promise<void>
  prepareSettings(projectName: string): Promise<void>
  buildForm(settings?: DfpropSettingsResult): DatabaseInfoForm
  showToast(): void
}

export default withIntroTypes<StDatabaseInfo>({
  state: {
    prepared: false,
    settings: undefined,
    form: {
      url: '',
      schema: '',
      user: '',
      password: '',
    },
  },

  async onMounted(): Promise<void> {
    await this.prepareSettings(this.props.projectName)
  },

  onChangeUrl(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, url: value },
    })
  },

  onChangeSchema(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, schema: value },
    })
  },

  onChangeUser(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, user: value },
    })
  },

  onChangePassword(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, password: value },
    })
  },

  async editClient(): Promise<void> {
    const settings = this.state.settings
    if (!settings) {
      return
    }

    const body: DfpropSettingsEditBody = {
      client: {
        databaseCode: settings.databaseCode,
        languageCode: settings.languageCode,
        containerCode: settings.containerCode,
        packageBase: settings.packageBase,
        jdbcDriverFqcn: settings.jdbcDriverFqcn,
        dbfluteVersion: settings.dbfluteVersion,
        mainSchemaSettings: {
          url: this.state.form.url,
          schema: this.state.form.schema,
          user: this.state.form.user,
          password: this.state.form.password,
        },
      },
    }

    await api
      .updateSettings(this.props.projectName, body)
      .then(async () => {
        await this.prepareSettings(this.props.projectName)
        this.showToast()
      })
      .catch((_) => {
        // API Client で modal 出す以上のハンドリングはしない
      })
  },

  async prepareSettings(projectName: string): Promise<void> {
    const data = await api.settings(projectName)
    this.update({
      prepared: true,
      settings: data,
      form: this.buildForm(data),
    })
  },

  buildForm(settings?: DfpropSettingsResult): DatabaseInfoForm {
    const mainSchemaSettings = settings?.mainSchemaSettings
    return {
      url: mainSchemaSettings?.url || '',
      schema: mainSchemaSettings?.schema || '',
      user: mainSchemaSettings?.user || '',
      password: mainSchemaSettings?.password || '',
    }
  },

  showToast(): void {
    this.successToast({
      title: 'Setting Updated',
      message: undefined,
    })
  },
})
