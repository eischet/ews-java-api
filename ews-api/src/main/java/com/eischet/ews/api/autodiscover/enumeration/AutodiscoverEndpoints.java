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

package com.eischet.ews.api.autodiscover.enumeration;

/**
 * Defines the types of Autodiscover endpoints that are available.
 */
public enum AutodiscoverEndpoints {

    /**
     * No endpoints available.
     */
    None(0),

    /**
     * The "legacy" Autodiscover endpoint.
     */
    Legacy(1),

    /**
     * The SOAP endpoint.
     */
    Soap(2),

    /**
     * The WS-Security endpoint.
     */
    WsSecurity(4),

    /**
     * The WS-Security/SymmetricKey endpoint.
     */
    WSSecuritySymmetricKey(8),

    /**
     * The WS-Security/X509Cert endpoint.
     */
    WSSecurityX509Cert(16);

    /**
     * The autodiscover end points.
     */
    private final int autodiscoverEndPoints;

    /**
     * Instantiates a new autodiscover endpoints.
     *
     * @param autodiscoverEndPoints the autodiscover end points
     */
    AutodiscoverEndpoints(int autodiscoverEndPoints) {
        this.autodiscoverEndPoints = autodiscoverEndPoints;
    }
}
