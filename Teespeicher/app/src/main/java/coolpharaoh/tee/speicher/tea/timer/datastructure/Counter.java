package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by paseb on 16.02.2018.
 */
//ToDo noch nicht fertig
public class Counter implements Serializable {
    int day, week, month;
    long overall;
    Date dayDate, weekDate, monthDate;

    public Counter(){
        day = 0;
        week = 0;
        month = 0;
        overall = 0;
        dayDate = Calendar.getInstance().getTime();
        weekDate = Calendar.getInstance().getTime();
        monthDate = Calendar.getInstance().getTime();
    }

    public int getDay(){
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getWeek(){
        return week;
    }

    public void setWeek(int week){
        this.week = week;
    }

    public int getMonth(){
        return month;
    }

    public void setMonth(int week){
        this.week = week;
    }

    public long getOverall(){
        return overall;
    }

    public void setOverall(long overall){
        this.overall = overall;
    }

    public void count(){
        Date currentDate = Calendar.getInstance().getTime();
        refreshDay(currentDate);
        refreshWeek(currentDate);
        refreshMonth(currentDate);
        this.day++;
        this.week++;
        this.month++;
        this.overall++;
    }

    public void refresh(){
        Date currentDate = Calendar.getInstance().getTime();
        refreshDay(currentDate);
        refreshWeek(currentDate);
        refreshMonth(currentDate);
    }

    private void refreshDay(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(dayDate);
        int countDay = cal.get(Calendar.DAY_OF_MONTH);
        int countMonth = cal.get(Calendar.MONTH);
        int countYear = cal.get(Calendar.YEAR);
        if(currentDay!=countDay || currentMonth!=countMonth || currentYear!=countYear){
            day = 0;
            dayDate = currentDate;
        }
    }

    private void refreshWeek(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(weekDate);
        int countWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int countYear = cal.get(Calendar.YEAR);
        if(currentWeek!=countWeek || currentYear!=countYear){
            week = 0;
            weekDate = currentDate;
        }
    }

    private void refreshMonth(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(monthDate);
        int countMonth = cal.get(Calendar.MONTH);
        int countYear = cal.get(Calendar.YEAR);
        if(currentMonth!=countMonth || currentYear!=countYear){
            month = 0;
            monthDate = currentDate;
        }
    }
}
