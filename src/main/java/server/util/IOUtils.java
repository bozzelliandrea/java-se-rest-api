package server.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class IOUtils {

    public static byte[] getByteForFile(String path) throws IOException {
        try (InputStream is = IOUtils.class.getResourceAsStream(path)) {
            Objects.requireNonNull(is, "File not found");
            byte[] content = new byte[is.available()];
            is.read(content);
            return content;
        }
    }
}
