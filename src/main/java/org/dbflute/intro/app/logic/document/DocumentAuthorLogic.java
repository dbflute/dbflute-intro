/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.logic.document;

import java.util.function.Supplier;

/**
 * @author cabos
 */
public class DocumentAuthorLogic {

    private static final Supplier<String> _authorSupplier = new Supplier<String>() {

        private String _author;

        @Override
        public String get() {
            if (this._author == null) {
                this.loadAuthor();
            }
            return this._author;
        }

        //_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
        // memorable code : load author from git system
        //_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
        //private void loadAuthor() {
        //    // done cabos "memorable code" get from os user and filter it by jflute (2017/08/10)
        //    // get user name from git
        //    Runtime runtime = Runtime.getRuntime();
        //    Process p;
        //    try {
        //        p = runtime.exec("git config user.name");
        //    } catch (IOException e) {
        //        throw new UncheckedIOException("fail to execute git command", e);
        //    }
        //
        //    // read user name
        //    try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")))) {
        //        this._author = reader.readLine();
        //    } catch (IOException e) {
        //        throw new UncheckedIOException("fail to read execute command result", e);
        //    }
        //}
        //_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/

        private void loadAuthor() {
            this._author = System.getProperty("user.name");
        }
    };

    public String getAuthor() {
        return _authorSupplier.get();
    }
}
