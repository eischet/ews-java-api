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
import com.eischet.ews.api.core.enumeration.service.error.ServiceErrorHandling;
import com.eischet.ews.api.core.response.GetItemResponse;
import com.eischet.ews.api.core.response.ServiceResponse;

/**
 * Represents a GetItem request specialized to return ServiceResponse.
 */
public final class GetItemRequestForLoad extends GetItemRequestBase<ServiceResponse> {

    /**
     * Initializes a new instance of the class.
     *
     * @param service           the service
     * @param errorHandlingMode the error handling mode
     * @throws Exception
     */
    public GetItemRequestForLoad(ExchangeService service, ServiceErrorHandling errorHandlingMode)
            throws Exception {
        super(service, errorHandlingMode);
    }

    /**
     * Creates the service response.
     *
     * @param service       the service
     * @param responseIndex the response index
     * @return Service response.
     */
    @Override
    protected ServiceResponse createServiceResponse(ExchangeService service,
                                                    int responseIndex) {
        return new GetItemResponse(this.getItemIds().getItemIdWrapperList(
                responseIndex), this.getPropertySet());

    }
}
