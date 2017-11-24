package es.uvigo.esei.dai.hybridserver.model.dao.xml;

import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;

import java.util.List;

public interface XMLDAO {
    void add(String uuid, String content);

    void delete(String uuid);

    XML get(String uuid);

    List<XML> list();
}
