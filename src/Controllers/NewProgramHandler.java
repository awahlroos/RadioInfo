package Controllers;

import Models.Channel;
import Models.Program;
import Models.ProgramHandler;

import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NewProgramHandler extends SwingWorker<ArrayList<Program>,Void> {
    private Channel channel;
    private ArrayList<Program> programList = new ArrayList<>();
    public NewProgramHandler(Channel channel){
        this.channel = channel;
    }

    @Override
    protected ArrayList<Program> doInBackground() throws Exception {
        ProgramHandler handler = new ProgramHandler(channel);
        return handler.addFromAPI();
    }

    @Override
    protected void done() {
        try {
            programList = get();
            channel.updatePTM(programList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
