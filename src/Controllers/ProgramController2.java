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

    public ProgramController2(Channel channel, StartView view){
        this.channel = channel;
        this.view = view;
    }

    public void getNewData(){

        ProgramTableModel programs = channel.getPTM();
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = view.getTableuRow(e);
                Program program = programs.getProgramDetails(rowIndex);
                try {
                    view.showDetails(program.getName(), program.getStartTime(), program.getEndTime(),
                            program.getImage(), program.getDescription());
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        view.setJTableOnClickListener(adapter);
    }

    public void startWorker(){
        NewProgramHandler worker = new NewProgramHandler(channel, view);
        worker.execute();
    }
}
