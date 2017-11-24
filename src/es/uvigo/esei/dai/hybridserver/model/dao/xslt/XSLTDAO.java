package es.uvigo.esei.dai.hybridserver.model.dao.xslt;

import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.util.List;

public interface XSLTDAO {
    void add(String uuid, String content, String xsd);
    void delete(String uuid);
    XSLT get(String uuid);
    List<XSLT> list();
}
