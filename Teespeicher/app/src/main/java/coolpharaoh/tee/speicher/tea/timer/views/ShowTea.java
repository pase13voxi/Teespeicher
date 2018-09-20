package coolpharaoh.tee.speicher.tea.timer.views;


import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Amount;
import coolpharaoh.tee.speicher.tea.timer.datastructure.N2Tea;
import coolpharaoh.tee.speicher.tea.timer.listadapter.CounterListAdapter;
import coolpharaoh.tee.speicher.tea.timer.listadapter.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.services.CountDownService;
import coolpharaoh.tee.speicher.tea.timer.services.MediaService;

public class ShowTea extends AppCompatActivity implements View.OnLongClickListener {

    private Toolbar toolbar;
    private TextView textViewBrewCount;
    private Button buttonBrewCount;
    private Button buttonNextBrew;
    private ToggleButton toggleVibration;
    private ToggleButton toggleNotification;
    private Button buttonNote;
    private TextView textViewTemperature;
    private Spinner spinnerMinutes;
    private Spinner spinnerSeconds;
    private TextView textViewMin;
    private TextView textViewSec;
    private TextView textViewDoppelPunkt;
    private TextView textViewTimer;
    private Button buttonStartTimer;
    private Button buttonExchange;
    private Button buttonInfo;
    Button buttonCalcAmount;
    private ImageView imageViewCup;
    private ImageView imageViewFill;
    private ImageView imageViewSteam;
    private UUID elementId;
    private int elementAt;
    private N2Tea selectedTea;
    private int brewCount = 0;
    private String name;
    private String sortOfTea;
    private int minutes;
    private int seconds;
    private boolean infoShown = false;
    //animation
    private long maxMilliSec;
    private int percent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tea);

        toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.showtea_heading);
        buttonBrewCount = findViewById(R.id.toolbar_brewcount);
        textViewBrewCount = findViewById(R.id.toolbar_text_brewcount);
        buttonNextBrew  = findViewById(R.id.toolbar_nextbrew);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Hole TextViews
        toggleVibration = findViewById(R.id.buttonVibration);
        toggleNotification = findViewById(R.id.buttonNotification);
        TextView textViewName = findViewById(R.id.textViewShowName);
        TextView textViewSortOfTea = findViewById(R.id.textViewShowTeesorte);
        buttonNote = findViewById(R.id.buttonNote);
        textViewTemperature = findViewById(R.id.textViewShowTemperatur);
        buttonInfo = findViewById(R.id.buttonInfo);
        buttonExchange = findViewById(R.id.buttonExchange);
        TextView textViewTeelamass = findViewById(R.id.textViewShowTeelamass);
        buttonCalcAmount = findViewById(R.id.buttonCalculateAmount);

        spinnerMinutes = findViewById(R.id.spinnerMinutes);
        spinnerSeconds = findViewById(R.id.spinnerSeconds);
        textViewMin = findViewById(R.id.textViewMin);
        textViewSec = findViewById(R.id.textViewSec);
        textViewDoppelPunkt = findViewById(R.id.textViewDoppelPunkt);
        textViewTimer = findViewById(R.id.textViewTimer);
        imageViewCup = findViewById(R.id.imageViewCup);
        imageViewFill = findViewById(R.id.imageViewFill);
        imageViewSteam = findViewById(R.id.imageViewSteam);

        //setzt Tranparenz der Textviews
        int alpha = 130;
        textViewName.getBackground().setAlpha(alpha);
        textViewSortOfTea.getBackground().setAlpha(alpha);
        textViewDoppelPunkt.getBackground().setAlpha(alpha);
        textViewTimer.getBackground().setAlpha(alpha);

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerTimeAdapter = ArrayAdapter.createFromResource(
                this, R.array.itemsTimer, R.layout.spinner_item);
        spinnerTimeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerMinutes.setAdapter(spinnerTimeAdapter);
        spinnerSeconds.setAdapter(spinnerTimeAdapter);

        //Vibration and Notification set
        toggleVibration.setChecked(MainActivity.settings.isVibration());
        toggleNotification.setChecked(MainActivity.settings.isNotification());

        //show Description
        if(MainActivity.settings.isShowteaAlert()){
            dialogShowTeaDescription();
        }

        //Hole Übergabeparemeter Position des Tees
        elementId  = (UUID) this.getIntent().getSerializableExtra("elementId");
        if(elementId == null){
            Toast toast = Toast.makeText(getApplicationContext(), R.string.showtea_error_text, Toast.LENGTH_SHORT);
            toast.show();
            buttonBrewCount.setVisibility(View.INVISIBLE);
        }else {
            elementAt = MainActivity.teaItems.getPositionById(elementId);
            selectedTea = MainActivity.teaItems.getTeaItems().get(elementAt);

            //Befülle TextViews
            name = selectedTea.getName();
            textViewName.setText(name);
            sortOfTea = selectedTea.getSortOfTea().getType();
            textViewSortOfTea.setText(sortOfTea);
            if(!selectedTea.getNote().equals("")){
                buttonNote.setVisibility(View.VISIBLE);
            }
            if(selectedTea.getTemperature().get(brewCount).getCelsius()!=-500) {
                if (MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                    textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, String.valueOf(selectedTea.getTemperature().get(brewCount).getCelsius())));
                } else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) {
                    textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, String.valueOf(selectedTea.getTemperature().get(brewCount).getFahrenheit())));
                }
            }else {
                if(MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                    textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, "-"));
                }else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) {
                    textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, "-"));
                }
            }
            if(!selectedTea.getCoolDownTime().get(brewCount).getTime().equals("-")){
                buttonExchange.setEnabled(true);
            }
            if(selectedTea.getAmount().getValue()!=-500) {
                if(selectedTea.getAmount().getUnit().equals("Ts"))
                    textViewTeelamass.setText(getResources().getString(R.string.showtea_display_ts, String.valueOf(selectedTea.getAmount().getValue())));
                else if(selectedTea.getAmount().getUnit().equals("Gr"))
                    textViewTeelamass.setText(getResources().getString(R.string.showtea_display_gr, String.valueOf(selectedTea.getAmount().getValue())));
            }else {
                buttonCalcAmount.setEnabled(false);
                if(selectedTea.getAmount().getUnit().equals("Ts"))
                    textViewTeelamass.setText(getResources().getString(R.string.showtea_display_ts, "-"));
                else if(selectedTea.getAmount().getUnit().equals("Gr"))
                    textViewTeelamass.setText(getResources().getString(R.string.showtea_display_gr, "-"));
            }
            minutes = selectedTea.getTime().get(brewCount).getMinutes();
            spinnerMinutes.setSelection(minutes);
            seconds = selectedTea.getTime().get(brewCount).getSeconds();
            spinnerSeconds.setSelection(seconds);
            //wenn nur ein Aufguss vorgesehen ist dann verschwindet der button_calculateamount
            if(selectedTea.getTemperature().size()==1){
                textViewBrewCount.setVisibility(View.INVISIBLE);
                buttonBrewCount.setVisibility(View.INVISIBLE);
                buttonNextBrew.setVisibility(View.INVISIBLE);
            }
        }

        toggleVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.settings.setVibration(toggleVibration.isChecked());
                MainActivity.settings.saveSettings(getApplicationContext());
            }
        });
        toggleVibration.setOnLongClickListener(this);

        toggleNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.settings.setNotification(toggleNotification.isChecked());
                MainActivity.settings.saveSettings(getApplicationContext());
            }
        });
        toggleNotification.setOnLongClickListener(this);

        buttonNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNote();
            }
        });
        buttonNote.setOnLongClickListener(this);

        buttonCalcAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogamount();
            }
        });
        buttonCalcAmount.setOnLongClickListener(this);

        buttonBrewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = null;
                if(elementAt != -1) {
                    int tmpSize = selectedTea.getTemperature().size();
                    items = new String[tmpSize];
                    for(int i=0; i<tmpSize; i++){
                        items[i] = String.valueOf(i+1) +". "+ getResources().getString(R.string.showtea_brew_count_content);
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setIcon(R.drawable.brewdark);
                builder.setTitle(R.string.showtea_brew_count_title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        brewCount = item;
                        brewCountChanged();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        buttonBrewCount.setOnLongClickListener(this);

        buttonNextBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                brewCount++;
                brewCountChanged();
            }
        });
        buttonNextBrew.setOnLongClickListener(this);

        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Infomationen anzeigen
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.showtea_cooldown_title);
                builder.setMessage(R.string.showtea_cooldown_text).setNeutralButton("OK", null).show();
            }
        });

        buttonExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!infoShown){
                    buttonInfo.setVisibility(View.VISIBLE);
                    infoShown = true;
                    //Abkühlzeit anzeigen
                    spinnerMinutes.setSelection(selectedTea.getCoolDownTime().get(brewCount).getMinutes());
                    spinnerSeconds.setSelection(selectedTea.getCoolDownTime().get(brewCount).getSeconds());
                }else {
                    buttonInfo.setVisibility(View.INVISIBLE);
                    infoShown = false;
                    spinnerMinutes.setSelection(minutes);
                    spinnerSeconds.setSelection(seconds);
                }
            }
        });
        buttonExchange.setOnLongClickListener(this);

        buttonStartTimer = findViewById(R.id.buttonStartTimer);
        buttonStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonStartTimer.getText().equals(getResources().getString(R.string.showtea_timer_start))){
                    //Mainlist aktualisieren
                    selectedTea.setCurrentDate();
                    //don't count when waiting for the right temperature
                    if(!infoShown) selectedTea.getCounter().count();
                    MainActivity.teaItems.sort();
                    MainActivity.teaItems.saveCollection(getApplicationContext());
                    //Button umbenennen
                    buttonStartTimer.setText(R.string.showtea_timer_reset);
                    buttonExchange.setEnabled(false);
                    //EingabeFelder ausblenden
                    spinnerMinutes.setVisibility(View.INVISIBLE);
                    spinnerSeconds.setVisibility(View.INVISIBLE);
                    textViewMin.setVisibility(View.INVISIBLE);
                    textViewSec.setVisibility(View.INVISIBLE);
                    textViewDoppelPunkt.setVisibility(View.INVISIBLE);
                    //Auswahl des Aufgusses verbieten
                    buttonBrewCount.setEnabled(false);
                    buttonNextBrew.setEnabled(false);
                    //Timeranzeige einblenden
                    textViewTimer.setVisibility((View.VISIBLE));
                    //Teetasse anzeigen
                    if(!infoShown && MainActivity.settings.isAnimation()) {
                        imageViewCup.setVisibility((View.VISIBLE));
                        imageViewFill.setVisibility((View.VISIBLE));
                        //Farbe des Inhalts der Tasse festlegen
                        imageViewFill.setColorFilter(selectedTea.getColoring().getColor(), PorterDuff.Mode.SRC_ATOP);
                    }
                    //In millisekunden umrechnen
                    int min = Integer.parseInt(spinnerMinutes.getSelectedItem().toString());
                    int sec = Integer.parseInt(spinnerSeconds.getSelectedItem().toString());
                    long millisec = TimeUnit.MINUTES.toMillis(min) + TimeUnit.SECONDS.toMillis(sec);
                    //for Animation getMaxMillis;
                    maxMilliSec =millisec;
                    //Counter erstellen
                    Intent counter = new Intent(getBaseContext(), CountDownService.class);
                    counter.putExtra("elementName", name);
                    counter.putExtra("millisec", millisec);
                    startService(counter);
                }else if(buttonStartTimer.getText().equals(getResources().getString(R.string.showtea_timer_reset))){
                    //Button umbenennen
                    buttonStartTimer.setText(R.string.showtea_timer_start);
                    if(!selectedTea.getCoolDownTime().get(brewCount).getTime().equals("-")){
                        buttonExchange.setEnabled(true);
                    }

                    //EingabeFelder ausblenden
                    spinnerMinutes.setVisibility(View.VISIBLE);
                    spinnerSeconds.setVisibility(View.VISIBLE);
                    textViewMin.setVisibility(View.VISIBLE);
                    textViewSec.setVisibility(View.VISIBLE);
                    textViewDoppelPunkt.setVisibility(View.VISIBLE);
                    //Auswahl des Aufgusses wieder erlauben
                    buttonBrewCount.setEnabled(true);
                    nextBrewEnable();
                    //Timeranzeige ausblenden
                    textViewTimer.setVisibility((View.INVISIBLE));
                    //Teetasse ausblenden
                    if(!infoShown && MainActivity.settings.isAnimation()) {
                        imageViewCup.setVisibility((View.INVISIBLE));
                        imageViewFill.setVisibility((View.INVISIBLE));
                        imageViewFill.setImageResource(R.drawable.fill0pr);
                        imageViewSteam.setVisibility((View.INVISIBLE));
                        //für animation zurücksetzen
                        imageViewCup.setImageResource(R.drawable.cup_new);
                        percent = 0;
                    }
                    //Counter canceln
                    stopService(new Intent(getBaseContext(), CountDownService.class));
                    //Mediaplayer zurück setzten
                    stopService(new Intent(getBaseContext(),MediaService.class));
                    //Nofications zurücksetzten
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    if (notificationManager==null){
                        throw new AssertionError("NotificationManager is null.");
                    } else {
                        notificationManager.cancelAll();
                    }
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_show_tea, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_note){
            dialogNote();
        }else if(id == R.id.action_settings){
            //Neues Intent anlegen
            Intent newteaScreen = new Intent(ShowTea.this, NewTea.class);
            newteaScreen.putExtra("elementId", elementId);
            newteaScreen.putExtra("showTea", true);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(newteaScreen);
            finish();
            return true;
        }else if(id == R.id.action_counter){
            dialogCounter();
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
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager==null){
            throw new AssertionError("NotificationManager is null.");
        } else {
            notificationManager.cancelAll();
        }

        super.onDestroy();
    }

    private void updateClock(Intent intent){
        if (intent.getExtras() != null) {
            long millis = intent.getLongExtra("countdown", 0);
            boolean ready = intent.getBooleanExtra("ready", false);
            if(!infoShown && MainActivity.settings.isAnimation()) {
                updateImage(millis);
            }
            if(ready){
                textViewTimer.setText(R.string.showtea_tea_ready);
                if(!infoShown && MainActivity.settings.isAnimation()) {
                    imageViewFill.setImageResource(R.drawable.fill100pr);
                    imageViewSteam.setVisibility((View.VISIBLE));
                }
            }else {
                String ms = String.format(Locale.getDefault(),"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millis),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                textViewTimer.setText(ms);
            }
        }
    }

    private void updateImage(long millisec){
        int percentTmp = 100-((int)(((float)millisec/(float)maxMilliSec) * 100));
        if(percentTmp>percent){
            percent = percentTmp;
            switch(percent){
                case 1: imageViewFill.setImageResource(R.drawable.fill1pr); break;
                case 2: imageViewFill.setImageResource(R.drawable.fill2pr); break;
                case 3: imageViewFill.setImageResource(R.drawable.fill3pr); break;
                case 4: imageViewFill.setImageResource(R.drawable.fill4pr); break;
                case 5: imageViewFill.setImageResource(R.drawable.fill5pr); break;
                case 6: imageViewFill.setImageResource(R.drawable.fill6pr); break;
                case 7: imageViewFill.setImageResource(R.drawable.fill7pr); break;
                case 8: imageViewFill.setImageResource(R.drawable.fill8pr); break;
                case 9: imageViewFill.setImageResource(R.drawable.fill9pr); break;
                case 10: imageViewFill.setImageResource(R.drawable.fill10pr); break;
                case 11: imageViewFill.setImageResource(R.drawable.fill11pr); break;
                case 12: imageViewFill.setImageResource(R.drawable.fill12pr); break;
                case 13: imageViewFill.setImageResource(R.drawable.fill13pr); break;
                case 14: imageViewFill.setImageResource(R.drawable.fill14pr); break;
                case 15: imageViewFill.setImageResource(R.drawable.fill15pr); break;
                case 16: imageViewFill.setImageResource(R.drawable.fill16pr); break;
                case 17: imageViewFill.setImageResource(R.drawable.fill17pr); break;
                case 18: imageViewFill.setImageResource(R.drawable.fill18pr); break;
                case 19: imageViewFill.setImageResource(R.drawable.fill19pr); break;
                case 20: imageViewFill.setImageResource(R.drawable.fill20pr); break;
                case 21: imageViewFill.setImageResource(R.drawable.fill21pr); break;
                case 22: imageViewFill.setImageResource(R.drawable.fill22pr); break;
                case 23: imageViewFill.setImageResource(R.drawable.fill23pr); break;
                case 24: imageViewFill.setImageResource(R.drawable.fill24pr); break;
                case 25: imageViewFill.setImageResource(R.drawable.fill25pr); break;
                case 26: imageViewFill.setImageResource(R.drawable.fill26pr); break;
                case 27: imageViewFill.setImageResource(R.drawable.fill27pr); break;
                case 28: imageViewFill.setImageResource(R.drawable.fill28pr); break;
                case 29: imageViewFill.setImageResource(R.drawable.fill29pr); break;
                case 30: imageViewFill.setImageResource(R.drawable.fill30pr); break;
                case 31: imageViewFill.setImageResource(R.drawable.fill31pr); break;
                case 32: imageViewFill.setImageResource(R.drawable.fill32pr); break;
                case 33: imageViewFill.setImageResource(R.drawable.fill33pr); break;
                case 34: imageViewFill.setImageResource(R.drawable.fill34pr); break;
                case 35: imageViewFill.setImageResource(R.drawable.fill35pr); break;
                case 36: imageViewFill.setImageResource(R.drawable.fill36pr); break;
                case 37: imageViewFill.setImageResource(R.drawable.fill37pr); break;
                case 38: imageViewFill.setImageResource(R.drawable.fill38pr); break;
                case 39: imageViewFill.setImageResource(R.drawable.fill39pr); break;
                case 40: imageViewFill.setImageResource(R.drawable.fill40pr); break;
                case 41: imageViewFill.setImageResource(R.drawable.fill41pr); break;
                case 42: imageViewFill.setImageResource(R.drawable.fill42pr); break;
                case 43: imageViewFill.setImageResource(R.drawable.fill43pr); break;
                case 44: imageViewFill.setImageResource(R.drawable.fill44pr); break;
                case 45: imageViewFill.setImageResource(R.drawable.fill45pr); break;
                case 46: imageViewFill.setImageResource(R.drawable.fill46pr); break;
                case 47: imageViewFill.setImageResource(R.drawable.fill47pr); break;
                case 48: imageViewFill.setImageResource(R.drawable.fill48pr); break;
                case 49: imageViewFill.setImageResource(R.drawable.fill49pr); break;
                case 50: imageViewFill.setImageResource(R.drawable.fill50pr); break;
                case 51: imageViewFill.setImageResource(R.drawable.fill51pr); break;
                case 52: imageViewFill.setImageResource(R.drawable.fill52pr); break;
                case 53: imageViewFill.setImageResource(R.drawable.fill53pr); break;
                case 54: imageViewFill.setImageResource(R.drawable.fill54pr); break;
                case 55: imageViewFill.setImageResource(R.drawable.fill55pr); break;
                case 56: imageViewFill.setImageResource(R.drawable.fill56pr); break;
                case 57: imageViewFill.setImageResource(R.drawable.fill57pr); break;
                case 58: imageViewFill.setImageResource(R.drawable.fill58pr); break;
                case 59: imageViewFill.setImageResource(R.drawable.fill59pr); break;
                case 60: imageViewFill.setImageResource(R.drawable.fill60pr); break;
                case 61: imageViewFill.setImageResource(R.drawable.fill61pr); break;
                case 62: imageViewFill.setImageResource(R.drawable.fill62pr); break;
                case 63: imageViewFill.setImageResource(R.drawable.fill63pr); break;
                case 64: imageViewFill.setImageResource(R.drawable.fill64pr); break;
                case 65: imageViewFill.setImageResource(R.drawable.fill65pr); break;
                case 66: imageViewFill.setImageResource(R.drawable.fill66pr); break;
                case 67: imageViewFill.setImageResource(R.drawable.fill67pr); break;
                case 68: imageViewFill.setImageResource(R.drawable.fill68pr); break;
                case 69: imageViewFill.setImageResource(R.drawable.fill69pr); break;
                case 70: imageViewFill.setImageResource(R.drawable.fill70pr); break;
                case 71: imageViewFill.setImageResource(R.drawable.fill71pr); break;
                case 72: imageViewFill.setImageResource(R.drawable.fill72pr); break;
                case 73: imageViewFill.setImageResource(R.drawable.fill73pr); break;
                case 74: imageViewFill.setImageResource(R.drawable.fill74pr); break;
                case 75: imageViewFill.setImageResource(R.drawable.fill75pr); break;
                case 76: imageViewFill.setImageResource(R.drawable.fill76pr); break;
                case 77: imageViewFill.setImageResource(R.drawable.fill77pr); break;
                case 78: imageViewFill.setImageResource(R.drawable.fill78pr); break;
                case 79: imageViewFill.setImageResource(R.drawable.fill79pr); break;
                case 80: imageViewFill.setImageResource(R.drawable.fill80pr); break;
                case 81: imageViewFill.setImageResource(R.drawable.fill81pr); break;
                case 82: imageViewFill.setImageResource(R.drawable.fill82pr); break;
                case 83: imageViewFill.setImageResource(R.drawable.fill83pr); break;
                case 84: imageViewFill.setImageResource(R.drawable.fill84pr); break;
                case 85: imageViewFill.setImageResource(R.drawable.fill85pr); break;
                case 86: imageViewFill.setImageResource(R.drawable.fill86pr); break;
                case 87: imageViewFill.setImageResource(R.drawable.fill87pr); break;
                case 88: imageViewFill.setImageResource(R.drawable.fill88pr); break;
                case 89: imageViewFill.setImageResource(R.drawable.fill89pr); break;
                case 90: imageViewFill.setImageResource(R.drawable.fill90pr); break;
                case 91: imageViewFill.setImageResource(R.drawable.fill91pr); break;
                case 92: imageViewFill.setImageResource(R.drawable.fill92pr); break;
                case 93: imageViewFill.setImageResource(R.drawable.fill93pr); break;
                case 94: imageViewFill.setImageResource(R.drawable.fill94pr); break;
                case 95: imageViewFill.setImageResource(R.drawable.fill95pr); break;
                case 96: imageViewFill.setImageResource(R.drawable.fill96pr); break;
                case 97: imageViewFill.setImageResource(R.drawable.fill97pr); break;
                case 98: imageViewFill.setImageResource(R.drawable.fill98pr); break;
                case 99: imageViewFill.setImageResource(R.drawable.fill99pr); break;
                case 100: imageViewFill.setImageResource(R.drawable.fill100pr); break;
            }
        }
    }

    private void brewCountChanged(){
        if(selectedTea.getTemperature().get(brewCount).getCelsius()!=-500) {
            if (MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, String.valueOf(selectedTea.getTemperature().get(brewCount).getCelsius())));
            } else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, String.valueOf(selectedTea.getTemperature().get(brewCount).getFahrenheit())));
            }
        }else {
            if(MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_celsius, "-"));
            }else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) {
                textViewTemperature.setText(getResources().getString(R.string.showtea_display_fahrenheit, "-"));
            }
        }
        if(!selectedTea.getCoolDownTime().get(brewCount).getTime().equals("-")){
            buttonExchange.setEnabled(true);
        }else{
            buttonExchange.setEnabled(false);
        }
        minutes = selectedTea.getTime().get(brewCount).getMinutes();
        spinnerMinutes.setSelection(minutes);
        seconds = selectedTea.getTime().get(brewCount).getSeconds();
        spinnerSeconds.setSelection(seconds);
        textViewBrewCount.setText(getResources().getString(R.string.showtea_brea_count_point, (brewCount+1)));

        nextBrewEnable();

        buttonInfo.setVisibility(View.INVISIBLE);
        infoShown = false;
    }

    private void nextBrewEnable(){
        if(brewCount==selectedTea.getTemperature().size()-1){
            buttonNextBrew.setEnabled(false);
        }else{
            buttonNextBrew.setEnabled(true);
        }
    }

    private void dialogNote(){
        ViewGroup parent = findViewById(R.id.showtea_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogNote = inflater.inflate(R.layout.dialognote,parent,false);
        final EditText editTextNote = alertLayoutDialogNote.findViewById(R.id.editTextNote);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogNote);
        adb.setTitle(R.string.showtea_action_note);
        adb.setIcon(R.drawable.note);
        editTextNote.setText(selectedTea.getNote());
        editTextNote.setSelected(false);
        adb.setPositiveButton(R.string.showtea_dialog_note_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selectedTea.setNote(editTextNote.getText().toString());
                if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_save, Toast.LENGTH_SHORT);
                    toast.show();
                }
                if(!selectedTea.getNote().equals("")){
                    buttonNote.setVisibility(View.VISIBLE);
                }else{
                    buttonNote.setVisibility(View.INVISIBLE);
                }
            }
        });
        adb.setNegativeButton(R.string.showtea_dialog_note_cancle, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    private void dialogCounter() {
        //aktualisiere den Counter
        selectedTea.getCounter().refresh();

        List<ListRowItem> counterList = new ArrayList<>();
        ListRowItem itemToday = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_day), String.valueOf(selectedTea.getCounter().getDay()));
        counterList.add(itemToday);
        ListRowItem itemWeek = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_week), String.valueOf(selectedTea.getCounter().getWeek()));
        counterList.add(itemWeek);
        ListRowItem itemMonth = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_month), String.valueOf(selectedTea.getCounter().getMonth()));
        counterList.add(itemMonth);
        ListRowItem itemAll = new ListRowItem(getResources().getString(R.string.showtea_dialog_counter_overall), String.valueOf(selectedTea.getCounter().getOverall()));
        counterList.add(itemAll);

        //Liste mit Adapter verknüpfen
        CounterListAdapter adapter = new CounterListAdapter(this, counterList);
        //Adapter dem Listview hinzufügen
        ListView listViewCounter = new ListView(this);
        listViewCounter.setAdapter(adapter);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(listViewCounter);
        adb.setTitle(R.string.showtea_action_counter);
        adb.setIcon(R.drawable.ic_action_counter);
        adb.setPositiveButton(R.string.showtea_dialog_counter_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.show();
    }

    private void dialogamount(){
        ViewGroup parent = findViewById(R.id.showtea_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogNote = inflater.inflate(R.layout.dialogamount,parent,false);
        final SeekBar seekBarAmountPerAmount = alertLayoutDialogNote.findViewById(R.id.seekBarAmountPerAmount);
        final TextView textViewAmountPerAmount = alertLayoutDialogNote.findViewById(R.id.textViewShowAmountPerAmount);
        // 10 for 1 liter
        fillAmountPerAmount(10, textViewAmountPerAmount);

        seekBarAmountPerAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean b) {
                fillAmountPerAmount(value, textViewAmountPerAmount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogNote);
        adb.setTitle(R.string.showtea_dialog_amount);
        adb.setIcon(R.drawable.spoon);
        adb.setPositiveButton(R.string.showtea_dialog_amount_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    private void fillAmountPerAmount(int value, TextView textViewAmountPerAmount){
        Amount tmpAmount = selectedTea.getAmount();
        float liter = (float)value/10;
        float amountPerLiter = (float)tmpAmount.getValue()*liter;
        if(tmpAmount.getUnit().equals("Ts")){
            textViewAmountPerAmount.setText(getResources().getString(R.string.showtea_dialog_amount_per_amount_ts, amountPerLiter, liter));
        }else if(tmpAmount.getUnit().equals("Gr")){
            textViewAmountPerAmount.setText(getResources().getString(R.string.showtea_dialog_amount_per_amount_gr, amountPerLiter, liter));
        }

    }

    private void dialogShowTeaDescription(){
        ViewGroup parent = findViewById(R.id.showtea_parent);

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogNote = inflater.inflate(R.layout.dialogshowteadescription,parent,false);
        final CheckBox dontshowagain = alertLayoutDialogNote.findViewById(R.id.checkboxDialogShowTeaDescription);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogNote);
        adb.setTitle(R.string.showtea_dialog_description_header);
        adb.setPositiveButton(R.string.showtea_dialog_description_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(dontshowagain.isChecked()){
                    MainActivity.settings.setShowteaAlert(false);
                    MainActivity.settings.saveSettings(getApplicationContext());
                }
            }
        });
        adb.show();
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.buttonNotification) {
            showTooltip(view, Gravity.BOTTOM,getResources().getString(R.string.showtea_tooltip_notification));
        } else if (view.getId() == R.id.buttonVibration) {
            showTooltip(view, Gravity.BOTTOM,getResources().getString(R.string.showtea_tooltip_vibration));
        } else if (view.getId() == R.id.buttonNote) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.showtea_tooltip_note));
        } else if (view.getId() == R.id.buttonExchange) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.showtea_tooltip_exchange));
        } else if (view.getId() == R.id.buttonCalculateAmount) {
            showTooltip(view, Gravity.BOTTOM,getResources().getString(R.string.showtea_tooltip_calculateamount));
        } else if (view.getId() == R.id.toolbar_brewcount) {
            showTooltip(view, Gravity.BOTTOM,getResources().getString(R.string.showtea_tooltip_brewcount));
        } else if (view.getId() == R.id.toolbar_nextbrew) {
            showTooltip(view, Gravity.BOTTOM,getResources().getString(R.string.showtea_tooltip_nextbrew));
        }
        return true;
    }

    //creates a Tooltip
    private void showTooltip(View v, int gravity, String text){
        Tooltip tooltip = new Tooltip.Builder(v)
                .setText(text)
                .setTextColor(getResources().getColor(R.color.white))
                .setGravity(gravity)
                .setCornerRadius(8f)
                .setCancelable(true)
                .setDismissOnClick(true)
                .show();
    }
}
