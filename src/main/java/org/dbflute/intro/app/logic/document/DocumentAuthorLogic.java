package org.dbflute.intro.app.logic.document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.function.Supplier;

/**
 * @author cabos
 */
public class DocumentAuthorLogic {

    private static final Supplier<String> _gitAuthorSupplier = new Supplier<String>() {

        private String _gitAuthor;

        @Override
        public String get() {
            if (this._gitAuthor == null) {
                this.loadAuthor();
            }
            return this._gitAuthor;
        }

        private void loadAuthor() {
            // get user name from git
            Runtime runtime = Runtime.getRuntime();
            Process p;
            try {
                p = runtime.exec("git config user.name");
            } catch (IOException e) {
                throw new UncheckedIOException("fail to execute git command", e);
            }

            // read user name
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")))) {
                this._gitAuthor = reader.readLine();
            } catch (IOException e) {
                throw new UncheckedIOException("fail to read execute command result", e);
            }
        }
    };

    public String getAuthorFromGitSystem() {
        return _gitAuthorSupplier.get();
    }
}
