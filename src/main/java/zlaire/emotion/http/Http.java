package zlaire.emotion.http;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class Http {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        JsonObject object = new JsonObject();
        object.addProperty("Event","chest_open_event1");
        testPost(object);
    }

    static void testGet() throws IOException, InterruptedException, ExecutionException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(10000))   //连接超时时间，单位为毫秒
                .build();

        HttpRequest request = HttpRequest.newBuilder()
//                .header("Content-Type", "application/json")
                .uri(URI.create("http://81.68.146.254:5000/api/event"))
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        response.thenAccept( a -> System.out.println(a.body()))

        ;

        Thread.sleep(1000);
    }

    public static String testPost(JsonObject json) throws IOException, InterruptedException, ExecutionException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(10000))   //连接超时时间，单位为毫秒
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create("http://127.0.0.1:5000/api/event" ))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        return response.body();
    }
    public static String encode(String str){
        return URLEncoder.encode(str, StandardCharsets.UTF_8).replace("+","%20");
    }
}
