package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.SocketIOManager;

public class ServiceThread implements Runnable {
    private final Socket socket;

    public ServiceThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {

            SocketIOManager ioManager = new SocketIOManager(socket);

            // Responder al cliente
            HTTPResponse response = new HTTPResponse();
            response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
            response.setStatus(HTTPResponseStatus.S200);
            response.setContent("Hybrid Server + \n Cristopher Álvarez Martínez");

            ioManager.println(response.toString());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
