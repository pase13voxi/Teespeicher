package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

/**
 * Created by paseb on 13.07.2017.
 * Class, that describes a NTea.
 */

public class NTea implements Serializable {

    //Attribute
    private UUID id;
    private String name;
    private SortOfTea sortOfTea;
    private ArrayList<NTemperature> temperature;
    private ArrayList<Time> coolDownTime;
    private ArrayList<Time> time;
    private Amount amount;
    private int color;
    private Date date;
    private String note;

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

    public ArrayList<NTemperature> getTemperature() {
        return temperature;
    }

    public void setTemperature(ArrayList<NTemperature> temperature) {
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

    //Konstruktor
    public NTea(UUID id, String name, SortOfTea sortOfTea, ArrayList<NTemperature> temperature, ArrayList<Time> coolDownTime,
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
    }

    static Comparator<NTea> TeaSortSortofTea = new Comparator<NTea>() {

        public int compare(NTea tea1, NTea tea2) {

            String sortno1 = tea1.getSortOfTea().getType();
            String sortno2 = tea2.getSortOfTea().getType();

            return sortno1.compareTo(sortno2);
        }
    };

    static Comparator<NTea> TeaSortDate = new Comparator<NTea>() {

        public int compare(NTea tea1, NTea tea2) {

            Date dateno1 = tea1.getDate();
            Date dateno2 = tea2.getDate();

            return dateno2.compareTo(dateno1);
        }
    };


}
