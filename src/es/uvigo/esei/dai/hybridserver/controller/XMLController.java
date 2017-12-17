package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.model.dao.xml.XMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;

import java.util.List;

public class XMLController {

    private XMLDAO xmldao;
    private List<ServerConfiguration> serverList;

    public List<ServerConfiguration> getServerList() {
        return serverList;
    }

    public XMLController(XMLDAO xmldao, List<ServerConfiguration> serverList) {
        this.xmldao = xmldao;
        this.serverList = serverList;
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
