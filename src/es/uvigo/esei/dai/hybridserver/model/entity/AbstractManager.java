package es.uvigo.esei.dai.hybridserver.model.entity;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import java.util.Map;

public abstract class AbstractManager {


    public abstract HTTPResponse GET(Map<String, String> resourceParameters);

    public abstract HTTPResponse POST(Map<String, String> resourceParameters);

    public abstract HTTPResponse DELETE(Map<String, String> resourceParameters);


    public static HTTPResponse S400(String content) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S400);
        response.putParameter("Content-Type", "text/html");
        response.setContent(content);

        return response;
    }

    public static HTTPResponse S404(String content) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S404);
        response.putParameter("Content-Type", "text/html");
        response.setContent(content);

        return response;
    }

    public static HTTPResponse S500(String content) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S500);
        response.putParameter("Content-Type", "text/html");
        response.setContent(content);

        return response;
    }

    public static HTTPResponse S200(String content, String contentType) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S200);
        response.putParameter("Content-Type", contentType);
        response.setContent(content);

        return response;
    }
}
