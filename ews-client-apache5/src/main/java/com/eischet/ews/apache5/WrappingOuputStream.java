package com.eischet.ews.apache5;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Wraps a ByteArrayOutputStream and sets it as the entity of the HttpPost
 * when the stream is closed.
 *
 * I would have done this differently, wrapping the stream etc., but this is
 * cast to a ByteArrayOutputStream in a number of places in the ews-api module,
 * forcing us to be compatible.
 */
public class WrappingOuputStream extends ByteArrayOutputStream {
    private final HttpPost httpPost;

    public WrappingOuputStream(final HttpPost httpPost) {
        this.httpPost = httpPost;
    }

    @Override
    public void close() throws IOException {
        super.close();
        httpPost.setEntity(new ByteArrayEntity(this.toByteArray(), ContentType.APPLICATION_XML));
    }

}
