/**
 * Copyright (c) 2009-2012, Netbout.com
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

import com.netbout.inf.Atom;
import com.netbout.inf.Index;
import com.netbout.inf.PredicateException;
import com.netbout.inf.atoms.NumberAtom;
import java.util.List;

/**
 * Show the message if its position is bigger or equal than this one.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
@Meta("from")
final class FromPred extends AbstractVarargPred {

    /**
     * Minimum position to show.
     */
    private final transient Long from;

    /**
     * Current position.
     */
    private transient long position;

    /**
     * Public ctor.
     * @param args The arguments
     */
    public FromPred(final List<Atom> args) {
        super(args);
        this.from = ((NumberAtom) this.arg(0)).value();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long next() {
        throw new PredicateException("FROM#next()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        throw new PredicateException("FROM#hasNext()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final Long message) {
        final boolean allow = this.position >= this.from;
        this.position += 1;
        return allow;
    }

}
