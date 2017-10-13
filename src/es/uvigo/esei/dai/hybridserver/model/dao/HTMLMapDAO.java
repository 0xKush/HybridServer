package es.uvigo.esei.dai.hybridserver.model.dao;

import es.uvigo.esei.dai.hybridserver.model.entity.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HTMLMapDAO implements HTMLDAO {
    private Map<String, String> pages = new HashMap<>();

    public HTMLMapDAO(Map<String, String> pages) {
        this.pages = pages;
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
            documentList.add(new Document(key, pages.get(key)));
        }


        return documentList;
    }

    @Override
    public void add(String uuid, String content) {
        this.pages.put(uuid, content);
    }

    @Override
    public void delete(String uuid) {
        this.pages.remove(uuid);
    }
}