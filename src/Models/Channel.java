package Models;

import java.util.ArrayList;

public class Channel {
    //Kommer ha ett objekt av ProgramListModel som uppdateras en gång i timmen mha timer?
    private String name;
    private String id;
    private String image;
    private ProgramTableModel ptm;

    public Channel(String name, String id, String image){
        this.image = image;
        this.name = name;
        this.id = id;
        ptm = new ProgramTableModel();
    }
    public String getName() {
        return name;
    }

    public String getId(){
        return id;
    }

    public String getImage() {
        return image;
    }

    public boolean isPtmEmpty(){
        return ptm.getRowCount() == 0;
    }

    public ProgramTableModel getPTM(){
        return ptm;
    }
    public void updatePTM(ArrayList<Program> programList){
        ptm.setProgramList(programList);
    }
}
