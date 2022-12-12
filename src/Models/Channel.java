package Models;

import java.util.ArrayList;

public class Channel {
    //Kommer ha ett objekt av ProgramListModel som uppdateras en gång i timmen mha timer?
    private String name;
    //Image
    private String id;



    private String image;
    public Channel(String name, String id, String image){
        this.image = image;
        this.name = name;
        this.id = id;
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
}
