package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.*;
import es.uvigo.esei.dai.hybridserver.model.entity.HTMLManager;

public class ServiceThread implements Runnable {
    private final Socket socket;
    private HTTPRequest request;
    private HTTPResponse response;
    private ControllerFactory factory;
    private HTMLManager htmlManager;

    public ServiceThread(Socket clientSocket, ControllerFactory factory) throws IOException {
        this.socket = clientSocket;
        this.factory = factory;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {

            SocketIOManager ioManager = new SocketIOManager(socket);
            request = new HTTPRequest(ioManager.getReader());
            response = new HTTPResponse();


            String resource = request.getResourceName();
            String method = request.getMethod().toString();
            Map<String, String> resourceParameters = request.getResourceParameters();

            switch (resource) {
                case "":
                case "html":
                    htmlManager = new HTMLManager();
                    response = htmlManager.createResponse(factory, resource, method, resourceParameters);
                    response.print(ioManager.getWriter());
                    break;
                default:
                    htmlManager = new HTMLManager();
                    response = htmlManager.createResponse(factory, resource, method, resourceParameters);
                    response.print(ioManager.getWriter());

            }


        } catch (IOException | HTTPParseException e) {
            e.printStackTrace();
        }
    }
}
