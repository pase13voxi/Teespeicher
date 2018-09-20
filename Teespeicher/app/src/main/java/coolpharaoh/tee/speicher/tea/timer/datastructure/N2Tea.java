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

public class N2Tea implements Serializable {

    //Attribute
    private UUID id;
    private String name;
    private SortOfTea sortOfTea;
    private ArrayList<Temperature> temperature;
    private ArrayList<Time> coolDownTime;
    private ArrayList<Time> time;
    private Amount amount;
    private Coloring coloring;
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

    public Coloring getColoring(){
        return coloring;
    }

    public void setColoring(Coloring coloring){
        this.coloring = coloring;
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
    public N2Tea(UUID id, String name, SortOfTea sortOfTea, ArrayList<Temperature> temperature, ArrayList<Time> coolDownTime,
                 ArrayList<Time> time, Amount amount, Coloring coloring) {
        this.id = id;
        this.name = name;
        this.sortOfTea = sortOfTea;
        this.temperature = temperature;
        this.coolDownTime = coolDownTime;
        this.time = time;
        this.amount = amount;
        this.coloring = coloring;
        setCurrentDate();
        this.note = "";
        this.counter = new Counter();
    }

    public static Comparator<N2Tea> TeaSortTea = new Comparator<N2Tea>() {

        public int compare(N2Tea tea1, N2Tea tea2) {

            String sortno1 = tea1.getName().toLowerCase();
            String sortno2 = tea2.getName().toLowerCase();

            return sortno1.compareTo(sortno2);
        }
    };

    public static Comparator<N2Tea> TeaSortSortofTea = new Comparator<N2Tea>() {

        public int compare(N2Tea tea1, N2Tea tea2) {

            String sortno1 = tea1.getSortOfTea().getType();
            String sortno2 = tea2.getSortOfTea().getType();

            return sortno1.compareTo(sortno2);
        }
    };

    public static Comparator<N2Tea> TeaSortDate = new Comparator<N2Tea>() {

        public int compare(N2Tea tea1, N2Tea tea2) {

            Date dateno1 = tea1.getDate();
            Date dateno2 = tea2.getDate();

            return dateno2.compareTo(dateno1);
        }
    };

    public static Comparator<N2Tea> TeaSortDrinkingBehaviorOverall = new Comparator<N2Tea>() {

        public int compare(N2Tea tea1, N2Tea tea2) {

            Long counterno1 = tea1.getCounter().getOverall();
            Long counterno2 = tea2.getCounter().getOverall();

            return counterno2.compareTo(counterno1);
        }
    };

    public static Comparator<N2Tea> TeaSortDrinkingBehaviorMonth = new Comparator<N2Tea>() {

        public int compare(N2Tea tea1, N2Tea tea2) {

            Integer counterno1 = tea1.getCounter().getMonth();
            Integer counterno2 = tea2.getCounter().getMonth();

            return counterno2.compareTo(counterno1);
        }
    };

    public static Comparator<N2Tea> TeaSortDrinkingBehaviorWeek = new Comparator<N2Tea>() {

        public int compare(N2Tea tea1, N2Tea tea2) {

            Integer counterno1 = tea1.getCounter().getWeek();
            Integer counterno2 = tea2.getCounter().getWeek();

            return counterno2.compareTo(counterno1);
        }
    };

    public static Comparator<N2Tea> TeaSortDrinkingBehaviorToday = new Comparator<N2Tea>() {

        public int compare(N2Tea tea1, N2Tea tea2) {

            Integer counterno1 = tea1.getCounter().getDay();
            Integer counterno2 = tea2.getCounter().getDay();

            return counterno2.compareTo(counterno1);
        }
    };
}
