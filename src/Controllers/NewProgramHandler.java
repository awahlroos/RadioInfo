package Controllers;

import Models.Channel;
import Models.Program;
import Models.ProgramHandler;
import Views.StartView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NewProgramHandler extends SwingWorker<ArrayList<Program>,Void> {
    private Channel channel;
    private StartView view;
    private ArrayList<Program> programList = new ArrayList<>();
    private ProgramHandler handler;
    public NewProgramHandler(Channel channel, StartView view){
        this.channel = channel;
        this.view = view;
    }

    @Override
    protected ArrayList<Program> doInBackground() throws Exception {
        handler = new ProgramHandler(channel);
        return handler.addFromAPI();
    }

    @Override
    protected void done() {
        try {
            channel.updatePTM(get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
