package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.util.ArrayList;

/**
 * Created by paseb on 13.07.2017.
 */

public class NTemperatureCelsius extends NTemperature {
    public NTemperatureCelsius(int temperature) {
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
