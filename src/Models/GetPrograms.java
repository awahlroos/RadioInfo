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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

        checkTime();

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
                Element eElement = (Element) node;
                //TODO: Går att lägga göra så Program tar in bara en node och att man plockar ut
                //alla attribut i Program istället
                programs.add(new Program(
                        eElement.getElementsByTagName("title").item(0).getTextContent(),
                        eElement.getElementsByTagName("starttimeutc").item(0).getTextContent(),
                        eElement.getElementsByTagName("endtimeutc").item(0).getTextContent()));
            }
        }
        return programs;
    }

    private Integer checkTime(){


        //Kommer behöva jämföra sen också mot API:ets format
        int var = 0;

        String string = "2022-12-15T20:00:00Z";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        ZonedDateTime parsedDate = ZonedDateTime.parse(string);
        ZonedDateTime zdtLocal = ZonedDateTime.ofInstant(parsedDate.toInstant(), ZoneId.systemDefault());
        //Used for formatting
        LocalDateTime zdtToLDT = zdtLocal.toLocalDateTime();
        String formatted = zdtToLDT.format(dtf);
        System.out.println(formatted);


        String currentTime = LocalDateTime.now().format(dtf);
        ZonedDateTime currentTimeZDT = ZonedDateTime.parse(currentTime);

        /*if(timenow.getHour() > 11){
            var=1;
        } else if (timenow.getHour() < 6) {
            var=2;
        }*/
        return var;
    }
}
