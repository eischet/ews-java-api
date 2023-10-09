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

package com.eischet.ews.api.misc.id;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.IdFormat;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

/**
 * Represents the Id of a public folder expressed in a specific format.
 */
public class AlternatePublicFolderId extends AlternateIdBase {

    /**
     * Name of schema type used for AlternatePublicFolderId element.
     */
    public final static String SchemaTypeName =
            "AlternatePublicFolderIdType";

    private String folderId;

    /**
     * Initializes a new instance of AlternatePublicFolderId.
     */
    public AlternatePublicFolderId() {
        super();
    }

    /**
     * Initializes a new instance of AlternatePublicFolderId.
     *
     * @param format   the format
     * @param folderId the folder id
     */
    public AlternatePublicFolderId(IdFormat format, String folderId) {
        super(format);
        this.setFolderId(folderId);
    }

    /**
     * The Id of the public folder.
     *
     * @return the folder id
     */
    public String getFolderId() {
        return this.folderId;

    }

    /**
     * Sets the folder id.
     *
     * @param folderId the new folder id
     */
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name
     */
    @Override
    protected String getXmlElementName() {
        return XmlElementNames.AlternatePublicFolderId;
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer the writer
     */
    @Override
    protected void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeAttributesToXml(writer);
        writer.writeAttributeValue(XmlAttributeNames.FolderId, this.getFolderId());
    }

    /**
     * Loads the attribute from XML.
     *
     * @param reader the reader
     */
    @Override
    public void loadAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        super.loadAttributesFromXml(reader);
        this.setFolderId(reader.readAttributeValue(XmlAttributeNames.FolderId));
    }

}
