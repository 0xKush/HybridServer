package es.uvigo.esei.dai.hybridserver.controller.factory;


import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.controller.HTMLController;
import es.uvigo.esei.dai.hybridserver.controller.XMLController;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.XSLTController;
import es.uvigo.esei.dai.hybridserver.model.dao.html.HTMLDBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xml.XMLDBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xslt.XSLTDBDAO;

import java.util.Properties;

public class DBControllerFactory implements ControllerFactory {

    private Configuration configuration;

    public DBControllerFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public HTMLController createHTMLController() {
        return new HTMLController(new HTMLDBDAO(configuration));
    }

    public XMLController createXMLController() {
        return new XMLController(new XMLDBDAO(configuration));
    }

    public XSDController createXSDController() { return new XSDController(new XSDDBDAO(configuration)); }

    public XSLTController createXSLTController() { return new XSLTController(new XSLTDBDAO(configuration)); }

}
