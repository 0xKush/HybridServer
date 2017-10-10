package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.entity.Document;

import java.util.List;

public interface ControllerFactory {
    Document get(String uuid);

    List<Document> list();
}
