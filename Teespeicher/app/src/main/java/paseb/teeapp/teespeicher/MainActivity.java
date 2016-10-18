package paseb.teeapp.teespeicher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {

    static public TeaAdapter adapter;
    static public TeaCollection teaItems;
    static public ActualSetting settings;
    static public TextView mToolbarCustomTitle;
    static public Button newTea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbarCustomTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        //hole ListView
        final ListView tealist = (ListView) findViewById(R.id.listViewTealist);
        //Liste aller Tees
        teaItems = new TeaCollection();
        if(!teaItems.loadCollection(getApplicationContext())){
            //Toast toast = Toast.makeText(getApplicationContext(), "Fehler beim Laden. Liste wurde neu erstellt.", Toast.LENGTH_SHORT);
            //toast.show();
            Tea teaExample1 = new Tea("Earl Grey","Schwarzer Tee",100,"3:30",5);
            teaExample1.setCurrentDate();
            teaItems.getTeaItems().add(teaExample1);
            Tea teaExample2 = new Tea("Pai Mu Tan","Weißer Tee",85,"2",4);
            teaExample2.setCurrentDate();
            teaItems.getTeaItems().add(teaExample2);
            Tea teaExample3 = new Tea("Sencha","Grüner Tee",80,"1:30",4);
            teaExample3.setCurrentDate();
            teaItems.getTeaItems().add(teaExample3);

            teaItems.saveCollection(getApplicationContext());
        }

        //Settings holen
        settings = new ActualSetting();
        if(!settings.loadSettings(getApplicationContext())){
            //Toast toast = Toast.makeText(getApplicationContext(), "Fehler beim Laden. Settings werden auf Standard eingestellt.", Toast.LENGTH_SHORT);
            //toast.show();
            settings.setLanguage("de");
            settings.saveSettings(getApplicationContext());

            //Hier wird jetzt die gewünschte Sprache abgegriffen
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.main_language_selection_title);
            builder.setItems(R.array.settings_languages, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if(item == 0){
                        settings.setLanguage("en");
                        newTea.setText(R.string.main_create_tea_en);
                    }else{
                        settings.setLanguage("de");
                        newTea.setText(R.string.main_create_tea);
                    }
                    teaItems.translateSortOfTea(settings.getLanguage(),getApplicationContext());
                    adapter.notifyDataSetChanged();
                    settings.saveSettings(getApplicationContext());
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
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
        //Übersetzung Englisch Deutsch
        if(settings.getLanguage().equals("de")){
            newTea.setText(R.string.main_create_tea);
        }else if(settings.getLanguage().equals("en")){
            newTea.setText(R.string.main_create_tea_en);
        }
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
                if(settings.getLanguage().equals("de")){
                    mi.setTitle(R.string.main_action_settings);
                }else if(settings.getLanguage().equals("en")){
                    mi.setTitle(R.string.main_action_settings_en);
                }
            }else if(mi.getItemId() == R.id.action_sort_date){
                if(settings.getLanguage().equals("de")){
                    mi.setTitle(R.string.main_action_sort_date);
                }else if(settings.getLanguage().equals("en")){
                    mi.setTitle(R.string.main_action_sort_date_en);
                }
                mi.setChecked(!settings.isSort());
            }else if(mi.getItemId() == R.id.action_sort_sort){
                if(settings.getLanguage().equals("de")){
                    mi.setTitle(R.string.main_action_sort_sort);
                }else if(settings.getLanguage().equals("en")){
                    mi.setTitle(R.string.main_action_sort_sort_en);
                }
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
            String[] menuItems = null;
            if(settings.getLanguage().equals("de")) {
                menuItems = getResources().getStringArray(R.array.itemMenu);
            }else if(settings.getLanguage().equals("en")){
                menuItems = getResources().getStringArray(R.array.itemMenu_en);
            }
            for(int i=0; i<menuItems.length; i++){
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.itemMenu_en);
        String menuItemName = menuItems[menuItemIndex];

        switch(menuItemName){
            case "Delete":
                teaItems.getTeaItems().remove(info.position);
                if(!teaItems.saveCollection(getApplicationContext())){
                    Toast toast = null;
                    if(settings.getLanguage().equals("de")) {
                        toast = Toast.makeText(getApplicationContext(), R.string.main_error_deletion, Toast.LENGTH_SHORT);
                    }else if(settings.getLanguage().equals("en")){
                        toast = Toast.makeText(getApplicationContext(), R.string.main_error_deletion_en, Toast.LENGTH_SHORT);
                    }
                    toast.show();
                }
                adapter.notifyDataSetChanged();
                break;
            case "Edit":
                //Neues Intent anlegen
                Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
                newteaScreen.putExtra("elementAt", info.position);
                // Intent starten und zur zweiten Activity wechseln
                startActivity(newteaScreen);
                break;
            default: break;
        }

        return true;
    }

}
