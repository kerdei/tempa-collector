import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.debug("Collector.listen()");

        try {
            MCUNetwork mcuNetwork = new MCUNetwork();
            LOG.debug("mcuNetwork.listen()");
            while (true) {
                mcuNetwork.listen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
