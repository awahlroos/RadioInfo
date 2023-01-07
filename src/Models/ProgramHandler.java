
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * ProgramHandler: Class containing logic for fetching data from API. Called from ProgramWorker (controller).
 */
public class ProgramHandler{

    private final String channelId;
    private final LocalDateTime currentTime;
    private final LocalDateTime rangeBefore;
    private final LocalDateTime rangeAfter;
    private final ArrayList<Program> programList;

    /**
     * ProgramHandler(): Get the current time and create the desired time interval for program tableau.
     */
    public ProgramHandler(String channelId){
        this.channelId = channelId;
        programList = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        currentTime = ZonedDateTime.parse(LocalDateTime.now().format(dtf)).toLocalDateTime();
        rangeBefore = currentTime.minusHours(6);
        rangeAfter = currentTime.plusHours(12);
    }

    /**
     * addFromAPI(): Logic for fetching program data from yesterday's or tomorrow's tableau
     * depending on the current time.
     */
    public ArrayList<Program> addFromAPI() throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document;

        if (currentTime.getHour() < 6) {
            document = builder.parse("http://api.sr.se/api/v2/scheduledepisodes?channelid="+channelId+
                    "&pagination=false&date=" + currentTime.minusDays(1));
            addToProgramList(document);
        }

        document = builder.parse("http://api.sr.se/api/v2/scheduledepisodes?channelid="+channelId+
                "&pagination=false");
        addToProgramList(document);

        if(currentTime.getHour() > 11) {
            document = builder.parse("http://api.sr.se/api/v2/scheduledepisodes?channelid="+channelId+
                    "&pagination=false&date=" + currentTime.plusDays(1));
            addToProgramList(document);
        }

        return programList;
    }

    /**
     * addToProgramList(): Contains logic to determine whether a program should be displayed in the tableau or not.
     */
    private void addToProgramList(Document doc){
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("scheduledepisode");

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element) node;

                LocalDateTime startTime = convertStringToDate(
                        element.getElementsByTagName("starttimeutc").item(0).getTextContent());
                LocalDateTime endTime = convertStringToDate(
                        element.getElementsByTagName("endtimeutc").item(0).getTextContent());


                if(endTime.isAfter(rangeBefore) && startTime.isBefore(rangeAfter)){

                    String description = "";
                    if(!(element.getElementsByTagName("description").getLength() == 0)){
                        description = element.getElementsByTagName("description").item(0).getTextContent();
                    }

                    String image = null;
                    if(!(element.getElementsByTagName("imageurl").getLength() == 0)){
                        image = element.getElementsByTagName("imageurl").item(0).getTextContent();
                    }

                    Program program = new Program(
                            element.getElementsByTagName("title").item(0).getTextContent(),
                            formatDate(startTime), formatDate(endTime), description, image);

                    programList.add(program);
                }
            }
        }
    }

    private LocalDateTime convertStringToDate(String string){
        ZonedDateTime parsedDate = ZonedDateTime.parse(string);
        ZonedDateTime zdtLocal = ZonedDateTime.ofInstant(parsedDate.toInstant(), ZoneId.systemDefault());
        return zdtLocal.toLocalDateTime();
    }

    private String formatDate(LocalDateTime date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        return date.format(dtf);
    }
}

