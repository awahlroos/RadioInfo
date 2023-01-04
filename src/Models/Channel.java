package Models;

import java.util.ArrayList;

/**
 * Channel: Class used for storing channels as objects.
 */
public class Channel {
    private final String name;
    private final String id;
    private final String image;
    private final ProgramTableModel ptm;
    private boolean isVisited;

    public Channel(String name, String id, String image){
        this.image = image;
        this.name = name;
        this.id = id;
        isVisited = false;
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

    public boolean getVisited(){
        return isVisited;
    }

    public void setVisited(boolean isVisited){
        this.isVisited = isVisited;
    }
}
