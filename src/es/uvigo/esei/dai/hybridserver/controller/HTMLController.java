package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.dao.HTMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.Document;
import java.util.List;

public class HTMLController {
    private HTMLDAO dao;

    public HTMLController(HTMLDAO dao) {
        this.dao = dao;
    }

    public Document get(String uuid) {
        return dao.get(uuid);
    }

    public List<Document> list() {
        return dao.list();
    }


}
