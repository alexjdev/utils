package org.alexjdev.utils.xml;

import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang.StringUtils.defaultString;

/**
 * Методы для упрощения работы с XML
 */
public class XMLUtils {
    public static DocumentBuilder getDocumentBuilder() {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        try {
            return dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void setTagValue(Document document, String uri, String localName, String nodeValue) {
        checkNotNull(nodeValue, "Node value для тега " + localName + " должен быть заполнен");
        Node node = document.getElementsByTagNameNS(uri, localName).item(0);
        setValueToTag(nodeValue, node);
    }

    public static void setValueToTag(String nodeValue, Node node) {
        if (node.getLastChild() != null) {
            node.getLastChild().setNodeValue(defaultString(nodeValue));
        } else {
            node.appendChild(node.getOwnerDocument().createTextNode(defaultString(nodeValue)));
        }
    }

    public static Document parseXMLString(String xmlString) {
        try {
            DocumentBuilder builder = getDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlString));
            return builder.parse(is);
        } catch (Exception err) {
            throw new RuntimeException(err.getMessage(), err);
        }
    }

    public static Document parseXML(byte[] xmlContent) {
        try {
            DocumentBuilder builder = getDocumentBuilder();
            InputSource is = new InputSource(new ByteArrayInputStream(xmlContent));
            return builder.parse(is);
        } catch (Exception err) {
            throw new RuntimeException(err.getMessage(), err);
        }
    }

    public static String getTagValue(Node node) {
        if (node != null) {
            if (node.getFirstChild() != null) {
                return node.getFirstChild().getNodeValue();
            }
        }
        return null;
    }

    public static Node findChildNode(Node parentNode, String childName) {
        NodeList childNodes = parentNode.getChildNodes();
        int count = childNodes.getLength();
        for (int idx = 0; idx < count; idx++) {
            Node child = childNodes.item(idx);
            if (childName.equals(child.getLocalName())) {
                return child;
            }
        }
        return null;
    }

    public static List<Node> findChildNodes(Node parentNode, String childName) {
        List<Node> result = new LinkedList<Node>();
        NodeList childNodes = parentNode.getChildNodes();
        int count = childNodes.getLength();
        for (int idx = 0; idx < count; idx++) {
            Node child = childNodes.item(idx);
            if (childName.equals(child.getLocalName())) {
                result.add(child);
            }
        }
        return result;
    }

    public static String getChildNodeValue(Node parentNode, String childName) {
        Node childNode = findChildNode(parentNode, childName);
        if (childNode != null) {
            return getTagValue(childNode);
        } else {
            return null;
        }
    }

    public static Date getChildNodeValueAsDate(Node parentNode, String childName) {
        String value = getChildNodeValue(parentNode, childName);
        if (value == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ") {
            public Date parse(String source, ParsePosition pos) {
                return super.parse(source.replaceFirst(":(?=[0-9]{2}$)", ""), pos);
            }
        };
        try {
            return df.parse(value);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String convertElementToString(Element signElement, String encoding) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, encoding);
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(signElement);
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (Exception e) {
            LogFactory.getLog(XMLUtils.class).error(e.getMessage(), e);
            return null;
        }
    }
}
