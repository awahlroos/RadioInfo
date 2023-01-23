package Controllers;

import Models.Channel;
import Models.ChannelHandler;
import Models.Program;
import Views.DetailsView;
import Views.StartView;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * ChannelWorker: Controller class that is responsible for initializing an API fetch.
 * Presents the fetched channels as buttons in a view.
 */
public class ChannelWorker extends SwingWorker<ArrayList<Channel>, Object> {

    private final ChannelHandler channelHandler = new ChannelHandler();
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
        return channelHandler.getFromAPI();
    }

    /**
     * done(): Called after doInBackground is finished.
     * Gets the retrieved channels from doInBackground. Iterates over the channels and add them to a clickable
     * button presented in the GUI.
     */
    @Override
    protected void done(){
        try {
            ArrayList<Channel> channels = get();
            for(Channel c : channels){
                Image img;
                String imgStr = c.getImage();

                try {
                    img = new ImageIcon(new URL(imgStr), "image").getImage();
                } catch (MalformedURLException e) {
                    URL imageUrl = this.getClass().getResource("/Assets/imageNotFound.png");
                    img = new ImageIcon(imageUrl, "image").getImage();
                }

                view.setButton(c.getName(), img);
                view.setChannelButtonListener(e -> {
                    getData(c, false, false);

                    //Use MouseAdapter to set on click listeners to get detailed program information.
                    a = new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            int rowIndex = view.getTableauRow(e);
                            Program program = c.getPTM().getProgramDetails(rowIndex);
                            Image img;

                            try {
                                img = new ImageIcon(new URL(program.getImage()), "image").getImage();
                            } catch (MalformedURLException ex) {
                                img = new ImageIcon(this.getClass().getResource("/Assets/imageNotFound.png"),
                                        "image").getImage();
                            }
                            new DetailsView(program.getName(), program.getStartTime(), program.getEndTime(),
                                        img, program.getDescription(), view.getFrameHolder(), view.getFrame());
                        }
                    };
                    view.setJTableOnClickListener(a);
                });
            }
            view.setVisible();
            //Set listener for manually updating data.
            view.setUpdateChannelsButtonListener(e -> {
                for(int i = 0; i < cachedChannels.size(); i++){
                    getData(cachedChannels.get(i), true, false);
                }
            });
            //Set listener for showing all channels data.
            view.setAllChannelsButtonListener(e -> view.setShowChannelsPanel());

        } catch (InterruptedException | ExecutionException e) {
            view.showErrorDialog("Error when fetching data");
        }
    }

    /**
     *  getData(): Fetch new tableau data if manually updating or after an hour of first loading the data, present
     *  the previously fetched data otherwise.
     */
    public void getData(Channel c, boolean forceUpdate, boolean autoUpdate){
        ProgramController programController = new ProgramController(c, view);

        //If a user clicks on a channel
        if(!forceUpdate && !autoUpdate) {
            view.setShowTableauPanel(c.getName(),c.getPTM());
        }

        //If the TableModel is empty (no programs) and the channel is not yet visited
        if(c.isPtmEmpty() && !c.getVisited()) {
            c.setVisited(true);
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