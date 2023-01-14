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
 * The bean class as param for remote API of POST /dfprop/schemapolicy/statement/register/{projectName}.
 * @author FreeGen
 */
type DfpropSchemapolicyStatementRegisterBody = {
  /** (Required) */
  type: string

  /** (NullAllowed) */
  subject?: string

  /** (NullAllowed) */
  condition?: DfpropSchemapolicyStatementRegisterBody_ConditionPart

  /** (NullAllowed) */
  expected?: DfpropSchemapolicyStatementRegisterBody_ExpectedPart

  /** (NullAllowed) */
  comment?: string
}

/**
 * The part class of ConditionPart.
 * @author FreeGen
 */
type DfpropSchemapolicyStatementRegisterBody_ConditionPart = {
  /** (Required) */
  operator: string

  /** (NotNull) */
  conditions: Array<string>
}

/**
 * The part class of ExpectedPart.
 * @author FreeGen
 */
type DfpropSchemapolicyStatementRegisterBody_ExpectedPart = {
  /** (Required) */
  operator: string

  /** (NotNull) */
  expected: Array<string>
}
