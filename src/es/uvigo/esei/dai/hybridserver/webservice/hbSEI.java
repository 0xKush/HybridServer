package es.uvigo.esei.dai.hybridserver.webservice;

import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface hbSEI {
    @WebMethod
    String[] HTMLUuidList();

    @WebMethod
    String[] XMLUuidList();

    @WebMethod
    String[] XSDUuidList();

    @WebMethod
    String[] XSLTUuidList();

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
