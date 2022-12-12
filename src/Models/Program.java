package Models;

public class Program {

    private String name, startTime, endTime;
    public Program(String name, String startTime, String endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
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

}
