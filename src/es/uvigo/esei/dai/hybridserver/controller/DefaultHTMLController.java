package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.dao.HTMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.Document;

import java.util.List;

public class DefaultHTMLController implements HTMLController {
    private HTMLDAO dao;

    @Override
    public Document get(String uuid) {
        return dao.get(uuid);
    }

    @Override
    public List<Document> list() {
        return dao.list();
    }
}
