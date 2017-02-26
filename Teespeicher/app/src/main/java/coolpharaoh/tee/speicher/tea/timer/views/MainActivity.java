package coolpharaoh.tee.speicher.tea.timer.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.AmountTs;
import coolpharaoh.tee.speicher.tea.timer.datastructure.SortOfTea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Tea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Temperature;
import coolpharaoh.tee.speicher.tea.timer.datastructure.TemperatureCelsius;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Time;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Variety;
import coolpharaoh.tee.speicher.tea.timer.listadapter.TeaAdapter;
import coolpharaoh.tee.speicher.tea.timer.datastructure.ActualSetting;
import coolpharaoh.tee.speicher.tea.timer.datastructure.TeaCollection;

public class MainActivity extends AppCompatActivity {

    static public TeaAdapter adapter;
    static public TeaCollection teaItems;
    static public ActualSetting settings;
    private TextView mToolbarCustomTitle;
    private Button newTea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbarCustomTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        //askPermissions();
        //hole ListView
        final ListView tealist = (ListView) findViewById(R.id.listViewTealist);
        //Liste aller Tees
        teaItems = new TeaCollection();
        if(!teaItems.loadCollection(getApplicationContext())){
            // TODO Auto-generated method stub
            //kann später entfernt werden
            if(!teaItems.loadOldCollection(getApplicationContext())) {
                //Toast toast = Toast.makeText(getApplicationContext(), "Fehler beim Laden. Liste wurde neu erstellt.", Toast.LENGTH_SHORT);
                //toast.show();
                ArrayList<Temperature> tmpTemperature = new ArrayList<>();
                tmpTemperature.add(new TemperatureCelsius(100));
                ArrayList<Time> tmpTime = new ArrayList<>();
                tmpTime.add(new Time("3:30"));
                Tea teaExample1 = new Tea("Earl Grey", new SortOfTea("Schwarzer Tee"), tmpTemperature,
                        tmpTime, new AmountTs(5), SortOfTea.getVariatyColor(Variety.BlackTea));
                teaExample1.setCurrentDate();
                teaItems.getTeaItems().add(teaExample1);
                tmpTemperature = new ArrayList<>();
                tmpTemperature.add(new TemperatureCelsius(85));
                tmpTime = new ArrayList<>();
                tmpTime.add(new Time("2"));
                Tea teaExample2 = new Tea("Pai Mu Tan", new SortOfTea("Weißer Tee"), tmpTemperature,
                        tmpTime, new AmountTs(4), SortOfTea.getVariatyColor(Variety.WhiteTea));
                teaExample2.setCurrentDate();
                teaItems.getTeaItems().add(teaExample2);
                tmpTemperature = new ArrayList<>();
                tmpTemperature.add(new TemperatureCelsius(80));
                tmpTime = new ArrayList<>();
                tmpTime.add(new Time("1:30"));
                Tea teaExample3 = new Tea("Sencha", new SortOfTea("Grüner Tee"), tmpTemperature,
                        tmpTime, new AmountTs(4), SortOfTea.getVariatyColor(Variety.GreenTea));
                teaExample3.setCurrentDate();
                teaItems.getTeaItems().add(teaExample3);

                teaItems.saveCollection(getApplicationContext());
            }else{
                teaItems.convertCollectionToNew(getApplicationContext());
                teaItems.saveCollection(getApplicationContext());
            }
        }

        //Settings holen
        settings = new ActualSetting();
        if(!settings.loadSettings(getApplicationContext())){
            settings.saveSettings(getApplicationContext());
        }

        //herausfinden welche Sprache gesetzt ist und Übersetztung der Liste starten
        String tmpLang = "";
        String language = getResources().getString(R.string.app_name);
        switch(language){
            case "Tee Speicher":
                tmpLang = "de"; break;
            case "Tea Memory":
                tmpLang = "en"; break;
        }
        if(!tmpLang.equals(settings.getLanguage())){
            teaItems.translateSortOfTea(getApplicationContext(), settings.getLanguage(), tmpLang);
            teaItems.saveCollection(getApplicationContext());
            settings.setLanguage(tmpLang);
            settings.saveSettings(getApplicationContext());
        }

        //Liste mit Adapter verknüpfen
        adapter = new TeaAdapter(this, teaItems.getTeaItems());
        //Adapter dem Listview hinzufügen
        tealist.setAdapter(adapter);

        //Menu wird hinzugefügt (Löschen, Ändern)
        registerForContextMenu(tealist);

        tealist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Neues Intent anlegen
                Intent showteaScreen = new Intent(MainActivity.this, ShowTea.class);
                showteaScreen.putExtra("elementAt", position);
                // Intent starten und zur zweiten Activity wechseln
                startActivity(showteaScreen);
            }
        });

        //Button NewTea + Aktion
        newTea = (Button) findViewById(R.id.newtea);
        newTea.setText(R.string.main_create_tea);
        newTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Neues Intent anlegen
                Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
                // Intent starten und zur zweiten Activity wechseln
                startActivity(newteaScreen);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu (Menu menu) {
        for(int i=0; i<menu.size(); i++){
            MenuItem mi = menu.getItem(i);
            if(mi.getItemId() == R.id.action_settings){
                mi.setTitle(R.string.main_action_settings);
            }else if(mi.getItemId() == R.id.action_about){
                mi.setTitle(R.string.main_action_about);
            }else if(mi.getItemId() == R.id.action_sort_date){
                mi.setTitle(R.string.main_action_sort_date);
                mi.setChecked(!settings.isSort());
            }else if(mi.getItemId() == R.id.action_sort_sort){
                mi.setTitle(R.string.main_action_sort_sort);
                mi.setChecked(settings.isSort());
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            //Neues Intent anlegen
            Intent settingScreen = new Intent(MainActivity.this, Settings.class);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(settingScreen);
            return true;
        }else if(id == R.id.action_about){
            //Neues Intent anlegen
            Intent aboutScreen = new Intent(MainActivity.this, About.class);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(aboutScreen);
        }else if(id == R.id.action_sort_date){
            settings.setSort(false);
            settings.saveSettings(getApplicationContext());
            //Liste sortieren und neu aufbauen
            teaItems.sort();
            teaItems.saveCollection(getApplicationContext());
            adapter.notifyDataSetChanged();
        }else if(id == R.id.action_sort_sort){
            settings.setSort(true);
            settings.saveSettings(getApplicationContext());
            //Liste sortieren und neu aufbauen
            teaItems.sort();
            teaItems.saveCollection(getApplicationContext());
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.listViewTealist){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(teaItems.getTeaItems().get(info.position).getName());
            //Übersetzung Englisch Deutsch
            String[] menuItems = getResources().getStringArray(R.array.itemMenu);
            for(int i=0; i<menuItems.length; i++){
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.itemMenu);
        String menuItemName = menuItems[menuItemIndex];
        String editOption = menuItems[0], deleteOption = menuItems[1];

        if(menuItemName.equals(editOption)){
            //Neues Intent anlegen
            Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
            newteaScreen.putExtra("elementAt", info.position);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(newteaScreen);
        }else if(menuItemName.equals(deleteOption)){
            teaItems.getTeaItems().remove(info.position);
            if(!teaItems.saveCollection(getApplicationContext())){
                Toast toast = Toast.makeText(getApplicationContext(), R.string.main_error_deletion, Toast.LENGTH_SHORT);
                toast.show();
            }
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    private void askPermissions(){
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //teaItems.deleteOldCollection();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to read and write your External storage", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
