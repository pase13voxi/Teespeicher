package coolpharaoh.tee.speicher.tea.timer.views;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.listadapter.AboutListAdapter;
import coolpharaoh.tee.speicher.tea.timer.listadapter.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.R;

public class About extends AppCompatActivity {

    private enum ListItems {
        Contact, Rating, Translation, Software, Billing
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
        List<ListRowItem> aboutList = new ArrayList<>();
        ListRowItem itemContact = new ListRowItem(getResources().getString(R.string.about_contact_heading),getResources().getString(R.string.about_contact_description));
        aboutList.add(itemContact);
        ListRowItem itemRating = new ListRowItem(getResources().getString(R.string.about_rating_heading), getResources().getString(R.string.about_rating_description));
        aboutList.add(itemRating);
        ListRowItem itemTranslation = new ListRowItem(getResources().getString(R.string.about_translation_heading),getResources().getString(R.string.about_translation_description));
        aboutList.add(itemTranslation);
        ListRowItem itemSoftware = new ListRowItem(getResources().getString(R.string.about_software_heading),getResources().getString(R.string.about_software_description));
        aboutList.add(itemSoftware);
        ListRowItem itemBilling = new ListRowItem(getResources().getString(R.string.about_billing_heading),getResources().getString(R.string.about_billing_description));
        aboutList.add(itemBilling);

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
                    case Translation:
                        //Neues Intent anlegen
                        Intent languagesScreen = new Intent(About.this, Languages.class);
                        // Intent starten und zur zweiten Activity wechseln
                        startActivity(languagesScreen);
                        break;
                    case Software:
                        //Neues Intent anlegen
                        Intent softwareScreen = new Intent(About.this, Software.class);
                        // Intent starten und zur zweiten Activity wechseln
                        startActivity(softwareScreen);
                        break;
                    case Billing:
                        /*Toast toast = Toast.makeText(getApplicationContext(), "Diese Funktionen ist zur Zeit noch nicht möglich.", Toast.LENGTH_SHORT);
                        toast.show();*/
                        //Neues Intent anlegen
                        Intent billingScreen = new Intent(About.this, Payment.class);
                        // Intent starten und zur zweiten Activity wechseln
                        startActivity(billingScreen);
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
