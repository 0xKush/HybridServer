package es.uvigo.esei.dai.hybridserver.model.entity;

public class Document {
    private String uuid;
    private String content;

    public Document(String uuid, String content) {
        this.uuid = uuid;
        this.content = content;
    }

    public Document() {

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

    public String emptyResource() {

        String toret = new StringBuilder()
                .append("<html>\n").append(" <head>\n")
                .append(" <title>Hybrid Server</title>\n")
                .append(" </head>\n")
                .append(" <body>\n")
                .append(" <h1>Welcome to Hybrid Server</h1>\n")
                .append(" <h3> Authors:</h3>\n")
                .append(" <h2>Cristopher</h2>\n")
                .append(" <h2>Alberto</h2>\n")
                .append(" </body>\n")
                .append("</html>\n").toString();

        return toret;
    }

    public String toString() {
        return "Document{" +
                "uuid='" + uuid + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
