package coolpharaoh.tee.speicher.tea.timer.views;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.listadapter.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.listadapter.SoftwareListAdapter;

public class Software extends AppCompatActivity {

    private TextView mToolbarCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbarCustomTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.software_heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<ListRowItem> softwareList = new ArrayList<>();
        ListRowItem itemPicker = new ListRowItem(getResources().getString(R.string.software_colorpicker_heading), getResources().getString(R.string.software_colorpicker_description));
        softwareList.add(itemPicker);
        ListRowItem itemTooltips = new ListRowItem(getResources().getString(R.string.software_tooltip_heading), getResources().getString(R.string.software_tooltip_description));
        softwareList.add(itemTooltips);
        ListRowItem itemStatistic = new ListRowItem(getResources().getString(R.string.software_statistic_heading), getResources().getString(R.string.software_statistic_description));
        softwareList.add(itemStatistic);

        //Liste mit Adapter verknüpfen
        SoftwareListAdapter adapter = new SoftwareListAdapter(this, softwareList);
        //Adapter dem Listview hinzufügen
        ListView listViewAbout = (ListView) findViewById(R.id.listview_software);
        listViewAbout.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
