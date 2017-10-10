package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;

public class HTMLManager implements ResourceManager {

    public HTTPResponse createResponse(String resource, String method) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        if (resource.equals("")) {
            response.setStatus(HTTPResponseStatus.S200);
            response.setContent(new Document().emptyResource());

        } else {

        }
        return response;
    }
}
