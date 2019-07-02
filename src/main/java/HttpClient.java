import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
    private static final String url = "http://localhost:8080/measurements/add";
    static List<StringEntity> storedStringEntities = new ArrayList<>();
    private static StringEntity currentEntity;

    static HttpResponse post(String temperature) {
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);

            //TODO
            String json = "" +
                    "\t{\n" +
                    "        \"meterID\": 1,\n" +
                    "        \"value\": " + temperature + ",\n" +
                    "        \"date\": \"" + LocalDateTime.now().toString() + "\",\n" +
                    "        \"userName\": \"kerdei\"\n" +
                    "    }";
            currentEntity = new StringEntity(json);
            post.setEntity(currentEntity);
            post.setHeader("content-type", "application/json;charset=UTF-8");
            LOG.info("post current temperature: " + temperature);
            return client.execute(post);
        } catch (IOException e) {
            LOG.info("Storing entity. Can't post because of an error:", e);
            storedStringEntities.add(currentEntity);
        }
        return null;
    }

    static HttpResponse post(StringEntity pastEntity) {
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setEntity(pastEntity);
            post.setHeader("content-type", "application/json;charset=UTF-8");
            LOG.info("post stored entity");
            return client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}