package com.lwdHouse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * 使用SAX解析xml：Simple Api for Xml
 */
public class SAX {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        InputStream input = SAX.class.getResourceAsStream("/book.xml");
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        sp.parse(input, new MyHandler());
    }
}

class MyHandler extends DefaultHandler{
    @Override
    public void startDocument() throws SAXException {
        System.out.println("start document");
    }

    @Override
    public void endDocument() throws SAXException {
        print("end document");
    }

    @Override
    /* uri和localName都是空 */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        print("start element:", localName, uri, qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        print("end element:", localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        print("characters:", new String(ch, start, length));
    }

    @Override
    public void error(SAXParseException e) throws SAXException {
        print("error: ", e);
    }

    private void print(Object... objs){
        for (Object obj : objs) {
            System.out.print(obj);
            System.out.print(" ");
        }
        System.out.println();
    }
}
