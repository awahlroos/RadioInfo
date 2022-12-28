package Controllers;

import Models.*;
import Views.StartView;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProgramWorker extends SwingWorker<ArrayList<Program>, Object>{

    ArrayList<Program> programs = new ArrayList<>();
    GetPrograms getPrograms;
    ProgramTimer timer;
    Channel channel;
    StartView view;
    public ProgramWorker(Channel channel, StartView view) {
        this.channel = channel;
        this.view = view;
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
        //TODO: kanske ändra/flytta den här logiken
        //If the model holding programmes is empty, fetch from api
        //if(channel.isPtmEmpty()) {
            return getPrograms.addFromAPI();
        //}
        //return programs;
        //}
        //If the model holding programmes is not empty, use old data
        //return programs;
    }

    @Override
    public void done(){
        //try {
        try {
            //TODO: Ska uppdatera PTM på EDT här -> gör controller till model och använd publish/process


            //TODO: Använd denna variabel istället för PTM
            programs = get();
            //channel.updatePTM(get());

            MouseAdapter adapter = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int rowIndex = view.getTableuRow(e);
                    Program program = programs.get(rowIndex);
                    //Program program = ptm.getProgramDetails(rowIndex);
                    try {
                        view.showDetails(program.getName(), program.getStartTime(), program.getEndTime(),
                                program.getImage(), program.getDescription());
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            };
            view.setJTableOnClickListener(adapter);

            //ptm.getProgramDetails()


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

            /*for(Program p : programs){
                channel.updatePTM(p);
                System.out.println(p.getName());
                System.out.println(p.getStartTime());
                System.out.println(p.getEndTime());
                System.out.println("-----------------------");
            }*/
            timer = new ProgramTimer(channel);
            //Programdata ska laddas ner första gången en användare väljer att visa program från en kanal
            // och ska sedan automatiskt uppdateras med nytt data från servern en gång i timmen eller då
            // användaren manuellt väljer att uppdatera datat.  Om kanaldata redan visats av användaren
            // ska inget nytt data laddas ner vid kanalbyte utan det cashade datat användas.
            timer.startTimer();
        //} catch (InterruptedException e) {
            /*throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }*/
    }

}
