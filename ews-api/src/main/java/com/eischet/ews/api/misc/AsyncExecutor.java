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

import java.util.Objects;
import java.util.concurrent.*;

public class AsyncExecutor extends ThreadPoolExecutor implements ExecutorService {
    final static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);

    public AsyncExecutor() {
        super(1, 5, 10, TimeUnit.SECONDS, queue);
    }

    public <T> Future<T> submit(Callable<T> task, AsyncCallback callback) {
        RunnableFuture<T> ftask = newTaskFor(Objects.requireNonNull(task));
        execute(ftask);
        if (callback != null) {
            callback.setTask(ftask);
        }
        new Thread(callback).start();
        return ftask;
    }
}
