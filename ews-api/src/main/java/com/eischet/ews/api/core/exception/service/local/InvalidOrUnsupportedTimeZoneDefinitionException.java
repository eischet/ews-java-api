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
 * The Class InvalidOrUnsupportedTimeZoneDefinitionException.
 * <p>
 * Thrown when time zone definition is not valid.
 *
 * @see com.eischet.ews.api.property.complex.time.TimeZoneDefinition
 * @see com.eischet.ews.api.property.complex.time.TimeZoneTransitionGroup
 */
public class InvalidOrUnsupportedTimeZoneDefinitionException extends ExchangeValidationException {

    /**
     * Constructs an <code>InvalidOrUnsupportedTimeZoneDefinitionException</code> with the specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidOrUnsupportedTimeZoneDefinitionException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message        the detail message (which is saved for later retrieval by the {@link
     *                       Throwable#getMessage()} method).
     * @param innerException the cause (which is saved for later retrieval by the {@link Throwable#getCause()}
     *                       method).  (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent
     *                       or unknown.)
     */
    public InvalidOrUnsupportedTimeZoneDefinitionException(String message, Exception innerException) {
        super(message, innerException);
    }

    public InvalidOrUnsupportedTimeZoneDefinitionException() {

        super();
    }
}
