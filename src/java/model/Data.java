package model;

import java.util.Calendar;

public class Data {
    Calendar c = Calendar.getInstance();

    String currentTime;
    
    public Data () {
        
    }
    
    public String getCurrentTime() {
        currentTime = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + " " + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR);
        return currentTime;
    }
    
}
