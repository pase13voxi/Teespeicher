package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.util.ArrayList;

/**
 * Created by paseb on 17.12.2016.
 */

public class TemperatureCelsius extends Temperature {
    public TemperatureCelsius(int temperature) {
        super(temperature);
    }

    @Override
    public int getCelsius() {
        return temperature;
    }

    @Override
    public int getFahrenheit() {
        return celsiusToFahrenheit(temperature);
    }
}
