package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;

public interface ResourceManager {
    HTTPResponse createResponse(String resource, String method);
}

