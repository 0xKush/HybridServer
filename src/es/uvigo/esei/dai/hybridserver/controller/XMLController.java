package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.dao.xml.XMLDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xslt.XSLTDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.wsManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMLController {

    private XMLDAO dao;
    private XSLTDAO xsltDao;
    private XSDDAO xsdDao;
    private wsManager ws;

    public wsManager getWs() {
        return ws;
    }

    public XMLController(XMLDAO dao, XSLTDAO xsltDao, XSDDAO xsdDao, List<ServerConfiguration> serverList) {
        this.dao = dao;
        this.xsltDao = xsltDao;
        this.xsdDao = xsdDao;
        this.ws = new wsManager(serverList);
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

    public XSD getXSD(String uuid) {
        XSD doc;
        doc = xsdDao.get(uuid);

        if (doc != null) {
            return doc;
        } else {
            if (!getWs().getRemoteServices().isEmpty()) {
                for (Map.Entry<ServerConfiguration, hbSEI> server : getWs().getRemoteServices().entrySet()) {
                    doc = server.getValue().getAssociatedXSD(uuid);
                    if (doc != null)
                        break;
                }
            }
        }
        return doc;
    }

    public XSLT getXSLT(String uuid) {
        XSLT doc;
        doc = xsltDao.get(uuid);

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
