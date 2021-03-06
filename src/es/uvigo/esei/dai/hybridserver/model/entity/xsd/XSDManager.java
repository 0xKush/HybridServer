package es.uvigo.esei.dai.hybridserver.model.entity.xsd;

import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.controller.XSDController;
import es.uvigo.esei.dai.hybridserver.controller.factory.ControllerFactory;
import es.uvigo.esei.dai.hybridserver.http.HTTPResponse;
import es.uvigo.esei.dai.hybridserver.model.entity.AbstractManager;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class XSDManager extends AbstractManager {


    private XSDController xsdController;

    public XSDManager(ControllerFactory factory) {

        if (factory != null)
            this.xsdController = factory.createXSDController();
        else
            this.xsdController = null;
    }


    @Override
    public HTTPResponse GET(Map<String, String> resourceParameters) {

        HTTPResponse response;

        if (this.xsdController == null) {

            response = S500("500 - Internal Server Error");

        } else {

            if (resourceParameters.isEmpty()) {

                List<XSD> list = this.xsdController.list();
                Iterator<XSD> it = list.iterator();
                StringBuilder content = new StringBuilder();

                content.append("<html>\n" +
                        "<head>\n" +
                        "\t<meta charset=\"UTF-8\">\n" +
                        "\t<title>Hybrid Server</title>\n" +
                        "\t<h1>HybridServer</h1>\n" +
                        "</head>\n" +
                        "<body><ul>");
                while (it.hasNext()) {

                    XSD xsd = it.next();
                    content.append("<li>\n" + "<a href=\"xsd?uuid=" + xsd.getUuid() + "\">" + xsd.getUuid() + "</a>"
                            + "</li>\n");
                }

                //==== Remotes ====
                Map<ServerConfiguration, List<XSD>> remotes = this.xsdController.remoteList();

                for (Map.Entry<ServerConfiguration, List<XSD>> remote : remotes.entrySet()) {
                    ServerConfiguration serverConfiguration = remote.getKey();
                    List<XSD> remoteList = remote.getValue();
                    it = remoteList.iterator();

                    content.append("\n<h1>" + serverConfiguration.getName() + "</h1>\n");

                    if (!remoteList.isEmpty()) {

                        while (it.hasNext()) {
                            XSD doc = it.next();
                            content.append("<li>\n" + "<a href=\"" + serverConfiguration.getHttpAddress() + "xsd?uuid=" + doc.getUuid() + "\">" + doc.getUuid() + "</a>"
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
                    XSD xsd = xsdController.get(uuid);

                    if (xsd != null)
                        response = S200(xsd.getContent(), "application/xml");
                    else
                        response = S404("404 - The XSD does not exist");

                } else {
                    response = S404("404 - Not Found");
                }
            }
        }
        return response;
    }

    @Override
    public HTTPResponse POST(Map<String, String> resourceParameters) {

        HTTPResponse response;

        UUID randomUuid = UUID.randomUUID();
        String uuid = randomUuid.toString();
        String content;

        if (this.xsdController == null) {

            response = S500("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("xsd")) {

                content = resourceParameters.get("xsd");
                this.xsdController.add(uuid, content);
                response = S200("<a href=\"xsd?uuid=" + uuid.toString() + "\">" + uuid.toString() + "</a>", "text/html");

            } else {
                return S400("400 - Bad Request");
            }
        }

        return response;
    }

    @Override
    public HTTPResponse DELETE(Map<String, String> resourceParameters) {

        HTTPResponse response;
        String uuid;

        if (this.xsdController == null) {

            response = S500("500 - Internal Server Error");

        } else {

            if (resourceParameters.containsKey("uuid")) {

                uuid = resourceParameters.get("uuid");

                if (this.xsdController.get(uuid) != null) {

                    this.xsdController.delete(uuid);
                    response = S200("The XSD has been deleted", "text/html");

                } else {
                    response = S404("404 - The XSD does not exist");
                }
            } else {
                response = S400("400 - Invalid parameter");
            }
        }
        return response;
    }
}
