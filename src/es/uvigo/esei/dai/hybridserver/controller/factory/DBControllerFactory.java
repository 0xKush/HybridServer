package es.uvigo.esei.dai.hybridserver.controller.factory;


import es.uvigo.esei.dai.hybridserver.configuration.Configuration;
import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.HTMLController;
import es.uvigo.esei.dai.hybridserver.controller.XMLController;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.XSLTController;
import es.uvigo.esei.dai.hybridserver.model.dao.html.HTMLDBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xml.XMLDBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDBDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xslt.XSLTDBDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.wsManager;

import java.util.List;

public class DBControllerFactory implements ControllerFactory {

    private Configuration configuration;

    public DBControllerFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public HTMLController createHTMLController() {
        return new HTMLController(new HTMLDBDAO(this.configuration), this.configuration.getServers());
    }

    @Override
    public XMLController createXMLController() {
        return new XMLController(new XMLDBDAO(this.configuration), new XSLTDBDAO(this.configuration), new XSDDBDAO(this.configuration), this.configuration.getServers());
    }

    @Override
    public XSDController createXSDController() {
        return new XSDController(new XSDDBDAO(this.configuration), this.configuration.getServers());
    }

    @Override
    public XSLTController createXSLTController() {
        return new XSLTController(new XSLTDBDAO(this.configuration), new XSDDBDAO(this.configuration), this.configuration.getServers());
    }
}
