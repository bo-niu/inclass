import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PrimeHttpClient {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private boolean isPrime;

    public boolean isPrime() {
        return isPrime;
    }

    public void setPrime(boolean prime) {
        isPrime = prime;
    }

    public boolean getPrimeRequest(String url) throws IOException, InterruptedException {

        int retry = 1;

//        System.out.println(url);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .build();

        while (retry > 0) {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200 && statusCode != 401) {
                retry = retry - 1;
            } else if (statusCode == 200) {
//                System.out.println("success!");
                isPrime = true;
                return true;
            } else {
                isPrime = false;
                return true;
            }
        }
        return false;
    }

}
