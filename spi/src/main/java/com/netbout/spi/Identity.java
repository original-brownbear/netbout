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
package com.netbout.spi;

import java.net.URL;
import java.util.Set;

/**
 * The identity of the person in a bout.
 *
 * <p>Identities are comparable by their names.
 *
 * <p>Instances of this interface must be thread-safe.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
public interface Identity extends Comparable<Identity> {

    /**
     * How many milliseconds we should way before this object becomes fully
     * available for requests (if you make a request earlier you may receive
     * invalid data or even a runtime exception).
     * @return The number of milliseconds we recommend to wait before making
     *  any manipulations with this object (and all other child objects, like
     *  its bouts, messages, etc.)
     */
    Long eta();

    /**
     * Who validated this user.
     * @return The URL of the authority
     */
    URL authority();

    /**
     * Get name of the identity, which is unique in the system.
     * @return The name of the identity
     */
    Urn name();

    /**
     * Start new bout.
     * @return The bout just created
     */
    Bout start();

    /**
     * Get an ordered list of all bouts this identity is taking
     * participation in.
     * @param query Search query, if necessary
     * @return The list of bouts
     */
    Iterable<Bout> inbox(String query);

    /**
     * Get bout by its unique ID.
     * @param number The number of the bout
     * @return The bout
     * @throws BoutNotFoundException If this bout doesn't exist
     */
    Bout bout(Long number) throws BoutNotFoundException;

    /**
     * Find another identity by name.
     * @param name Unique name of identity
     * @return The identity just found
     * @throws UnreachableUrnException If such a friend can't be reached
     */
    Identity friend(Urn name) throws UnreachableUrnException;

    /**
     * Find friends by keyword.
     * @param keyword The keyword
     * @return The list of identities found
     */
    Set<Identity> friends(String keyword);

    /**
     * Get profile.
     * @return The profile of this identity
     */
    Profile profile();

}
