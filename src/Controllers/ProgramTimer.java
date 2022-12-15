package Controllers;

import Models.Channel;

import java.util.Timer;
import java.util.TimerTask;

public class ProgramTimer extends Timer {

    private Channel channel;

    public ProgramTimer (Channel channel){
        this.channel=channel;
    }

    //Synchronized function to update latest fetch
    public void startTimer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("hej");
            }
        }, 0, 1000);
    }

}
