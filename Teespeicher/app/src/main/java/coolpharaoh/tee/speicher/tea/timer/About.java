package coolpharaoh.tee.speicher.tea.timer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class About extends AppCompatActivity {

    private enum ListItems {
        Contact, Rating, /*Donate,*/ Software
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.about_heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ListView beschreiben
        List<ListRowItem> aboutList = new ArrayList<ListRowItem>();
        ListRowItem itemContact = new ListRowItem(getResources().getString(R.string.about_contact_heading),getResources().getString(R.string.about_contact_description));
        aboutList.add(itemContact);
        ListRowItem itemRating = new ListRowItem(getResources().getString(R.string.about_rating_heading), getResources().getString(R.string.about_rating_description));
        aboutList.add(itemRating);
        /*ListRowItem itemDonate = new ListRowItem(getResources().getString(R.string.about_donate_heading),getResources().getString(R.string.about_donate_description));
        aboutList.add(itemDonate);*/
        ListRowItem itemSoftware = new ListRowItem(getResources().getString(R.string.about_software_heading),getResources().getString(R.string.about_software_description));
        aboutList.add(itemSoftware);

        //Liste mit Adapter verknüpfen
        AboutListAdapter adapter = new AboutListAdapter(this, aboutList);
        //Adapter dem Listview hinzufügen
        ListView listViewAbout = (ListView) findViewById(R.id.listview_about);
        listViewAbout.setAdapter(adapter);

        listViewAbout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItems item = ListItems.values()[position];
                switch(item){
                    case Contact:
                        //Neues Intent anlegen
                        Intent contactScreen = new Intent(About.this, Contact.class);
                        // Intent starten und zur zweiten Activity wechseln
                        startActivity(contactScreen);
                        break;
                    case Rating:
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        break;
                    /*case Donate:
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/")));
                        break;*/
                    case Software:
                        //Neues Intent anlegen
                        Intent softwareScreen = new Intent(About.this, Software.class);
                        // Intent starten und zur zweiten Activity wechseln
                        startActivity(softwareScreen);
                        break;
                }

            }
        });
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
