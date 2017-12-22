package es.uvigo.esei.dai.hybridserver.model.entity.xml;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.XMLController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import javax.xml.transform.TransformerException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.uvigo.esei.dai.hybridserver.model.entity.xml.DOMParsing.validateWithXSD;
import static es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLTUtils.transform;

public class XMLManager extends AbstractManager {

    private XMLController xmlController;

    public XMLManager(ControllerFactory factory) {

        if (factory != null)
            this.xmlController = factory.createXMLController();
        else
            this.xmlController = null;

    }


    public HTTPResponse GET(Map<String, String> resourceParameters) {

        HTTPResponse response;

        if (this.xmlController == null) {

            response = S500("500 - Internal Server Error");

        } else {

            if (resourceParameters.isEmpty()) {

                List<XML> list = this.xmlController.list();
                Iterator<XML> it = list.iterator();
                StringBuilder content = new StringBuilder();

                content.append("<html>\n" +
                        "<head>\n" +
                        "\t<meta charset=\"UTF-8\">\n" +
                        "\t<title>Hybrid Server</title>\n" +
                        "\t<h1>HybridServer</h1>\n" +
                        "</head>\n" +
                        "<body><ul>");

                while (it.hasNext()) {

                    XML xml = it.next();
                    content.append("<li>\n" + "<a href=\"xml?uuid=" + xml.getUuid() + "\">" + xml.getUuid() + "</a>"
                            + "</li>\n");
                }


                //==== Remotes ====
                Map<ServerConfiguration, List<XML>> remotes = this.xmlController.remoteList();

                for (Map.Entry<ServerConfiguration, List<XML>> remote : remotes.entrySet()) {
                    ServerConfiguration serverConfiguration = remote.getKey();
                    List<XML> remoteList = remote.getValue();
                    it = remoteList.iterator();

                    content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                    if (!remoteList.isEmpty()) {

                        while (it.hasNext()) {
                            XML doc = it.next();
                            content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "xml?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
                                    + "</li>\n");

                        }
                    }
                }

                content.append("\t</ul>\n" +
                        "\t\n" +
                        "</body>\n" +
                        "</html>");
                response = S200(content.toString(), "text/html");


            } else {

                if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                    String uuid = resourceParameters.get("uuid");
                    XML xml = xmlController.get(uuid);

                    if (xml != null) {

                        response = S200(xml.getContent(), "application/xml");

                    } else {
                        response = S404("404 - The XML does not exist");
                    }
                } else if (resourceParameters.size() == 2 && resourceParameters.containsKey("uuid") && resourceParameters.containsKey("xslt")) {

                    String uuidXML = resourceParameters.get("uuid");
                    XML xml = xmlController.get(uuidXML);

                    if (xml != null) {

                        String uuidXSLT = resourceParameters.get("xslt");
                        XSLT xslt = xmlController.getXSLT(uuidXSLT);

                        if (xslt != null) {

                            String uuidXSD = xslt.getXsd();
                            XSD xsd = xmlController.getXSD(uuidXSD);

                            if (xsd != null) {

                                try {
                                    boolean validated = validateWithXSD(xml, xsd);

                                    if (validated) {

                                        String transformedXML = transform(xml, xslt);
                                        response = S200(transformedXML, "text/html");

                                    } else {
                                        response = S400("400 - The XML could not be validated");
                                    }

                                } catch (TransformerException e) {
                                    e.printStackTrace();
                                    response = S500("500 - The XML could not be transformed");
                                }

                            } else {
                                response = S400("400 - The XSD does not exist");
                            }

                        } else {
                            response = S404("404 - The XSLT asociated does not exist");
                        }

                    } else {
                        response = S404("404 - The XML does not exist");
                    }

                } else {
                    response = S404("404 - Not Found");
                }
            }
        }
        return response;
    }


    public HTTPResponse POST(Map<String, String> resourceParameters) {

        HTTPResponse response;

        UUID randomUuid = UUID.randomUUID();
        String uuid = randomUuid.toString();
        String content;


        if (this.xmlController == null) {

            response = S500("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("xml")) {

                content = resourceParameters.get("xml");
                this.xmlController.add(uuid, content);

                response = S200("<a href=\"xml?uuid=" + uuid.toString() + "\">" + uuid.toString() + "</a>", "text/html");

            } else {
                return S400("400 - Bad Request");
            }
        }

        return response;
    }

    public HTTPResponse DELETE(Map<String, String> resourceParameters) {

        HTTPResponse response;
        String uuid;

        if (this.xmlController == null) {

            response = S500("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("uuid")) {

                uuid = resourceParameters.get("uuid");

                if (this.xmlController.get(uuid) != null) {

                    this.xmlController.delete(uuid);
                    response = S200("The XML has been deleted", "text/html");

                } else {
                    response = S404("404 - The XML does not exist");
                }
            } else {
                response = S400("400 - Invalid parameter");
            }
        }
        return response;
    }

}
