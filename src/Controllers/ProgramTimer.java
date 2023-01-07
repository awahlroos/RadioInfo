package Controllers;

import Models.Channel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ProgramTimer: Class to manage updates hourly. Called first time a user loads channel data.
 */
public class ProgramTimer extends Timer {

    private final Channel channel;
    private final ChannelWorker worker;

    public ProgramTimer (Channel channel, ChannelWorker worker){
        this.channel = channel;
        this.worker = worker;
    }

    public void startTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                worker.getData(channel, false, true);
            }
        }, 3600*1000, 3600*1000);
    }
}
