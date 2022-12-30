import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    public static HttpClient newHttpClient() {
        return new HttpClient();
    }

    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> t) throws Exception {
        Socket socket = new Socket(request.uri.getHost(), 80);
        sendRequest(request, socket);

        return getResponse(socket);
    }

    private <T> HttpResponse getResponse(Socket socket) {
        T body = null;
        int code = 0;
        Map<String, String> headers = new HashMap<>();
        
        try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String line = in.readLine();
            code = Integer.parseInt(line.split(" ")[1]);
            
            line = in.readLine();
            while (line != null) {
                if (line.equals("")) {
                    body = (T) in.readLine();
                    break;
                }
                
                String[] r = line.split(": ");
                String key = r[0];
                if (key.equals("access-control-expose-headers")) {
                    line = in.readLine();
                    continue;
                }
                
                String value = r[1];
                headers.put(key, value);

                line = in.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new HttpResponse<>(body, code, headers);
    }

    private static <T> void saveToFile(Path path, T body) throws IOException {
        File f = new File(path.toUri());

        try (FileWriter fileWriter = new FileWriter(f)) {
            //fileWriter.write(body);
            fileWriter.flush();
        }
    }
    
    private static void sendRequest(HttpRequest request, Socket socket) throws IOException {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream())) {
            out.println(request.method + " " + request.uri.getPath() + " HTTP/1.1\r\n");
            out.println("Host: " + request.uri.getHost());

            for (var m : request.headers.entrySet()) {
                out.println(m.getKey() + ": " + m.getValue() + "\r\n");
            }
            out.flush();

            out.println(request.body);
            out.flush();
        }
    }
}
