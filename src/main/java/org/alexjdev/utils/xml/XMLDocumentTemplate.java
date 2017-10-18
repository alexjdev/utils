package org.alexjdev.utils.xml;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;

/**
 * Синхронизированный шаблон XML документа
 */
public class XMLDocumentTemplate {

    private final Document document;

    public XMLDocumentTemplate(String resourcePath) {
        InputStream template = this.getClass().getResourceAsStream(resourcePath);
        try {
            DocumentBuilder dBuilder = ParserHelper.getDocumentBuilder();
            document = dBuilder.parse(template);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(template);
        }
    }

    public synchronized Document getTemplate() {
        try {
            DocumentBuilder documentBuilder = ParserHelper.getDocumentBuilder();
            Document newDocument = documentBuilder.newDocument();
            Node copiedRoot = newDocument.importNode(document.getDocumentElement(), true);
            newDocument.appendChild(copiedRoot);
            return newDocument;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
