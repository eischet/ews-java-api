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

package com.eischet.ews.api.core.exception.service.remote;

import com.eischet.ews.api.core.EwsUtilities;
import com.eischet.ews.api.core.response.CreateAttachmentResponse;
import com.eischet.ews.api.core.response.ServiceResponseCollection;

/**
 * Represents an error that occurs when a call to the CreateAttachment web
 * method fails.
 */
public final class CreateAttachmentException extends ServiceRemoteException {

    /**
     * Constant serialized ID used for compatibility.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The response.
     */
    private final ServiceResponseCollection<CreateAttachmentResponse> responses;

    /**
     * Initializes a new instance of CreateAttachmentException.
     *
     * @param serviceResponses the service response
     * @param message          the message
     */
    public CreateAttachmentException(ServiceResponseCollection<CreateAttachmentResponse> serviceResponses,
                                     String message) {
        super(message);
        EwsUtilities.ewsAssert(serviceResponses != null,
                "MultiServiceResponseException.ctor", "serviceResponses is null");

        this.responses = serviceResponses;
    }

    /**
     * Initializes a new instance of CreateAttachmentException.
     *
     * @param serviceResponses the service response
     * @param message          the message
     * @param innerException   the inner exception
     */
    protected CreateAttachmentException(
            ServiceResponseCollection<CreateAttachmentResponse> serviceResponses,
            String message, Exception innerException) {
        super(message, innerException);
        EwsUtilities.ewsAssert(serviceResponses != null,
                "MultiServiceResponseException.ctor", "serviceResponses is null");

        this.responses = serviceResponses;
    }
}
