package es.uvigo.esei.dai.hybridserver.model.dao;

import es.uvigo.esei.dai.hybridserver.model.entity.Document;

import java.util.List;


public interface HTMLDAO {
    public Document get(String uuid);
    public List<Document> list();
}
