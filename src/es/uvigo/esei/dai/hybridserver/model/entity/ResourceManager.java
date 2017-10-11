package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.controller.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;

import java.util.Map;

public interface ResourceManager {
    HTTPResponse createResponse(ControllerFactory factory, String resource, String method, Map<String, String> resourceParameters);
}

