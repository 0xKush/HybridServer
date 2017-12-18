package es.uvigo.esei.dai.hybridserver;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface hbSEI {
    @WebMethod
    List<Document> HTMLUuidList();

    @WebMethod
    List<String> XMLUuidList();

    @WebMethod
    List<String> XSDUuidList();

    @WebMethod
    List<String> XSLTUuidList();

    @WebMethod
    String getHTMLContent(String uuid);

    @WebMethod
    String getXMLContent(String uuid);

    @WebMethod
    String getXSDContent(String uuid);

    @WebMethod
    String getXSLTContent(String uuid);

    @WebMethod
    String XSDUuid(String XSLTUuid);
}
