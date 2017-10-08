package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.Socket;

import es.uvigo.esei.dai.hybridserver.SocketIOManager;
import es.uvigo.esei.dai.hybridserver.controller.DefaultHTMLController;
import es.uvigo.esei.dai.hybridserver.controller.HTMLController;
import es.uvigo.esei.dai.hybridserver.http.*;
import es.uvigo.esei.dai.hybridserver.model.entity.Document;

public class ServiceThread implements Runnable {
    private final Socket socket;
    private HTTPRequest request;
    private HTTPResponse response;

    public ServiceThread(Socket clientSocket) throws IOException {
        this.socket = clientSocket;
    }

    @Override
    public void run() {
        try (Socket socket = this.socket) {

            SocketIOManager ioManager = new SocketIOManager(socket);

            //Receive request & filter by uuid
            HTMLController htmlController = new DefaultHTMLController();
            request = new HTTPRequest(ioManager.getReader());

            if (!request.getResourceParameters().isEmpty()) {

                String uuid = request.getResourceParameters().get("uuid");
                Document page = htmlController.get(uuid);

                response = new HTTPResponse();



            } else {

            }






            /*
            HTTPResponse response = new HTTPResponse();
            response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
            response.setStatus(HTTPResponseStatus.S200);
            response.setContent("Hybrid Server + \n Cristopher Álvarez Martínez");

            ioManager.println(response.toString());
            */


        } catch (IOException | HTTPParseException e) {
            e.printStackTrace();
        }
    }
}
