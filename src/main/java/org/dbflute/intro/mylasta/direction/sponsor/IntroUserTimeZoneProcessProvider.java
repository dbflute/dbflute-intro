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
package org.dbflute.intro.mylasta.direction.sponsor;

import java.util.TimeZone;

import org.dbflute.optional.OptionalObject;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.servlet.request.RequestManager;
import org.lastaflute.web.servlet.request.UserTimeZoneProcessProvider;

/**
 * @author p1us2er0
 */
public class IntroUserTimeZoneProcessProvider implements UserTimeZoneProcessProvider {

    public static final TimeZone centralTimeZone = TimeZone.getDefault();

    @Override
    public boolean isUseTimeZoneHandling() {
        return false;
    }

    @Override
    public boolean isAcceptCookieTimeZone() {
        return false;
    }

    @Override
    public OptionalThing<TimeZone> findBusinessTimeZone(ActionRuntime runtime, RequestManager requestManager) {
        return OptionalObject.empty();
    }

    @Override
    public TimeZone getRequestedTimeZone(RequestManager requestManager) { // not null
        return centralTimeZone; // you can change it if you like
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{useTimeZoneHandling=").append(isUseTimeZoneHandling());
        sb.append(", acceptCookieTimeZone=").append(isAcceptCookieTimeZone());
        sb.append("}");
        return sb.toString();
    }
}
