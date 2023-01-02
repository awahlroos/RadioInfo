package Controllers;

import Models.Channel;
import Models.GetChannels;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChannelWorker extends SwingWorker<Void, Object> implements ChannelListener {

    private GetChannels getChannels = new GetChannels();
    private StartView view;
    private ArrayList<Channel> cachedChannels = new ArrayList<>();
    private ProgramController2 programController;
    public ChannelWorker(){
        getChannels.setChangeListener(this);
        view = new StartView("Radio Info");
        this.execute();
    }

    @Override
    protected Void doInBackground() throws ParserConfigurationException, IOException, SAXException {
        getChannels.getFromAPI();
        return null;
    }

    @Override
    public void done(){
            //ArrayList<Channel> channels = get();

            //view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            //Setup for buttons and add listeners
            //for(Channel c : channels){

            //}

            view.setUpdateChannelsButtonListener(e -> {
                for(int i=0; i < cachedChannels.size(); i++){
                    try {
                        getData(cachedChannels.get(i), true, false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                //TODO: Maybe use this
                /*for (Channel cachedChannel : cachedChannels) {
                    try {
                        getData(cachedChannel, true, false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }*/
            });
            view.setAllChannelsButtonListener(e -> view.setShowChannelsPanel());


        //view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void getData(Channel c, boolean forceUpdate, boolean autoUpdate) throws InterruptedException {

        if(!forceUpdate && !autoUpdate) {
            view.setShowTableauPanel(c.getName(),c.getImage(),c.getPTM());
        }
        //if(view.getChannelsViewActive() && (forceUpdate || autoUpdate)){
        //    view.setShowChannelsPanel();
        //} else if(/*(!view.getChannelsViewActive() && forceUpdate && !autoUpdate) ||*/
        //        (view.getChannelsViewActive() && !forceUpdate && !autoUpdate)){
        //    view.setShowTableauPanel(c.getName(), c.getImage(), c.getPTM());
        //}

        if(c.isPtmEmpty()) {
            ProgramTimer timer = new ProgramTimer(c, this);
            timer.startTimer();
            //programController = new ProgramController2(c, view);
            programController.getNewData();
            programController.startWorker();
            cachedChannels.add(c);

        } else if (forceUpdate || autoUpdate){
            c.getPTM().resetProgramList();
            programController = new ProgramController2(c, view);
            programController.startWorker();
        }
        //programController.getNewData();
    }

    @Override
    public void hasUpdated() {
        Channel c = getChannels.getLastAdded();
        view.setButton(c.getName(), c.getImage());

        view.setChannelButtonListener(e -> {
            try {
                programController = new ProgramController2(c, view);
                //Hamnar i loop
                getData(c, false, false);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}