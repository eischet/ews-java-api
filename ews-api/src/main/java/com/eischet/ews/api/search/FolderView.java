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
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.search.FolderTraversal;
import com.eischet.ews.api.core.enumeration.search.OffsetBasePoint;
import com.eischet.ews.api.core.enumeration.service.ServiceObjectType;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the view settings in a folder search operation.
 */
public final class FolderView extends PagedView {

    private static final Logger LOG = Logger.getLogger(FolderView.class.getCanonicalName());

    /**
     * The traversal.
     */
    private FolderTraversal traversal = FolderTraversal.Shallow;

    /**
     * Gets the name of the view XML element.
     *
     * @return Xml Element name
     */
    @Override
    protected String getViewXmlElementName() {
        return XmlElementNames.IndexedPageFolderView;
    }

    /**
     * Gets the type of service object this view applies to.
     *
     * @return A ServiceObjectType value.
     */
    @Override
    protected ServiceObjectType getServiceObjectType() {
        return ServiceObjectType.Folder;
    }

    /**
     * Writes the attribute to XML.
     *
     * @param writer The writer
     */
    @Override
    public void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        writer.writeAttributeValue(XmlAttributeNames.Traversal, this.getTraversal());
    }

    /**
     * Initializes a new instance of the FolderView class.
     *
     * @param pageSize The maximum number of elements the search operation should
     *                 return.
     */
    public FolderView(int pageSize) {
        super(pageSize);
    }

    /**
     * Initializes a new instance of the FolderView class.
     *
     * @param pageSize The maximum number of elements the search operation should
     *                 return.
     * @param offset   The offset of the view from the base point.
     */
    public FolderView(int pageSize, int offset) {
        super(pageSize, offset);
    }

    /**
     * Initializes a new instance of the FolderView class.
     *
     * @param pageSize        The maximum number of elements the search operation should
     *                        return.
     * @param offset          The offset of the view from the base point.
     * @param offsetBasePoint The base point of the offset.
     */
    public FolderView(int pageSize, int offset,
                      OffsetBasePoint offsetBasePoint) {
        super(pageSize, offset, offsetBasePoint);
    }

    /**
     * Gets the search traversal mode. Defaults to FolderTraversal.Shallow.
     *
     * @return the traversal
     */
    public FolderTraversal getTraversal() {
        return traversal;
    }

    /**
     * Sets the search traversal mode. Defaults to FolderTraversal.Shallow.
     *
     * @param traversal the new traversal
     */
    public void setTraversal(FolderTraversal traversal) {
        this.traversal = traversal;
    }
}
