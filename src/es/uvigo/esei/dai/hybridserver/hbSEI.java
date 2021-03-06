package es.uvigo.esei.dai.hybridserver;

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
    List<Document> HTMLUuidList();

    @WebMethod
    List<XML> XMLUuidList();

    @WebMethod
    List<XSD> XSDUuidList();

    @WebMethod
    List<XSLT> XSLTUuidList();

    @WebMethod
    Document getHTML(String uuid);

    @WebMethod
    XML getXML(String uuid);

    @WebMethod
    XSD getXSD(String uuid);

    @WebMethod
    XSLT getXSLT(String uuid);

    @WebMethod
    XSD getAssociatedXSD(String XSLTUuid);
}
