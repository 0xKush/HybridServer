package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.hbSEI;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractManager {


    public abstract HTTPResponse responseForGET(Map<String, String> resourceParameters);

    public abstract HTTPResponse responseForPOST(Map<String, String> resourceParameters);

    public abstract HTTPResponse responseForDELETE(Map<String, String> resourceParameters);


    public static HTTPResponse responseForBadRequest(String content) {

        //Tools.info("S400(Bad Request)");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S400);
        response.putParameter("Content-Type", "text/html");
        response.setContent(content);

        return response;
    }

    public static HTTPResponse responseForNotFound(String content) {

        //Tools.info("S404(Not Found)");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S404);
        response.putParameter("Content-Type", "text/html");
        response.setContent(content);

        return response;
    }

    public static HTTPResponse responseForInternalServerError(String content) {

        //Tools.info("S500(Internal Server Error)");

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        response.setStatus(HTTPResponseStatus.S500);
        response.putParameter("Content-Type", "text/html");
        response.setContent(content);

        return response;
    }

    public static List<hbSEI> wsConnection(List<ServerConfiguration> serverList) throws MalformedURLException, WebServiceException {
        List<hbSEI> remoteServices = new ArrayList<>();

        if (serverList != null) {

            for (ServerConfiguration server : serverList) {
                if (server != null) {
                    URL url = new URL(server.getWsdl());
                    QName name = new QName(server.getNamespace(), server.getService());
                    Service service = Service.create(url, name);

                    hbSEI webService = service.getPort(hbSEI.class);
                    remoteServices.add(webService);
                }
            }
        }
        return remoteServices;
    }
}
