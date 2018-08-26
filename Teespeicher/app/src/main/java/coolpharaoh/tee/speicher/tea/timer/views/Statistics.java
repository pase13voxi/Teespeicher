package coolpharaoh.tee.speicher.tea.timer.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.felix.horizontalbargraph.HorizontalBar;
import br.com.felix.horizontalbargraph.model.BarItem;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Tea;

public class Statistics extends AppCompatActivity {

    private TextView mToolbarCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.software_heading);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HorizontalBar horizontal = findViewById(R.id.statistic_chart);
        horizontal.init(this).hasAnimation(true).addAll(getItems()).build();

    }

    private List<BarItem> getItems(){
        List<BarItem> items = new ArrayList<>();

        ArrayList<Tea> teaList = MainActivity.teaItems.getTeaItems();

        for(int i=0; i<teaList.size(); i++){
            Tea tea = teaList.get(i);
            items.add(new BarItem(tea.getName(), (double) tea.getCounter().getOverall(), tea.getColor(), Color.WHITE));
        }

        return items;
    }

}
