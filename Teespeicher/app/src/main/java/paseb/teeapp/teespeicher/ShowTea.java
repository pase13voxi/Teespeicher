package paseb.teeapp.teespeicher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class ShowTea extends AppCompatActivity {

    private Toolbar toolbar;
    private int elementAt;
    private Spinner spinnerMinutes;
    private Spinner spinnerSeconds;
    private TextView textViewMin;
    private TextView textViewSec;
    private TextView textViewDoppelPunkt;
    private TextView textViewTimer;
    private Button buttonStartTimer;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tea);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Hole TextViews
        TextView textViewName = (TextView) findViewById(R.id.textViewShowName);
        TextView textViewSortOfTea = (TextView) findViewById(R.id.textViewShowTeesorte);
        TextView textViewTemperature = (TextView) findViewById(R.id.textViewShowTemperatur);
        TextView textViewTeelamass = (TextView) findViewById(R.id.textViewShowTeelamass);
        spinnerMinutes = (Spinner) findViewById(R.id.spinnerMinutes);
        spinnerSeconds = (Spinner) findViewById(R.id.spinnerSeconds);
        textViewMin = (TextView) findViewById(R.id.textViewMin);
        textViewSec = (TextView) findViewById(R.id.textViewSec);
        textViewDoppelPunkt = (TextView) findViewById(R.id.textViewDoppelPunkt);
        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerTimeAdapter = ArrayAdapter.createFromResource(
                this, R.array.itemsTimer, R.layout.spinner_item);
        spinnerTimeAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerMinutes.setAdapter(spinnerTimeAdapter);
        spinnerSeconds.setAdapter(spinnerTimeAdapter);


        //Hole Übergabeparemeter Position des Tees
        elementAt  = this.getIntent().getIntExtra("elementAt", -1);
        if(elementAt==-1){
            Toast toast = Toast.makeText(getApplicationContext(), "Fehler beim Anzeigen des Elements", Toast.LENGTH_SHORT);
            toast.show();
        }else {
            Tea selectedTea = MainActivity.teaItems.getTeaItems().get(elementAt);

            //Befülle TextViews
            name = selectedTea.getName();
            textViewName.setText(name);
            textViewSortOfTea.setText(selectedTea.getSortOfTea());
            if(selectedTea.getTemperature()!=-500)
                textViewTemperature.setText(String.valueOf(selectedTea.getTemperature())+" °C");
            else
                textViewTemperature.setText("- °C");
            if(selectedTea.getTeelamass()!=-500)
                textViewTeelamass.setText(String.valueOf(selectedTea.getTeelamass())+" Tl/L");
            else
                textViewTeelamass.setText("- Tl/L");
            spinnerMinutes.setSelection(selectedTea.getMinutes());
            spinnerSeconds.setSelection(selectedTea.getSeconds());
        }

        buttonStartTimer = (Button) findViewById(R.id.buttonStartTimer);
        buttonStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonStartTimer.getText().equals("start")){
                    //Mainlist aktualisieren
                    MainActivity.teaItems.getTeaItems().get(elementAt).setCurrentDate();
                    MainActivity.teaItems.sort();
                    MainActivity.teaItems.saveCollection(getApplicationContext());
                    MainActivity.adapter.notifyDataSetChanged();
                    //Button umbenennen
                    buttonStartTimer.setText("reset");
                    //EingabeFelder ausblenden
                    spinnerMinutes.setVisibility(View.INVISIBLE);
                    spinnerSeconds.setVisibility(View.INVISIBLE);
                    textViewMin.setVisibility(View.INVISIBLE);
                    textViewSec.setVisibility(View.INVISIBLE);
                    textViewDoppelPunkt.setVisibility(View.INVISIBLE);
                    //Timeranzeige einblenden
                    textViewTimer.setVisibility((View.VISIBLE));
                    //In millisekunden umrechnen
                    int min = Integer.parseInt(spinnerMinutes.getSelectedItem().toString());
                    int sec = Integer.parseInt(spinnerSeconds.getSelectedItem().toString());
                    long millisec = TimeUnit.MINUTES.toMillis(min) + TimeUnit.SECONDS.toMillis(sec);
                    //Counter erstellen
                    Intent counter = new Intent(getBaseContext(), CountDownService.class);
                    counter.putExtra("elementAt", name);
                    counter.putExtra("millisec", millisec);
                    startService(counter);
                }else if(buttonStartTimer.getText().equals("reset")){
                    //Button umbenennen
                    buttonStartTimer.setText("start");
                    //EingabeFelder ausblenden
                    spinnerMinutes.setVisibility(View.VISIBLE);
                    spinnerSeconds.setVisibility(View.VISIBLE);
                    textViewMin.setVisibility(View.VISIBLE);
                    textViewSec.setVisibility(View.VISIBLE);
                    textViewDoppelPunkt.setVisibility(View.VISIBLE);
                    //Timeranzeige ausblenden
                    textViewTimer.setVisibility((View.INVISIBLE));
                    //Counter canceln
                    stopService(new Intent(getBaseContext(), CountDownService.class));
                    //Mediaplayer zurück setzten
                    stopService(new Intent(getBaseContext(),MediaService.class));

                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_show_tea, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu (Menu menu) {
        for(int i=0; i<menu.size(); i++){
            MenuItem mi = menu.getItem(i);
            if(mi.getItemId() == R.id.action_vibrate){
                mi.setChecked(MainActivity.settings.isVibration());
            }else if(mi.getItemId() == R.id.action_notification){
                mi.setChecked(MainActivity.settings.isNotification());
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.action_settings){
            //Neues Intent anlegen
            Intent newteaScreen = new Intent(ShowTea.this, NewTea.class);
            elementAt = MainActivity.teaItems.getPositionByName(name);
            newteaScreen.putExtra("elementAt", elementAt);
            newteaScreen.putExtra("showTea", true);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(newteaScreen);
            finish();
            return true;
        }else if(id == R.id.action_vibrate){
            MainActivity.settings.setVibration(!MainActivity.settings.isVibration());
            MainActivity.settings.saveSettings(getApplicationContext());
        }else if(id == R.id.action_notification){
            MainActivity.settings.setNotification(!MainActivity.settings.isNotification());
            MainActivity.settings.saveSettings(getApplicationContext());
        }

        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateClock(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(CountDownService.COUNTDOWN_BR));
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, CountDownService.class));
        stopService(new Intent(getBaseContext(),MediaService.class));
        super.onDestroy();
    }

    private void updateClock(Intent intent){
        if (intent.getExtras() != null) {
            long millis = intent.getLongExtra("countdown", 0);
            boolean ready = intent.getBooleanExtra("ready", false);
            if(ready){
                textViewTimer.setText("Fertig");
            }else {
                String ms = String.format("%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                textViewTimer.setText(ms);
            }
        }
    }

}
