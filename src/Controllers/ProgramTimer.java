package Controllers;

import java.util.Timer;
import java.util.TimerTask;

public class ProgramTimer extends Timer {

    public ProgramTimer (){}

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
