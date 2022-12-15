package Models;

import Models.Channel;
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

public class GetChannels {

    public GetChannels(){}
    public ArrayList<Channel> getFromAPI() throws ParserConfigurationException, IOException, SAXException {
        ArrayList<Channel> channels = new ArrayList<>();
        String image;
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //Build Document
        Document document = builder.parse("http://api.sr.se/api/v2/channels?pagination=false");

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();
        //Get all employees
        NodeList nList = document.getElementsByTagName("channel");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                //Print each employee's detail
                Element eElement = (Element) node;

                if(!(eElement.getElementsByTagName("image").getLength() == 0)){
                    image = eElement.getElementsByTagName("image").item(0).getTextContent();
                } else {
                    image = null;
                }
                channels.add(new Channel(
                        eElement.getAttribute("name"),
                        //TODO: Uncomment image when done
                        eElement.getAttribute("id"), null /*image*/));
            }
        }
        return channels;
    }
}
