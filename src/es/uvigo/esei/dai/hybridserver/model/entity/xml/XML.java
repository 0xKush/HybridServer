package es.uvigo.esei.dai.hybridserver.model.entity.xml;

public class XML {
    private String uuid;
    private String content;


    public XML(String uuid, String content) {
        super();
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
        return "XML [uuid=" + uuid + ", content=" + content + "]";
    }
}
