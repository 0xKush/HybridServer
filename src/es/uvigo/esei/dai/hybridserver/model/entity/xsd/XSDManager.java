package es.uvigo.esei.dai.hybridserver.model.entity.xsd;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.uvigo.esei.dai.hybridserver.model.entity.wsManager.wsConnection;
import static es.uvigo.esei.dai.hybridserver.model.entity.wsManager.wsGetContent;
import static es.uvigo.esei.dai.hybridserver.model.entity.wsManager.wsGetList;

public class XSDManager extends AbstractManager {


    private XSDController xsdController;
    private Map<ServerConfiguration, hbSEI> remoteServices;

    public XSDManager(ControllerFactory factory) {
        if (factory != null) {
            this.xsdController = factory.createXSDController();
            this.remoteServices = wsConnection(this.xsdController.getServerList());
        } else {
            this.xsdController = null;
            this.remoteServices = null;
        }
    }


    @Override
    public HTTPResponse responseForGET(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        String remoteContent;


        if (this.xsdController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.isEmpty()) {

                //Tools.info("resource:xml - without parameters");

                List<XSD> list = this.xsdController.list();
                Iterator<XSD> it = list.iterator();
                StringBuilder content = new StringBuilder();

                content.append("<html>\n" +
                        "<head>\n" +
                        "\t<meta charset=\"UTF-8\">\n" +
                        "\t<title>Hybrid Server</title>\n" +
                        "\t<h1>HybridServer</h1>\n" +
                        "</head>\n" +
                        "<body><ul>");
                while (it.hasNext()) {

                    XSD xsd = it.next();
                    content.append("<li>\n" + "<a href=\"xsd?uuid=" + xsd.getUuid() + "\">" + xsd.getUuid() + "</a>"
                            + "</li>\n");
                }

                //== == == == == == WebServices == == == == == == ==

                if (this.remoteServices != null) {
                    remoteContent = wsGetList(this.remoteServices, "xsd");
                    content.append(remoteContent);
                }

                //== == == == == == WebServices END == == == == ====

                content.append("\t</ul>\n" +
                        "\t\n" +
                        "</body>\n" +
                        "</html>");

                response.setStatus(HTTPResponseStatus.S200);
                response.putParameter("Content-Type", "text/html");
                response.setContent(content.toString());


            } else {

                //Tools.info("resource:xml - with parameters");

                if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                    String uuid = resourceParameters.get("uuid");
                    XSD xsd = xsdController.get(uuid);

                    if (xsd != null) {

                        //Tools.info("S200(OK)");

                        response.setStatus(HTTPResponseStatus.S200);
                        response.putParameter("Content-Type", "application/xml");
                        response.setContent(xsd.getContent());

                    } else {
                        //== == == == == == WebServices == == == == == == ==
                        if (this.remoteServices != null) {
                            if ((remoteContent = wsGetContent(this.remoteServices, "xsd", uuid)) != null) {

                                response.setStatus(HTTPResponseStatus.S200);
                                response.putParameter("Content-Type", "application/xml");
                                response.setContent(remoteContent);

                            } else {
                                response = responseForNotFound("404 - The XSD does not exist");
                            }
                        } else {
                            response = responseForNotFound("404 - The XSD does not exist");
                        }
                        //== == == == == == WebServices END == == == == ====
                    }
                } else {
                    response = responseForNotFound("404 - Not Found");
                }
            }
        }
        return response;
    }

    @Override
    public HTTPResponse responseForPOST(Map<String, String> resourceParameters) {

        //Tools.info("Response for Post");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        UUID randomUuid = UUID.randomUUID();
        String uuid = randomUuid.toString();
        String content;

        if (this.xsdController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");
        } else {

            if (resourceParameters.containsKey("xsd")) {

                content = resourceParameters.get("xsd");
                this.xsdController.add(uuid, content);

                response.setStatus(HTTPResponseStatus.S200);
                response.putParameter("Content-Type", "text/html");
                response.setContent("<a href=\"xsd?uuid=" + uuid.toString() + "\">" + uuid.toString() + "</a>");


            } else {
                return responseForBadRequest("400 - Bad Request");
            }
        }

        return response;
    }

    @Override
    public HTTPResponse responseForDELETE(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        String uuid;

        if (this.xsdController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("uuid")) {

                uuid = resourceParameters.get("uuid");

                if (this.xsdController.get(uuid) != null) {

                    this.xsdController.delete(uuid);
                    response.setStatus(HTTPResponseStatus.S200);
                    response.putParameter("Content-Type", "text/html");
                    response.setContent("The XSD has been deleted");

                } else {
                    response = responseForNotFound("404 - The XSD does not exist");
                }
            } else {
                response = responseForBadRequest("400 - Invalid parameter");
            }
        }
        return response;
    }
}
