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

package com.eischet.ews.api.misc;

import com.eischet.ews.api.core.exception.http.EWSHttpException;
import com.eischet.ews.api.core.exception.http.HttpErrorException;
import com.eischet.ews.api.http.ExchangeHttpClient;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CallableMethod implements Callable<ExchangeHttpClient.Request> {

    private static final Logger LOG = Logger.getLogger(CallableMethod.class.getCanonicalName());

    ExchangeHttpClient.Request request;

    public CallableMethod(ExchangeHttpClient.Request request) {
        this.request = request;
    }

    protected ExchangeHttpClient.Request executeMethod() throws EWSHttpException, HttpErrorException, IOException {
        request.executeRequest();
        return request;
    }

    public ExchangeHttpClient.Request call() {
        try {
            return executeMethod();
        } catch (EWSHttpException | IOException | HttpErrorException e) {
            LOG.log(Level.SEVERE, "error executing web request", e);
        }
        return request;
    }
}
