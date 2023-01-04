package Models;

import java.util.ArrayList;

public class Channel {
    private final String name;
    private final String id;
    private final String image;
    private final ProgramTableModel ptm;

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
