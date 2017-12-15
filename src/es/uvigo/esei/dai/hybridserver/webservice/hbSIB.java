package es.uvigo.esei.dai.hybridserver.webservice;

import es.uvigo.esei.dai.hybridserver.model.dao.html.HTMLDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xml.XMLDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xsd.XSDDAO;
import es.uvigo.esei.dai.hybridserver.model.dao.xslt.XSLTDAO;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;

//@WebService(endpointInterface = "es.uvigo.esei.dai.hybridserver.webservice.hbSEI")
public class hbSIB implements hbSEI {

    private HTMLDAO HTMLDao;
    private XMLDAO XMLDao;
    private XSDDAO XSDDao;
    private XSLTDAO XSLTDao;

    public hbSIB(HTMLDAO HTMLDao, XMLDAO XMLDao, XSDDAO XSDDao, XSLTDAO XSLTDao) {
        this.HTMLDao = HTMLDao;
        this.XMLDao = XMLDao;
        this.XSDDao = XSDDao;
        this.XSLTDao = XSLTDao;
    }

    @Override
    public String[] HTMLUuidList() {
        List<Document> htmlList = this.HTMLDao.list();
        List<String> HTMLUuidList = new ArrayList<>();

        for (Document doc : htmlList) {
            HTMLUuidList.add(doc.getUuid());
        }

        return (String[]) HTMLUuidList.toArray();
    }

    @Override
    public String[] XMLUuidList() {
        List<XML> xmlList = this.XMLDao.list();
        List<String> XMLUuidList = new ArrayList<>();

        for (XML doc : xmlList) {
            XMLUuidList.add(doc.getUuid());
        }

        return (String[]) XMLUuidList.toArray();
    }

    @Override
    public String[] XSDUuidList() {
        List<XSD> xsdList = this.XSDDao.list();
        List<String> XSDUuidList = new ArrayList<>();

        for (XSD doc : xsdList) {
            XSDUuidList.add(doc.getUuid());
        }

        return (String[]) XSDUuidList.toArray();
    }

    @Override
    public String[] XSLTUuidList() {

        List<XSLT> xsltList = this.XSLTDao.list();
        List<String> XSLTUuidList = new ArrayList<>();

        for (XSLT doc : xsltList) {
            XSLTUuidList.add(doc.getUuid());
        }

        return (String[]) XSLTUuidList.toArray();
    }

    @Override
    public String getHTMLContent(String uuid) {
        Document doc;

        if ((doc = this.HTMLDao.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String getXMLContent(String uuid) {
        XML doc;

        if ((doc = this.XMLDao.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String getXSDContent(String uuid) {
        XSD doc;

        if ((doc = this.XSDDao.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String getXSLTContent(String uuid) {
        XSLT doc;

        if ((doc = this.XSLTDao.get(uuid)) != null)
            return doc.getContent();
        else
            return null;
    }

    @Override
    public String XSDUuid(String uuid) {
        XSLT doc;

        if ((doc = this.XSLTDao.get(uuid)) != null)
            return doc.getXsd();
        else
            return null;
    }
}
