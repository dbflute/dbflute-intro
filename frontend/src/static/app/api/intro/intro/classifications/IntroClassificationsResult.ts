/*
 * Copyright 2014-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

/**
 * The bean class as return for remote API of GET /intro/classifications.
 * @author FreeGen
 */
type IntroClassificationsResult = {
  /** (NotNull) */
  targetDatabaseList: Array<IntroClassificationsResult_DatabaseDefPart>

  /** (NotNull) */
  targetLanguageList: Array<IntroClassificationsResult_LanguageDefPart>

  /** (NotNull) */
  targetContainerList: Array<IntroClassificationsResult_ContainerDefPart>
}

/**
 * The part class of DatabaseDefPart.
 * @author FreeGen
 */
type IntroClassificationsResult_DatabaseDefPart = {
  /** (Required) */
  databaseCode: string

  /** (Required) */
  databaseName: string

  /** (Required) */
  driverName: string

  /** (Required) */
  urlTemplate: string

  /** (NullAllowed) */
  defaultSchema?: string

  /** (Required) */
  schemaRequired: boolean

  /** (Required) */
  schemaUpperCase: boolean

  /** (Required) */
  userInputAssist: boolean

  /** (Required) */
  embeddedJar: boolean
}

/**
 * The part class of LanguageDefPart.
 * @author FreeGen
 */
type IntroClassificationsResult_LanguageDefPart = {
  /** (Required) */
  languageCode: string

  /** (Required) */
  languageName: string
}

/**
 * The part class of ContainerDefPart.
 * @author FreeGen
 */
type IntroClassificationsResult_ContainerDefPart = {
  /** (Required) */
  containerCode: string

  /** (Required) */
  containerName: string
}
