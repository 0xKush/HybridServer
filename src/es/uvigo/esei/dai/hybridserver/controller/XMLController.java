package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.dao.xml.XMLDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.wsManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMLController {

    private XMLDAO dao;
    private wsManager ws;

    public wsManager getWs() {
        return ws;
    }

    public XMLController(XMLDAO dao, wsManager wsManager) {
        this.dao = dao;
        this.ws = wsManager;
    }

    public XML get(String uuid) {

        XML doc;
        doc = dao.get(uuid);

        if (doc != null) {
            return doc;
        } else {
            if (!getWs().getRemoteServices().isEmpty()) {
                for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {
                    doc = server.getValue().getXML(uuid);
                    if (doc != null)
                        break;
                }
            }
        }
        return doc;
    }

    public List<XML> list() {
        return dao.list();
    }

    public Map<ServerConfiguration, List<XML>> remoteList() {

        Map<ServerConfiguration, List<XML>> remoteList = new LinkedHashMap<>();

        if (!getWs().getRemoteServices().isEmpty()) {

            for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {

                ServerConfiguration serverConfiguration = server.getKey();
                List<XML> remoteDocumentList = server.getValue().XMLUuidList();
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
