package es.uvigo.esei.dai.hybridserver.controller;


import es.uvigo.esei.dai.hybridserver.model.dao.HTMLDBDAO;

import java.util.Properties;

public class DBControllerFactory implements ControllerFactory {

    private Properties properties;

    public DBControllerFactory(Properties properties) {
        this.properties = properties;
    }

    @Override
    public HTMLController createHtmlController() {
        return new HTMLController(new HTMLDBDAO(properties));
    }
}
