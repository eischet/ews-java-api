/*
 * The MIT License
 * Copyright (c) 2012 Microsoft Corporation
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.eischet.ews.api.credential;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.text.IsEmptyString.isEmptyOrNullString;
import static org.junit.Assert.assertThat;

import com.eischet.ews.api.core.EwsUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(JUnit4.class) public class WSSecurityBasedCredentialsTest {

  private static final Logger LOG = Logger.getLogger(WSSecurityBasedCredentialsTest.class.getCanonicalName());

  private WSSecurityBasedCredentials wsSecurityBasedCredentials;
  private XMLStreamWriter xmlStreamWriter = null;
  private Writer stringWriter = null;

  @Before public void initTest() throws XMLStreamException {
    // testObject
    wsSecurityBasedCredentials = new WSSecurityBasedCredentials() {

    };

    // testContext
    stringWriter = new StringWriter();
    xmlStreamWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(stringWriter);
  }

  @After public void tearDown() {
    if (stringWriter != null) {
      try {
        stringWriter.close();
      } catch (IOException e) {
        LOG.log(Level.WARNING, e.getMessage(), e);
      }
    }
    if (xmlStreamWriter != null) {
      try {
        xmlStreamWriter.close();
      } catch (XMLStreamException e) {
        LOG.log(Level.WARNING, e.getMessage(), e);
      }
    }
  }

  @Test public void testEmitExtraSoapHeaderNamespaceAliases() throws XMLStreamException {
    xmlStreamWriter.writeStartDocument();
    xmlStreamWriter.writeStartElement("test");

    wsSecurityBasedCredentials.emitExtraSoapHeaderNamespaceAliases(xmlStreamWriter);

    xmlStreamWriter.writeEndElement();
    xmlStreamWriter.writeEndDocument();
    xmlStreamWriter.flush();

    assertThat(stringWriter.toString(),
               allOf(not(isEmptyOrNullString()), containsString("xmlns"), containsString("test"),
                     containsString(EwsUtilities.WSSecuritySecExtNamespacePrefix),
                     containsString(EwsUtilities.WSAddressingNamespacePrefix),
                     containsString(EwsUtilities.WSSecuritySecExtNamespace),
                     containsString(EwsUtilities.WSAddressingNamespace)));
  }

}
