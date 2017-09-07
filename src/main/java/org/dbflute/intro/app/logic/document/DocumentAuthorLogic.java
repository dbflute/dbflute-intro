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
