package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.configuration.Configuration;
import es.uvigo.esei.dai.hybridserver.configuration.XMLConfigurationLoader;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.io.*;
import java.util.Properties;


public class Launcher {
    public static void main(String[] args) {
        HybridServer server = null;

        if (args.length == 0) {
            server = new HybridServer();

        } else if (args.length == 1) {

            try (FileReader input2 = new FileReader(args[0])) {
                File xmlFile= new File(args[0]);
                XMLConfigurationLoader xmlLoader = new XMLConfigurationLoader();
                Configuration config = xmlLoader.load(xmlFile);
                //Properties config = new Properties();
                //config.load(input2);
                server = new HybridServer(config);

            } catch (Exception e) {
                Tools.error(e);
                System.exit(1);
            }
        } else {
            Tools.error(new IOException("args > 1"));
            System.exit(1);
        }

        server.start();

    }
}



