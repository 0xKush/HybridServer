package es.uvigo.esei.dai.hybridserver.http;

import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HTTPRequest {

    private HTTPRequestMethod method;
    private String resourceChain;
    private String httpVersion;

    private String resourceName;
    private String[] resourcePath = {};
    private Map<String, String> resourceParameters = new LinkedHashMap<>();


    private Map<String, String> headersParameters = new LinkedHashMap<>();
    private int contentLenght;
    private String content;
    private BufferedReader input;

    public HTTPRequest(Reader reader) throws IOException, HTTPParseException {

        try {
            String line;
            input = new BufferedReader(reader);

            parseRequestLine(input.readLine());

            if (getMethod() == HTTPRequestMethod.GET) {

                while (!((line = input.readLine()).equals("")))
                    parseHeaders(line);

                parseResourceParameters();

            }

            if (getMethod() == HTTPRequestMethod.POST) {

                while (!((line = input.readLine()).equals("")))
                    parseHeaders(line);

                content = input.readLine();
                checkContentType();
                parseResourceParameters();


            }
        } catch (Exception e) {
        }

    }

    public void checkContentType() {
        try {

            String type = headersParameters.get("Content-Type");

            if (type != null && type.startsWith("application/x-www-form-urlencoded"))
                content = URLDecoder.decode(content, "UTF-8");

        } catch (Exception e) {
        }

    }

    public void parseRequestLine(String requestLine) {

        try {

            List<String> token1 = Splitter.on(" ").trimResults().splitToList(requestLine);
            List<String> token2 = Splitter.onPattern("[?|&]").omitEmptyStrings().splitToList(token1.get(1));

            method = HTTPRequestMethod.valueOf(token1.get(0));
            resourceChain = token1.get(1);
            httpVersion = token1.get(2);
            resourceName = token2.get(0).substring(1);
            resourcePath = parsePath(resourceName);

        } catch (Exception e) {
        }
    }

    public void parseResourceParameters() {

        try {

            List<String> token1 = Splitter.on("?").trimResults().splitToList(resourceChain);
            List<String> token2 = Splitter.on("&").trimResults().splitToList(token1.get(1));

            Iterator<String> it = token2.iterator();

            while (it.hasNext()) {
                List<String> token5 = Splitter.on("=").trimResults().splitToList(it.next());
                resourceParameters.put(token5.get(0), token5.get(1));
            }

        } catch (Exception e) {
        }
    }

    public void parseHeaders(String headers) {

        try {
            String[] kv = headers.split(":");
            headersParameters.put(kv[0], kv[1].trim());

            if (getMethod() == HTTPRequestMethod.POST)
                contentLenght = Integer.parseInt(headersParameters.get("Content-Length"));
            else
                contentLenght = 0;
        } catch (Exception e) {
        }
    }

    public String[] parsePath(String path) {

        String[] toret = {};
        if (!path.isEmpty())
            toret = path.split("/");

        return toret;
    }

    public HTTPRequestMethod getMethod() {
        // TODO Auto-generated method stub
        return method;
    }

    public String getResourceChain() {
        // TODO Auto-generated method stub
        return resourceChain;
    }

    public String[] getResourcePath() {
        // TODO Auto-generated method stub
        return resourcePath;
    }

    public String getResourceName() {
        // TODO Auto-generated method stub
        return resourceName;
    }

    public Map<String, String> getResourceParameters() {
        // TODO Auto-generated method stub
        return resourceParameters;
    }

    public String getHttpVersion() {
        // TODO Auto-generated method stub
        return httpVersion;
    }

    public Map<String, String> getHeaderParameters() {
        // TODO Auto-generated method stub
        return headersParameters;
    }

    public String getContent() {
        // TODO Auto-generated method stub
        return content;
    }

    public int getContentLength() {
        // TODO Auto-generated method stub
        return contentLenght;
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
