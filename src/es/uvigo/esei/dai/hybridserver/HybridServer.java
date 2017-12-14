package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.controller.factory.DBControllerFactory;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.*;

public class HybridServer {
    private int servicePort;
    private int numClients;
    private String dbUrl, dbUser, dbPass;
    private ControllerFactory factory;
    private Configuration config;

    private Thread serverThread;
    private ExecutorService threadPool;
    private boolean stop;


    public HybridServer() {
        servicePort = 12345;
        numClients = 50;
        dbUrl = "jdbc:mysql://localhost:3306/hstestdb";
        dbUser = "hsdb";
        dbPass = "hsdbpass";
    }

    public HybridServer(Configuration config) {

        factory = new DBControllerFactory(config);
    }

    public HybridServer(Properties properties) {

        servicePort = Integer.parseInt(properties.getProperty("port"));
        numClients = Integer.parseInt(properties.getProperty("numClients"));
        dbUrl = properties.getProperty("db.url");
        dbUser = properties.getProperty("db.user");
        dbPass = properties.getProperty("db.password");

        config = new Configuration(servicePort, numClients, null, dbUser, dbPass, dbUrl, new ArrayList<ServerConfiguration>());
        factory = new DBControllerFactory(config);
    }

    public int getPort() {
        return servicePort;
    }

    public void start() {
        Tools.info("RUNNING" + "\n" +
                "Port: " + servicePort + "\n" +
                "numClients: " + numClients + "\n" +
                "dbUrl: " + dbUrl + "\n" +
                "dbUser: " + dbUser + "\n" +
                "dbPass: " + dbPass);


        this.serverThread = new Thread() {
            @Override
            public void run() {
                try (final ServerSocket serverSocket = new ServerSocket(servicePort)) {

                    threadPool = Executors.newFixedThreadPool(numClients);

                    while (true) {

                        Socket socket = serverSocket.accept();
                        if (stop)
                            break;

                        threadPool.execute(new ServiceThread(socket, factory));

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        this.stop = false;
        this.serverThread.start();
    }

    public void stop() {
        this.stop = true;

        try (Socket socket = new Socket("localhost", servicePort)) {
            // Esta conexión se hace, simplemente, para "despertar" el hilo servidor
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {

            this.serverThread.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.serverThread = null;


        threadPool.shutdownNow();

        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
