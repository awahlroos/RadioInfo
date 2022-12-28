package Models;

public class Program {

    private String name, startTime, endTime, description, image;
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
