/**
 * Copyright (c) 2009-2012, Netbout.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the NetBout.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.netbout.client;

import com.jcabi.log.Logger;
import com.rexsl.test.AssertionPolicy;
import com.rexsl.test.TestResponse;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

/**
 * Asserts that {@code /page/eta} equals to zero.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
@AssertionPolicy.Quiet
public final class EtaAssertion implements AssertionPolicy {

    /**
     * Minimum delay in nanosec.
     */
    private static final long MIN_DELAY = 30 * 1000L * 1000 * 1000;

    /**
     * Maximum delay in nanosec.
     */
    private static final long MAX_DELAY = 5 * 60 * 1000L * 1000 * 1000;

    /**
     * Recently detected ETA.
     */
    private transient Long eta = 0L;

    /**
     * {@inheritDoc}
     */
    @Override
    public void assertThat(final TestResponse response) {
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            response.assertXPath("/page/identity/eta");
            this.eta = Long.valueOf(
                response.xpath("/page/identity/eta/text()").get(0)
            );
            if (this.eta > 0) {
                throw new AssertionError(
                    Logger.format(
                        "ETA=%[nano]s for '%s', the page is not ready",
                        this.eta,
                        response.xpath("/page/identity/name/text()").get(0)
                    )
                );
            }
        } else if (response.getStatus() == HttpURLConnection.HTTP_UNAVAILABLE) {
            this.eta = 1L;
            throw new AssertionError("service is temporary unavailable");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRetryNeeded(final int attempt) {
        boolean again = false;
        if (this.eta > 0) {
            again = true;
            final long delay = Math.min(
                Math.max(
                    this.eta * attempt,
                    EtaAssertion.MIN_DELAY * attempt
                ),
                EtaAssertion.MAX_DELAY
            );
            Logger.warn(
                this,
                "again(attempt #%d): let's wait %[nano]s and try again",
                attempt,
                delay
            );
            try {
                TimeUnit.NANOSECONDS.sleep(delay);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        }
        return again;
    }

}