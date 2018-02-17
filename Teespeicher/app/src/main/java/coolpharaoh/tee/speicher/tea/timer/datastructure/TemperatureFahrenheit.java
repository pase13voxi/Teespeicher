package coolpharaoh.tee.speicher.tea.timer.datastructure;

/**
 * Created by paseb on 17.12.2016.
 */

public class TemperatureFahrenheit extends Temperature {
    public TemperatureFahrenheit(int temperature) {
        super(temperature);
    }

    @Override
    public int getCelsius() {
        return fahrenheitToCelsius(temperature);
    }

    @Override
    public int getFahrenheit() {
        return temperature;
    }
}
