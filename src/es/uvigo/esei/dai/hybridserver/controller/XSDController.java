package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;

import java.util.List;

public class XSDController {

    private XSDDAO dao;
    private List<ServerConfiguration> serverList;

    public List<ServerConfiguration> getServerList() {
        return serverList;
    }

    public XSDController(XSDDAO dao, List<ServerConfiguration> serverList) {
        this.dao = dao;
        this.serverList = serverList;
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
