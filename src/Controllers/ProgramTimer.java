package Controllers;

import Models.Channel;
import java.awt.event.ActionListener;

/**
 * ProgramTimer: Class to manage updates hourly. Called first time a user loads channel data.
 */
public class ProgramTimer {

    private final Channel channel;
    private final ChannelWorker worker;

    public ProgramTimer (Channel channel, ChannelWorker worker){
        this.channel = channel;
        this.worker = worker;
    }

    /**
     * startTimer(): Method to manage periodic update of programs. Calls getData hourly which is responsible for
     * initiating the method to make API-call.
     */
    public void startTimer(){
        ActionListener updateData = evt -> worker.getData(channel, false, true);

        javax.swing.Timer timer = new javax.swing.Timer(3600*1000, updateData);
        timer.setRepeats(true);
        timer.start();
    }
}
