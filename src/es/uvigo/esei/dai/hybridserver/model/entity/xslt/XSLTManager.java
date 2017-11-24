package es.uvigo.esei.dai.hybridserver.model.entity.xslt;

import es.uvigo.esei.dai.hybridserver.controller.XSLTController;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;

import java.util.Map;

public class XSLTManager extends AbstractManager {

    private XSLTController xsltController;

    public XSLTManager(XSLTController xsltController) {
        this.xsltController = xsltController;
    }


    @Override
    public HTTPResponse responseForGET(Map<String, String> resourceParameters) {
        return null;
    }

    @Override
    public HTTPResponse responseForPOST(Map<String, String> resourceParameters) {
        return null;
    }

    @Override
    public HTTPResponse responseForDELETE(Map<String, String> resourceParameters) {
        return null;
    }
}
