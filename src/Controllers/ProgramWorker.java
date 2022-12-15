package Controllers;

import Models.Channel;
import Models.GetChannels;
import Models.GetPrograms;
import Models.Program;
import Views.StartView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProgramWorker extends SwingWorker<ArrayList<Program>, Object> {

    ArrayList<Program> programs = new ArrayList<>();
    GetPrograms getPrograms;
    ProgramTimer timer;

    Channel channel;
    public ProgramWorker(Channel channel) {
        this.channel = channel;
        getPrograms = new GetPrograms(channel);
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * <p>
     * Note that this method is executed only once.
     *
     * <p>
     * Note: this method is executed in a background thread.
     *
     * @return the computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    protected ArrayList<Program> doInBackground() throws Exception {
        return getPrograms.getFromAPI();
    }

    @Override
    public void done(){
        try {
            programs = get();
            for(Program p : programs){
                System.out.println(p.getName());
                System.out.println(p.getStartTime());
                System.out.println(p.getEndTime());
                System.out.println("-----------------------");
            }
            timer = new ProgramTimer(channel);
            //Programdata ska laddas ner första gången en användare väljer att visa program från en kanal
            // och ska sedan automatiskt uppdateras med nytt data från servern en gång i timmen eller då
            // användaren manuellt väljer att uppdatera datat.  Om kanaldata redan visats av användaren
            // ska inget nytt data laddas ner vid kanalbyte utan det cashade datat användas.
            timer.startTimer();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
