package es.uvigo.esei.dai.hybridserver.controller.factory;

import es.uvigo.esei.dai.hybridserver.controller.HTMLController;
import es.uvigo.esei.dai.hybridserver.controller.XMLController;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.XSLTController;

public interface ControllerFactory {
    HTMLController createHTMLController();
    XMLController createXMLController();
    XSDController createXSDController();
    XSLTController createXSLTController();
}
