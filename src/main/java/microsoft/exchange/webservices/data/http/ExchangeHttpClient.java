package microsoft.exchange.webservices.data.http;

import microsoft.exchange.webservices.data.core.exception.http.EWSHttpException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Map;

public interface ExchangeHttpClient extends Closeable {

    interface Request {

        void setUrl(URL toURL);

        void setRequestMethod(String get);

        void setAllowAutoRedirect(boolean b);

        void setPreAuthenticate(boolean preAuthenticate);

        void setTimeout(int timeout);

        void setContentType(String s);

        void setAccept(String s);

        void setUserAgent(String userAgent);

        void setAcceptGzipEncoding(boolean acceptGzipEncoding);

        void setHeaders(Map<String, String> httpHeaders);

        void setUseDefaultCredentials(boolean useDefaultCredentials);

        void prepareConnection();

        void close() throws IOException;

        OutputStream getOutputStream() throws EWSHttpException;

        int executeRequest() throws IOException, EWSHttpException;

        int getResponseCode() throws EWSHttpException;

        InputStream getInputStream() throws EWSHttpException, IOException;

        void setAllowAuthentication(boolean b);

        String getResponseHeaderField(String headerName) throws EWSHttpException;

        Map<String, String> getResponseHeaders() throws EWSHttpException;

        String getResponseContentType() throws EWSHttpException;

        void setCredentials(String domain, String user, String pwd);

        URL getUrl();

        String getContentEncoding() throws EWSHttpException;

        String getRequestMethod();

        Map<String, String> getRequestProperty() throws EWSHttpException;

        InputStream getErrorStream() throws EWSHttpException;

        String getResponseText() throws EWSHttpException;
    }

    Request createRequest();
    Request createPoolingRequest();

}
