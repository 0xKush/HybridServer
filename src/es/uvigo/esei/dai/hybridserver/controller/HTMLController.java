package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.entity.Document;

import java.util.List;

public interface HTMLController {
    public Document get(String uuid);

    public List<Document> list();
}

