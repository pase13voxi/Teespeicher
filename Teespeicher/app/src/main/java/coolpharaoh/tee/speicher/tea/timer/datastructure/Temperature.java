package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by paseb on 17.12.2016.
 */

public abstract class Temperature implements Serializable {
    protected int temperature;

    public Temperature(int temperature){
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
}
