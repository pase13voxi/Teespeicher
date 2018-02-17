package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

/**
 * Created by paseb on 13.07.2017.
 * Class, that describes a Tea.
 */

public class Tea implements Serializable {

    //Attribute
    private UUID id;
    private String name;
    private SortOfTea sortOfTea;
    private ArrayList<Temperature> temperature;
    private ArrayList<Time> coolDownTime;
    private ArrayList<Time> time;
    private Amount amount;
    private int color;
    private Date date;
    private String note;
    private Counter counter;

    //Getter Setter
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SortOfTea getSortOfTea() {
        return sortOfTea;
    }

    public void setSortOfTea(SortOfTea sortOfTea) {
        this.sortOfTea = sortOfTea;
    }

    public ArrayList<Temperature> getTemperature() {
        return temperature;
    }

    public void setTemperature(ArrayList<Temperature> temperature) {
        this.temperature = temperature;
    }

    public ArrayList<Time> getCoolDownTime(){
        return coolDownTime;
    }

    public void setCoolDownTime(ArrayList<Time> coolDownTime){
        this.coolDownTime = coolDownTime;
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

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
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

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    //Konstruktor
    public Tea(UUID id, String name, SortOfTea sortOfTea, ArrayList<Temperature> temperature, ArrayList<Time> coolDownTime,
                ArrayList<Time> time, Amount amount, int color) {
        this.id = id;
        this.name = name;
        this.sortOfTea = sortOfTea;
        this.temperature = temperature;
        this.coolDownTime = coolDownTime;
        this.time = time;
        this.amount = amount;
        this.color = color;
        setCurrentDate();
        this.note = "";
        this.counter = new Counter();
    }

    static Comparator<Tea> TeaSortTea = new Comparator<Tea>() {

        public int compare(Tea tea1, Tea tea2) {

            String sortno1 = tea1.getName().toLowerCase();
            String sortno2 = tea2.getName().toLowerCase();

            return sortno1.compareTo(sortno2);
        }
    };

    static Comparator<Tea> TeaSortSortofTea = new Comparator<Tea>() {

        public int compare(Tea tea1, Tea tea2) {

            String sortno1 = tea1.getSortOfTea().getType();
            String sortno2 = tea2.getSortOfTea().getType();

            return sortno1.compareTo(sortno2);
        }
    };

    static Comparator<Tea> TeaSortDate = new Comparator<Tea>() {

        public int compare(Tea tea1, Tea tea2) {

            Date dateno1 = tea1.getDate();
            Date dateno2 = tea2.getDate();

            return dateno2.compareTo(dateno1);
        }
    };


}
