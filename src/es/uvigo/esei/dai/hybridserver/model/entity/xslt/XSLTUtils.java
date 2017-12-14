package es.uvigo.esei.dai.hybridserver.model.entity.xslt;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;
import org.xml.sax.SAXException;

public class XSLTUtils {

    public static void transform(Source xmlSource, Source xsltSource, Result result) throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(xsltSource);
        transformer.transform(xmlSource, result);
    }

    public static String transform(File xml, File xslt) throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(xslt));

        StringWriter writer = new StringWriter();

        transformer.transform(new StreamSource(xml), new StreamResult(writer));

        return writer.toString();
    }

    public static String transform(XML xml, XSLT xslt) throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(new StreamSource(new StringReader(xslt.getContent())));
        StringWriter writer = new StringWriter();

        transformer.transform(new StreamSource(new StringReader(xml.getContent())), new StreamResult(writer));

        return writer.toString();
    }

}