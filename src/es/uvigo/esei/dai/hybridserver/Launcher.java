package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.io.*;


public class Launcher {
    public static void main(String[] args) {
        HybridServer server = null;

        if (args.length == 0) {
            server = new HybridServer();

        } else if (args.length == 1) {
            try {
                File xmlFile = new File(args[0]);
                XMLConfigurationLoader xmlLoader = new XMLConfigurationLoader();
                Configuration config = xmlLoader.load(xmlFile);
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



