package Controllers;

import Models.Channel;
import Models.GetChannels;
import Models.GetPrograms;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChannelWorker extends SwingWorker<ArrayList<Channel>, Object>{

    ArrayList<Channel> channels = new ArrayList<>();
    GetChannels getChannels = new GetChannels();

    public ChannelWorker(){

    }

    //TODO: Flytta inläsning av data från swing till annan tråd (Thread, timer, etc)
    @Override
    protected ArrayList<Channel> doInBackground() throws ParserConfigurationException, IOException, SAXException {
        return getChannels.getFromAPI();
    }


    @Override
    public void done(){
        try {
            channels = get();

            StartView view = new StartView("Radio Info", channels);
            //view.setChannelButtonListener(e -> System.out.println("Hej"));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    /*@Override
    public void newChannel(Models.Channel c) {
        publish(c);
    }

    enum Parsing {CHANNEL,UNKNOWN}

    private ChannelModel tableModel;

    public Worker(ChannelModel tableModel) {
        super();
        this.tableModel=tableModel;
    }*/
}