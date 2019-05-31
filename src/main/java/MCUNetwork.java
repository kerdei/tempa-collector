import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

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
            LOG.info(httpResponse.getStatusLine().toString());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}
