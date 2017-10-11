package es.uvigo.esei.dai.hybridserver.model.entity;

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

    public void setContent(String content) {
        this.content = content;
    }

    public String toString() {
        StringBuilder toRet = new StringBuilder();

        // Si la peticion viene vacia
        if (content.equals("") && uuid.equals("")) {
            toRet.append("<html>\n").append(" <head>\n")
                    .append(" <title>Hybrid Server</title>\n")
                    .append(" </head>\n")
                    .append(" <body>\n")
                    .append(" <h1>Welcome to Hybrid Server</h1>\n")
                    .append(" <h3> Authors:</h3>\n")
                    .append(" <h2>Cristopher</h2>\n")
                    .append(" <h2>Alberto</h2>\n")
                    .append(" </body>\n")
                    .append("</html>\n").toString();

            //Si la peticion
        } else if (uuid.equals("")) {
            toRet.append("<html>\n").append(" <head>\n")
                    .append(" <title>Hybrid Server</title>\n")
                    .append(" </head>\n")
                    .append(" <body>\n")
                    .append(content)
                    .append(" </body>\n")
                    .append("</html>\n").toString();

        } else {
            toRet.append("<html>\n").append(" <head>\n")
                    .append(" <title>Hybrid Server</title>\n")
                    .append(" </head>\n")
                    .append(" <body>\n")
                    .append(uuid + ":" + content + "\n")
                    .append(" </body>\n")
                    .append("</html>\n").toString();
        }

        return toRet.toString();
    }
}
