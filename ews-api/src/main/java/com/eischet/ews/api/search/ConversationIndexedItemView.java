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

package com.eischet.ews.api.search;

import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.search.OffsetBasePoint;
import com.eischet.ews.api.core.enumeration.service.ServiceObjectType;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.service.local.ServiceVersionException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.request.ServiceRequestBase;

/**
 * Represents the view settings in a folder search operation.
 */
public final class ConversationIndexedItemView extends PagedView {

    private final OrderByCollection orderBy = new OrderByCollection();


    /**
     * Initializes a new instance of the <see cref="ItemView"/> class.
     *
     * @param pageSize The maximum number of elements the search operation should return.
     */
    public ConversationIndexedItemView(int pageSize) {
        super(pageSize);
    }

    /**
     * Initializes a new instance of the ItemView class.
     *
     * @param pageSize The maximum number of elements the search operation should return.
     * @param offset   The offset of the view from the base point.
     */
    public ConversationIndexedItemView(int pageSize, int offset) {
        super(pageSize, offset);
        this.setOffset(offset);
    }

    /**
     * Initializes a new instance of the ItemView class.
     *
     * @param pageSize        The maximum number of elements the search operation should return.
     * @param offset          The offset of the view from the base point.
     * @param offsetBasePoint The base point of the offset.
     */
    public ConversationIndexedItemView(int pageSize, int offset, OffsetBasePoint offsetBasePoint) {
        super(pageSize, offset, offsetBasePoint);

    }

    /**
     * Gets the type of service object this view applies to.
     *
     * @return A ServiceObjectType value.
     */
    @Override
    protected ServiceObjectType getServiceObjectType() {
        return ServiceObjectType.Conversation;
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer The writer.
     */
    @Override
    public void writeAttributesToXml(EwsServiceXmlWriter writer) {
        // Do nothing
    }

    /**
     * Gets the name of the view XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getViewXmlElementName() {
        return XmlElementNames.IndexedPageItemView;
    }

    /**
     * Validates this view.
     *
     * @param request The request using this view.
     */
    @Override
    public void internalValidate(ServiceRequestBase request) throws ServiceVersionException, ExchangeValidationException {
        super.internalValidate(request);
    }

    /**
     * Internals the write search settings to XML.
     *
     * @param writer  The writer.
     * @param groupBy The group by.
     */
    @Override
    protected void internalWriteSearchSettingsToXml(EwsServiceXmlWriter writer, Grouping groupBy) throws ExchangeXmlException {
        super.internalWriteSearchSettingsToXml(writer, groupBy);
    }

    /**
     * Writes OrderBy property to XML.
     *
     * @param writer The writer
     */
    @Override
    public void writeOrderByToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        this.orderBy.writeToXml(writer, XmlElementNames.SortOrder);
    }

    /**
     * Writes to XML.
     *
     * @param writer The writer
     */
    public void writeToXml(EwsServiceXmlWriter writer) throws Exception {
        writer.writeStartElement(XmlNamespace.Messages, this.getViewXmlElementName());

        this.internalWriteViewToXml(writer);

        writer.writeEndElement(); // this.GetViewXmlElementName()
    }

    /**
     * Gets the property against which the returned item should be ordered.
     */
    public OrderByCollection getOrderBy() {
        return this.orderBy;
    }
}
