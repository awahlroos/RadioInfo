package Controllers;

import Models.Channel;
import Models.GetChannels;
import Models.Program;
import Views.DetailsView;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ChannelWorker extends SwingWorker<ArrayList<Channel>, Object> {

    private GetChannels getChannels = new GetChannels();
    private StartView view;
    private ArrayList<Channel> cachedChannels = new ArrayList<>();
    private ProgramController2 programController;
    private ArrayList<Channel> channels;
    private MouseAdapter a;
    public ChannelWorker(){
        view = new StartView("Radio Info");
        this.execute();
    }

    @Override
    protected ArrayList<Channel> doInBackground() throws ParserConfigurationException, IOException, SAXException {
        return getChannels.getFromAPI();
    }

    @Override
    public void done(){
        view.setVisible();
        view.setButton("Hej", null);
        try {
            channels  = get();
            for(Channel c : channels){
                view.setButton(c.getName(), c.getImage());
                view.setChannelButtonListener(e -> {
                    getData(c, false, false);
                    a = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int rowIndex = view.getTableauRow(e);
                            Program program = c.getPTM().getProgramDetails(rowIndex);
                            try {
                                new DetailsView(program.getName(), program.getStartTime(), program.getEndTime(),
                                        program.getImage(), program.getDescription(), view.getFrameHolder(), view.getFrame());
                            } catch (MalformedURLException ex) {
                                throw new RuntimeException(ex);
                            }
                            /*try {
                                view.showDetails(program.getName(), program.getStartTime(), program.getEndTime(),
                                        program.getImage(), program.getDescription());
                            } catch (MalformedURLException ex) {
                                try {
                                    //If incorrect image display image not found
                                    view.showDetails(program.getName(), program.getStartTime(), program.getEndTime(),
                                            null, program.getDescription());
                                } catch (MalformedURLException exc) {
                                    view.showErrorDialog("Incorrect image URL for program");
                                }
                            }*/
                        }
                    };
                    view.setJTableOnClickListener(a);
                });
            }

            //view.setVisible();
            view.setUpdateChannelsButtonListener(e -> {

                /*for(int i = 0; i < cachedChannels.size(); i++){
                    try {
                        getData(cachedChannels.get(i), true, false);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }*/

                for (Channel cachedChannel : cachedChannels) {
                    getData(cachedChannel, true, false);
                }
            });
            view.setAllChannelsButtonListener(e -> view.setShowChannelsPanel());

        } catch (InterruptedException | ExecutionException e) {
            view.showErrorDialog("Error when fetching data");
        }
    }

    public void getData(Channel c, boolean forceUpdate, boolean autoUpdate){

        programController = new ProgramController2(c, view);

        if(!forceUpdate && !autoUpdate) {
            view.setShowTableauPanel(c.getName(),c.getPTM());
        }

        if(c.isPtmEmpty()) {
            programController.startWorker();
            ProgramTimer timer = new ProgramTimer(c, this);
            timer.startTimer();
            cachedChannels.add(c);

        } else if (forceUpdate || autoUpdate){
            c.getPTM().resetProgramList();
            programController.startWorker();
        }
    }
}