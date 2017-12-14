package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Launcher {
    public static void main(String[] args) {
        HybridServer server = null;

        if (args.length == 0) {
            server = new HybridServer();

        } else if (args.length == 1) {

            try (InputStream input = new FileInputStream(args[0])) {
                //File xmlFile= new File(args[0]);
                //XMLConfigurationLoader xmlLoader = new XMLConfigurationLoader();
                //Configuration config = xmlLoader.load(xmlFile);
                Properties config = new Properties();
                config.load(input);
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



