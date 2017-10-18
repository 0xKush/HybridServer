package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.controller.HTMLController;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.uvigo.esei.dai.hybridserver.model.entity.HTMLAppend.setHTML;
import static es.uvigo.esei.dai.hybridserver.model.entity.HTMLAppend.setPageList;
import static es.uvigo.esei.dai.hybridserver.model.entity.HTMLAppend.setPage;

public class HTMLManager implements ResourceManager {

    public HTTPResponse responseForHTML(ControllerFactory factory, String method, Map<String, String> resourceParameters) {

        switch (method) {
            case "GET":
                return responseForGET(factory, resourceParameters);
            case "POST":
                return responseForPOST(factory, resourceParameters);
            case "DELETE":
                return responseForDELETE(factory, resourceParameters);
            default:
                return responseForInvalidResource("400 - Bad Request");
        }
    }

    private HTTPResponse responseForDELETE(ControllerFactory factory, Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        HTMLController htmlController = factory.createHtmlController();
        String uuid;

        if (resourceParameters.containsKey("uuid")) {

            uuid = resourceParameters.get("uuid");

            if (htmlController.get(uuid) != null) {

                htmlController.delete(uuid);
                response.setStatus(HTTPResponseStatus.S200);
                response.setContent("The page has been deleted");

            } else
                response = responseForNotFound("404 - The page does not exists");
        } else
            response = responseForNotFound("404 - Invalid parameter");

        return response;
    }

    private HTTPResponse responseForPOST(ControllerFactory factory, Map<String, String> resourceParameters) {

        //Tools.info("Response for Post");
        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        HTMLController htmlController = factory.createHtmlController();

        UUID randomUuid = UUID.randomUUID();
        String uuid = randomUuid.toString();
        String content;

        if (resourceParameters.containsKey("html")) {

            content = resourceParameters.get("html");
            htmlController.add(uuid, content);

            response.setContent(setPage(htmlController.get(uuid)));
            //response.putParameter("Content-Type", "text/html");
            response.setStatus(HTTPResponseStatus.S200);

        } else {
            return responseForInvalidResource("404 - Bad Request");
        }


        return response;
    }

    public HTTPResponse responseForRoot() {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());


        response.setStatus(HTTPResponseStatus.S200);
        //response.putParameter("Content-Type", "text/html");
        response.setContent(setHTML(new Document().toString(true)));

        return response;
    }

    public HTTPResponse responseForInvalidResource(String content) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S400);
        //response.putParameter("Content-Type", "text/html");
        response.setContent(setHTML(new Document(content).toString(true)));

        return response;
    }

    public HTTPResponse responseForNotFound(String content) {

        //Tools.info("S404(Not Found)");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S404);
        response.setContent(setHTML(new Document(content).toString(true)));

        return response;
    }

    public HTTPResponse responseForGET(ControllerFactory factory, Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        HTMLController htmlController = factory.createHtmlController();


        if (resourceParameters.isEmpty()) {

            //Tools.info("resource:html - without parameters");

            List<Document> list = htmlController.list();
            Iterator<Document> it = list.iterator();
            StringBuilder documents = new StringBuilder();

            while (it.hasNext()) {

                Document doc = it.next();
                documents.append(setPageList(doc));
            }

            //Tools.info("S200(OK)");

            response.setStatus(HTTPResponseStatus.S200);
            //response.putParameter("Content-Type", "text/html");
            response.setContent(setHTML(documents.toString()));


        } else {

            //Tools.info("resource:html - with parameters");

            if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                String uuid = resourceParameters.get("uuid");
                Document doc = htmlController.get(uuid);

                if (doc != null) {

                    //Tools.info("S200(OK)");

                    response.setStatus(HTTPResponseStatus.S200);
                    //response.putParameter("Content-Type", "text/html");
                    response.setContent(doc.toString());

                } else
                    response = responseForNotFound("404 - The page does not exists");
            } else
                response = responseForNotFound("404 - Not Found");
        }
        return response;
    }
}
