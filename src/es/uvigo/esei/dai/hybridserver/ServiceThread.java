package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.*;
import es.uvigo.esei.dai.hybridserver.model.entity.HTMLManager;
import es.uvigo.esei.dai.hybridserver.utils.SocketIOManager;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import javax.xml.ws.http.HTTPException;

public class ServiceThread implements Runnable {
    private final Socket socket;
    private HTTPRequest request;
    private HTTPResponse response;
    private ControllerFactory factory;
    private HTMLManager htmlManager;
    private SocketIOManager ioManager;

    public ServiceThread(Socket clientSocket, ControllerFactory factory) throws IOException {
        this.socket = clientSocket;
        this.factory = factory;
        this.htmlManager = new HTMLManager();
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {
            try {
                ioManager = new SocketIOManager(socket);
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
                        response = htmlManager.responseForBadRequest("400 - Bad Request");
                        response.print(ioManager.getWriter());
                        break;
                }

            } catch (HTTPException e) {
                response = htmlManager.responseForBadRequest("400 - Bad Request");
                response.print(ioManager.getWriter());
                e.printStackTrace();

            } catch (Exception e) {
                response = htmlManager.responseForInternalServerError("500 - Internal Server Error");
                response.print(ioManager.getWriter());
                e.printStackTrace();
            }

        } catch (IOException e) {
            Tools.error(e);
            e.printStackTrace();
        }
    }
}
