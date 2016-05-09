package paseb.teeapp.teespeicher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static public TeaAdapter adapter;
    static public TeaCollection teaItems;
    static public ActualSetting settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //Settings holen
        settings = new ActualSetting();
        if(!settings.loadSettings(getApplicationContext())){
            Toast toast = Toast.makeText(getApplicationContext(), "Fehler beim Laden. Settings werden auf Standard eingestellt.", Toast.LENGTH_SHORT);
            toast.show();
            settings.saveSettings(getApplicationContext());
        }

        //hole ListView
        final ListView tealist = (ListView) findViewById(R.id.listViewTealist);
        //Liste aller Tees
        teaItems = new TeaCollection();
        if(!teaItems.loadCollection(getApplicationContext())){
            Toast toast = Toast.makeText(getApplicationContext(), "Fehler beim Laden. Liste wurde neu erstellt.", Toast.LENGTH_SHORT);
            toast.show();
            teaItems.saveCollection(getApplicationContext());
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
        Button newTea = (Button) findViewById(R.id.newtea);
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
            if(mi.getItemId() == R.id.action_sort_date){
                mi.setChecked(!settings.isSort());
            }else if(mi.getItemId() == R.id.action_sort_sort){
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

        switch(menuItemName){
            case "Löschen":
                teaItems.getTeaItems().remove(info.position);
                if(!teaItems.saveCollection(getApplicationContext())){
                    Toast toast = Toast.makeText(getApplicationContext(), "Löschung konnte nicht durchgeführt werden.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                adapter.notifyDataSetChanged();
                break;
            case "Bearbeiten":
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
