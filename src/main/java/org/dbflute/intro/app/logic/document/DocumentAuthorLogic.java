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
package org.dbflute.intro.app.logic.document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * @author cabos
 * @author deco
 * @author jflute
 */
public class DocumentAuthorLogic {

    private static final Supplier<String> _authorSupplier = new Supplier<String>() {

        private String _author;

        @Override
        public String get() {
            if (this._author == null) {
                try {
                    this.loadAuthor();
                } catch (UncheckedIOException ignored) {
                }
            }
            return this._author;
        }

        private void loadAuthor() {
            this._author = System.getProperty("user.name");
        }
    };

    private static final Supplier<String> _gitBranchSupplier = new Supplier<String>() {

        private String _gitBranchName;

        @Override
        public String get() {
            if (this._gitBranchName == null) {
                this.loadGitBranchName();
            }
            return this._gitBranchName;
        }

        private synchronized void loadGitBranchName() {
            // get user name from git
            Runtime runtime = Runtime.getRuntime();
            Process p;
            try {
                p = runtime.exec("git symbolic-ref --short HEAD");
            } catch (IOException e) {
                throw new UncheckedIOException("fail to execute git command", e);
            }

            // read user name
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")))) {
                this._gitBranchName = reader.readLine();
            } catch (IOException e) {
                throw new UncheckedIOException("fail to read execute command result", e);
            }
        }
    };

    public String getAuthor() {
        return _authorSupplier.get();
    }

    public String getGitBranchName() {
        return _gitBranchSupplier.get();
    }
}
