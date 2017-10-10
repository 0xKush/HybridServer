package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.dao.HTMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.Document;
import java.util.List;

public class MapControllerFactory implements ControllerFactory {
    private HTMLDAO dao;

    public MapControllerFactory(HTMLDAO dao) {
        this.dao = dao;
    }

    public Document get(String uuid) {
        return dao.get(uuid);
    }

    public List<Document> list() {
        return dao.list();
    }


}
