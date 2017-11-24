package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.controller.factory.DBControllerFactory;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.*;

public class HybridServer {
    private int SERVICE_PORT = 12345;
    private int NUM_CLIENTS = 50;
    private Thread serverThread;
    private ExecutorService threadPool;
    private boolean stop;
    private ControllerFactory factory;
    private Configuration config;

    public HybridServer() {

    }

    public HybridServer(Configuration config) {
        this.config = config;
    }


    public HybridServer(Properties properties) {

        SERVICE_PORT = Integer.parseInt(properties.getProperty("port"));
        NUM_CLIENTS = Integer.parseInt(properties.getProperty("numClients"));

        factory = new DBControllerFactory(properties);
    }

    public int getPort() {
        return SERVICE_PORT;
    }

    public void start() {
        Tools.info("PORT: " + SERVICE_PORT);
        Tools.info("NUM_CLIENTS: " + NUM_CLIENTS);

        this.serverThread = new Thread() {
            @Override
            public void run() {
                try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) {

                    threadPool = Executors.newFixedThreadPool(NUM_CLIENTS);

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

        try (Socket socket = new Socket("localhost", SERVICE_PORT)) {
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
