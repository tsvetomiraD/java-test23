import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HttpRequest {
    public Map<String, String> headers = new HashMap<>();
    public URI uri = null;
    public String method = null;
    InputStream body = null;

    public static Builder newBuilder() {
        return new Builder(new HttpRequest());
    }

    public static class Builder {
        HttpRequest httpRequest;

        protected Builder(HttpRequest httpRequest) {
            this.httpRequest = httpRequest;
        }

        public HttpRequest.Builder uri(URI uri) {
            httpRequest.uri = uri;
            return this;
        }

        public HttpRequest.Builder header(String var1, String var2) {
            httpRequest.headers.put(var1, var2);
            return this;
        }

        public HttpRequest.Builder headers(String... var1) {
            if (var1.length % 2 != 0) {
                throw new IllegalArgumentException("headers should be paired");
            }

            int count = 0;
            String s1 = null;
            for (String s : var1) {
                if (count % 2 == 0) {
                    s1 = s;
                    count++;
                    continue;
                }
                count++;
                httpRequest.headers.put(s1, s);
            }
            return this;
        }

        public HttpRequest.Builder setHeader(String var1, String var2) {
            return this;
        }

        public HttpRequest.Builder GET() {
            httpRequest.method = "GET";
            return this;
        }

        public HttpRequest.Builder POST(HttpRequest.BodyPublisher var1) {
            httpRequest.method = "POST";
            httpRequest.body = var1.body;
            return this;
        }

        public HttpRequest.Builder PUT(HttpRequest.BodyPublisher var1) {
            httpRequest.method = "PUT";
            httpRequest.body = var1.body;
            return this;
        }

        public HttpRequest.Builder DELETE() {
            httpRequest.method = "DELETE";
            return this;
        }

        public HttpRequest build() {
            return httpRequest;
        }
    }

    public static class BodyPublishers {
        public static BodyPublisher ofString(String body) {
            return ofString(body, StandardCharsets.UTF_8);
        }

        public static BodyPublisher ofString(String s, Charset charset) {
            s = new String(s.getBytes(), charset);
            InputStream inputStream = new ByteArrayInputStream(s.getBytes());
            return new BodyPublisher(inputStream);
        }

        public static BodyPublisher fromFile(Path path) throws Exception {
            InputStream is = new FileInputStream(path.toFile());
            return new BodyPublisher(is);
        }

        public static BodyPublisher ofByteArray(byte[] buf) {
            return BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(buf));
        }

        public static BodyPublisher ofInputStream(Supplier<? extends InputStream> streamSupplier) {
            return new BodyPublisher(streamSupplier.get());
        }

        public static BodyPublisher noBody() {
            return new BodyPublisher(null);
        }
    }

    public static class BodyPublisher {
        InputStream body;
        protected BodyPublisher(InputStream body) {
            this.body = body;
        }
    }
}
