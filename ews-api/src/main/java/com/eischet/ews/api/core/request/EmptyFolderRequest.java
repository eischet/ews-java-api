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

package com.eischet.ews.api.core.request;

import com.eischet.ews.api.core.*;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.misc.XmlNamespace;
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.response.ServiceResponse;
import com.eischet.ews.api.misc.FolderIdWrapperList;

/**
 * Represents an EmptyFolder request.
 */
public final class EmptyFolderRequest extends DeleteRequest<ServiceResponse> {

    private final FolderIdWrapperList folderIds = new FolderIdWrapperList();
    private boolean deleteSubFolders;

    /**
     * Initializes a new instance of the EmptyFolderRequest class.
     *
     * @param service           The service.
     * @param errorHandlingMode Indicates how errors should be handled.
     * @throws Exception on error
     */
    public EmptyFolderRequest(ExchangeService service, ServiceErrorHandling errorHandlingMode)
            throws Exception {
        super(service, errorHandlingMode);
    }

    /**
     * Validates request.
     *
     * @throws Exception on error
     */
    @Override
    protected void validate() throws Exception {
        super.validate();
        EwsUtilities.validateParam(this.getFolderIds(), "FolderIds");
        this.getFolderIds().validate(this.getService().
                getRequestedServerVersion());
    }

    /**
     * Gets the expected response message count.
     *
     * @return Number of expected response messages.
     */
    @Override
    protected int getExpectedResponseMessageCount() {
        return this.getFolderIds().getCount();
    }

    /**
     * Creates the service response.
     *
     * @param service       The service.
     * @param responseIndex Index of the response.
     * @return Service object
     */
    @Override
    protected ServiceResponse createServiceResponse(ExchangeService service,
                                                    int responseIndex) {
        return new ServiceResponse();
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name.
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.EmptyFolder;
    }

    /**
     * Gets the name of the response XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getResponseXmlElementName() {
        return XmlElementNames.EmptyFolderResponse;
    }

    /**
     * Gets the name of the response message XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getResponseMessageXmlElementName() {
        return XmlElementNames.EmptyFolderResponseMessage;
    }

    /**
     * Writes XML elements.
     *
     * @param writer The writer.
     * @throws Exception
     */
    @Override
    protected void writeElementsToXml(EwsServiceXmlWriter writer)
            throws Exception {
        this.getFolderIds().writeToXml(
                writer,
                XmlNamespace.Messages,
                XmlElementNames.FolderIds);
    }

    /**
     * Writes XML attribute.
     *
     * @param writer The writer.
     */
    @Override
    protected void writeAttributesToXml(EwsServiceXmlWriter writer) throws ExchangeXmlException {
        super.writeAttributesToXml(writer);
        writer.writeAttributeValue(XmlAttributeNames.DeleteSubFolders, this.deleteSubFolders);
    }

    /**
     * Gets the request version.
     *
     * @return Earliest Exchange version in which this request is supported.
     */
    @Override
    protected ExchangeVersion getMinimumRequiredServerVersion() {
        return ExchangeVersion.Exchange2010_SP1;
    }

    /**
     * Gets the folder ids.
     *
     * @return The folder ids.
     */
    public FolderIdWrapperList getFolderIds() {
        return this.folderIds;
    }

    /**
     * Gets a value indicating whether empty folder should also delete sub folder.
     *
     * @value true if empty folder should also delete sub folder, otherwise false.
     */
    protected boolean getDeleteSubFolders() {
        return deleteSubFolders;
    }

    /**
     * Sets a value indicating whether empty folder should also delete sub folder.
     *
     * @value true if empty folder should also delete sub folder, otherwise false.
     */
    public void setDeleteSubFolders(boolean value) {
        this.deleteSubFolders = value;
    }

}
