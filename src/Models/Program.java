package Models;

/**
 * Program: Class used for storing programs as objects.
 */
public class Program {

    private final String name;
    private final String startTime;
    private final String endTime;
    private final String description;
    private final String image;
    public Program(String name, String startTime, String endTime, String description, String image){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.image = image;
    }

    public String getName(){
        return this.name;
    }
    public String getStartTime(){
        return this.startTime;
    }
    public String getEndTime(){
        return this.endTime;
    }
    public String getDescription(){
        return this.description;
    }
    public String getImage() {
        return image;
    }
}
