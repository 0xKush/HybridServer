package es.uvigo.esei.dai.hybridserver.model.dao.xsd;

import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;

import java.util.List;

public interface XSDDAO {
    void add(String uuid, String content);

    void delete(String uuid);

    XSD get(String uuid);

    List<XSD> list();
}
