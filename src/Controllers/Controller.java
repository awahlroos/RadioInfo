package Controllers;

import Controllers.ChannelWorker;
import Models.GetPrograms;
import Models.Program;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Controller {
    StartView view;
    public Controller(){
        ChannelWorker chWorker = new ChannelWorker();
        chWorker.execute();
    }
}
