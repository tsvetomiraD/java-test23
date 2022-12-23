import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestBuilderImpl implements HttpRequest.Builder {
    HttpRequest  httpRequest;
    protected HttpRequestBuilderImpl(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }
    @Override
    public HttpRequest.Builder uri(URI uri) {
        httpRequest.uri = uri;
        return this;
    }

    @Override
    public HttpRequest.Builder header(String var1, String var2) {
        httpRequest.headers.put(var1, var2);
        return this;
    }

    @Override
    public HttpRequest.Builder headers(String... var1) {
        int count = 0;
        String s1 = null;
        for (String s: var1) {
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

    @Override
    public HttpRequest.Builder setHeader(String var1, String var2) {
        return this;
    }

    @Override
    public HttpRequest.Builder GET() {
        httpRequest.method = "GET";
        return this;
    }

    @Override
    public HttpRequest.Builder POST(HttpRequest.BodyPublisher var1) {
        httpRequest.method = "POST";
        httpRequest.body = var1.body;
        return this;
    }

    @Override
    public HttpRequest.Builder PUT(HttpRequest.BodyPublisher var1) {
    httpRequest.method = "PUT";
        return this;
    }

    @Override
    public HttpRequest.Builder DELETE() {
        httpRequest.method = "DELETE";
        return this;
    }

    @Override
    public HttpRequest build() {
        return httpRequest;
    }
}
