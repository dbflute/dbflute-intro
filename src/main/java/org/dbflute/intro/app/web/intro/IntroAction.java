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
package org.dbflute.intro.app.web.intro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroReadLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.base.cls.IntroClsAssist;
import org.dbflute.intro.bizfw.server.BootingInternetDomain;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * Intro全体で共有するようなリクエストを司るAction。
 * @author p1us2er0
 * @author jflute
 * @author deco
 * @author subaru
 */
public class IntroAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroReadLogic introReadLogic;
    @Resource
    private IntroClsAssist introClsAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    /**
     * MANIFEST.MFの内容を戻す。<br>
     * そのままkey/valueで表示するだけなので、BeanクラスにはせずMapで戻している。
     * @return mfファイルの内容をそのままkey/valueにしている (NotNull)
     */
    @Execute
    public JsonResponse<Map<String, Object>> manifest() {
        return asJson(introReadLogic.getManifestMap());
    }

    // TODO you 結局、必要な画面だけで取得しているだけなので、もっと区分値が増えたら無駄が多くなる by jflute (2023/01/12)
    // 最初にアクセスされた画面で一括で取得して再利用するか？画面ごとに取得するようにするか？
    // アプリ区分値で関連付けして、TypeScriptのstructを自動生成するか？どうにかしたいところ。
    /**
     * サーバーサイドの区分値の情報一括で戻す。
     * @return map型のキーに対してそれぞれの区分値が定義されている (NotNull)
     */
    @Execute
    public JsonResponse<Map<String, Map<?, ?>>> classifications() {
        // TODO you MapじゃなくてちゃんとしたBeanクラスにしたいところ by jflute (2023/01/12)
        Map<String, Map<?, ?>> classificationMap = introClsAssist.getClassificationMap();
        return asJson(classificationMap);
    }

    // TODO you これフロントで実際には使ってないようなので、ひとまず無くていいような？ by jflute (2023/01/12)
    /**
     * Introサーバーの設定を一括で戻す。
     * @return map型でそれぞれの設定が格納されている (NotNull)
     */
    @Execute
    public JsonResponse<Map<String, Object>> configuration() {
        // TODO you こちらもMapじゃなくてちゃんとしたBeanクラスにしたいところ by jflute (2023/01/12)
        Map<String, Object> map = new LinkedHashMap<>();
        BootingInternetDomain domain = new BootingInternetDomain();
        String serverUrl = domain.toCompleteDomain();
        String apiServerUrl = domain.toCompleteApiDomain();
        map.put("serverUrl", serverUrl);
        map.put("apiServerUrl", apiServerUrl);
        return asJson(map);
    }
}
