package Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Program {

    private String name;
    private LocalDateTime startTime, endTime;
    public Program(String name, LocalDateTime startTime, LocalDateTime endTime){
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName(){
        return this.name;
    }
    public LocalDateTime getStartTime(){
        return this.startTime;
    }
    public LocalDateTime getEndTime(){
        return this.endTime;
    }

}
