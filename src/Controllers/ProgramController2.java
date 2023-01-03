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
    private ProgramTableModel programs;
    private MouseAdapter adapter;
    private NewProgramHandler worker;

    public ProgramController2(Channel channel, StartView view){
        this.channel = channel;
        this.view = view;
        programs = channel.getPTM();
    }

    public void initMouseHandler(MouseAdapter a){
        a = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = view.getTableuRow(e);
                Program program = programs.getProgramDetails(rowIndex);
                try {
                    System.out.println("showDetails");
                    view.showDetails(program.getName(), program.getStartTime(), program.getEndTime(),
                            program.getImage(), program.getDescription());
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        view.setJTableOnClickListener(a);
    }

    public void startWorker(){
        worker = new NewProgramHandler(channel, view);
        worker.execute();
    }
}
