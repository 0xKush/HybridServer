package es.uvigo.esei.dai.hybridserver.model.entity;


public class HTMLAppend {

    public static String setWelcomePage() {

        StringBuilder toRet = new StringBuilder();

        toRet.append(" <h2> Authors: </h2>\n")
                .append(" <h1> Cristopher </h1>")
                .append(" <h1> Alberto</h1> \n");

        return toRet.toString();
    }

    public static String setErrorRequest(String content) {

        StringBuilder toRet = new StringBuilder();

        toRet.append(content);

        return toRet.toString();
    }

    public static String setHeader() {
        StringBuilder toRet = new StringBuilder();

        toRet.append("<html>\n")
                .append(" <head>\n")
                .append(" <title>Hybrid Server</title>\n")
                .append(" <h1>Hybrid Server</h1>\n")
                .append(" </head>\n")
                .append(" <body>\n");

        return toRet.toString();
    }

    public static String setFooter() {
        StringBuilder toRet = new StringBuilder();

        toRet.append("</html>\n")
                .append("</body>\n");

        return toRet.toString();
    }

    public static String setHTML(String s) {

        StringBuilder toRet = new StringBuilder();

        toRet.append(setHeader())
                .append("\n")
                .append(s)
                .append("\n")
                .append(setFooter());

        return toRet.toString();
    }

    public static String setListItem(Document d) {
        StringBuilder toRet = new StringBuilder();

        toRet.append("<li>\n")
                .append("<a href=\"localhost:12345/html?uuid=" + d.getUuid() + "\">")
                .append(d.getUuid())
                .append("</a>\n")
                .append("</li>\n");

        return toRet.toString();
    }
}
