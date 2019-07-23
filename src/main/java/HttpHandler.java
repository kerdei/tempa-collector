import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class HttpHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HttpHandler.class);
    private static final String url = "https://tempa-backend.cfapps.io/measurements/add";
    static List<HttpRequest> storedRequests = new ArrayList<>();
    private static HttpRequest currentRequest;

    static HttpResponse<String> post(String temperature) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String json = "" +
                    "\t{\n" +
                    "        \"meterID\": 1,\n" +
                    "        \"value\": " + temperature + ",\n" +
                    "        \"date\": \"" + LocalDateTime.now().toString() + "\",\n" +
                    "        \"userName\": \"kerdei\"\n" +
                    "    }";

            currentRequest = HttpRequest.newBuilder(URI.create(url))
                    .header("content-type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            LOG.info("post current temperature: " + temperature);
            return client.send(currentRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            LOG.info("Storing current request. Can't post because of an error:", e);
            storedRequests.add(currentRequest);
        }
        return null;
    }

    static void postStoredTemperatureRequests(HttpResponse httpResponse) {
        LOG.info("Status code: " + httpResponse.statusCode());
        if (!HttpHandler.storedRequests.isEmpty()) {
            for (HttpRequest httpRequest : HttpHandler.storedRequests) {
                LOG.info("posting stored measurements");
                HttpHandler.postStoredRequest(httpRequest);
            }
            HttpHandler.storedRequests.clear();
        }
    }

    private static HttpResponse postStoredRequest(HttpRequest storedRequest) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            LOG.info("post stored entity");
            return client.send(storedRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}