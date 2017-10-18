package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.*;
import es.uvigo.esei.dai.hybridserver.model.entity.HTMLManager;
import es.uvigo.esei.dai.hybridserver.utils.SocketIOManager;

public class ServiceThread implements Runnable {
    private final Socket socket;
    private HTTPRequest request;
    private HTTPResponse response;
    private ControllerFactory factory;
    private HTMLManager htmlManager;

    public ServiceThread(Socket clientSocket, ControllerFactory factory) throws IOException {
        this.socket = clientSocket;
        this.factory = factory;
        this.htmlManager = new HTMLManager();
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
                    response = htmlManager.responseForRoot();
                    response.print(ioManager.getWriter());
                    break;

                case "html":
                    response = htmlManager.responseForHTML(factory, method, resourceParameters);
                    response.print(ioManager.getWriter());
                    break;

                default:
                    response = htmlManager.responseForInvalidResource("400 - Bad Request");
                    response.print(ioManager.getWriter());
                    break;
            }


        } catch (IOException | HTTPParseException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
