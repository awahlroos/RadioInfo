package Controllers;

import Models.Channel;
import Models.GetChannels;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChannelWorker extends SwingWorker<ArrayList<Channel>, Object>{

    private ArrayList<Channel> channels = new ArrayList<>();
    private GetChannels getChannels = new GetChannels();
    private StartView view;
    private ArrayList<Channel> cachedChannels = new ArrayList<>();

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
                view.setChannelButtonListener(e -> {
                    try {
                        getData(c, false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            view.setUpdateChannelsButtonListener(e -> {
                for (Channel cachedChannel : cachedChannels) {
                    try {
                        getData(cachedChannel, true);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                //For-loop cannot be enhanced in order to not iterate the actual arraylist and crash
                /*for(int i = 0; i < cachedChannels.size(); i++){
                    try {
                        getData(cachedChannels.get(i), true, view.getChannelsViewActive());
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }*/
            });

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

    public void getData(Channel c, boolean forceUpdate) throws InterruptedException {

        view.setAllChannelsButtonListener(e -> view.setShowChannelsPanel());
        if(view.getChannelsViewActive() && forceUpdate){
            view.setShowChannelsPanel();
        } else if(!view.getChannelsViewActive() && forceUpdate){

        } else {
            view.setShowTableauPanel(c.getName(), c.getImage(), c.getPTM());
        }

        if(c.isPtmEmpty()) {
            //New handler
            //ProgramWorker worker = new ProgramWorker(c, view);
            //worker.execute();
            ProgramTimer timer = new ProgramTimer(c, this);
            timer.startTimer();

            ProgramController2 controller2 = new ProgramController2(c, view);
            controller2.getNewData();
            //TODO: Om en kanal failar att ladda in kommer detta ändå köras, man kan sätta lyssnare för att lösa detta
            cachedChannels.add(c);

        } else if(forceUpdate){
            c.getPTM().resetProgramList();
            ProgramController2 controller2 = new ProgramController2(c, view);
            controller2.getNewData();
        }
    }
}