package Controllers;

import Models.Channel;
import Models.GetChannels;
import Models.ProgramHandler;
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
    private ProgramController2 c2;
    private ProgramHandler worker;

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
                        c2 = new ProgramController2(c, view);
                        c2.getNewData();
                        getData(c, false, false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }

            view.setUpdateChannelsButtonListener(e -> {
                for (Channel cachedChannel : cachedChannels) {
                    try {
                        getData(cachedChannel, true, false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            view.setAllChannelsButtonListener(e -> view.setShowChannelsPanel());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void getData(Channel c, boolean forceUpdate, boolean autoUpdate) throws InterruptedException {


        if(view.getChannelsViewActive() && forceUpdate){
            view.setShowChannelsPanel();
        } else if((!view.getChannelsViewActive() && forceUpdate && !autoUpdate) ||
                (view.getChannelsViewActive() && !forceUpdate && !autoUpdate)){
            view.setShowTableauPanel(c.getName(), c.getImage(), c.getPTM());
        }

        if(c.isPtmEmpty()) {
            //New handler
            worker = new ProgramHandler(c);
            worker.execute();
            ProgramTimer timer = new ProgramTimer(c, this);
            timer.startTimer();

            //ProgramController2 controller2 = new ProgramController2(c, view);
            //controller2.getNewData();
            //TODO: Om en kanal failar att ladda in kommer detta ändå köras, man kan sätta lyssnare för att lösa detta

            cachedChannels.add(c);

        } else if (forceUpdate || autoUpdate){
            c.getPTM().resetProgramList();
            worker = new ProgramHandler(c);
            worker.execute();
            //c2.getNewData();
            //ProgramController2 controller2 = new ProgramController2(c, view);
        }
        c2.getNewData();

    }
}