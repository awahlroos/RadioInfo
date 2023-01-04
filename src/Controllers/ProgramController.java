package Controllers;

import Models.Channel;
import Views.StartView;

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
