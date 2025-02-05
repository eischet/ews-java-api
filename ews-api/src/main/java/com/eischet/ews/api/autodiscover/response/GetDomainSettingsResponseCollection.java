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

package com.eischet.ews.api.autodiscover.response;

import com.eischet.ews.api.autodiscover.AutodiscoverResponseCollection;
import com.eischet.ews.api.core.XmlElementNames;

/**
 * Represents a collection of response to GetDomainSettings.
 */
public final class GetDomainSettingsResponseCollection extends
        AutodiscoverResponseCollection<GetDomainSettingsResponse> {

    /**
     * Initializes a new instance of the AutodiscoverResponseCollection class.
     */
    public GetDomainSettingsResponseCollection() {
    }

    /**
     * Create a response instance.
     *
     * @return GetDomainSettingsResponse.
     */
    @Override
    protected GetDomainSettingsResponse createResponseInstance() {
        return new GetDomainSettingsResponse();
    }

    /**
     * Gets the name of the response collection XML element.
     *
     * @return Response collection XMl element name.
     */
    @Override
    protected String getResponseCollectionXmlElementName() {
        return XmlElementNames.DomainResponses;
    }

    /**
     * Gets the name of the response instance XML element.
     *
     * @return Response instance XMl element name.
     */
    @Override
    protected String getResponseInstanceXmlElementName() {
        return XmlElementNames.DomainResponse;
    }

}
