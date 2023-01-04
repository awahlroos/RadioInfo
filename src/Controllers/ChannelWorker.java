package Controllers;

import Models.Channel;
import Models.GetChannels;
import Models.Program;
import Views.DetailsView;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * ChannelWorker: Controller class that is responsible for initializing an API fetch.
 * Presents the fetched channels as buttons in a view.
 */
public class ChannelWorker extends SwingWorker<ArrayList<Channel>, Object> {

    private final GetChannels getChannels = new GetChannels();
    private final StartView view;
    private final ArrayList<Channel> cachedChannels = new ArrayList<>();
    private MouseAdapter a;
    public ChannelWorker(){
        view = new StartView("Radio Info");
        this.execute();
    }

    /**
     * doInBackground(): Executed on a background thread. Calls a model to fetch from the API.
     */
    @Override
    protected ArrayList<Channel> doInBackground() throws ParserConfigurationException, IOException, SAXException {
        return getChannels.getFromAPI();
    }

    /**
     * done(): Called after doInBackground is finished.
     *
     */
    @Override
    public void done(){
        try {
            ArrayList<Channel> channels = get();
            for(Channel c : channels){
                view.setButton(c.getName(), c.getImage());
                view.setChannelButtonListener(e -> {
                    getData(c, false, false);
                    //Use MouseAdapter to set on click listeners.
                    a = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int rowIndex = view.getTableauRow(e);
                            Program program = c.getPTM().getProgramDetails(rowIndex);
                            try {
                                new DetailsView(program.getName(), program.getStartTime(), program.getEndTime(),
                                        program.getImage(), program.getDescription(), view.getFrameHolder(), view.getFrame());
                            } catch (MalformedURLException ex) {
                                try {
                                    //If image URL not found, set to null to display default image
                                    new DetailsView(program.getName(), program.getStartTime(), program.getEndTime(),
                                            null, program.getDescription(), view.getFrameHolder(), view.getFrame());
                                } catch (MalformedURLException exc) {
                                    view.showErrorDialog("Incorrect image URL for program");
                                }
                            }
                        }
                    };
                    view.setJTableOnClickListener(a);
                });
            }
            view.setVisible();
            view.setUpdateChannelsButtonListener(e -> {
                for(int i = 0; i < cachedChannels.size(); i++){
                    getData(cachedChannels.get(i), true, false);
                }
            });
            view.setAllChannelsButtonListener(e -> view.setShowChannelsPanel());

        } catch (InterruptedException | ExecutionException e) {
            view.showErrorDialog("Error when fetching data");
        }
    }

    public void getData(Channel c, boolean forceUpdate, boolean autoUpdate){
        ProgramController programController = new ProgramController(c, view);

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