package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.dao.xslt.XSLTDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.util.List;

public class XSLTController {
    private XSLTDAO dao;

    public XSLTController(XSLTDAO dao) {
        this.dao = dao;
    }

    public XSLT get(String uuid) {
        return dao.get(uuid);
    }

    public List<XSLT> list() {
        return dao.list();
    }

    public void add(String uuid, String content, String xsd) {
        dao.add(uuid, content,xsd);
    }

    public void delete(String uuid) {
        dao.delete(uuid);
    }
}
