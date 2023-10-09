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

package com.eischet.ews.api.property.complex;

import com.eischet.ews.api.attribute.EditorBrowsable;
import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.attribute.EditorBrowsableState;
import com.eischet.ews.api.core.enumeration.property.ImAddressKey;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlDeserializationException;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import javax.xml.stream.XMLStreamException;

/**
 * Represents an entry of an ImAddressDictionary.
 */
@EditorBrowsable(state = EditorBrowsableState.Never)
public final class ImAddressEntry extends DictionaryEntryProperty<ImAddressKey> {

    /**
     * The im address.
     */
    private String imAddress;

    /**
     * Initializes a new instance of the "ImAddressEntry" class.
     */
    protected ImAddressEntry() {
        super(ImAddressKey.class);
    }

    /**
     * Initializes a new instance of the ="ImAddressEntry" class.
     *
     * @param key       The key.
     * @param imAddress The im address.
     */
    protected ImAddressEntry(ImAddressKey key, String imAddress) {
        super(ImAddressKey.class, key);
        this.imAddress = imAddress;
    }

    /**
     * Gets the Instant Messaging address of the entry.
     *
     * @return imAddress
     */
    public String getImAddress() {
        return this.imAddress;
    }

    /**
     * Sets the Instant Messaging address of the entry.
     *
     * @param value the new im address
     */
    public void setImAddress(Object value) {

        this.canSetFieldValue(this.imAddress, value);
    }

    /**
     * Reads the text value from XML.
     *
     * @param reader accepts EwsServiceXmlReader
     */
    @Override
    public void readTextValueFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.imAddress = reader.readValue();
    }

    /**
     * Writes elements to XML.
     *
     * @param writer The writer.
     */
    public void writeElementsToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeValue(this.imAddress, XmlElementNames.ImAddress);
    }
}
