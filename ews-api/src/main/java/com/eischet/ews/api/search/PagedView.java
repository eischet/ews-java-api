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

import com.eischet.ews.api.attribute.EditorBrowsable;
import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.enumeration.attribute.EditorBrowsableState;
import com.eischet.ews.api.core.enumeration.search.OffsetBasePoint;
import com.eischet.ews.api.core.exception.service.local.ExchangeValidationException;
import com.eischet.ews.api.core.exception.service.local.ServiceVersionException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.request.ServiceRequestBase;

/**
 * Represents a view settings that support paging in a search operation.
 */
@EditorBrowsable(state = EditorBrowsableState.Never)
public abstract class PagedView extends ViewBase {

    /**
     * The page size.
     */
    private int pageSize;

    /**
     * The offset base point.
     */
    private OffsetBasePoint offsetBasePoint = OffsetBasePoint.Beginning;

    /**
     * The offset.
     */
    private int offset;

    /**
     * Write to XML.
     *
     * @param writer The Writer
     * @throws Exception the exception
     */
    @Override
    protected void internalWriteViewToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.internalWriteViewToXml(writer);

        writer.writeAttributeValue(XmlAttributeNames.Offset, this.getOffset());
        writer.writeAttributeValue(XmlAttributeNames.BasePoint, this
                .getOffsetBasePoint());
    }

    /**
     * Gets the maximum number of item or folder the search operation should
     * return.
     *
     * @return The maximum number of item or folder that should be returned by
     * the search operation.
     */
    @Override
    protected Integer getMaxEntriesReturned() {
        return this.getPageSize();
    }

    /**
     * Internals the write search settings to XML.
     *
     * @param writer  the writer
     * @param groupBy the group by clause
     */
    @Override
    protected void internalWriteSearchSettingsToXml(EwsServiceXmlWriter writer, Grouping groupBy) throws ExchangeXmlException {
        if (groupBy != null) {
            groupBy.writeToXml(writer);
        }
    }

    /**
     * Writes OrderBy property to XML.
     *
     * @param writer the writer
     */
    @Override
    public void writeOrderByToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        // No order by for paged view
    }

    /**
     * Validates this view.
     *
     * @param request The request using this view.
     * @throws ServiceVersionException    the service version exception
     * @throws ExchangeValidationException the service validation exception
     */
    @Override
    public void internalValidate(ServiceRequestBase request) throws ServiceVersionException, ExchangeValidationException {
        super.internalValidate(request);
    }

    /**
     * Initializes a new instance of the "PagedView" class.
     *
     * @param pageSize The maximum number of elements the search operation should
     *                 return.
     */
    protected PagedView(int pageSize) {
        super();
        this.setPageSize(pageSize);
    }

    /**
     * Initializes a new instance of the "PagedView" class.
     *
     * @param pageSize The maximum number of elements the search operation should
     *                 return.
     * @param offset   The offset of the view from the base point.
     */
    protected PagedView(int pageSize, int offset) {
        this(pageSize);
        this.setOffset(offset);
    }

    /**
     * Initializes a new instance of the "PagedView" class.
     *
     * @param pageSize        The maximum number of elements the search operation should
     *                        return.
     * @param offset          The offset of the view from the base point.
     * @param offsetBasePoint The base point of the offset.
     */
    protected PagedView(int pageSize, int offset,
                        OffsetBasePoint offsetBasePoint) {
        this(pageSize, offset);
        this.setOffsetBasePoint(offsetBasePoint);
    }

    /**
     * Gets the maximum number of item or folder the search operation should
     * return.
     *
     * @return the page size
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Sets the maximum number of item or folder the search operation should
     * return.
     *
     * @param pageSize the new page size
     */
    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("The value must be greater than 0.");
        }
        this.pageSize = pageSize;
    }

    /**
     * Gets the base point of the offset.
     *
     * @return the offset base point
     */
    public OffsetBasePoint getOffsetBasePoint() {
        return offsetBasePoint;
    }

    /**
     * Sets the base point of the offset.
     *
     * @param offsetBasePoint the new offset base point
     */
    public void setOffsetBasePoint(OffsetBasePoint offsetBasePoint) {
        this.offsetBasePoint = offsetBasePoint;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Sets the offset.
     *
     * @param offset the new offset
     */
    public void setOffset(int offset) {
        if (offset >= 0) {
            this.offset = offset;
        } else {
            throw new IllegalArgumentException("The offset must be greater than 0.");
        }
    }

}
