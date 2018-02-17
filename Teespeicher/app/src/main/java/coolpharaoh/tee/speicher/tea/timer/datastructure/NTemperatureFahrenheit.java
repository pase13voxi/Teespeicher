package coolpharaoh.tee.speicher.tea.timer.datastructure;

/**
 * Created by paseb on 17.12.2016.
 */

public class NTemperatureFahrenheit extends NTemperature {
    public NTemperatureFahrenheit(int temperature) {
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
