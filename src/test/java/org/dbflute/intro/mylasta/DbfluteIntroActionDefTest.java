/*
 * Copyright 2014-2015 the original author or authors.
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
package org.dbflute.intro.mylasta;

import org.dbflute.intro.unit.DbfluteIntroBaseTestCase;
import org.dbflute.utflute.lastaflute.police.ActionComponentPolice;
import org.dbflute.utflute.lastaflute.police.HotDeployDestroyerPolice;
import org.dbflute.utflute.lastaflute.police.NonActionExtendsActionPolice;
import org.dbflute.utflute.lastaflute.police.NonWebHasWebReferencePolice;
import org.dbflute.utflute.lastaflute.police.WebPackageNinjaReferencePolice;

/**
 * @author t-awane
 */
public class DbfluteIntroActionDefTest extends DbfluteIntroBaseTestCase {

    /**
     * コンポーネントをテストします。
     */
    public void test_component() {
        policeStoryOfJavaClassChase(new ActionComponentPolice(tp -> getComponent(tp)));
    }

    /**
     * コンポーネントをテストします。
     */
    public void test_hotDeployDestroyer() {
        policeStoryOfJavaClassChase(new HotDeployDestroyerPolice(tp -> getComponent(tp)));
    }

    /**
     * コンポーネントをテストします。
     */
    public void test_nonActionExtendsAction() {
        policeStoryOfJavaClassChase(new NonActionExtendsActionPolice());
    }

    /**
     * コンポーネントをテストします。
     */
    public void test_nonWebHasWebReference() {
        policeStoryOfJavaClassChase(new NonWebHasWebReferencePolice());
    }

    /**
     * コンポーネントをテストします。
     */
    public void test_webPackageNinjaReferencePolice() {
        policeStoryOfJavaClassChase(new WebPackageNinjaReferencePolice());
    }
}
