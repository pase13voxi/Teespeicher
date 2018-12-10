package coolpharaoh.tee.speicher.tea.timer.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tooltip.Tooltip;

import java.util.ArrayList;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.ActualSetting;
import coolpharaoh.tee.speicher.tea.timer.datastructure.AmountTs;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Coloring;
import coolpharaoh.tee.speicher.tea.timer.datastructure.N2Tea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.SortOfTea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.TeaCollection;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Temperature;
import coolpharaoh.tee.speicher.tea.timer.datastructure.TemperatureCelsius;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Time;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Variety;
import coolpharaoh.tee.speicher.tea.timer.listadapter.TeaAdapter;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    static public TeaCollection teaItems;
    static public ActualSetting settings;
    static private boolean startApplication = true;

    private TextView mToolbarCustomTitle;
    private FloatingActionButton newTea;
    private Spinner spinnerSort;
    private ListView tealist;
    private TeaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        askPermissions();

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.app_name);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        //hole ListView
        tealist = findViewById(R.id.listViewTealist);
        //Liste aller Tees
        teaItems = new TeaCollection();
        if (!teaItems.loadCollection(getApplicationContext())) {
            // TODO Auto-generated method stub
            //kann später entfernt werden
            if (!teaItems.loadOld2Collection(getApplicationContext())) {
                if (!teaItems.loadOldCollection(getApplicationContext())) {
                    ArrayList<Temperature> tmpTemperature = new ArrayList<>();
                    tmpTemperature.add(new TemperatureCelsius(100));
                    ArrayList<Time> tmpCoolDownTime = new ArrayList<>();
                    tmpCoolDownTime.add(new Time(Temperature.celsiusToCoolDownTime(100)));
                    ArrayList<Time> tmpTime = new ArrayList<>();
                    tmpTime.add(new Time("3:30"));
                    N2Tea teaExample1 = new N2Tea(teaItems.nextId(), "Earl Grey", new SortOfTea("Schwarzer Tee"), tmpTemperature,
                            tmpCoolDownTime, tmpTime, new AmountTs(5), new Coloring(SortOfTea.getVariatyColor(Variety.BlackTea)));
                    teaExample1.setCurrentDate();
                    teaItems.getTeaItems().add(teaExample1);
                    tmpTemperature = new ArrayList<>();
                    tmpTemperature.add(new TemperatureCelsius(85));
                    tmpCoolDownTime = new ArrayList<>();
                    tmpCoolDownTime.add(new Time(Temperature.celsiusToCoolDownTime(85)));
                    tmpTime = new ArrayList<>();
                    tmpTime.add(new Time("2"));
                    N2Tea teaExample2 = new N2Tea(teaItems.nextId(), "Pai Mu Tan", new SortOfTea("Weißer Tee"), tmpTemperature,
                            tmpCoolDownTime, tmpTime, new AmountTs(4), new Coloring(SortOfTea.getVariatyColor(Variety.WhiteTea)));
                    teaExample2.setCurrentDate();
                    teaItems.getTeaItems().add(teaExample2);
                    tmpTemperature = new ArrayList<>();
                    tmpTemperature.add(new TemperatureCelsius(80));
                    tmpCoolDownTime = new ArrayList<>();
                    tmpCoolDownTime.add(new Time(Temperature.celsiusToCoolDownTime(80)));
                    tmpTime = new ArrayList<>();
                    tmpTime.add(new Time("1:30"));
                    N2Tea teaExample3 = new N2Tea(teaItems.nextId(), "Sencha", new SortOfTea("Grüner Tee"), tmpTemperature,
                            tmpCoolDownTime, tmpTime, new AmountTs(4), new Coloring(SortOfTea.getVariatyColor(Variety.GreenTea)));
                    teaExample3.setCurrentDate();
                    teaItems.getTeaItems().add(teaExample3);

                    teaItems.saveCollection(getApplicationContext());
                } else {
                    teaItems.convertCollectionToNew();
                    teaItems.saveCollection(getApplicationContext());
                }
            } else {
                teaItems.convertCollection2ToNew();
                teaItems.saveCollection(getApplicationContext());
            }
        }

        //Settings holen
        settings = new ActualSetting();
        if (!settings.loadSettings(getApplicationContext())) {
            if (!settings.loadOldSettings(getApplicationContext())) {
                //setzte Default wenn nicht vorhanden
                settings.saveSettings(getApplicationContext());
            }
        }

        //herausfinden welche Sprache gesetzt ist und Übersetztung der Liste starten
        String tmpLang = "";
        String language = getResources().getString(R.string.app_name);
        switch (language) {
            case "Tee Speicher":
                tmpLang = "de";
                break;
            case "Tea Memory":
                tmpLang = "en";
                break;
        }
        if (!tmpLang.equals(settings.getLanguage())) {
            teaItems.translateSortOfTea(getApplicationContext(), settings.getLanguage(), tmpLang);
            teaItems.saveCollection(getApplicationContext());
            settings.setLanguage(tmpLang);
            settings.saveSettings(getApplicationContext());
        }

        //Setzte Spinner Groß
        spinnerSort = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, R.array.main_sort_menu, R.layout.spinner_item_sort);

        spinnerVarietyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sort);
        spinnerSort.setAdapter(spinnerVarietyAdapter);

        //setzte spinner
        spinnerSort.setSelection(settings.getSort());

        //sortierung hat sich verändert
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        settings.setSort(0);
                        settings.saveSettings(getApplicationContext());
                        //Liste sortieren und neu aufbauen
                        teaItems.sort();
                        teaItems.saveCollection(getApplicationContext());
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        settings.setSort(1);
                        settings.saveSettings(getApplicationContext());
                        //Liste sortieren und neu aufbauen
                        teaItems.sort();
                        teaItems.saveCollection(getApplicationContext());
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        settings.setSort(2);
                        settings.saveSettings(getApplicationContext());
                        //Liste sortieren und neu aufbauen
                        teaItems.sort();
                        teaItems.saveCollection(getApplicationContext());
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

                showteaScreen.putExtra("elementId", teaItems.getTeaItems().get(position).getId());
                // Intent starten und zur zweiten Activity wechseln
                startActivity(showteaScreen);
            }
        });

        //Button NewTea + Aktion
        newTea = findViewById(R.id.newtea);
        newTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Neues Intent anlegen
                Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
                // Intent starten und zur zweiten Activity wechseln
                startActivity(newteaScreen);
            }
        });
        newTea.setOnLongClickListener(this);

        //Show theses Hints only on start of the application
        if (startApplication) {
            startApplication = false;
            //show dialog problem
            if (settings.isMainProblemAlert()) {
                dialogMainProblem();
            }
            //show dialog rating
            if (settings.isMainRateAlert() && settings.getMainRatecounter() >= 20) {
                settings.resetMainRatecounter();
                dialogMainRating();
            } else {
                settings.incrementMainRatecounter();
            }
            settings.saveSettings(getApplicationContext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //Neues Intent anlegen
            Intent settingScreen = new Intent(MainActivity.this, Settings.class);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(settingScreen);
        } else if (id == R.id.action_about) {
            //Neues Intent anlegen
            Intent aboutScreen = new Intent(MainActivity.this, About.class);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(aboutScreen);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listViewTealist) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle(teaItems.getTeaItems().get(info.position).getName());

            //Übersetzung Englisch Deutsch
            String[] menuItems = getResources().getStringArray(R.array.itemMenu);
            for (int i = 0; i < menuItems.length; i++) {
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

        if (menuItemName.equals(editOption)) {
            //Neues Intent anlegen
            Intent newteaScreen = new Intent(MainActivity.this, NewTea.class);
            newteaScreen.putExtra("elementId", teaItems.getTeaItems().get(info.position).getId());


            // Intent starten und zur zweiten Activity wechseln
            startActivity(newteaScreen);
        } else if (menuItemName.equals(deleteOption)) {
            int position = info.position;
            teaItems.getTeaItems().remove(position);
            if (!teaItems.saveCollection(getApplicationContext())) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.main_error_deletion, Toast.LENGTH_SHORT);
                toast.show();
            }
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.newtea) {
            showTooltip(view, Gravity.TOP, getResources().getString(R.string.main_tooltip_newtea));
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void showTooltip(View v, int gravity, String text) {
        new Tooltip.Builder(v)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setCancelable(true)
                .setDismissOnClick(true)
                .show();
    }

    private void dialogMainProblem() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ViewGroup parent = findViewById(R.id.main_parent);

            LayoutInflater inflater = getLayoutInflater();
            View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogmainproblem, parent, false);
            final CheckBox dontshowagain = alertLayoutDialogProblem.findViewById(R.id.checkboxDialogMainProblem);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setView(alertLayoutDialogProblem);
            adb.setTitle(R.string.main_dialog_problem_header);
            adb.setPositiveButton(R.string.main_dialog_problem_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (dontshowagain.isChecked()) {
                        MainActivity.settings.setMainProblemAlert(false);
                        MainActivity.settings.saveSettings(getApplicationContext());
                    }
                    Intent problemsScreen = new Intent(MainActivity.this, Problems.class);
                    startActivity(problemsScreen);
                }
            });
            adb.setNegativeButton(R.string.main_dialog_problem_cancle, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (dontshowagain.isChecked()) {
                        MainActivity.settings.setMainProblemAlert(false);
                        MainActivity.settings.saveSettings(getApplicationContext());
                    }
                }
            });
            adb.show();
        }
    }

    private void dialogMainRating() {
        ViewGroup parent = findViewById(R.id.main_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogProblem = inflater.inflate(R.layout.dialogmainrating, parent, false);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogProblem);
        adb.setTitle(R.string.main_dialog_rating_header);
        adb.setPositiveButton(R.string.main_dialog_rating_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.settings.setMainRateAlert(false);
                MainActivity.settings.saveSettings(getApplicationContext());

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        adb.setNeutralButton(R.string.main_dialog_rating_neutral, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.setNegativeButton(R.string.main_dialog_rating_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.settings.setMainRateAlert(false);
                MainActivity.settings.saveSettings(getApplicationContext());
            }
        });
        adb.show();
    }
}
