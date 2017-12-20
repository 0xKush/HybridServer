package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.dao.xslt.XSLTDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.wsManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.util.List;
import java.util.Map;

public class XSLTController {
    private XSLTDAO dao;
    private wsManager ws;


    public wsManager getWs() {
        return ws;
    }

    public XSLTController(XSLTDAO dao, List<ServerConfiguration> serverList) {
        this.dao = dao;
        this.ws = new wsManager(serverList);
    }

    public XSLT get(String uuid) {
        XSLT doc;
        doc = dao.get(uuid);

        if (doc != null) {
            return doc;
        } else {
            if (!getWs().getRemoteServices().isEmpty()) {
                for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {
                    doc = server.getValue().getXSLT(uuid);
                    if (doc != null)
                        break;
                }
            }
        }
        return doc;
    }

    public List<XSLT> list() {
        return dao.list();
    }

    public Map<ServerConfiguration, List<XSLT>> remoteList() {

        Map<ServerConfiguration, List<XSLT>> remoteList = null;

        if (!getWs().getRemoteServices().isEmpty()) {

            for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {

                ServerConfiguration serverConfiguration = server.getKey();
                List<XSLT> remoteDocumentList = server.getValue().XSLTUuidList();
                remoteList.put(serverConfiguration, remoteDocumentList);
            }

        }

        return remoteList;
    }

    public void add(String uuid, String content, String xsd) {
        dao.add(uuid, content, xsd);
    }

    public void delete(String uuid) {
        dao.delete(uuid);
    }
}
