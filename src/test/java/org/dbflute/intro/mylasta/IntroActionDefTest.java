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
package org.dbflute.intro.mylasta;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.police.ActionComponentPolice;
import org.dbflute.utflute.lastaflute.police.HotDeployDestroyerPolice;
import org.dbflute.utflute.lastaflute.police.InjectedResourceDefinitionPolice;
import org.dbflute.utflute.lastaflute.police.LastaPresentsSomethingPolice;
import org.dbflute.utflute.lastaflute.police.NonActionExtendsActionPolice;
import org.dbflute.utflute.lastaflute.police.NonWebHasWebReferencePolice;
import org.dbflute.utflute.lastaflute.police.WebPackageNinjaReferencePolice;

/**
 * @author t-awane
 * @author jflute
 */
public class IntroActionDefTest extends UnitIntroTestCase {

    public void test_component() {
        policeStoryOfJavaClassChase(new ActionComponentPolice(tp -> getComponent(tp)));
    }

    public void test_hotDeployDestroyer() {
        policeStoryOfJavaClassChase(new HotDeployDestroyerPolice(tp -> getComponent(tp)));
    }

    public void test_nonActionExtendsAction() {
        policeStoryOfJavaClassChase(new NonActionExtendsActionPolice());
    }

    public void test_nonWebHasWebReference() {
        policeStoryOfJavaClassChase(new NonWebHasWebReferencePolice());
    }

    public void test_webPackageNinjaReferencePolice() {
        policeStoryOfJavaClassChase(new WebPackageNinjaReferencePolice());
    }

    public void test_injectedResourceDefinitionPolice() throws Exception {
        policeStoryOfJavaClassChase(new InjectedResourceDefinitionPolice().shouldBePrivateField(field -> {
            return true; // means all fields
        }));
    }

    public void test_lastaPresentsSomethingPolice() throws Exception {
        policeStoryOfJavaClassChase(new LastaPresentsSomethingPolice().formImmutable());
    }
}
