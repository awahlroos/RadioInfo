package Controllers;

import Models.Channel;
import Models.GetChannels;
import Models.Program;
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
        try {
            channels  = get();
            for(Channel c : channels){
                view.setButton(c.getName(), c.getImage());
                view.setChannelButtonListener(e -> {
                    try {
                        getData(c, false, false);
                        a = new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                int rowIndex = view.getTableuRow(e);
                                Program program = c.getPTM().getProgramDetails(rowIndex);
                                try {
                                    view.showDetails(program.getName(), program.getStartTime(), program.getEndTime(),
                                            program.getImage(), program.getDescription());
                                } catch (MalformedURLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        };
                        view.setJTableOnClickListener(a);

                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            view.setUpdateChannelsButtonListener(e -> {
                for(int i = 0; i < cachedChannels.size(); i++){
                    try {
                        getData(cachedChannels.get(i), true, false);
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

        programController = new ProgramController2(c, view);

        if(!forceUpdate && !autoUpdate) {
            view.setShowTableauPanel(c.getName(),c.getImage(),c.getPTM());
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