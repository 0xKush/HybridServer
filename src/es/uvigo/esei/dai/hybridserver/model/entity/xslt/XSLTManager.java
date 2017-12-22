package es.uvigo.esei.dai.hybridserver.model.entity.xslt;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.XSLTController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class XSLTManager extends AbstractManager {

    private XSLTController xsltController;

    public XSLTManager(ControllerFactory factory) {
        if (factory != null) {
            this.xsltController = factory.createXSLTController();
        } else {
            this.xsltController = null;
        }
    }


    @Override
    public HTTPResponse responseForGET(Map<String, String> resourceParameters) {
        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        if (this.xsltController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.isEmpty()) {

                List<XSLT> list = this.xsltController.list();
                Iterator<XSLT> it = list.iterator();
                StringBuilder content = new StringBuilder();

                content.append("<html>\n" +
                        "<head>\n" +
                        "\t<meta charset=\"UTF-8\">\n" +
                        "\t<title>Hybrid Server</title>\n" +
                        "\t<h1>HybridServer</h1>\n" +
                        "</head>\n" +
                        "<body><ul>");
                while (it.hasNext()) {

                    XSLT xslt = it.next();
                    content.append("<li>\n" + "<a href=\"xslt?uuid=" + xslt.getUuid() + "\">" + xslt.getUuid() + "</a>"
                            + "</li>\n");
                }

                //==== Remotes ====
                Map<ServerConfiguration, List<XSLT>> remotes = this.xsltController.remoteList();

                for (Map.Entry<ServerConfiguration, List<XSLT>> remote : remotes.entrySet()) {
                    ServerConfiguration serverConfiguration = remote.getKey();
                    List<XSLT> remoteList = remote.getValue();
                    it = remoteList.iterator();

                    content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                    if (!remoteList.isEmpty()) {
                        while (it.hasNext()) {
                            XSLT doc = it.next();
                            content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "xslt?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                                    + "</li>\n");

                        }
                    }
                }

                content.append("\t</ul>\n" +
                        "\t\n" +
                        "</body>\n" +
                        "</html>");

                response.setStatus(HTTPResponseStatus.S200);
                response.putParameter("Content-Type", "text/html");
                response.setContent(content.toString());


            } else {

                if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                    String uuid = resourceParameters.get("uuid");
                    XSLT xslt = xsltController.get(uuid);

                    if (xslt != null) {

                        response.setStatus(HTTPResponseStatus.S200);
                        response.putParameter("Content-Type", "application/xml");
                        response.setContent(xslt.getContent());

                    } else {
                        response = responseForNotFound("404 - The XSLT does not exist");
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

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        UUID randomUuid = UUID.randomUUID();
        String uuid = randomUuid.toString();
        String content;
        XSD xsd;

        if (this.xsltController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");
        } else {

            if (resourceParameters.containsKey("xslt") && resourceParameters.containsKey("xsd")) {

                if ((xsd = this.xsltController.getXSD(resourceParameters.get("xsd"))) != null) {

                    content = resourceParameters.get("xslt");
                    this.xsltController.add(uuid, content, xsd.getUuid());

                    response.setStatus(HTTPResponseStatus.S200);
                    response.putParameter("Content-Type", "text/html");
                    response.setContent("<a href=\"xslt?uuid=" + uuid.toString() + "\">" + uuid.toString() + "</a>");

                } else {
                    return responseForNotFound("404 - Not Found");
                }


            } else
                return responseForBadRequest("400 - Bad Request");
        }

        return response;
    }

    @Override
    public HTTPResponse responseForDELETE(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        String uuid;

        if (this.xsltController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("uuid")) {

                uuid = resourceParameters.get("uuid");

                if (this.xsltController.get(uuid) != null) {

                    this.xsltController.delete(uuid);
                    response.setStatus(HTTPResponseStatus.S200);
                    response.putParameter("Content-Type", "text/html");
                    response.setContent("The XSLT has been deleted");

                } else {
                    response = responseForNotFound("404 - The XSLT does not exist");
                }
            } else {
                response = responseForBadRequest("400 - Invalid parameter");
            }
        }
        return response;
    }
}
