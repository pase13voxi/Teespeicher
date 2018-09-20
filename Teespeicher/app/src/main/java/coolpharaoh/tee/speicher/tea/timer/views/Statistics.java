package coolpharaoh.tee.speicher.tea.timer.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.felix.horizontalbargraph.HorizontalBar;
import br.com.felix.horizontalbargraph.model.BarItem;
import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Coloring;
import coolpharaoh.tee.speicher.tea.timer.datastructure.N2Tea;

public class Statistics extends AppCompatActivity {

    private static final String CATEGORY_OVERALL = "overall";
    private static final String CATEGORY_MONTH = "month";
    private static final String CATEGORY_WEEK = "week";
    private static final String CATEGORY_TODAY = "today";

    private TextView mToolbarCustomTitle;
    private Spinner spinnerCategory;
    private boolean firstView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.statistics_heading);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshAllCounter();

        final HorizontalBar horizontal = findViewById(R.id.statistic_chart);
        horizontal.init(this).hasAnimation(true).addAll(getItems(CATEGORY_OVERALL)).build();


        spinnerCategory = findViewById(R.id.spinner_category);

        ArrayAdapter<CharSequence> spinnerCategoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.statistics_category, R.layout.spinner_item_sort);

        spinnerCategoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sort);
        spinnerCategory.setAdapter(spinnerCategoryAdapter);

        //sortierung hat sich verändert
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if(!firstView) {
                            horizontal.removeAll();
                            horizontal.addAll(getItems(CATEGORY_OVERALL));
                        }else{
                            firstView = false;
                        }
                        break;
                    case 1:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(CATEGORY_MONTH));
                        break;
                    case 2:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(CATEGORY_WEEK));
                        break;
                    case 3:
                        horizontal.removeAll();
                        horizontal.addAll(getItems(CATEGORY_TODAY));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private List<BarItem> getItems(String category){
        List<BarItem> items = new ArrayList<>();

        ArrayList<N2Tea> teaList = MainActivity.teaItems.getTeaItems();
        switch (category){
            case CATEGORY_OVERALL: Collections.sort(teaList,N2Tea.TeaSortDrinkingBehaviorOverall); break;
            case CATEGORY_MONTH: Collections.sort(teaList,N2Tea.TeaSortDrinkingBehaviorMonth); break;
            case CATEGORY_WEEK: Collections.sort(teaList,N2Tea.TeaSortDrinkingBehaviorWeek); break;
            case CATEGORY_TODAY: Collections.sort(teaList,N2Tea.TeaSortDrinkingBehaviorToday); break;
        }


        for(int i=0; i<teaList.size(); i++){
            N2Tea tea = teaList.get(i);
            int color = tea.getColoring().getColor();
            double counter = 0d;
            switch (category){
                case CATEGORY_OVERALL: counter = tea.getCounter().getOverall(); break;
                case CATEGORY_MONTH: counter = tea.getCounter().getMonth(); break;
                case CATEGORY_WEEK: counter = tea.getCounter().getWeek(); break;
                case CATEGORY_TODAY: counter = tea.getCounter().getDay(); break;
            }

            items.add(new BarItem(tea.getName(), counter, color, Coloring.discoverForegroundColor(color)));
        }

        return items;
    }

    private void refreshAllCounter(){
        ArrayList<N2Tea> teaList = MainActivity.teaItems.getTeaItems();
        for(int i=0; i<teaList.size(); i++){
            teaList.get(i).getCounter().refresh();
        }
    }

}
