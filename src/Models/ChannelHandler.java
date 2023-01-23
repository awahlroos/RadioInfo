package Models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ChannelHandler: Class containing logic for fetching data from API. Called from ChannelWorker (controller).
 */
public class ChannelHandler {

    public ChannelHandler(){}


    /**
     * getFromAPI(): Creates a document builder and parse the content from XML to a Document. The document can then
     * be used to retrieve specific tag information, in this case a channel.
     */
    public ArrayList<Channel> getFromAPI() throws ParserConfigurationException, IOException, SAXException {

        ArrayList<Channel> channels = new ArrayList<>();
        String image;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse("http://api.sr.se/api/v2/channels?pagination=false");
        document.getDocumentElement().normalize();

        NodeList nodeList = document.getElementsByTagName("channel");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;

                //Check if a channel has an image
                if(!(element.getElementsByTagName("image").getLength() == 0)){
                    image = element.getElementsByTagName("image").item(0).getTextContent();
                } else {
                    image = null;
                }

                channels.add(new Channel(
                        element.getAttribute("name"), element.getAttribute("id"), image));
            }
        }
        return channels;
    }
}
