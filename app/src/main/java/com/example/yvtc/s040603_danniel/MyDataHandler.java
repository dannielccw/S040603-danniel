package com.example.yvtc.s040603_danniel;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by yvtc on 2017/4/17.
 */

public class MyDataHandler extends DefaultHandler {
    boolean isTitle = false;
    boolean inItem = false;
    boolean isLink = false;
    public ArrayList<String> titles = new ArrayList<>();
    public ArrayList<String> links = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (qName.equals("title"))
        {
            isTitle = true;
        }
        if (qName.equals("item"))
        {
            inItem = true;
        }
        if (qName.equals("link"))
        {
            isLink = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (qName.equals("title"))
        {
            isTitle = false;
        }
        if (qName.equals("link"))
        {
            isLink = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isTitle && inItem)
        {
            String str = new String(ch).substring(start, length);
            titles.add(str);
            Log.d("TITLE", str);
        }
        if (isLink && inItem) {
            String str = new String(ch).substring(start, length);
            links.add(str);
            Log.d("TITLE", str);
        }
    }
}
