/**
 * HybridServer
 * Copyright (C) 2017 Miguel Reboiro-Jato
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.uvigo.esei.dai.hybridserver.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.DOMParsing;

public class XMLConfigurationLoader {
    public Configuration load(File xmlFile)
            throws Exception {
            File xsdFile = new File("configuration.xsd");
            Document doc = DOMParsing.loadAndValidateWithExternalXSD(xmlFile.getAbsolutePath(), xsdFile.getAbsolutePath());

            //Connections
            NodeList connections = doc.getElementsByTagName("connections");
            Node connection = connections.item(0);
            Element elem_connection = (Element) connection;

            String webservice;
            int http, numClients;

            http = Integer.parseInt(elem_connection.getElementsByTagName("http").item(0).getTextContent());
            webservice = elem_connection.getElementsByTagName("webservice").item(0).getTextContent();
            numClients = Integer.parseInt(elem_connection.getElementsByTagName("numClients").item(0).getTextContent());


            //Database
            NodeList databases = doc.getElementsByTagName("database");
            Node database = databases.item(0);
            Element elem_database = (Element) database;

            String user, password, url;

            user = elem_database.getElementsByTagName("user").item(0).getTextContent();
            password = elem_database.getElementsByTagName("password").item(0).getTextContent();
            url = elem_database.getElementsByTagName("url").item(0).getTextContent();


            //Servers
            NodeList servers = doc.getElementsByTagName("server");
            String name, wsdl, namespace, service, httpAddress;
            ServerConfiguration serverConfiguration;
            List<ServerConfiguration> serversList = new ArrayList<>();

            for (int i = 0; i < servers.getLength(); i++) {
                Node server = servers.item(i);
                Element elem_server = (Element) server;

                name = elem_server.getAttribute("name");
                wsdl = elem_server.getAttribute("wsdl");
                namespace = elem_server.getAttribute("namespace");
                service = elem_server.getAttribute("service");
                httpAddress = elem_server.getAttribute("httpAddress");

                serverConfiguration = new ServerConfiguration(name, wsdl, namespace, service, httpAddress);
                serversList.add(serverConfiguration);
            }

            return new Configuration(http, numClients, webservice, user, password, url, serversList);
    }
}
