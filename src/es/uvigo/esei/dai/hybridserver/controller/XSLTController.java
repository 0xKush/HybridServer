package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xslt.XSLTDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.wsManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XSLTController {

    private XSLTDAO dao;
    private XSDDAO xsdDao;
    private wsManager ws;


    public wsManager getWs() {
        return ws;
    }

    public XSLTController(XSLTDAO dao, XSDDAO xsdDao, wsManager wsManager) {
        this.dao = dao;
        this.xsdDao = xsdDao;
        this.ws = wsManager;
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

    public XSD getXSD(String uuid) {
        XSD doc;
        doc = xsdDao.get(uuid);

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

    public List<XSLT> list() {
        return dao.list();
    }

    public Map<ServerConfiguration, List<XSLT>> remoteList() {

        Map<ServerConfiguration, List<XSLT>> remoteList = new LinkedHashMap<>();

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
