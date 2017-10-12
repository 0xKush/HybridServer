package es.uvigo.esei.dai.hybridserver.http;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HTTPResponse {
    private HTTPResponseStatus status;
    private String version;
    private String content;

    private Map<String, String> parameters = new LinkedHashMap<>();

    public HTTPResponse() {
        this.status = null;
        this.version = null;

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

        return parameters.putIfAbsent(name, value);
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
        writer.append(' ');
        writer.write(Integer.toString(this.getStatus().getCode()));
        writer.append(' ');
        writer.write(this.getStatus().getStatus());
        writer.append("\r\n");
        if (!(this.parameters.isEmpty())) {
            for (Map.Entry<String, String> cabecera : this.parameters.entrySet()) {
                writer.write(cabecera.getKey() + ": " + cabecera.getValue());
            }
            writer.append("\r\n");
        }
        writer.append("\r\n");
        if (this.content != null) {
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
