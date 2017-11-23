package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

public class HTTPResponse {

    private HTTPResponseStatus status;
    private String version;
    private String content;
    private Map<String, String> parameters;

    public HTTPResponse() {
        parameters = new HashMap<>();

    }

    public HTTPResponseStatus getStatus() {

        return this.status;
    }

    public void setStatus(HTTPResponseStatus status) {
        this.status = status;
    }

    public String getVersion() {

        return this.version;
    }

    public void setVersion(String version) {

        this.version = version;
    }

    public String getContent() {

        return this.content;
    }

    public void setContent(String content) {

        this.content = content;
        this.putParameter("Content-Length", Integer.toString(content.length()));
    }

    public Map<String, String> getParameters() {

        return this.parameters;
    }

    public String putParameter(String name, String value) {

        return parameters.put(name, value);
    }

    public boolean containsParameter(String name) {

        return parameters.containsKey(name);
    }

    public String removeParameter(String name) {

        return parameters.remove(name);
    }

    public void clearParameters() {
        parameters.clear();
    }

    public List<String> listParameters() {

        return (List<String>) parameters.values();
    }

    public void print(Writer writer) throws IOException, NullPointerException {
        writer.write(this.getVersion());

        if (this.getStatus() != null)
            writer.write(" " + this.getStatus().getCode() + " " + this.getStatus().getStatus());


        Iterator<String> it = parameters.keySet().iterator();
        String aux;

        if (it.hasNext()) {
            writer.write("\r\n");
            while (it.hasNext()) {
                aux = it.next();
                writer.write(aux + ": " + parameters.get(aux) + "\r\n");

                if (!it.hasNext()) {
                    writer.write("\r\n");
                }
            }
        } else {
            writer.write("\r\n\r\n");
        }

        if (this.getContent() != null) {
            writer.write(this.getContent());
        }

        writer.flush();
    }

    @Override
    public String toString() {
        final StringWriter writer = new StringWriter();

        try {
            this.print(writer);
        } catch (IOException e) {
        }

        return writer.toString();
    }
}
