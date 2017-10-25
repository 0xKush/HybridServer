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

public class HTMLManager {

    private HTMLController htmlController;

    public HTTPResponse responseForHTML(ControllerFactory factory, String method, Map<String, String> resourceParameters) {

        if (factory != null)
            this.htmlController = factory.createHtmlController();
        else
            this.htmlController = null;

        switch (method) {
            case "GET":
                return responseForGET(resourceParameters);
            case "POST":
                return responseForPOST(resourceParameters);
            case "DELETE":
                return responseForDELETE(resourceParameters);
            default:
                return responseForBadRequest("400 - Bad Request");
        }
    }


    public HTTPResponse responseForRoot() {
        //Tools.info("Response for root");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());


        response.setStatus(HTTPResponseStatus.S200);
        response.setContent(setHTML(new Document().toString(true)));

        return response;
    }

    public HTTPResponse responseForGET(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());


        if (this.htmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.isEmpty()) {

                //Tools.info("resource:html - without parameters");

                List<Document> list = this.htmlController.list();
                Iterator<Document> it = list.iterator();
                StringBuilder documents = new StringBuilder();

                while (it.hasNext()) {

                    Document doc = it.next();
                    documents.append(setPageList(doc));
                }

                //Tools.info("S200(OK)");

                response.setStatus(HTTPResponseStatus.S200);
                response.setContent(setHTML(documents.toString()));


            } else {

                //Tools.info("resource:html - with parameters");

                if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                    String uuid = resourceParameters.get("uuid");
                    Document doc = htmlController.get(uuid);

                    if (doc != null) {

                        //Tools.info("S200(OK)");

                        response.setStatus(HTTPResponseStatus.S200);
                        response.setContent(doc.toString());

                    } else {
                        response = responseForNotFound("404 - The page does not exists");
                    }
                } else {
                    response = responseForNotFound("404 - Not Found");
                }
            }
        }
        return response;
    }

    private HTTPResponse responseForDELETE(Map<String, String> resourceParameters) {

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
                    response.setContent("The page has been deleted");

                } else {
                    response = responseForNotFound("404 - The page does not exists");
                }
            } else {
                response = responseForNotFound("404 - Invalid parameter");
            }
        }
        return response;
    }

    private HTTPResponse responseForPOST(Map<String, String> resourceParameters) {

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

                response.setContent(setPage(this.htmlController.get(uuid)));
                response.setStatus(HTTPResponseStatus.S200);

            } else {
                return responseForBadRequest("400 - Bad Request");
            }
        }

        return response;
    }

    public HTTPResponse responseForBadRequest(String content) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S400);
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

    public HTTPResponse responseForInternalServerError(String content) {
        //Tools.info("S500(Internal Server Error)");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S500);
        response.setContent(setHTML(new Document(content).toString(true)));

        return response;
    }


}
