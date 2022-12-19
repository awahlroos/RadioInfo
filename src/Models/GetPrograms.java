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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class GetPrograms {
    private Channel channel;
    private ArrayList<Program> programs;

    private LocalDateTime currentTime;
    private LocalDateTime startTime;
    private final LocalDateTime rangeBefore;
    private final LocalDateTime rangeAfter;

    public GetPrograms(Channel channel){
        this.channel = channel;
        programs = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        currentTime = ZonedDateTime.parse(LocalDateTime.now().format(dtf)).toLocalDateTime().minusHours(10);
        rangeBefore = currentTime.minusHours(6);
        rangeAfter = currentTime.plusHours(12);
        System.out.println("Current time: " + currentTime);
    }

    public ArrayList<Program> getFromAPI() throws ParserConfigurationException, IOException, SAXException {
        //Get Document Builder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document;

        if (currentTime.getHour() < 6) {
            document = builder.parse("http://api.sr.se/api/v2/scheduledepisodes?channelid="+channel.getId()+
                    "&pagination=false&date=" + currentTime.minusDays(1));
            addToProgramList(document);
        }

        document = builder.parse("http://api.sr.se/api/v2/scheduledepisodes?channelid="+channel.getId()+
                "&pagination=false");
        addToProgramList(document);

        if(currentTime.getHour() > 11) {
            document = builder.parse("http://api.sr.se/api/v2/scheduledepisodes?channelid="+channel.getId()+
                    "&pagination=false&date=" + currentTime.plusDays(1));
            addToProgramList(document);
        }
        return programs;
    }

    private void addToProgramList(Document doc){
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("scheduledepisode");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;
                //TODO: Går att lägga göra så Program tar in bara en node och att man plockar ut
                //alla attribut i Program istället

                startTime = convertStringToDate(
                        eElement.getElementsByTagName("starttimeutc").item(0).getTextContent());

                if(startTime.isAfter(rangeBefore) && startTime.isBefore(rangeAfter)){
                    programs.add(new Program(
                            eElement.getElementsByTagName("title").item(0).getTextContent(),
                            startTime,
                            convertStringToDate(
                                    eElement.getElementsByTagName("endtimeutc").item(0).getTextContent())));
                }

            }
        }
    }

    private LocalDateTime convertStringToDate(String string){
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        ZonedDateTime parsedDate = ZonedDateTime.parse(string);
        ZonedDateTime zdtLocal = ZonedDateTime.ofInstant(parsedDate.toInstant(), ZoneId.systemDefault());
        //Used for formatting
        return zdtLocal.toLocalDateTime();
        //Used for comparison
        //String formattedDate = zdtToLDT.format(dtf);

        //String currentTime = LocalDateTime.now().format(dtf);
        //ZonedDateTime currentTimeZDT = ZonedDateTime.parse(currentTime);
    }
}
