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

package com.eischet.ews.api.sync;

import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.service.folder.Folder;
import com.eischet.ews.api.property.complex.FolderId;
import com.eischet.ews.api.property.complex.ServiceId;

/**
 * Represents a change on a folder as returned by a synchronization operation.
 */
public final class FolderChange extends Change {
    /**
     * Initializes a new instance of FolderChange.
     */
    public FolderChange() {
        super();
    }

    /**
     * Creates a FolderId instance.
     *
     * @return A FolderId.
     */
    @Override
    public ServiceId createId() {
        return new FolderId();
    }

    /**
     * Gets the folder the change applies to. Folder is null when ChangeType
     * is equal to ChangeType.Delete. In that case, use the FolderId property to
     * retrieve the Id of the folder that was deleted.
     *
     * @return the folder
     */
    public Folder getFolder() {
        return (Folder) this.getServiceObject();
    }

    /**
     * Gets the folder the change applies to. Folder is null when ChangeType
     * is equal to ChangeType.Delete. In that case, use the FolderId property to
     * retrieve the Id of the folder that was deleted.
     *
     * @return the folder id
     */
    public FolderId getFolderId() throws ExchangeXmlException {
        return (FolderId) this.getId();
    }

}
