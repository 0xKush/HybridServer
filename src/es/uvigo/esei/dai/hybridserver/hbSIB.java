package es.uvigo.esei.dai.hybridserver;

import es.uvigo.esei.dai.hybridserver.controller.HTMLController;
import es.uvigo.esei.dai.hybridserver.controller.XMLController;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.XSLTController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;

@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver.hbSEI", serviceName = "HybridServerService")
public class hbSIB implements hbSEI {


    private HTMLController htmlController;
    private XMLController xmlController;
    private XSDController xsdController;
    private XSLTController xsltController;

    public void setControllers(ControllerFactory factory) {
        this.htmlController = factory.createHTMLController();
        this.xmlController = factory.createXMLController();
        this.xsdController = factory.createXSDController();
        this.xsltController = factory.createXSLTController();
    }

    @Override
    public List<Document> HTMLUuidList() {

        return this.htmlController.list();
    }

    @Override
    public List<XML> XMLUuidList() {
        return this.xmlController.list();

    }

    @Override
    public List<XSD> XSDUuidList() {
        return this.xsdController.list();
    }

    @Override
    public List<XSLT> XSLTUuidList() {
        return this.xsltController.list();
    }

    @Override
    public String getHTMLContent(String uuid) {
        Document doc;

        if ((doc = this.htmlController.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String getXMLContent(String uuid) {
        XML doc;

        if ((doc = this.xmlController.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String getXSDContent(String uuid) {
        XSD doc;

        if ((doc = this.xsdController.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String getXSLTContent(String uuid) {
        XSLT doc;

        if ((doc = this.xsltController.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String XSDUuid(String uuid) {
        XSLT doc;

        if ((doc = this.xsltController.get(uuid)) != null)
            return doc.getXsd();
        else
            return null;
    }
}
