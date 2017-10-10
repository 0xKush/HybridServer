package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.*;
import es.uvigo.esei.dai.hybridserver.model.entity.HTMLManager;
import es.uvigo.esei.dai.hybridserver.model.entity.ResourceManager;

public class ServiceThread implements Runnable {
    private final Socket socket;
    private HTTPRequest request;
    private HTTPResponse response;
    private ControllerFactory controller;

    public ServiceThread(Socket clientSocket, ControllerFactory controller) throws IOException {
        this.socket = clientSocket;
        this.controller = controller;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {

            SocketIOManager ioManager = new SocketIOManager(socket);
            request = new HTTPRequest(ioManager.getReader());
            String resource = "", method = "";

            resource = request.getResourceName();
            method = request.getMethod().toString();

            switch (resource) {
                case "":
                case "html":
                    ResourceManager manager = new HTMLManager();
                    response = manager.createResponse(resource, method);
                    break;
                default:
                    break;
            }


        } catch (IOException | HTTPParseException e) {
            e.printStackTrace();
        }
    }
}
