package coolpharaoh.tee.speicher.tea.timer.datastructure;

/**
 * Created by paseb on 13.07.2017.
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
