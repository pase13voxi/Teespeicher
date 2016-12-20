package coolpharaoh.tee.speicher.tea.timer.datastructure;

import java.io.Serializable;

/**
 * Created by paseb on 17.12.2016.
 */

public interface Amount extends Serializable {
    String getUnit();
    int getValue();
}
