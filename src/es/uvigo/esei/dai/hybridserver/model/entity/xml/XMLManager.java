package es.uvigo.esei.dai.hybridserver.model.entity.xml;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.XMLController;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.XSLTController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.uvigo.esei.dai.hybridserver.model.entity.xml.DOMParsing.validateWithXSD;
import static es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLTUtils.transform;

public class XMLManager extends AbstractManager {

    private XMLController xmlController;
    private XSDController xsdController;
    private XSLTController xsltController;

    public XMLManager(ControllerFactory factory) {

        if (factory != null) {
            this.xmlController = factory.createXMLController();
            this.xsdController = factory.createXSDController();
            this.xsltController = factory.createXSLTController();

        } else {
            this.xmlController = null;
            this.xsdController = null;
            this.xsltController = null;
        }
    }


    public HTTPResponse responseForGET(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());


        if (this.xmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

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
                response.setStatus(HTTPResponseStatus.S200);
                response.putParameter("Content-Type", "text/html");
                response.setContent(content.toString());


            } else {

                if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                    String uuid = resourceParameters.get("uuid");
                    XML xml = xmlController.get(uuid);

                    if (xml != null) {

                        response.setStatus(HTTPResponseStatus.S200);
                        response.putParameter("Content-Type", "application/xml");
                        response.setContent(xml.getContent());

                    } else {
                        response = responseForNotFound("404 - The XML does not exist");
                    }
                } else if (resourceParameters.size() == 2 && resourceParameters.containsKey("uuid") && resourceParameters.containsKey("xslt")) {

                    String uuidXML = resourceParameters.get("uuid");
                    XML xml = xmlController.get(uuidXML);

                    if (xml != null) {

                        String uuidXSLT = resourceParameters.get("xslt");
                        XSLT xslt = xsltController.get(uuidXSLT);

                        if (xslt != null) {

                            String uuidXSD = xslt.getXsd();
                            XSD xsd = xsdController.get(uuidXSD);

                            if (xsd != null) {

                                try {
                                    validateWithXSD(xml, xsd);
                                    String transformedXML = transform(xml, xslt);

                                    response.setStatus(HTTPResponseStatus.S200);
                                    response.putParameter("Content-Type", "text/html");
                                    response.setContent(transformedXML);

                                } catch (ParserConfigurationException | IOException | SAXException e) {
                                    e.printStackTrace();
                                    response = responseForBadRequest("400 - The XML could not be validated");

                                } catch (TransformerException e) {
                                    e.printStackTrace();
                                    response = responseForInternalServerError("500 - The XML could not be transformed");
                                }

                            } else {
                                response = responseForBadRequest("400 - The XSD does not exist");
                            }

                        } else {
                            response = responseForNotFound("404 - The XSLT asociated does not exist");
                        }

                    } else {
                        response = responseForNotFound("404 - The XML does not exist");
                    }

                } else {
                    response = responseForNotFound("404 - Not Found");
                }
            }
        }
        return response;
    }


    public HTTPResponse responseForPOST(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());

        UUID randomUuid = UUID.randomUUID();
        String uuid = randomUuid.toString();
        String content;


        if (this.xmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");
        } else {

            if (resourceParameters.containsKey("xml")) {

                content = resourceParameters.get("xml");
                this.xmlController.add(uuid, content);

                response.setStatus(HTTPResponseStatus.S200);
                response.putParameter("Content-Type", "text/html");
                response.setContent("<a href=\"xml?uuid=" + uuid.toString() + "\">" + uuid.toString() + "</a>");

            } else {
                return responseForBadRequest("400 - Bad Request");
            }
        }

        return response;
    }

    public HTTPResponse responseForDELETE(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        String uuid;

        if (this.xmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("uuid")) {

                uuid = resourceParameters.get("uuid");

                if (this.xmlController.get(uuid) != null) {

                    this.xmlController.delete(uuid);
                    response.setStatus(HTTPResponseStatus.S200);
                    response.putParameter("Content-Type", "text/html");
                    response.setContent("The XML has been deleted");

                } else {
                    response = responseForNotFound("404 - The XML does not exist");
                }
            } else {
                response = responseForBadRequest("400 - Invalid parameter");
            }
        }
        return response;
    }

}
