package Models;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.spec.PSource;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProgramHandler extends SwingWorker<Void, Program> {

    private final Channel channel;
    private final LocalDateTime currentTime;
    private final LocalDateTime rangeBefore;
    private final LocalDateTime rangeAfter;

    public ProgramHandler(Channel channel){
        this.channel = channel;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        currentTime = ZonedDateTime.parse(LocalDateTime.now().format(dtf)).toLocalDateTime();
        rangeBefore = currentTime.minusHours(6);
        rangeAfter = currentTime.plusHours(12);
    }

    @Override
    protected Void doInBackground() throws Exception {

        System.out.println("----- Hämtning från API, kanal: " + channel.getName() + " -----");
        System.out.println(LocalDateTime.now().getSecond());
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
        return null;
    }

    private void addToProgramList(Document doc) throws InterruptedException {
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("scheduledepisode");

        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) node;

                LocalDateTime startTime = convertStringToDate(
                        eElement.getElementsByTagName("starttimeutc").item(0).getTextContent());


                if(startTime.isAfter(rangeBefore) && startTime.isBefore(rangeAfter)){

                    String description = "";
                    if(!(eElement.getElementsByTagName("description").getLength() == 0)){
                        description = eElement.getElementsByTagName("description").item(0).getTextContent();
                    }

                    String image = null;
                    if(!(eElement.getElementsByTagName("imageurl").getLength() == 0)){
                        image = eElement.getElementsByTagName("imageurl").item(0).getTextContent();
                    }

                    Program program = new Program(
                            eElement.getElementsByTagName("title").item(0).getTextContent(),
                            formatDate(startTime),
                            formatDate(convertStringToDate(
                                    eElement.getElementsByTagName("endtimeutc").item(0).getTextContent())),
                            description,
                            image);

                    publish(program);
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
        String strDate = date.format(dtf);
        return strDate;
    }

    //Update the PTM on EDT
    @Override
    protected void process(List<Program> chunks) {
        for(Program p : chunks){
            channel.updatePTM(p);
        }
    }
}
