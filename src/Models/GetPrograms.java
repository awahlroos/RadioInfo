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

public class GetPrograms {
    Channel channel;
    public GetPrograms(Channel channel){
        this.channel = channel;
    }
    public ArrayList<Program> getFromAPI() throws ParserConfigurationException, IOException, SAXException {
        ArrayList<Program> programs = new ArrayList<>();
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //Build Document
        Document document = builder.parse("http://api.sr.se/api/v2/scheduledepisodes?channelid="+channel.getId()+
                "&pagination=false");

        //Normalize the XML Structure; It's just too important !!
        document.getDocumentElement().normalize();

        //Get all employees
        NodeList nList = document.getElementsByTagName("scheduledepisode");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                //Print each employee's detail
                Element eElement = (Element) node;
                programs.add(new Program(
                        eElement.getElementsByTagName("title").item(0).getTextContent(),
                        eElement.getElementsByTagName("starttimeutc").item(0).getTextContent(),
                        eElement.getElementsByTagName("endtimeutc").item(0).getTextContent()));
                //System.out.println("First Name : "  + eElement.getElementsByTagName("firstName").item(0).getTextContent());
                /*System.out.println("Last Name : "   + eElement.getElementsByTagName("lastName").item(0).getTextContent());
                System.out.println("Location : "    + eElement.getElementsByTagName("location").item(0).getTextContent());*/
            }
        }

        return programs;
    }
}
