package es.uvigo.esei.dai.hybridserver.model.entity;


public class HTMLAppend {

    public static String setWelcomePage() {

        StringBuilder toRet = new StringBuilder();

        toRet.append(" <h2> Authors: </h2>")
                .append(" <h3> Cristopher </h3>")
                .append(" <h3> Alberto</h3>");

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
                .append(setFooter());

        return toRet.toString();
    }

    public static String setPageList(Document d) {
        StringBuilder toRet = new StringBuilder();

        toRet.append("<li>\n")
                .append("<a href=\"html?uuid=" + d.getUuid() + "\">" + d.getUuid() + "</a>")
                .append("</li>\n");

        return toRet.toString();
    }

    public static String setPage(Document d) {
        StringBuilder toRet = new StringBuilder();

        toRet.append("<a href=\"html?uuid=" + d.getUuid() + "\">" + d.getUuid() + "</a>");

        return toRet.toString();
    }
}
