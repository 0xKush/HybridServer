package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HTMLManager implements ResourceManager {

    public HTTPResponse createResponse(ControllerFactory factory, String resource, String method, Map<String, String> resourceParameters) {
        String content = "", uuid = "";
        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        if (resource.equals("")) {
            response.setStatus(HTTPResponseStatus.S200);
            response.setContent(new Document(content, uuid).toString());

        } else if (resource.equals("html")) {

            if (!resourceParameters.isEmpty()) {
                if (resourceParameters.containsKey("uuid")) {

                    uuid = resourceParameters.get("uuid");
                    Document doc = factory.createHtmlController().get(uuid);

                    if (doc != null) {

                        response.setStatus(HTTPResponseStatus.S200);
                        response.setContent(doc.toString());

                    } else {
                        content = HTTPResponseStatus.S404.getStatus();

                        response.setStatus(HTTPResponseStatus.S404);
                        response.setContent(new Document(content, uuid).toString());
                    }
                }
            } else {

                List<Document> list = factory.createHtmlController().list();
                Iterator<Document> it = list.iterator();
                StringBuilder documents = new StringBuilder();

                System.out.println("LIST");
                System.out.println(list);
                while (it.hasNext()) {
                    Document doc = it.next();
                    System.out.println("A");
                    documents.append(doc.toString() + "\n");
                }

                response.setStatus(HTTPResponseStatus.S200);
                response.setContent(documents.toString());


            }
        } else {
            content = HTTPResponseStatus.S400.getStatus();

            response.setStatus(HTTPResponseStatus.S400);
            response.setContent(new Document(content, uuid).toString());
        }
        return response;
    }
}
