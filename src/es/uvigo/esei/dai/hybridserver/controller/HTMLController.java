package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.dao.html.HTMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;
import es.uvigo.esei.dai.hybridserver.model.entity.wsManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HTMLController {
    private HTMLDAO dao;
    private wsManager ws;

    public wsManager getWs() {
        return ws;
    }

    public HTMLController(HTMLDAO dao, List<ServerConfiguration> serverList) {
        this.dao = dao;
        this.ws = new wsManager(serverList);
    }

    public Document get(String uuid) {
        Document doc;
        doc = dao.get(uuid);

        if (doc != null) {
            return doc;
        } else {
            if (!getWs().getRemoteServices().isEmpty()) {
                for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {
                    doc = server.getValue().getHTML(uuid);
                    if (doc != null)
                        break;
                }
            }
        }
        return doc;
    }

    public List<Document> list() {
        return dao.list();
    }

    public Map<ServerConfiguration, List<Document>> remoteList() {

        Map<ServerConfiguration, List<Document>> remoteList = new LinkedHashMap<>();

        if (!getWs().getRemoteServices().isEmpty()) {
            //Tools.info("remote services are not empty");
            for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {

                ServerConfiguration serverConfiguration = server.getKey();
                List<Document> remoteDocumentList = server.getValue().HTMLUuidList();
                remoteList.put(serverConfiguration, remoteDocumentList);
            }

        }

        return remoteList;
    }

    public void add(String uuid, String content) {
        dao.add(uuid, content);
    }

    public void delete(String uuid) {
        dao.delete(uuid);
    }

}
