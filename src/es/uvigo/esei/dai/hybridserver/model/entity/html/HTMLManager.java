package es.uvigo.esei.dai.hybridserver.model.entity.html;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.HTMLController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
;


public class HTMLManager extends AbstractManager {


    private HTMLController htmlController;

    public HTMLManager(ControllerFactory factory) {

        if (factory != null) {
            this.htmlController = factory.createHTMLController();

        } else {
            this.htmlController = null;
        }
    }

    public HTTPResponse responseForRoot() {
        //Tools.info("Response for root");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());


        response.setStatus(HTTPResponseStatus.S200);
        response.putParameter("Content-Type", "text/html");

        response.setContent("<html>\n" +
                "\t<head>\n" +
                "\t\t<meta charset=\"UTF-8\">\n" +
                "\t\t<title>Hybrid Server</title>\n" +
                "\t\t<h1>HybridServer</h1>\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<h2> Grupo de proyecto 1.3</h2>\n" +
                "\t\t<h3> Cristopher Álvarez Martínez </h3>\n" +
                "\t\t<h3> Alberto Lopez Rodriguez </h3>\n" +
                "\t</body>\n" +
                "</html>");

        return response;
    }

    public HTTPResponse responseForGET(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        if (this.htmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.isEmpty()) {

                List<Document> localList = this.htmlController.list();
                Iterator<Document> it = localList.iterator();
                StringBuilder content = new StringBuilder();


                content.append("<html>\n" +
                        "<head>\n" +
                        "\t<meta charset=\"UTF-8\">\n" +
                        "\t<title>Hybrid Server</title>\n" +
                        "\t<h1>HybridServer</h1>\n" +
                        "</head>\n" +
                        "<body><ul>");

                while (it.hasNext()) {
                    Document doc = it.next();
                    content.append("<li>\n" + "<a href=\"html?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                            + "</li>\n");
                }

                //== == == == == == WebServices == == == == == == ==
                Map<ServerConfiguration, List<Document>> remotes = this.htmlController.remoteList();

                if (remotes != null) {

                    for (Map.Entry<ServerConfiguration, List<Document>> remote : remotes.entrySet()) {
                        ServerConfiguration serverConfiguration = remote.getKey();
                        List<Document> remoteList = remote.getValue();
                        it = remoteList.iterator();

                        content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                        if (!remoteList.isEmpty()) {
                            while (it.hasNext()) {
                                Document doc = it.next();
                                content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "html?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                                        + "</li>\n");

                            }
                        }
                    }
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

                //Tools.info("resource:html - with parameters");

                if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                    String uuid = resourceParameters.get("uuid");
                    Document doc = htmlController.get(uuid);

                    if (doc != null) {

                        response.setStatus(HTTPResponseStatus.S200);
                        response.putParameter("Content-Type", "text/html");
                        response.setContent(doc.getContent());

                    } else {
                        response = responseForNotFound("404 - The page does not exist");
                    }
                } else {
                    response = responseForNotFound("404 - Not Found");
                }
            }
        }
        return response;
    }

    public HTTPResponse responseForDELETE(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        String uuid;

        if (this.htmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("uuid")) {

                uuid = resourceParameters.get("uuid");

                if (this.htmlController.get(uuid) != null) {

                    this.htmlController.delete(uuid);
                    response.setStatus(HTTPResponseStatus.S200);
                    response.putParameter("Content-Type", "text/html");
                    response.setContent("The page has been deleted");

                } else {
                    response = responseForNotFound("404 - The page does not exist");
                }
            } else {
                response = responseForBadRequest("400 - Invalid parameter");
            }
        }
        return response;
    }

    public HTTPResponse responseForPOST(Map<String, String> resourceParameters) {

        //Tools.info("Response for Post");
        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        UUID randomUuid = UUID.randomUUID();
        String uuid = randomUuid.toString();
        String content;

        if (this.htmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");
        } else {

            if (resourceParameters.containsKey("html")) {

                content = resourceParameters.get("html");
                this.htmlController.add(uuid, content);

                response.setStatus(HTTPResponseStatus.S200);
                response.putParameter("Content-Type", "text/html");
                response.setContent("<a href=\"html?uuid=" + uuid.toString() + "\">" + uuid.toString() + "</a>");

            } else {
                return responseForBadRequest("400 - Bad Request");
            }
        }

        return response;
    }

}
