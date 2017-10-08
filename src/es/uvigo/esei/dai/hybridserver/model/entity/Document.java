package es.uvigo.esei.dai.hybridserver.model.entity;

public class Document {
    private String uuid;
    private String content;

    public Document(String uuid, String content) {
        this.uuid = uuid;
        this.content = content;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Document{" +
                "uuid='" + uuid + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
