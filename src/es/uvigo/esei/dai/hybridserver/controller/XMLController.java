package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.dao.xml.XMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;

import java.util.List;

public class XMLController {

    private XMLDAO xmldao;

    public XMLController(XMLDAO xmldao) {
        this.xmldao = xmldao;
    }

    public XML get(String uuid) {
        return xmldao.get(uuid);
    }

    public List<XML> list() {
        return xmldao.list();
    }

    public void add(String uuid, String content) {
        xmldao.add(uuid, content);
    }

    public void delete(String uuid) {
        xmldao.delete(uuid);
    }
}
