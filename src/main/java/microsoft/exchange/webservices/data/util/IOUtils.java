package microsoft.exchange.webservices.data.util;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

    public static void closeQuietly(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
}
