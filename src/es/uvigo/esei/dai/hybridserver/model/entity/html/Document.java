package es.uvigo.esei.dai.hybridserver.model.entity.html;

public class Document {
    private String uuid;
    private String content;

    public Document(String uuid, String content) {
        this.uuid = uuid;
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
        return "Page (uuid=" + uuid + ", content=" + content + ")";
    }


}
