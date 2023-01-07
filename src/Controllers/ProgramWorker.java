package Controllers;

import Models.Channel;
import Models.Program;
import Models.ProgramHandler;
import Views.StartView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * ProgramWorker: Class responsible for fetching data from API using a model class, then updating ProgramTableModel.
 */
public class ProgramWorker extends SwingWorker<ArrayList<Program>,Void> {
    private final Channel channel;
    private final StartView view;

    public ProgramWorker(Channel channel, StartView view){
        this.channel = channel;
        this.view = view;
    }

    @Override
    protected ArrayList<Program> doInBackground() throws Exception {
        ProgramHandler handler = new ProgramHandler(channel.getId());
        return handler.addFromAPI();
    }

    @Override
    protected void done() {
        try {
            ArrayList<Program> programs = get();
            if(programs.size()==0){
                programs.add(new Program("Inga program att visa", "-", "-", "-", null));
            }
            channel.updatePTM(programs);
        } catch (InterruptedException | ExecutionException e) {
            view.showErrorDialog("Error when fetching data for channel " + channel.getName());
        }
    }
}
