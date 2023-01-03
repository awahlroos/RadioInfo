package Controllers;

import Models.Channel;
import Models.Program;
import Models.ProgramTableModel;
import Views.StartView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

public class ProgramController2 {
    private Channel channel;
    private StartView view;
    private NewProgramHandler worker;

    public ProgramController2(Channel channel, StartView view){
        this.channel = channel;
        this.view = view;
    }

    public void startWorker(){
        worker = new NewProgramHandler(channel, view);
        worker.execute();
    }
}
