package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class wsManager {
    public static Map<ServerConfiguration, hbSEI> wsConnection(List<ServerConfiguration> serverList) {
        Map<ServerConfiguration, hbSEI> remoteServices = new HashMap<>();

        if (serverList != null) {
            for (ServerConfiguration server : serverList) {
                try {
                    if (server != null) {
                        URL url = new URL(server.getWsdl());
                        QName name = new QName(server.getNamespace(), server.getService());
                        Service service = Service.create(url, name);

                        hbSEI webService = service.getPort(hbSEI.class);
                        remoteServices.put(server, webService);
                    }
                } catch (WebServiceException e) {

                } catch (MalformedURLException e) {

                }
            }
        }
        return remoteServices;
    }

    public static String wsGetContent(Map<ServerConfiguration, hbSEI> remoteServices, String resource, String uuid) {
        String content = null;
        for (Map.Entry<ServerConfiguration, hbSEI> server : remoteServices.entrySet()) {

            switch (resource) {
                case "html":
                    content = server.getValue().getHTMLContent(uuid);
                    break;

                case "xml":
                    content = server.getValue().getXMLContent(uuid);
                    break;

                case "xsd":
                    content = server.getValue().getXSDContent(uuid);
                    break;

                case "xslt":
                    content = server.getValue().getXSLTContent(uuid);
                    break;

                case "xml-xsd":
                    content = server.getValue().XSDUuid(uuid);
                    break;

                default:
                    break;
            }

            if (content != null)
                break;
        }
        return content;
    }

    public static String wsGetList(Map<ServerConfiguration, hbSEI> remoteServices, String resource) {
        StringBuilder content = new StringBuilder();

        if (!remoteServices.isEmpty()) {

            for (Map.Entry<ServerConfiguration, hbSEI> server : remoteServices.entrySet()) {
                ServerConfiguration serverConfiguration = server.getKey();

                if (resource.equalsIgnoreCase("html")) {
                    List<Document> remoteList = server.getValue().HTMLUuidList();
                    Iterator<Document> it = remoteList.iterator();

                    content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                    if (!remoteList.isEmpty()) {
                        while (it.hasNext()) {
                            Document doc = it.next();
                            content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "html?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                                    + "</li>\n");
                        }
                    }

                } else if (resource.equalsIgnoreCase("xml")) {
                    List<XML> remoteList = server.getValue().XMLUuidList();
                    Iterator<XML> it = remoteList.iterator();

                    content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                    if (!remoteList.isEmpty()) {
                        while (it.hasNext()) {
                            XML doc = it.next();
                            content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "xml?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                                    + "</li>\n");
                        }
                    }

                } else if (resource.equalsIgnoreCase("xsd")) {
                    List<XSD> remoteList = server.getValue().XSDUuidList();
                    Iterator<XSD> it = remoteList.iterator();

                    content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                    if (!remoteList.isEmpty()) {
                        while (it.hasNext()) {
                            XSD doc = it.next();
                            content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "xsd?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                                    + "</li>\n");
                        }
                    }

                } else if (resource.equalsIgnoreCase("xslt")) {
                    List<XSLT> remoteList = server.getValue().XSLTUuidList();
                    Iterator<XSLT> it = remoteList.iterator();

                    content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                    if (!remoteList.isEmpty()) {
                        while (it.hasNext()) {
                            XSLT doc = it.next();
                            content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "xslt?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                                    + "</li>\n");
                        }
                    }
                }
            }
        }
        return content.toString();
    }
}

