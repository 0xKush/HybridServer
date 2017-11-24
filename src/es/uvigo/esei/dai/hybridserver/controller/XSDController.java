package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;

import java.util.List;

public class XSDController {

    private XSDDAO dao;

    public XSDController(XSDDAO dao) {
        this.dao = dao;
    }

    public XSD get(String uuid) {
        return dao.get(uuid);
    }

    public List<XSD> list() {
        return dao.list();
    }

    public void add(String uuid, String content) {
        dao.add(uuid, content);
    }

    public void delete(String uuid) {
        dao.delete(uuid);
    }
}
