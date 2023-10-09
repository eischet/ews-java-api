package com.eischet.ews.api.core.exception;

/**
 * Represents a general exception from the Exchange Web Services.
 * <p>
 * The original EWS code had "throws Exception" in over 8000 places (!) and defined a handful of
 * exception classes that directly extend Exception. I assume that this code has been generated from the equivalent
 * C# code, where checked exceptions don't exist.
 * This has been changed so that any exception thrown by EWS code derives from this class.
 * </p>
 * <p>grep -ri "throws exception" * | wc -l # result: 8241</p>
 */
public class ExchangeException extends Exception {
    public ExchangeException() {
    }
    public ExchangeException(final String message) {
        super(message);
    }

    public ExchangeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExchangeException(final Throwable cause) {
        super(cause);
    }
}
