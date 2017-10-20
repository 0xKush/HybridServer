package es.uvigo.esei.dai.hybridserver.model.entity;

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

    @Override
    public String toString() {
        StringBuilder toRet = new StringBuilder();
        toRet.append(getContent());
        return toRet.toString();
    }

    public String toString(boolean noDao) {
        if (getContent() == null)
            return HTMLAppend.setWelcomePage();
        else
            return HTMLAppend.setErrorRequest(getContent());
    }


}
