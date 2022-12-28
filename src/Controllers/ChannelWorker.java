package Controllers;

import Models.Channel;
import Models.GetChannels;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChannelWorker extends SwingWorker<ArrayList<Channel>, Object>{

    ArrayList<Channel> channels = new ArrayList<>();
    GetChannels getChannels = new GetChannels();
    StartView view;

    public ChannelWorker(){}

    //TODO: Flytta inläsning av data från swing till annan tråd (Thread, timer, etc)
    @Override
    protected ArrayList<Channel> doInBackground() throws ParserConfigurationException, IOException, SAXException {
        return getChannels.getFromAPI();
    }

    @Override
    public void done(){
        try {

            channels = get();
            view = new StartView("Radio Info");


            //Setup for buttons and add listeners
            for(Channel c : channels){
                view.setButton(c.getName(), c.getImage());
                view.setChannelButtonListener(e -> getData(c, false));
            }



        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        //Programdata ska laddas ner första gången en användare väljer att visa program från en kanal
        // och ska sedan automatiskt uppdateras med nytt data från servern en gång i timmen eller då
        // användaren manuellt väljer att uppdatera datat.  Om kanaldata redan visats av användaren
        // ska inget nytt data laddas ner vid kanalbyte utan det cashade datat användas.
    }

    private void getData(Channel c, boolean forceUpdate){
        //if(c.isPtmEmpty()){
        view.setAllChannelsButtonListener(e -> view.setShowChannelsPanel());
        view.setShowTableauPanel(c.getName(), c.getImage(), c.getTableau());

        if(c.isPtmEmpty()) {
            ProgramWorker worker = new ProgramWorker(c, view);
            //Update data
            worker.execute();
        } else if(forceUpdate){

        }
    }

}