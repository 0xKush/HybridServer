package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.model.dao.html.HTMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;

import java.util.List;

public class HTMLController {
    private HTMLDAO dao;
    private List<ServerConfiguration> serverList;

    public List<ServerConfiguration> getServerList() {
        return serverList;
    }

    public HTMLController(HTMLDAO dao, List<ServerConfiguration> serverList) {
        this.dao = dao;
        this.serverList = serverList;
    }

    public Document get(String uuid) {
        return dao.get(uuid);
    }

    public List<Document> list() {
        return dao.list();
    }

    public void add(String uuid, String content) {
        dao.add(uuid, content);
    }

    public void delete(String uuid) {
        dao.delete(uuid);
    }

}
