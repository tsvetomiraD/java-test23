import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse<T> {
    String response;
    int code;

    public HttpResponse(String response, int code) {
        this.response = response;
        this.code = code;
    }

    public String body() {
        return response;
    }

    public Map<String, String> headers() {
        Map<String, String> headers = new HashMap<>();
        int indexOf = response.indexOf("headers\":{") + "headers\":{".length();
        int indexTo = response.indexOf("}", indexOf);
        String head = response.substring(indexOf, indexTo);
        String[] arr = head.split(",");
        for (String s : arr) {
            headers.put(s.split(":")[0], s.split(":")[1]);
        }

        return headers;
    }

    public  int	statusCode() {
        return code;
    }
}
