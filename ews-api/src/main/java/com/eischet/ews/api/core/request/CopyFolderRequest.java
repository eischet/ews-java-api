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

import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.XmlElementNames;
import com.eischet.ews.api.core.enumeration.misc.ExchangeVersion;
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.response.MoveCopyFolderResponse;

/**
 * Represents a CopyFolder request.
 */
public class CopyFolderRequest extends MoveCopyFolderRequest<MoveCopyFolderResponse> {

    /**
     * Initializes a new instance of the CopyFolderRequest class.
     *
     * @param service           the service
     * @param errorHandlingMode the error handling mode
     */
    public CopyFolderRequest(ExchangeService service, ServiceErrorHandling errorHandlingMode) throws Exception {
        super(service, errorHandlingMode);
    }

    /**
     * Creates the service response.
     *
     * @param service       The Service
     * @param responseIndex Index of the response.
     * @return Service response.
     */
    @Override
    protected MoveCopyFolderResponse createServiceResponse(
            ExchangeService service, int responseIndex) {
        return new MoveCopyFolderResponse();
    }

    /**
     * Gets the name of the XML element.
     *
     * @return XML element name
     */
    @Override
    public String getXmlElementName() {
        return XmlElementNames.CopyFolder;
    }

    /**
     * Gets the name of the response XML element.
     *
     * @return XML element name
     */
    @Override
    protected String getResponseXmlElementName() {
        return XmlElementNames.CopyFolderResponse;
    }

    /**
     * Gets the name of the response message XML element.
     *
     * @return XML element name.
     */
    @Override
    protected String getResponseMessageXmlElementName() {
        return XmlElementNames.CopyFolderResponseMessage;
    }

    /**
     * Gets the request version.
     *
     * @return Earliest Exchange version in which this request is supported.
     */
    @Override
    protected ExchangeVersion getMinimumRequiredServerVersion() {
        return ExchangeVersion.Exchange2007_SP1;
    }

}
