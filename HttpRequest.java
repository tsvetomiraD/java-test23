

import jdk.internal.net.http.RequestPublishers;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Flow;
import java.util.function.Supplier;

public class HttpRequest {
    public Map<String, String> headers = new HashMap<>();
    public URI uri = null;
    public String method = null;
    String body = null;
    public static Builder newBuilder() {
        return new HttpRequestBuilderImpl(new HttpRequest());
    }

    public interface Builder {
        HttpRequest.Builder uri(URI var1);


        HttpRequest.Builder header(String var1, String var2);

        HttpRequest.Builder headers(String... var1);

        HttpRequest.Builder setHeader(String var1, String var2);

        HttpRequest.Builder GET();

        HttpRequest.Builder POST(HttpRequest.BodyPublisher var1);

        HttpRequest.Builder PUT(HttpRequest.BodyPublisher var1);

        HttpRequest.Builder DELETE();

        HttpRequest build();
    }

    public static class BodyPublishers {
        private BodyPublishers() {
        }


        public static BodyPublisher ofString(String body) {
            return ofString(body, StandardCharsets.UTF_8);
        }

        public static BodyPublisher ofString(String s, Charset charset) {
            s = new String(s.getBytes(), charset);
            return new BodyPublisher(s);
        }


        public static BodyPublisher fromFile(Path path) throws Exception {
            Reader r = new FileReader(path.toFile());
            BufferedReader br = new BufferedReader(r);
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            return new BodyPublisher(sb.toString());
        }


        public static BodyPublisher noBody() {
                return new BodyPublisher(null);
        }
    }
    public static class BodyPublisher {
        String body;
        long contentLength;
        protected BodyPublisher(String body) {
            this.body = body;
        }
    }
}
