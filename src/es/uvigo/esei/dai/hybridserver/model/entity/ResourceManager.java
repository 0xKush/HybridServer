package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;

import java.util.Map;

public interface ResourceManager {
    HTTPResponse responseForHTML(ControllerFactory factory, String method, Map<String, String> resourceParameters);

    HTTPResponse responseForRoot();

    HTTPResponse responseForInvalidResource();
}

