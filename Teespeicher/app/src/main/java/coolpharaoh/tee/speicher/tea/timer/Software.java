package coolpharaoh.tee.speicher.tea.timer;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Software extends AppCompatActivity {

    static public TextView mToolbarCustomTitle;

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

        List<ListRowItem> softwareList = new ArrayList<ListRowItem>();
        ListRowItem itemContact = new ListRowItem(getResources().getString(R.string.software_tesseract_heading),getResources().getString(R.string.software_tesseract_description));
        softwareList.add(itemContact);
        ListRowItem itemRating = new ListRowItem(getResources().getString(R.string.software_cropper_heading), getResources().getString(R.string.software_cropper_description));
        softwareList.add(itemRating);

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