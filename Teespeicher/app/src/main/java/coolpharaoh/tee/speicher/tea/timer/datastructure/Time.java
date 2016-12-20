package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;

/**
 * Created by paseb on 17.12.2016.
 */

public class Time implements Serializable {
    String time;
    int minutes;
    int seconds;

    public Time(String time){
        this.time = time;
        getMinutesAndSeconds(time);
    }

    public String getTime() {
        return time;
    }

    public int getMinutes(){
        return minutes;
    }

    public int getSeconds(){
        return seconds;
    }

    private void getMinutesAndSeconds(String time){
        if (time.equals("-")) {
            minutes = 0;
            seconds = 0;
        } else {
            String[] split = time.split(":");
            minutes = Integer.parseInt(split[0]);
            if (split.length > 1) seconds = Integer.parseInt(split[1]) ;
            else seconds = 0;
        }
    }
}
