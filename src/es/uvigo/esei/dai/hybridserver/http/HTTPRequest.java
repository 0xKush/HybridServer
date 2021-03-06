package es.uvigo.esei.dai.hybridserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HTTPRequest {

    //request line
    private HTTPRequestMethod method;
    private String resourceChain;
    private String httpVersion;
    private String resourceName;
    private String[] resourcePath = {};

    //headers parameters
    private Map<String, String> headersParameters = new LinkedHashMap<>();
    private int contentLength;
    private String content;

    //resource parameters
    private Map<String, String> resourceParameters = new LinkedHashMap<>();

    private BufferedReader input;

    public HTTPRequest(Reader reader) throws IOException, HTTPParseException {

        try {

            String line;

            this.input = new BufferedReader(reader);
            this.parseRequestLine(input.readLine());

            while (!((line = this.input.readLine()).equals("")) && !(line.equals(null)))
                this.parseHeaders(line);

            if (getContentLength() > 0) {
                char[] contentArray = new char[getContentLength()];
                this.input.read(contentArray);
                this.content = new String(contentArray);
                this.checkContentType();
            }

            this.parseResourceParameters();

        } catch (Exception e) {
            //e.printStackTrace();
            throw new HTTPParseException("Error during the parse");
        }
    }


    private void parseRequestLine(String requestLine) {

        String[] aux = requestLine.split(" ");

        if (aux.length > 2) {

            method = HTTPRequestMethod.valueOf(aux[0]);
            resourceChain = aux[1];
            httpVersion = aux[2];
            resourceName = aux[1].split("\\?")[0].substring(1);

            if (!resourceName.isEmpty())
                resourcePath = resourceName.split("/");
        }


    }

    private void parseHeaders(String headers) {

        String[] kv = headers.split(":");

        if (kv.length > 1)
            headersParameters.put(kv[0], kv[1].trim());

        if ((headersParameters.get("Content-Length")) != null)
            contentLength = Integer.parseInt(headersParameters.get("Content-Length"));
        else
            contentLength = 0;
    }


    private void checkContentType() throws UnsupportedEncodingException {

        String type;

        if ((headersParameters.get("Content-Type")) != null) {
            type = headersParameters.get("Content-Type");

            if (type.startsWith("application/x-www-form-urlencoded"))
                content = URLDecoder.decode(content, "UTF-8");
        }
    }

    private void parseResourceParameters() {

        String[] s_resourceParameters_array = {}, token1, token2;

        if (contentLength == 0) {
            token1 = resourceChain.split("\\?");

            if (token1.length > 1) {
                token2 = token1[1].split("&");

                if (token2.length > 1)
                    s_resourceParameters_array = token2;
                else
                    s_resourceParameters_array = token1;
            }

        } else {

            token2 = content.split("&");

            if (token2.length >= 1)
                s_resourceParameters_array = token2;


        }


        for (int i = 0; i < s_resourceParameters_array.length; i++) {

            String[] kv = s_resourceParameters_array[i].split("=");

            if (kv.length > 1)
                resourceParameters.put(kv[0], kv[1]);
        }
    }

    public HTTPRequestMethod getMethod() {

        return method;
    }

    public String getResourceChain() {

        return resourceChain;
    }

    public String[] getResourcePath() {

        return resourcePath;
    }

    public String getResourceName() {

        return resourceName;
    }

    public Map<String, String> getResourceParameters() {

        return resourceParameters;
    }

    public String getHttpVersion() {

        return httpVersion;
    }

    public Map<String, String> getHeaderParameters() {

        return headersParameters;
    }

    public String getContent() {

        return content;
    }

    public int getContentLength() {

        return contentLength;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.getMethod().name()).append(' ').append(this.getResourceChain())
                .append(' ').append(this.getHttpVersion()).append("\r\n");

        for (Map.Entry<String, String> param : this.getHeaderParameters().entrySet()) {
            sb.append(param.getKey()).append(": ").append(param.getValue()).append("\r\n");
        }

        if (this.getContentLength() > 0) {
            sb.append("\r\n").append(this.getContent());
        }

        return sb.toString();
    }
}
