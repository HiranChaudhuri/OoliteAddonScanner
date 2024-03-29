/*
 */
package com.chaudhuri.ooliteaddonscanner2.plist;

import com.chaudhuri.ooliteaddonscanner2.model.Expansion;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/** Parses plist stored as XML.
 *
 * @author hiran
 */
public class XMLPlistParser {
    private static final Logger log = LogManager.getLogger(XMLPlistParser.class);
    
    /**
     * Handler that collects parsing errors as warnings in the Expansion.
     */
    public static class MySaxErrorHandler implements ErrorHandler {
        private Expansion expansion;
        
        /**
         * Creates a new MySaxErrorHandler. 
         * 
         * @param expansion the expansion to store the errors in
         */
        public MySaxErrorHandler(Expansion expansion) {
            this.expansion = expansion;
        }

        @Override
        public void warning(SAXParseException saxpe) throws SAXException {
            expansion.addWarning(String.format("%s (Warning)", saxpe.getMessage()));
        }

        @Override
        public void error(SAXParseException saxpe) throws SAXException {
            expansion.addWarning(String.format("%s (Error)", saxpe.getMessage()));
        }

        @Override
        public void fatalError(SAXParseException saxpe) throws SAXException {
            // these conditions will make the parser throw an exception, which is added to the index anyway
            expansion.addWarning(String.format("%s (Fatal)", saxpe.getMessage()));
        }
        
    }
    
    /**
     * Prevent this class from instantiation.
     */
    private XMLPlistParser() {
    }
    
    /**
     * Serializes a dom node to an XML string.
     * 
     * @param n the dom node
     * @return the string
     * @throws TransformerException something went wrong
     */
    public static String serialize(Node n) throws TransformerException {
        StringWriter sw = new StringWriter();
        Transformer t = TransformerFactory.newDefaultInstance().newTransformer();
        t.transform(new DOMSource(n), new StreamResult(sw));
        return sw.toString();
    }

    /**
     * Turns a PList array, represented as dom element, into an array of
     * parsed elements.
     * 
     * @param array the dom element to parse
     * @return the result ArrayList
     */
    private static List<Object> parseArray(Element array) {
        log.debug("parseArray({})", array);
        
        ArrayList<Object> result = new ArrayList<>();
        NodeList nl = array.getChildNodes();
        for (int i=0;i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                result.add(parseElement((Element)n));
            }
        }
        
        return result;
    }
    
    private static Map<String, Object> parseDict(Element dict) {
        log.debug("parseDict({})", dict);
        
        Map<String, Object> result = new TreeMap<>();
        NodeList nl = dict.getChildNodes();
        
        String key = null;
        
        for (int i=0;i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                if (key == null) {
                    // parse the key
                    if (!"key".equals(n.getNodeName())) {
                        throw new IllegalArgumentException(String.format("Expected element 'key', found %s", n.getNodeName()));
                    }
                    key = String.valueOf(parseElement((Element)n));
                    if ("".equals(key)) {
                        throw new IllegalArgumentException("Key must not be empty");
                    }
                } else {
                    // do something about the value
                    Object value = parseElement((Element)n);
                    result.put(key, value);
                    key = null;
                }
            }
        }
        
        return result;
    }
    
    private static Object parseElement(Element e) {
        log.debug("parseElement({})", e);
        try {
            switch (e.getTagName()) {
                case "array":
                    return parseArray(e);
                case "integer":
                    return Integer.parseInt(e.getTextContent());
                case "real":
                    return Double.parseDouble(e.getTextContent());
                case "string":
                case "key":
                    return e.getTextContent();
                case "dict":
                    return parseDict(e);
                case "true":
                    return Boolean.TRUE;
                case "false":
                    return Boolean.FALSE;
                default:
                    throw new IllegalArgumentException(String.format("Unknown element %s", e.getTagName()));
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(String.format("Could not parse element %s", e.getNodeName()), ex);
        }
    }
    
    /**
     * Parses a dictionary from an XML DOM tree.
     * 
     * @param doc the parsed XML
     * @param eh the errorhandler to use
     * @return the map of maps
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws IOException something went wrong
     * @throws TransformerException something went wrong
     */
    public static Map<String, Object> parseListOfMaps(Document doc, ErrorHandler eh) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        log.debug("parseMap({})", doc);
        
        Element plist = doc.getDocumentElement();
        if (!"plist".equals(plist.getNodeName())) {
            throw new IllegalArgumentException("Expected root node plist");
        }
        if (!"1.0".equals(plist.getAttribute("version"))) {
            throw new IllegalArgumentException("Expected plist 1.0 format");
        }

        if (log.isTraceEnabled()) {
            log.trace("Parsed {}", serialize(doc));
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        NodeList nl = plist.getChildNodes();
        for (int i= 0; i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                result.add(parseDict((Element)n));
            }
        }
        
        if (result.size() != 1) {
            throw new IllegalArgumentException("Not exactly one map in document?");
        }
        
        return result.get(0);
    }
    
    /**
     * Parses an XML stream into a Dom document. Optionally you can specify
     * an errorhandler.
     * 
     * @param in the input stream to parse
     * @param eh the handler to report errors
     * @return the parsed document
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws IOException something went wrong
     */
    public static Document parseInputStream(InputStream in, ErrorHandler eh) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        if(eh != null) {
            db.setErrorHandler(eh);
        }
        return db.parse(in);
    }

    /** Parses an XML file, checks it is a plist/dict and returns the map of values
     * found.
     * 
     * @param in
     * @param eh
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException 
     */
    public static Map<String, Object> parseDictionary(InputStream in, ErrorHandler eh) throws ParserConfigurationException, SAXException, IOException {
        Document doc = parseInputStream(in, eh);
        Element plist = doc.getDocumentElement();
        return parseDict((Element)plist.getElementsByTagName("dict").item(0));
    }
    
    /** Parses an XML file, checks if it is a plist and returns the list of values
     * found.
     * 
     * @param in
     * @param eh
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException 
     */
    public static List<Object> parseList(InputStream in, ErrorHandler eh) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        log.debug("parseList({})", in);
        
        Document doc = parseInputStream(in, eh);
        Element plist = doc.getDocumentElement();
        if (!"plist".equals(plist.getNodeName())) {
            throw new IllegalArgumentException("Expected root node plist");
        }
        if (!"1.0".equals(plist.getAttribute("version"))) {
            throw new IllegalArgumentException("Expected plist 1.0 format");
        }

        if (log.isTraceEnabled()) {
            log.trace("Parsed {}", serialize(doc));
        }
        
        ArrayList<Object> result = new ArrayList<>();
        NodeList nl = plist.getChildNodes();
        for (int i= 0; i<nl.getLength();i++) {
            Node n = nl.item(i);
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                result.add(parseElement((Element)n));
            }
        }
        
        return result;
    }
    
    /**
     * Parses an XML document to DOM.
     * 
     * @param in the inputstream to parse
     * @param eh the errorhandler to use
     * @return the parsed DOM document
     * @throws ParserConfigurationException something went wrong
     * @throws SAXException something went wrong
     * @throws IOException something went wrong
     */
    public static Document parseXml(InputStream in, ErrorHandler eh) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        if(eh != null) {
            db.setErrorHandler(eh);
        }
        return db.parse(in);
    }
}
