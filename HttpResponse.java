import java.nio.file.Path;
import java.util.Map;

public class HttpResponse<T> {
    T body;
    int code;
    Map<String, String> headers;

    public HttpResponse(T body, int code,Map<String, String> headers) {
        this.body = body;
        this.code = code;
        this.headers = headers;
    }

    public T body() {
        return body;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public int statusCode() {
        return code;
    }

    public static class BodyHandlers {
        public static BodyHandler<String> ofString() {
            return new BodyHandler<>();
        }

        public static BodyHandler<Path> ofFile(Path path) {
            return new BodyHandler<>(path);
        }
    }
    public static class BodyHandler<T> {
        Path path;
        BodyHandler(Path path) {
            this.path = path;
        }
        BodyHandler() {}
    }

}
