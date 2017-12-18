package es.uvigo.esei.dai.hybridserver.model.entity.xml;


import java.io.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import es.uvigo.esei.dai.hybridserver.model.entity.SimpleErrorHandler;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class DOMParsing {

    public static Document loadAndValidateWithExternalXSD(String documentPath, String schemaPath)
            throws ParserConfigurationException, SAXException, IOException {
        // Construcción del schema
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(schemaPath));

        // Construcción del parser del documento. Se establece el esquema y se activa
        // la validación y comprobación de namespaces
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setNamespaceAware(true);
        factory.setSchema(schema);

        // Se añade el manejador de errores
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new SimpleErrorHandler());

        return builder.parse(new File(documentPath));
    }


    public static void validateWithXSD(XML xml, XSD xsd) throws SAXException, ParserConfigurationException, IOException {

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(new StringReader(xsd.getContent())));
        Validator validator = schema.newValidator();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder parser = dbf.newDocumentBuilder();
        Document doc = parser.parse(new ByteArrayInputStream(xml.getContent().getBytes()));

        validator.validate(new DOMSource(doc));


    }


}
