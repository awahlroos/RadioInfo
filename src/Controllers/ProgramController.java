package Controllers;

import Models.Channel;
import Views.StartView;

/**
 * ProgramController: Class to initiate and execute a SwingWorker for fetching programs.
 * Only purpose for this class is to break dependency from ChannelWorker.
 */
public class ProgramController {
    private final Channel channel;
    private final StartView view;

    public ProgramController(Channel channel, StartView view){
        this.channel = channel;
        this.view = view;
    }

    public void startWorker(){
        ProgramWorker worker = new ProgramWorker(channel, view);
        worker.execute();
    }
}
