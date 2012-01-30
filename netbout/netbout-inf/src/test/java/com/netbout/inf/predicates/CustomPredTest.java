/**
 * Copyright (c) 2009-2011, netBout.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are PROHIBITED without prior written permission from
 * the author. This product may NOT be used anywhere and on any computer
 * except the server platform of netBout Inc. located at www.netbout.com.
 * Federal copyright law prohibits unauthorized reproduction by any means
 * and imposes fines up to $25,000 for violation. If you received
 * this code occasionally and without intent to use it, please report this
 * incident to the author by email.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.netbout.inf.predicates;

import com.netbout.bus.Bus;
import com.netbout.bus.BusMocker;
import com.netbout.inf.Predicate;
import com.netbout.inf.PredicateMocker;
import com.netbout.spi.MessageMocker;
import com.netbout.spi.Urn;
import com.netbout.spi.UrnMocker;
import java.util.Arrays;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case of {@link CustomPred}.
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class CustomPredTest {

    /**
     * CustomPred can match through Hub.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void positivelyMatchesThroughHub() throws Exception {
        final String result = "some text to return";
        final Urn name = new UrnMocker().mock();
        final Bus bus = new BusMocker()
            .doReturn(result, "evaluate-predicate")
            .mock();
        final Predicate pred = new CustomPred(
            bus,
            name,
            Arrays.asList(new Predicate[] {new PredicateMocker().mock()})
        );
        MatcherAssert.assertThat(
            (String) pred.evaluate(new MessageMocker().mock(), 0),
            Matchers.equalTo(result)
        );
    }

}
