package es.uvigo.esei.dai.hybridserver;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.*;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;
import es.uvigo.esei.dai.hybridserver.model.entity.html.HTMLManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XMLManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSDManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLTManager;
import es.uvigo.esei.dai.hybridserver.utils.SocketIOManager;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import javax.xml.ws.http.HTTPException;

public class ServiceThread implements Runnable {
    private final Socket socket;
    private HTTPRequest request;
    private HTTPResponse response;
    private ControllerFactory factory;
    private HTMLManager HTMLManager;
    private XMLManager XMLManager;
    private XSDManager XSDManager;
    private XSLTManager XSLTManager;
    private SocketIOManager ioManager;

    public ServiceThread(Socket clientSocket, ControllerFactory factory) throws IOException {
        this.socket = clientSocket;
        this.factory = factory;
        this.HTMLManager = new HTMLManager(factory.createHTMLController());
        this.XMLManager = new XMLManager(factory.createXMLController());
        this.XSDManager = new XSDManager(factory.createXSDController());
        this.XSLTManager = new XSLTManager(factory.createXSLTController());
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
                        response = HTMLManager.responseForRoot();
                        response.print(ioManager.getWriter());
                        break;

                    case "html":
                        if (method.equals("GET"))
                            response = HTMLManager.responseForGET(resourceParameters);

                        else if (method.equals("POST"))
                            response = HTMLManager.responseForPOST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = HTMLManager.responseForDELETE(resourceParameters);
                        else
                            response = AbstractManager.responseForBadRequest("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    case "xml":
                        if (method.equals("GET"))
                            response = XMLManager.responseForGET(resourceParameters);

                        else if (method.equals("POST"))
                            response = XMLManager.responseForPOST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = XMLManager.responseForDELETE(resourceParameters);
                        else
                            response = AbstractManager.responseForBadRequest("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    case "xsd":
                        if (method.equals("GET"))
                            response = XSDManager.responseForGET(resourceParameters);

                        else if (method.equals("POST"))
                            response = XSDManager.responseForPOST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = XSDManager.responseForDELETE(resourceParameters);
                        else
                            response = AbstractManager.responseForBadRequest("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    case "xslt":
                        if (method.equals("GET"))
                            response = XSLTManager.responseForGET(resourceParameters);

                        else if (method.equals("POST"))
                            response = XSLTManager.responseForPOST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = XSLTManager.responseForDELETE(resourceParameters);
                        else
                            response = AbstractManager.responseForBadRequest("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    default:
                        response = AbstractManager.responseForBadRequest("400 - Bad Request");
                        response.print(ioManager.getWriter());
                        break;
                }

            } catch (HTTPException e) {
                response = AbstractManager.responseForBadRequest("400 - Bad Request");
                response.print(ioManager.getWriter());
                e.printStackTrace();

            } catch (Exception e) {
                response = AbstractManager.responseForInternalServerError("500 - Internal Server Error");
                response.print(ioManager.getWriter());
                e.printStackTrace();
            }

        } catch (IOException e) {
            Tools.error(e);
            e.printStackTrace();
        }
    }
}
