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
package com.netbout.rest;

import com.netbout.rest.auth.FacebookRs;
import com.netbout.rest.page.PageBuilder;
import com.netbout.spi.Identity;
import com.netbout.spi.Urn;
import com.rexsl.core.Manifests;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * RESTful front of login functions.
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */
@Path("/g")
public final class LoginRs extends AbstractRs {

    /**
     * Login page.
     * @return The JAX-RS response
     * @see <a href="http://developers.facebook.com/docs/authentication/">facebook.com</a>
     */
    @GET
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public Response login() {
        try {
            this.identity();
            throw new ForwardException(this, this.base(), "Already logged in");
            // @checkstyle EmptyBlock (3 lines)
        } catch (LoginRequiredException ex) {
            // swallow it, it's normal situation
        }
        final UriBuilder fburi = UriBuilder
            .fromPath("https://www.facebook.com/dialog/oauth")
            .queryParam("client_id", Manifests.read("Netbout-FbId"))
            .queryParam("redirect_uri", this.base().path("/fb").build());
        return new PageBuilder()
            .stylesheet(this.base().path("/xsl/login.xsl"))
            .build(AbstractPage.class)
            .init(this)
            .link("facebook", fburi)
            .anonymous()
            .build();
    }

    /**
     * Facebook authentication page (callback hits it).
     * @param code Facebook "authorization code"
     * @return The JAX-RS response
     */
    @GET
    @Path("/facebook")
    public Response fbauth(@QueryParam("code") final String code) {
        this.logoff();
        final Identity remote = (Identity) new FacebookRs()
            .auth(Urn.create("urn:facebook:0"), code)
            .getEntity();
        Identity identity;
        try {
            identity = this.hub().identity(remote.name());
        } catch (com.netbout.spi.UnreachableUrnException ex) {
            throw new ForwardException(this, ex);
        }
        return new PageBuilder()
            .build(AbstractPage.class)
            .init(this)
            .authenticated(identity)
            .status(Response.Status.SEE_OTHER)
            .location(this.base().build())
            .build();
    }

    /**
     * Logout page.
     * @return The JAX-RS response
     * @see <a href="http://developers.facebook.com/docs/authentication/">facebook.com</a>
     */
    @Path("/out")
    @GET
    public Response logout() {
        return Response
            .status(Response.Status.TEMPORARY_REDIRECT)
            .location(this.base().build())
            .header(
                "Set-Cookie",
                String.format(
                    // @checkstyle LineLength (1 line)
                    "netbout=deleted;Domain=.%s;Path=/%s;Expires=Thu, 01-Jan-1970 00:00:01 GMT",
                    this.base().build().getHost(),
                    this.httpServletRequest().getContextPath()
                )
            )
            .build();
    }

}
