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

import com.eischet.ews.api.core.PropertySet;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.service.item.Item;
import com.eischet.ews.api.sync.ItemChange;

/**
 * Represents the response to a folder item synchronization operation.
 */
public final class SyncFolderItemsResponse extends
        SyncResponse<Item, ItemChange> {

    /**
     * Initializes a new instance of the class.
     *
     * @param propertySet the property set
     */
    public SyncFolderItemsResponse(PropertySet propertySet) {
        super(propertySet);
    }

    /**
     * Gets the name of the includes last in range XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getIncludesLastInRangeXmlElementName() {
        return XmlElementNames.IncludesLastItemInRange;
    }

    /**
     * Creates an item change instance.
     *
     * @return ItemChange instance
     */
    @Override
    protected ItemChange createChangeInstance() {
        return new ItemChange();
    }

    /**
     * Gets a value indicating whether this request returns full or summary property.
     * "true" if summary property only; otherwise, "false".
     *
     * @return the summary property only
     */
    @Override
    protected boolean getSummaryPropertiesOnly() {
        return true;
    }

}
