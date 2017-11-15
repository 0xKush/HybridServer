package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.controller.DBControllerFactory;
import es.uvigo.esei.dai.hybridserver.controller.MapControllerFactory;
import es.uvigo.esei.dai.hybridserver.model.dao.HTMLDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.HTMLMapDAO;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
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


    public HybridServer(Map<String, String> pages) {
        factory = new MapControllerFactory(pages);
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

            if (this.threadPool.awaitTermination(10, TimeUnit.SECONDS))
                this.threadPool.shutdownNow();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.serverThread = null;
    }
}
