package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.ServerConfiguration;
import es.uvigo.esei.dai.hybridserver.hbSEI;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class wsManager {
    private Map<ServerConfiguration, hbSEI> remoteServices;

    public wsManager(List<ServerConfiguration> serverList) {

        this.remoteServices = new LinkedHashMap<>();

        if (serverList != null) {
            for (ServerConfiguration server : serverList) {
                try {
                    if (server != null) {
                        URL url = new URL(server.getWsdl());
                        QName name = new QName(server.getNamespace(), server.getService());
                        Service service = Service.create(url, name);
                        hbSEI webService = service.getPort(hbSEI.class);
                        this.remoteServices.put(server, webService);
                    }
                } catch (WebServiceException | MalformedURLException e) {/*server down*/ }
            }
        }
    }

    public Map<ServerConfiguration, hbSEI> getRemoteServices() {
        return remoteServices;
    }


}

