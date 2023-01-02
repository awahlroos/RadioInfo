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
    public NewProgramHandler(Channel channel, StartView view){
        this.channel = channel;
        this.view = view;
    }

    @Override
    protected ArrayList<Program> doInBackground() throws Exception {
        ProgramHandler handler = new ProgramHandler(channel);
        view.setViewCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        return handler.addFromAPI();
    }

    @Override
    protected void done() {
        try {
            view.setViewCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            programList = get();
            channel.updatePTM(programList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
