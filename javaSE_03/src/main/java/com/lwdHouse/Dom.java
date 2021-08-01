package com.lwdHouse;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 使用 Dom 解析xml
 *
 */
public class Dom
{
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        InputStream input = Dom.class.getResourceAsStream("/book.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(input);     // Document继承了Node
        printNode(doc, 0);
    }

    private static void printNode(Node node, int indent){
        for (int i = 0; i < indent; i++) {
            System.out.print("   ");
        }

        switch (node.getNodeType()){
            case Node.DOCUMENT_NODE:
                System.out.println("Document: "+node.getNodeName());
                break;
            case Node.ELEMENT_NODE:
                System.out.println("Element: "+node.getNodeName());
                break;
            case Node.TEXT_NODE:
                System.out.println("Text: "+node.getNodeName() + "=" + node.getNodeValue());
                break;
            case Node.ATTRIBUTE_NODE:
                System.out.println("Attr: "+node.getNodeName() + "=" + node.getNodeValue());
                break;
            default:
                System.out.println("NodeType: " + node.getNodeType() + ", NodeName: " + node.getNodeName());
                break;
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            printNode(child, indent+1);
        }
    }
}
