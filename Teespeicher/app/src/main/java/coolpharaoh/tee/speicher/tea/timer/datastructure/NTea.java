package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by paseb on 11.12.2016.
 */

public class NTea implements Serializable {

    //Attribute
    private String name;
    private String sortOfTea;
    private ArrayList<Temperature> temperature;
    private ArrayList<Time> time;
    private Amount amount;
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

    public ArrayList<Temperature> getTemperature() {
        return temperature;
    }

    public void setTemperature(ArrayList<Temperature> temperature) {
        this.temperature = temperature;
    }

    public ArrayList<Time> getTime() {
        return time;
    }

    public void setTime(ArrayList<Time> time) {
        this.time = time;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCurrentDate() {
        //getCurrentDate
        this.date = Calendar.getInstance().getTime();
    }

    //Konstruktor
    public NTea(String name, String sortOfTea, ArrayList<Temperature> temperature, ArrayList<Time> time, Amount amount) {
        this.name = name;
        this.sortOfTea = sortOfTea;
        this.temperature = temperature;
        this.time = time;
        this.amount = amount;
    }

    public static Comparator<NTea> TeaSortSortofTea = new Comparator<NTea>() {

        public int compare(NTea tea1, NTea tea2) {

            String sortno1 = tea1.getSortOfTea();
            String sortno2 = tea2.getSortOfTea();

            return sortno1.compareTo(sortno2);
        }
    };

    public static Comparator<NTea> TeaSortDate = new Comparator<NTea>() {

        public int compare(NTea tea1, NTea tea2) {

            Date dateno1 = tea1.getDate();
            Date dateno2 = tea2.getDate();

            return dateno2.compareTo(dateno1);
        }
    };


}
