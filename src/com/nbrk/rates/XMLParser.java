package com.nbrk.rates;

import android.os.StrictMode;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: X120e
 * Date: 16.07.12
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class XMLParser {
    public XMLParser() {}

    /**
     * Getting XML from URL making HTTP request
     * @param url String
     * @return String
     */
    public String getXMLFromUrl(String url) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String xml = null;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return xml;
    }

    /**
     * Getting XML DOM element
     * @param url
     * @return Document
     */
    public Document getDomElement(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            URL serviceURL = new URL(url);

            DocumentBuilder db = dbf.newDocumentBuilder();
            //InputSource is = new InputSource();
            //is.setCharacterStream(new StringReader(xml));
            doc = db.parse(new InputSource(serviceURL.openStream()));
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    /**
     * Getting node value
     * @param element
     * @return String
     */
    public final String getElementValue(Node element) {
        Node child;
        if (element != null) {
            if (element.hasChildNodes()){
                for (child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Getting node value
     * @param item node
     * @param str String
     * @return
     */
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
}
