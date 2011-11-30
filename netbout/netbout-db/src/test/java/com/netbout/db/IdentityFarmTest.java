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
package com.netbout.db;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Test case of {@link IdentityFarm}.
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public final class IdentityFarmTest {

    /**
     * Farm to work with.
     */
    private final transient IdentityFarm farm = new IdentityFarm();

    /**
     * Find bouts of some identity.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void testBoutsFinding() throws Exception {
        final BoutFarm bfarm = new BoutFarm();
        final Long bout = bfarm.getNextBoutNumber();
        bfarm.startedNewBout(bout);
        final ParticipantFarm pfarm = new ParticipantFarm();
        final String identity = "Steven Jobs";
        this.farm.changedIdentityPhoto(identity, "");
        pfarm.addedBoutParticipant(bout, identity);
        final List<Long> numbers = this.farm.getBoutsOfIdentity(identity);
        MatcherAssert.assertThat(numbers, Matchers.hasItem(bout));
    }

    /**
     * Set and change identity photo.
     * @throws Exception If there is some problem inside
     */
    @Test
    public void testChangeIdentityPhoto() throws Exception {
        final String name = "John Cleese";
        final String photo = "http://localhost/img.png";
        this.farm.changedIdentityPhoto(name, photo);
        MatcherAssert.assertThat(
            this.farm.getIdentityPhoto(name),
            Matchers.equalTo(photo)
        );
    }

}
