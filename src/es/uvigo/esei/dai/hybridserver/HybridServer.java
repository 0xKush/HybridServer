package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.http.ServiceThread;
import org.apache.http.HttpVersion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;

public class HybridServer {
    private static final int SERVICE_PORT = 8888;
    private static final int NUM_CLIENTS = 50;
    private Thread serverThread;
    private boolean stop;

    public HybridServer() {

    }

    public HybridServer(Map<String, String> pages) {

    }

    public HybridServer(Properties properties) {

    }

    public int getPort() {
        return SERVICE_PORT;
    }

    public void start() {
        this.serverThread = new Thread() {
            @Override
            public void run() {
                try (final ServerSocket serverSocket = new ServerSocket(SERVICE_PORT)) {

                    ExecutorService threadPool = Executors.newFixedThreadPool(NUM_CLIENTS);

                    while (true) {

                        Socket socket = serverSocket.accept();
                        if (stop)
                            break;

                        threadPool.execute(new ServiceThread(socket));

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
    }
}
