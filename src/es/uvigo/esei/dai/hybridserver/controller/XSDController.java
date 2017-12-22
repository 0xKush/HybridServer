package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.wsManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XSDController {

    private XSDDAO dao;
    private wsManager ws;


    public wsManager getWs() {
        return ws;
    }

    public XSDController(XSDDAO dao, List<ServerConfiguration> serverList) {
        this.dao = dao;
        this.ws = new wsManager(serverList);
    }

    public XSD get(String uuid) {
        XSD doc;
        doc = dao.get(uuid);

        if (doc != null) {
            return doc;
        } else {
            if (!getWs().getRemoteServices().isEmpty()) {
                for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {
                    doc = server.getValue().getXSD(uuid);
                    if (doc != null)
                        break;
                }
            }
        }
        return doc;
    }

    public List<XSD> list() {
        return dao.list();
    }

    public Map<ServerConfiguration, List<XSD>> remoteList() {

        Map<ServerConfiguration, List<XSD>> remoteList = new LinkedHashMap<>();

        if (!getWs().getRemoteServices().isEmpty()) {

            for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {

                ServerConfiguration serverConfiguration = server.getKey();
                List<XSD> remoteDocumentList = server.getValue().XSDUuidList();
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
