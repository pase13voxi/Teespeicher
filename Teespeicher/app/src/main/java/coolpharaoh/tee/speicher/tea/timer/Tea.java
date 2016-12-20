package coolpharaoh.tee.speicher.tea.timer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by paseb on 05.02.2016.
 */
public class Tea implements Serializable {
    private String name;
    private String sortOfTea;
    private ArrayList<Integer> temperature;
    private ArrayList<String> time;
    private ArrayList<Integer> minutes;
    private ArrayList<Integer> seconds;
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

    public ArrayList<Integer> getTemperature() {
        return temperature;
    }

    public void setTemperature(ArrayList<Integer> temperature) {
        this.temperature = temperature;
    }

    public ArrayList<String> getTime() {
        return time;
    }

    public void setTime(ArrayList<String> time) {
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

    public ArrayList<Integer> getMinutes() {
        return minutes;
    }

    public void setMinutes(ArrayList<Integer> minutes) {
        this.minutes = minutes;
    }

    public ArrayList<Integer> getSeconds() {
        return seconds;
    }

    public void setSeconds(ArrayList<Integer> seconds) {
        this.seconds = seconds;
    }

    //Konstruktor
    public Tea(String name, String sortOfTea, ArrayList<Integer> temperature, ArrayList<String> time, int teelamass) {
        this.name = name;
        this.sortOfTea = sortOfTea;
        this.temperature = temperature;
        this.time = time;
        this.teelamass = teelamass;
        getMinutesAndSeconds(time);
    }

    //Programmingpart
    private void getMinutesAndSeconds(ArrayList<String> time){
        minutes = new ArrayList<>();
        seconds = new ArrayList<>();
        for(int i=0; i<time.size(); i++) {
            if (time.get(i).equals("-")) {
                minutes.add(0);
                seconds.add(0);
            } else {
                String[] split = time.get(i).split(":");
                minutes.add(Integer.parseInt(split[0]));
                if (split.length > 1) seconds.add(Integer.parseInt(split[1]));
                else seconds.add(0);
            }
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