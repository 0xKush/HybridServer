package es.uvigo.esei.dai.hybridserver.controller;

import es.uvigo.esei.dai.hybridserver.model.dao.HTMLMapDAO;

import java.util.Map;

public class MapControllerFactory implements ControllerFactory {
    private Map<String, String> pages;

    public MapControllerFactory(Map<String, String> pages) {
        this.pages = pages;
    }

    @Override
    public HTMLController createHtmlController() {
        return new HTMLController(new HTMLMapDAO(pages));
    }
}
