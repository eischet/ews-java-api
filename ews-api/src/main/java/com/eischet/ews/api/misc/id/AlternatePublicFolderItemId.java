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
 * Represents the Id of a public folder item expressed in a specific format.
 */
public class AlternatePublicFolderItemId extends AlternatePublicFolderId {

    /**
     * Schema type associated with AlternatePublicFolderItemId.
     */
    public final static String SchemaTypeName =
            "AlternatePublicFolderItemIdType";

    /**
     * Item id.
     */
    private String itemId;

    /**
     * Initializes a new instance of the class.
     */
    public AlternatePublicFolderItemId() {
        super();
    }

    /**
     * Initializes a new instance of the class.
     *
     * @param format   the format
     * @param folderId the folder id
     * @param itemId   the item id
     */
    public AlternatePublicFolderItemId(IdFormat format, String folderId,
                                       String itemId) {
        super(format, folderId);
        this.itemId = itemId;
    }

    /**
     * Gets The Id of the public folder item.
     *
     * @return the item id
     */
    public String getItemId() {
        return this.itemId;
    }

    /**
     * Sets the item id.
     *
     * @param itemId the new item id
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getXmlElementName() {
        return XmlElementNames.AlternatePublicFolderItemId;
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer the writer
     */
    @Override
    protected void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeAttributesToXml(writer);
        writer.writeAttributeValue(XmlAttributeNames.ItemId, this.getItemId());
    }

    /**
     * Loads the attribute from XML.
     *
     * @param reader the reader
     */
    @Override
    public void loadAttributesFromXml(EwsServiceXmlReader reader) throws ExchangeXmlException {
        super.loadAttributesFromXml(reader);
        this.itemId = reader.readAttributeValue(XmlAttributeNames.ItemId);
    }

}
