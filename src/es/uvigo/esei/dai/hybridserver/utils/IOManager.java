package es.uvigo.esei.dai.hybridserver.utils;

import java.io.IOException;

public interface IOManager {
    public void println(String line) throws IOException;

    public String readLine() throws IOException;

    public boolean canRead() throws IOException;
}
