package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;

/**
 * Created by paseb on 13.07.2017.
 */

public abstract class NTemperature implements Serializable {
    protected int temperature;

    public NTemperature(int temperature){
        this.temperature = temperature;
    }

    abstract public int getCelsius();
    abstract public int getFahrenheit();
    public static int celsiusToFahrenheit(int celsius){
        if(celsius==-500){
            return -500;
        }else {
            float tmp = (float) (9.0 / 5.0 * celsius + 32.0);
            return Math.round(tmp);
        }
    };
    public static int fahrenheitToCelsius(int fahrenheit){
        if(fahrenheit == -500){
            return -500;
        }else {
            float tmp = (float) ((5.0 / 9.0) * (fahrenheit - 32.0));
            return Math.round(tmp);
        }
    };
    public static String celsiusToSteepingTime(int celsius){
        float tmpTime = (100 - (float)celsius) / 2;
        int minute = (int)tmpTime;
        int sek = (int)((tmpTime - ((float) minute)) * 60);
        String timeText = minute + ":" + sek;
        return timeText;
    }
}