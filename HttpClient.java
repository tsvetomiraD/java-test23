import com.sun.net.httpserver.Headers;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    public static HttpClient newHttpClient() {
        return new HttpClient();
    }

    public <T> HttpResponse<T> send(HttpRequest request) throws Exception {
        URL url = request.uri.toURL();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        sendRequest(request, con);

        int code = con.getResponseCode();
        String content = getResponse(con);

        return new HttpResponse<>(content, code);
    }

    private static String getResponse(HttpURLConnection con) throws IOException {
        StringBuilder content = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
        }

        con.disconnect();

        return content.toString();
    }

    private static void sendRequest(HttpRequest request, HttpURLConnection con) throws IOException {
        for (var s : request.headers.entrySet()) {
            con.setRequestProperty(s.getKey(), s.getValue());
        }
        con.setRequestMethod(request.method);

        if (request.body == null) {
            return;
        }

        con.setDoOutput(true);
        try (OutputStream outStream = con.getOutputStream();
             OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8")) {
            outStreamWriter.write(request.body);
            outStreamWriter.flush();
        }
    }
}
