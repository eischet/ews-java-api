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

import com.eischet.ews.api.core.EwsServiceXmlWriter;
import com.eischet.ews.api.core.ExchangeService;
import com.eischet.ews.api.core.XmlAttributeNames;
import com.eischet.ews.api.core.enumeration.service.DeleteMode;
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.exception.service.local.ServiceXmlSerializationException;
import com.eischet.ews.api.core.exception.xml.ExchangeXmlException;
import com.eischet.ews.api.core.response.ServiceResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents an abstract Delete request.
 *
 * @param <TResponse> The type of the response.
 */
abstract class DeleteRequest<TResponse extends ServiceResponse> extends
        MultiResponseServiceRequest<TResponse> {

    private static final Logger LOG = Logger.getLogger(DeleteRequest.class.getCanonicalName());

    /**
     * Delete mode. Default is SoftDelete.
     */
    private DeleteMode deleteMode = DeleteMode.SoftDelete;

    /**
     * Initializes a new instance of the DeleteRequest class.
     *
     * @param service           The Servcie
     * @param errorHandlingMode Indicates how errors should be handled.
     * @throws Exception
     */
    protected DeleteRequest(ExchangeService service,
                            ServiceErrorHandling errorHandlingMode)
            throws Exception {
        super(service, errorHandlingMode);
    }

    /**
     * Writes XML attribute.
     *
     * @param writer The writer.
     * @throws ServiceXmlSerializationException the service xml serialization exception
     */
    @Override
    protected void writeAttributesToXml(EwsServiceXmlWriter writer)
            throws ExchangeXmlException {
        super.writeAttributesToXml(writer);
        writer.writeAttributeValue(XmlAttributeNames.DeleteType, this.getDeleteMode());
    }

    /**
     * Gets the delete mode.
     *
     * @return the delete mode
     */
    public DeleteMode getDeleteMode() {
        return this.deleteMode;
    }

    /**
     * Gets the delete mode.e
     *
     * @param deleteMode the new delete mode
     */
    public void setDeleteMode(DeleteMode deleteMode) {
        this.deleteMode = deleteMode;
    }

}
