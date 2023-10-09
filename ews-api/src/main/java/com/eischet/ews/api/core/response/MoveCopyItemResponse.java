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

package com.eischet.ews.api.core.response;

import com.eischet.ews.api.core.EwsServiceXmlReader;
import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.ServiceObject;
import com.eischet.ews.api.core.service.item.Item;

import java.util.List;

/**
 * Represents a response to a Move or Copy operation.
 */
public final class MoveCopyItemResponse extends ServiceResponse implements
        IGetObjectInstanceDelegate<ServiceObject> {

    /**
     * The item.
     */
    private Item item;

    /**
     * Represents a response to a Move or Copy operation.
     */
    public MoveCopyItemResponse() {
        super();
    }

    /**
     * Gets Item instance.
     *
     * @param service        the service
     * @param xmlElementName the xml element name
     * @return the object instance
     */
    private Item getObjectInstance(ExchangeService service,
                                   String xmlElementName) throws ExchangeXmlException {
        return EwsUtilities.createEwsObjectFromXmlElementName(Item.class, service, xmlElementName);
    }

    /**
     * Reads response elements from XML.
     *
     * @param reader the reader
     * @throws Exception the exception
     */
    @Override
    protected void readElementsFromXml(EwsServiceXmlReader reader)
            throws Exception {
        super.readElementsFromXml(reader);
        List<Item> items = reader.readServiceObjectsCollectionFromXml(
                XmlElementNames.Items, this, false, /* clearPropertyBag */
                null, /* requestedPropertySet */
                false); /* summaryPropertiesOnly */

        // We only receive the copied or moved item if the copy or move
        // operation was within
        // a single mailbox. No item is returned if the operation is
        // cross-mailbox, from a
        // mailbox to a public folder or from a public folder to a mailbox.
        if (!items.isEmpty()) {
            this.item = items.get(0);
        }
    }

    /**
     * Gets the object instance delegate.
     *
     * @param service        the service
     * @param xmlElementName the xml element name
     * @return the object instance delegate
     */
    @Override
    public ServiceObject getObjectInstanceDelegate(ExchangeService service,
                                                   String xmlElementName) throws ExchangeXmlException {
        return this.getObjectInstance(service, xmlElementName);
    }

    /**
     * Gets the copied or moved item. Item is null if the copy or move
     * operation was between two mailboxes or between a mailbox and a public
     * folder.
     *
     * @return the item
     */
    public Item getItem() {
        return this.item;
    }

}
