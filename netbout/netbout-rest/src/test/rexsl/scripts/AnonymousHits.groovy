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
 *
 * @author Yegor Bugayenko (yegor@netbout.com)
 * @version $Id$
 */

import com.rexsl.test.TestClient
import com.rexsl.test.XhtmlConverter
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.MediaType
import org.junit.Assert
import org.xmlmatchers.XmlMatchers
import org.hamcrest.Matchers

// In this script we are trying to make different hits to the site
// from anonymous user. All of our hits should lead to /login page.

[
    '/',
    '/123',
    '/g'
].each { url ->
    def r = new TestClient(rexsl.home)
        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML)
        .get(url)
    Assert.assertThat(r.status, Matchers.equalTo(HttpURLConnection.HTTP_OK))
    Assert.assertThat(
        XhtmlConverter.the(r.body),
        XmlMatchers.hasXPath("/processing-instruction('xml-stylesheet')[contains(.,'/login.xsl')]")
    )
    Assert.assertThat(
        XhtmlConverter.the(r.body),
        XmlMatchers.hasXPath('/page/facebook[@href]')
    )
    Assert.assertThat(
        XhtmlConverter.the(r.body),
        XmlMatchers.hasXPath("/page/links/link[@name='self']")
    )
}