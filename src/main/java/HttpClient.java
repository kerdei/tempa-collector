import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

class HttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);

    static HttpResponse post(String temperature) {
        try {
            org.apache.http.client.HttpClient client = new DefaultHttpClient();
            String url = "http://localhost:8080/measurements/add";
            HttpPost post = new HttpPost(url);

            String json = "" +
                    "\t{\n" +
                    "        \"meterID\": 1,\n" +
                    "        \"value\": " + temperature + ",\n" +
                    "        \"date\": \"" + LocalDateTime.now().toString() + "\",\n" +
                    "        \"userName\": \"kerdei\"\n" +
                    "    }";
            StringEntity entity = new StringEntity(json);
            post.setEntity(entity);
            post.setHeader("content-type", "application/json;charset=UTF-8");
            LOG.info("post json: ", json);
            return client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}