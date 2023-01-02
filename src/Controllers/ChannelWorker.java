package Controllers;

import Models.Channel;
import Models.GetChannels;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChannelWorker extends SwingWorker<ArrayList<Channel>, Object>{

    private GetChannels getChannels = new GetChannels();
    private StartView view;
    private ArrayList<Channel> cachedChannels = new ArrayList<>();
    private ProgramController2 programController;
    public ChannelWorker(){}

    @Override
    protected ArrayList<Channel> doInBackground() throws ParserConfigurationException, IOException, SAXException {
        System.out.println("as");
        return getChannels.getFromAPI();
    }

    @Override
    public void done(){
        try {

            ArrayList<Channel> channels = get();
            view = new StartView("Radio Info");
            //view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            //Setup for buttons and add listeners
            for(Channel c : channels){
                view.setButton(c.getName(), c.getImage());
                view.setChannelButtonListener(e -> {
                    try {
                        programController = new ProgramController2(c, view);
                        programController.getNewData();
                        programController.startWorker();

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
        //view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void getData(Channel c, boolean forceUpdate, boolean autoUpdate) throws InterruptedException {

        if(view.getChannelsViewActive() && forceUpdate){
            view.setShowChannelsPanel();
        } else if((!view.getChannelsViewActive() && forceUpdate && !autoUpdate) ||
                (view.getChannelsViewActive() && !forceUpdate && !autoUpdate)){
            view.setShowTableauPanel(c.getName(), c.getImage(), c.getPTM());
        }

        if(c.isPtmEmpty()) {
            ProgramTimer timer = new ProgramTimer(c, this);
            timer.startTimer();
            cachedChannels.add(c);
        } else if (forceUpdate || autoUpdate){
            c.getPTM().resetProgramList();
            programController = new ProgramController2(c, view);
            programController.startWorker();
        }
        programController.getNewData();
    }
}