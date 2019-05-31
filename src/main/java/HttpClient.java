import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    static HttpResponse post(String temperature) {
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            String url = "http://localhost:8080/measurements/add";
            HttpPost post = new HttpPost(url);

            String format = formatter.format(LocalDate.now());
            String json = "" +
                    "\t{\n" +
                    "        \"meterID\": 1,\n" +
                    "        \"value\": " + temperature + ",\n" +
                    "        \"date\": \"" + format + "\",\n" +
                    "        \"userName\": \"kerdei\"\n" +
                    "    }";
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            post.setHeader("content-type", "application/json;charset=UTF-8");
            return client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }
}