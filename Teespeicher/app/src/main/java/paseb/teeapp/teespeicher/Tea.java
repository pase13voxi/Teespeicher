package paseb.teeapp.teespeicher;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by paseb on 05.02.2016.
 */
public class Tea implements Serializable {

    //Attribute
    private String name;
    private String sortOfTea;
    private int temperature;
    private String time;
    private int minutes;
    private int seconds;
    private int teelamass;
    private Date date;

    //Getter Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortOfTea() {
        return sortOfTea;
    }

    public void setSortOfTea(String sortOfTea) {
        this.sortOfTea = sortOfTea;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        getMinutesAndSeconds(time);
    }

    public int getTeelamass() {
        return teelamass;
    }

    public void setTeelamass(int teelamass) {
        this.teelamass = teelamass;
    }

    public Date getDate() {
        return date;
    }

    public void setCurrentDate() {
        //getCurrentDate
        this.date = Calendar.getInstance().getTime();
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    //Konstruktor
    public Tea(String name, String sortOfTea, int temperature, String time, int teelamass) {
        this.name = name;
        this.sortOfTea = sortOfTea;
        this.temperature = temperature;
        this.time = time;
        this.teelamass = teelamass;
        getMinutesAndSeconds(time);
    }

    //Programmingpart
    private void getMinutesAndSeconds(String time){
        if(time.equals("-")){
            minutes = 0;
            seconds = 0;
        }else {
            String[] split = time.split(":");
            minutes = Integer.parseInt(split[0]);
            if (split.length > 1) seconds = Integer.parseInt(split[1]);
            else seconds = 0;
        }
    }

    public static Comparator<Tea> TeaSortSortofTea = new Comparator<Tea>() {

        public int compare(Tea tea1, Tea tea2) {

            String sortno1 = tea1.getSortOfTea();
            String sortno2 = tea2.getSortOfTea();

            return sortno1.compareTo(sortno2);
        }
    };

    public static Comparator<Tea> TeaSortDate = new Comparator<Tea>() {

        public int compare(Tea tea1, Tea tea2) {

            Date dateno1 = tea1.getDate();
            Date dateno2 = tea2.getDate();

            return dateno2.compareTo(dateno1);
        }
    };


}