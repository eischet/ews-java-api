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

package com.eischet.ews.api.core.exception.service.local;

/**
 * Represents an error that occurs when the XML for a request cannot be
 * serialized.
 */
public class ServiceXmlSerializationException extends ServiceLocalException {

    /**
     * Constant serialized ID used for compatibility.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ServiceXmlSerializationException Constructor.
     */
    public ServiceXmlSerializationException() {
        super();
    }

    /**
     * Instantiates a new service xml serialization exception.
     *
     * @param message the message
     */
    public ServiceXmlSerializationException(String message) {
        super(message);

    }

    /**
     * Instantiates a new service xml serialization exception.
     *
     * @param message        the message
     * @param innerException the inner exception
     */
    public ServiceXmlSerializationException(String message,
                                            Exception innerException) {
        super(message, innerException);
    }

}
