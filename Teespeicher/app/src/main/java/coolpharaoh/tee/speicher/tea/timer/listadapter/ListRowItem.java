package coolpharaoh.tee.speicher.tea.timer.listadapter;

/**
 * Created by paseb on 03.11.2016.
 */

public class ListRowItem {
    private String heading;
    private String description;
    public ListRowItem(String heading, String description){
        this.heading = heading;
        this.description = description;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getHeading() {
        return heading;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
