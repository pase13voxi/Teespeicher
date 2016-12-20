package coolpharaoh.tee.speicher.tea.timer.datastructure;

/**
 * Created by paseb on 17.12.2016.
 */

public class AmountGramm implements Amount {
    private int amount;

    public AmountGramm(int amount){
        this.amount = amount;
    }

    @Override
    public String getUnit() {
        return "Gr";
    }

    @Override
    public int getValue() {
        return amount;
    }
}
