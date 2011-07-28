package org.linnaeus.manager;

import org.linnaeus.bean.Advice;
import org.linnaeus.util.XmlParser;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 28/07/11
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
public class AdviceManager {

    private static AdviceManager instance = new AdviceManager();

    private AdviceManager(){

    }

    public static AdviceManager getInstance(){
        return instance;
    }

    public String[] getCategories(){
        ArrayList<String> categories = XmlParser.parseCategories();
        return categories.toArray(new String[0]);
    }
}
