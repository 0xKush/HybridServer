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
        this.HTMLManager = new HTMLManager(this.factory);
        this.XMLManager = new XMLManager(this.factory);
        this.XSDManager = new XSDManager(this.factory);
        this.XSLTManager = new XSLTManager(this.factory);
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
                            response = HTMLManager.GET(resourceParameters);

                        else if (method.equals("POST"))
                            response = HTMLManager.POST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = HTMLManager.DELETE(resourceParameters);
                        else
                            response = AbstractManager.S400("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    case "xml":
                        if (method.equals("GET"))
                            response = XMLManager.GET(resourceParameters);

                        else if (method.equals("POST"))
                            response = XMLManager.POST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = XMLManager.DELETE(resourceParameters);
                        else
                            response = AbstractManager.S400("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    case "xsd":
                        if (method.equals("GET"))
                            response = XSDManager.GET(resourceParameters);

                        else if (method.equals("POST"))
                            response = XSDManager.POST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = XSDManager.DELETE(resourceParameters);
                        else
                            response = AbstractManager.S400("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    case "xslt":
                        if (method.equals("GET"))
                            response = XSLTManager.GET(resourceParameters);

                        else if (method.equals("POST"))
                            response = XSLTManager.POST(resourceParameters);

                        else if (method.equals("DELETE"))
                            response = XSLTManager.DELETE(resourceParameters);
                        else
                            response = AbstractManager.S400("400 - Bad Request");


                        response.print(ioManager.getWriter());
                        break;

                    default:
                        response = AbstractManager.S400("400 - Bad Request");
                        response.print(ioManager.getWriter());
                        break;
                }

            } catch (HTTPException e) {
                response = AbstractManager.S400("400 - Bad Request");
                response.print(ioManager.getWriter());
                e.printStackTrace();

            } catch (Exception e) {
                response = AbstractManager.S500("500 - Internal Server Error");
                response.print(ioManager.getWriter());
                e.printStackTrace();
            }

        } catch (IOException e) {
            Tools.error(e);
            e.printStackTrace();
        }
    }
}
