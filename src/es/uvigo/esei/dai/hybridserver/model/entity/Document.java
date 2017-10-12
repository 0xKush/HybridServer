package es.uvigo.esei.dai.hybridserver.model.entity;

import es.uvigo.esei.dai.hybridserver.utils.Tools;

public class Document {
    private String uuid;
    private String content;

    public Document(String uuid, String content) {
        this.uuid = uuid;
        this.content = content;
    }

    public Document() {
        this.uuid = null;
        this.content = null;
    }

    public Document(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

 /*
    public String toString() {
        String toRet = null;

        // welcome page
        if (content.equals("") && uuid.equals("")) {
            toRet = HTMLAppend.setWelcomePage();
        }


        // bad request
        if (uuid.equals("")) {
            Tools.info("error request ");
            toRet = HTMLAppend.setErrorRequest(content);
        }

        // pagina o lista de ellas
        if (!(content.equals("")) && !(uuid.equals(""))) {
            toRet = HTMLAppend.setDocument(content, uuid);
        }

        return toRet;
    }
    */

    @Override
    public String toString() {
        StringBuilder toRet = new StringBuilder();
        toRet.append(getContent() + "\n");
        return toRet.toString();
    }

    public String toString(boolean noDao) {
        if (getContent() == null)
            return HTMLAppend.setWelcomePage();
        else
            return HTMLAppend.setErrorRequest(getContent());
    }


}
