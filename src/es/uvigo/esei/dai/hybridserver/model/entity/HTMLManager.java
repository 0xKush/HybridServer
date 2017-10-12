package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.utils.Tools;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static es.uvigo.esei.dai.hybridserver.model.entity.HTMLAppend.setHTML;
import static es.uvigo.esei.dai.hybridserver.model.entity.HTMLAppend.setListItem;

public class HTMLManager implements ResourceManager {

    public HTTPResponse responseForHTML(ControllerFactory factory, String method, Map<String, String> resourceParameters) {

        switch (method) {
            case "GET":
                return responseForGET(factory, method, resourceParameters);
            default:
                return responseForInvalidResource();
        }
    }

    public HTTPResponse responseForRoot() {
        Tools.info("S200(OK) ~ Root");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());


        response.setStatus(HTTPResponseStatus.S200);
        //response.putParameter("Content-Type", "text/html");
        response.setContent(setHTML(new Document().toString(true)));

        return response;
    }

    public HTTPResponse responseForInvalidResource() {

        Tools.info("S400(Bad Request (Invalid Resource))");

        String content;
        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        content = "S400(Bad Request)";


        response.setStatus(HTTPResponseStatus.S400);
        response.putParameter("Content-Type", "text/html");
        response.setContent(setHTML(new Document(content).toString(true)));

        return response;
    }

    public HTTPResponse responseForNotFound() {

        Tools.info("S404(Not Found)");

        String content;
        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        content = "S404(Not Found)";


        response.setStatus(HTTPResponseStatus.S404);
        response.setContent(setHTML(new Document(content).toString(true)));

        return response;
    }

    public HTTPResponse responseForGET(ControllerFactory factory, String method, Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        if (resourceParameters.isEmpty()) {

            Tools.info("resource:html - without parameters");

            List<Document> list = factory.createHtmlController().list();
            Iterator<Document> it = list.iterator();
            StringBuilder documents = new StringBuilder();

            while (it.hasNext()) {

                Document doc = it.next();
                documents.append(setListItem(doc));
            }

            Tools.info("S200(OK)");

            response.setStatus(HTTPResponseStatus.S200);
            //response.putParameter("Content-Type", "text/html");
            response.setContent(setHTML(documents.toString()));


        } else {

            Tools.info("resource:html - with parameters");

            if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                String uuid = resourceParameters.get("uuid");
                Document doc = factory.createHtmlController().get(uuid);

                if (doc != null) {

                    Tools.info("S200(OK)");

                    response.setStatus(HTTPResponseStatus.S200);
                    //response.putParameter("Content-Type", "text/html");
                    response.setContent(setHTML(doc.toString()));

                } else
                    response = responseForNotFound();
            } else
                response = responseForNotFound();
        }
        return response;
    }
}
