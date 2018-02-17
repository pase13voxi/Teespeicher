package coolpharaoh.tee.speicher.tea.timer.datastructure;

/**
 * Created by paseb on 17.12.2016.
 */

public class AmountTs implements Amount {
    private int amount;
    public AmountTs(int amount){
        this.amount = amount;
    }
    @Override
    public String getUnit() {
        return "Ts";
    }

    @Override
    public int getValue() {
        return amount;
    }
}
