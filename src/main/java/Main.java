import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        LOG.debug("START TEMPA-COLLECTOR");
        try {
            MCUConnection mcuConnection = new MCUConnection();
            LOG.debug("mcuConnection.listenAndForwardTemperature");
            while (true) {
                mcuConnection.listenAndFowardTemperature();
            }
        } catch (IOException e) {
            LOG.error("IO exception at receiving");
            e.printStackTrace();
        }

    }

}
