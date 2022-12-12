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

    public ProgramWorker(Channel channel) {
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
