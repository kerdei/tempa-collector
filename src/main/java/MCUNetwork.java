import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class MCUNetwork {

    private static final Logger LOG = LoggerFactory.getLogger(MCUNetwork.class);

    private DatagramSocket datagramSocket;
    private byte[] data;
    private int dataLength = 255;

    MCUNetwork() throws IOException {
        datagramSocket = new DatagramSocket(65172);
    }

    void listen() throws IOException {
        data = new byte[dataLength];

        DatagramPacket packet = new DatagramPacket(data, dataLength);

        try {
            LOG.info("Wait for packet...");
            datagramSocket.setSoTimeout(0); //Disable timeout
            datagramSocket.receive(packet);
            data = packet.getData();
            String temperature = new String(data, 0, packet.getLength());
            LOG.info("send temperature ", temperature);
            HttpResponse httpResponse = HttpClient.post(temperature);
            if (httpResponse != null) {
                LOG.info(httpResponse.getStatusLine().toString());
                if (!HttpClient.storedStringEntities.isEmpty()) {
                    for (StringEntity stringEntity : HttpClient.storedStringEntities) {
                        LOG.info("posting stored measurements");
                        HttpClient.post(stringEntity);
                    }
                    HttpClient.storedStringEntities.clear();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
