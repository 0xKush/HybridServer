package es.uvigo.esei.dai.hybridserver.model.dao;

import es.uvigo.esei.dai.hybridserver.model.entity.Document;

import java.util.*;

public class HTMLMapDAO implements HTMLDAO {
    private Map<String, String> pages;

    public HTMLMapDAO(Map<String, String> pages) {
        this.pages = new HashMap<>();
    }

    @Override
    public Document get(String uuid) {
        if (this.pages.containsKey(uuid)) {
            return new Document(uuid, pages.get(uuid));
        }
        return null;
    }

    @Override
    public List<Document> list() {
        List<Document> documentList = new ArrayList<>();
        String key;

        Iterator<String> it = this.pages.keySet().iterator();

        while (it.hasNext()) {
            key = it.next();
            documentList.add(new Document(key.toString(), pages.get(key.toString())));
        }


        return documentList;
    }
}


/*

pages.put("6df1047e-cf19-4a83-8cf3-38f5e53f7725", "This is the html page 6df1047e-cf19-4a83-8cf3-38f5e53f7725.");
pages.put("79e01232-5ea4-41c8-9331-1c1880a1d3c2", "This is the html page 79e01232-5ea4-41c8-9331-1c1880a1d3c2.");
pages.put("a35b6c5e-22d6-4707-98b4-462482e26c9e", "This is the html page a35b6c5e-22d6-4707-98b4-462482e26c9e.");
pages.put("3aff2f9c-0c7f-4630-99ad-27a0cf1af137", "This is the html page 3aff2f9c-0c7f-4630-99ad-27a0cf1af137.");
pages.put("77ec1d68-84e1-40f4-be8e-066e02f4e373", "This is the html page 77ec1d68-84e1-40f4-be8e-066e02f4e373.");
pages.put("8f824126-0bd1-4074-b88e-c0b59d3e67a3", "This is the html page 8f824126-0bd1-4074-b88e-c0b59d3e67a3.");
pages.put("c6c80c75-b335-4f68-b7a7-59434413ce6c", "This is the html page c6c80c75-b335-4f68-b7a7-59434413ce6c.");
pages.put("f959ecb3-6382-4ae5-9325-8fcbc068e446", "This is the html page f959ecb3-6382-4ae5-9325-8fcbc068e446.");
pages.put("2471caa8-e8df-44d6-94f2-7752a74f6819", "This is the html page 2471caa8-e8df-44d6-94f2-7752a74f6819.");
pages.put("fa0979ca-2734-41f7-84c5-e40e0886e408", "This is the html page fa0979ca-2734-41f7-84c5-e40e0886e408.")

*/