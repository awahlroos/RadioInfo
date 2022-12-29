package Controllers;

import Models.Channel;

import java.util.Timer;
import java.util.TimerTask;

public class ProgramTimer extends Timer {

    private Channel channel;
    ChannelWorker worker;

    public ProgramTimer (Channel channel, ChannelWorker worker){
        this.channel = channel;
        this.worker = worker;
    }

    //Synchronized function to update latest fetch
    public void startTimer(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    worker.getData(channel, false, true );
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 30*1000, 30*1000);
    }


}
