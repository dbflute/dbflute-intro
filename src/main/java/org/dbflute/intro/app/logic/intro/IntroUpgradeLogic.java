/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.logic.intro;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;

/**
 * @author p1us2er0
 * @author jflute
 */
public class IntroUpgradeLogic {

    public void upgrade() {
        File jarPathFile = new File("./dbflute-intro.jar");

        Class<?> clazz = this.getClass();
        URL location = clazz.getResource("/" + clazz.getName().replaceAll("\\.", "/") + ".class");
        String path = location.getPath();

        if (path.lastIndexOf("!") != -1) {
            try {
                jarPathFile = new File(
                        URLDecoder.decode(path.substring("file:/".length(), path.lastIndexOf("!")), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }

        try {
            FileUtils.copyURLToFile(new URL("http://p1us2er0.github.io/dbflute-intro/download/" + jarPathFile.getName()), jarPathFile);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
