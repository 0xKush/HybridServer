package es.uvigo.esei.dai.hybridserver.model.entity.xml;

import es.uvigo.esei.dai.hybridserver.configuration.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.XMLController;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.XSLTController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.hbSEI;
import es.uvigo.esei.dai.hybridserver.http.HTTPHeaders;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponseStatus;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static es.uvigo.esei.dai.hybridserver.model.entity.wsManager.wsConnection;
import static es.uvigo.esei.dai.hybridserver.model.entity.wsManager.wsGetContent;
import static es.uvigo.esei.dai.hybridserver.model.entity.wsManager.wsGetList;
import static es.uvigo.esei.dai.hybridserver.model.entity.xml.DOMParsing.validateWithXSD;
import static es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLTUtils.transform;

public class XMLManager extends AbstractManager {

    private XMLController xmlController;
    private XSDController xsdController;
    private XSLTController xsltController;
    private Map<ServerConfiguration, hbSEI> remoteServices;

    public XMLManager(ControllerFactory factory) {

        if (factory != null) {
            this.xmlController = factory.createXMLController();
            this.xsdController = factory.createXSDController();
            this.xsltController = factory.createXSLTController();
            this.remoteServices = wsConnection(this.xmlController.getServerList());


        } else {
            this.xmlController = null;
            this.xsdController = null;
            this.xsltController = null;
            this.remoteServices = null;
        }
    }


    public HTTPResponse responseForGET(Map<String, String> resourceParameters) {

        HTTPResponse response = new HTTPResponse();
        response.setVersion(HTTPHeaders.HTTP_1_1.getHeader());
        String remoteContent;


        if (this.xmlController == null) {
            response = responseForInternalServerError("500 - Internal Server Error");

        } else {

            if (resourceParameters.isEmpty()) {

                //Tools.info("resource:xml - without parameters");

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


                //== == == == == == WebServices == == == == == == ==

                if (this.remoteServices != null) {
                    remoteContent = wsGetList(this.remoteServices, "xml");
                    content.append(remoteContent);
                }

                //== == == == == == WebServices END == == == == ====

                content.append("\t</ul>\n" +
                        "\t\n" +
                        "</body>\n" +
                        "</html>");
                response.setStatus(HTTPResponseStatus.S200);
                response.putParameter("Content-Type", "text/html");
                response.setContent(content.toString());


            } else {

                //Tools.info("resource:xml - with parameters");

                if (resourceParameters.size() == 1 && resourceParameters.containsKey("uuid")) {

                    String uuid = resourceParameters.get("uuid");
                    XML xml = xmlController.get(uuid);

                    if (xml != null) {

                        //Tools.info("S200(OK)");

                        response.setStatus(HTTPResponseStatus.S200);
                        response.putParameter("Content-Type", "application/xml");
                        response.setContent(xml.getContent());

                    } else {
                        //== == == == == == WebServices == == == == == == ==
                        if (this.remoteServices != null) {
                            if ((remoteContent = wsGetContent(this.remoteServices, "xml", uuid)) != null) {

                                response.setStatus(HTTPResponseStatus.S200);
                                response.putParameter("Content-Type", "application/xml");
                                response.setContent(remoteContent);

                            } else {
                                response = responseForNotFound("404 - The XML does not exist");
                            }
                        } else {
                            response = responseForNotFound("404 - The XML does not exist");
                        }
                        //== == == == == == WebServices END == == == == ====
                    }
                } else if (resourceParameters.size() == 2 && resourceParameters.containsKey("uuid") && resourceParameters.containsKey("xslt")) {

                    String uuidXML = resourceParameters.get("uuid");
                    XML xml;

                    if ((xml = xmlController.get(uuidXML)) == null) {
                        //== == == == == == WebServices == == == == == == ==
                        if (this.remoteServices != null) {
                            String xmlContent = wsGetContent(this.remoteServices, "xml", uuidXML);

                            if (xmlContent != null) {
                                xml = new XML(uuidXML, xmlContent);

                            } else {
                                response = responseForBadRequest("400 - The XML does not exist");
                            }
                        } else {
                            response = responseForBadRequest("400 - The XML does not exist");
                        }
                        //== == == == == == WebServices END == == == == ===
                    }

                    String uuidXSLT = resourceParameters.get("xslt");
                    XSLT xslt;
                    XSD xsd;

                    if ((xslt = xsltController.get(uuidXSLT)) == null) {
                        //== == == == == == WebServices == == == == == == ==

                        if (this.remoteServices != null) {
                            String xsltContent = wsGetContent(this.remoteServices, "xslt", uuidXSLT);

                            if (xsltContent != null) {

                                String uuidXSD = wsGetContent(this.remoteServices, "xml-xsd", uuidXSLT);

                                if (uuidXSD != null) {
                                    xslt = new XSLT(uuidXSLT, xsltContent, uuidXSD);

                                    String xsdContent = wsGetContent(this.remoteServices, "xsd", uuidXSD);

                                    if (xsdContent != null) {
                                        xsd = new XSD(uuidXSD, xsdContent);
                                    }

                                } else {
                                    response = responseForBadRequest("400 - The XSD does not exist");
                                }

                            } else {
                                response = responseForBadRequest("400 - The XSLT does not exist");
                            }
                        } else {
                            response = responseForBadRequest("400 - The XSLT does not exist");
                        }
                    } else {
                        //== == == == == == WebServices END == == == == ===
                        String uuidXSD = xslt.getXsd();

                        if ((xsd = xsdController.get(uuidXSD)) == null) {
                            //== == == == == == WebServices == == == == == == ==

                            if (this.remoteServices != null) {
                                String xsdContent = wsGetContent(this.remoteServices, "xml-xsd", uuidXSD);

                                if (xsdContent != null) {
                                    xsd = new XSD(uuidXSD, xsdContent);

                                } else {
                                    response = responseForBadRequest("400 - The XSD does not exist");
                                }
                            } else {
                                response = responseForBadRequest("400 - The XSD does not exist");
                            }
                            //== == == == == == WebServices END == == == == ===
                        }

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
                    }
                }
            }
        }
        return response;
    }


    public HTTPResponse responseForPOST(Map<String, String> resourceParameters) {

        //Tools.info("Response for Post");

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
