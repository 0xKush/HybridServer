package es.uvigo.esei.dai.hybridserver.model.dao.html;

import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;

import java.util.List;


public interface HTMLDAO {
    Document get(String uuid);

    List<Document> list();

    void add(String uuid, String content);

    void delete(String uuid);

}
