import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.http.HttpResponse;

class MCUConnection {

    private static final Logger LOG = LoggerFactory.getLogger(MCUConnection.class);
    private static final int COLLECTOR_PORT = 65172;

    //DatagramSocket's parameters
    private DatagramSocket datagramSocket;
    private byte[] data;
    private int dataLength = 255;

    MCUConnection() {
        try {
            datagramSocket = new DatagramSocket(COLLECTOR_PORT);
            datagramSocket.setSoTimeout(0); //Disable timeout
        } catch (SocketException e) {
            LOG.error("the socket could not be opened,or the socket could not bind to the specified local port");
        }
    }

    void listenAndFowardTemperature() throws IOException {
        LOG.info("Waiting for a new packet...");
        data = new byte[dataLength];
        DatagramPacket packet = new DatagramPacket(data, dataLength);
        String temperature = getTemperatureFromMCU(packet);

        LOG.info("Packet received, sending to the backend...");
        HttpResponse httpResponse = HttpHandler.post(temperature);
        if (httpResponse != null) {
            HttpHandler.postStoredTemperatureRequests(httpResponse);
        }
    }

    private String getTemperatureFromMCU(DatagramPacket packet) throws IOException {

        datagramSocket.receive(packet);
        data = packet.getData();
        String temperature = new String(data, 0, packet.getLength());
        LOG.info("send temperature ", temperature);
        return temperature;
    }


}
