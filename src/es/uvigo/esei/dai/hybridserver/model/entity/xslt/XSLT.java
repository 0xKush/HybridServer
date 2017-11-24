package es.uvigo.esei.dai.hybridserver.model.entity.xslt;

public class XSLT {
    private String uuid;
    private String content;
    private String xsd;

    public XSLT(String uuid, String content, String xsd) {
        super();
        this.uuid = uuid;
        this.content = content;
        this.xsd = xsd;
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

    public String getXsd() {
        return xsd;
    }

    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    @Override
    public String toString() {
        return "XSLT [uuid=" + uuid + ", contenido=" + content + ", xsd=" + xsd + "]";
    }
}
