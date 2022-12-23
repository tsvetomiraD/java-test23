import java.net.URI;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("SOme String"))
                .build();
        HttpResponse<String> response =
                client.send(request);
        response.headers();
        System.out.println(response.body());
    }
}