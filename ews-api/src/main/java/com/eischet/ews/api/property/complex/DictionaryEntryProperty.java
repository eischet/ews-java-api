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
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.enumeration.attribute.EditorBrowsableState;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.ServiceObject;

import javax.xml.stream.XMLStreamException;

/**
 * Represents an entry of a DictionaryProperty object.
 *
 * All descendants of DictionaryEntryProperty must implement a parameterless
 * constructor. That constructor does not have to be public. That constructor
 * does not have to be public.
 *
 * @param <TKey> the generic type
 */
@EditorBrowsable(state = EditorBrowsableState.Never)
public abstract class DictionaryEntryProperty<TKey> extends ComplexProperty {

    /**
     * The key.
     */
    private TKey key;
    private final Class<TKey> instance;

    /**
     * Initializes a new instance of the "DictionaryEntryProperty&lt;TKey&gt;"
     * class.
     */
    protected DictionaryEntryProperty(Class<TKey> cls) {
        this.instance = cls;
    }

    /**
     * Initializes a new instance of the "DictionaryEntryProperty&lt;TKey&gt;"
     * class.
     *
     * @param key The key.
     */
    protected DictionaryEntryProperty(Class<TKey> cls, TKey key) {
        super();
        this.key = key;
        this.instance = cls;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    protected TKey getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param value the value to set
     */
    protected void setKey(TKey value) {
        this.key = value;
    }

    /**
     * Reads the attribute from XML.
     *
     * @param reader accepts EwsServiceXmlReader
     */
    @Override
    public void readAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        this.key = reader.readAttributeValue(instance,
                XmlAttributeNames.Key);
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer accepts EwsServiceXmlWriter
     */
    @Override
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.Key, this.getKey());
    }

    /**
     * Writes the set update to XML.
     *
     * @param writer                        the writer
     * @param ewsObject                     the ews object
     * @param ownerDictionaryXmlElementName name of the owner dictionary XML element
     * @return true if update XML was written
     * @throws XMLStreamException               the XML stream exception
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    protected boolean writeSetUpdateToXml(EwsServiceXmlWriter writer,
                                          ServiceObject ewsObject, String ownerDictionaryXmlElementName)
            throws XMLStreamException, ServiceXmlSerializationException, ExchangeXmlException {
        return false;
    }

    /**
     * Writes the delete update to XML.
     *
     * @param writer    the writer
     * @param ewsObject the ews object
     * @return true if update XML was written
     * @throws XMLStreamException               the XML stream exception
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    protected boolean writeDeleteUpdateToXml(EwsServiceXmlWriter writer,
                                             ServiceObject ewsObject) throws XMLStreamException, ServiceXmlSerializationException, ExchangeXmlException {
        return false;
    }

}
