package coolpharaoh.tee.speicher.tea.timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class NewTea extends AppCompatActivity {

    private enum Sort {
        BlackTea, GreenTea, YellowTea, WhiteTea, OolongTea, PuErhTea,
        HerbalTea, FruitTea, RooibusTea, Other
    }
    private int brewcount = 1;
    private ArrayList<Integer> temperatureList;
    private ArrayList<String> timeList;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PHOTO_REQUEST_CODE = 1;
    private TessBaseAPI tessBaseAPI;
    private Uri outputFileUri;
    private String lang = "deu";
    private String result = "empty";

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/Teespeicher/";
    private static final String TESSDATA = "tessdata";

    TextView textViewTeeArt;
    Spinner spinnerTeeArt;
    CheckBox checkboxTeeArt;
    EditText editTextTeeArt;
    EditText editTextName;
    EditText editTextTemperatur;
    EditText editTextZiehzeit;
    EditText editTextTeelamass;
    TextView textViewBrew;
    Button leftArrow;
    Button rightArrow;
    CheckBox dontShowAgain;
    int elementAt;
    boolean showTea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tea);

        //Toolbar definieren und erstellen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.newtea_heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Eingabefelder bestimmen
        textViewTeeArt = (TextView) findViewById(R.id.textViewTeaSort);
        spinnerTeeArt = (Spinner) findViewById(R.id.spinnerTeeart);
        checkboxTeeArt = (CheckBox) findViewById(R.id.checkBoxSelfInput);
        editTextTeeArt = (EditText) findViewById(R.id.editTextSelfInput);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextTemperatur = (EditText) findViewById(R.id.editTextTemperatur);
        editTextZiehzeit = (EditText) findViewById(R.id.editTextZiehzeit);
        editTextTeelamass = (EditText) findViewById(R.id.editTextTeelamass);
        textViewBrew = (TextView) findViewById(R.id.textViewCountBrew);
        Button scanName = (Button) findViewById(R.id.buttonScan);
        leftArrow = (Button) findViewById(R.id.buttonArrowLeft);
        rightArrow = (Button) findViewById(R.id.buttonArrowRight);
        Button addTea = (Button) findViewById(R.id.buttonfertig);

        //feste Texte setzten
        textViewTeeArt.setText(R.string.tea_sort);
        editTextName.setHint(getResources().getString(R.string.newtea_hint_name));
        addTea.setText(R.string.newtea_button_create);
        spinnerTeeArt.setPrompt(getResources().getString(R.string.tea_sort));
        checkboxTeeArt.setText(R.string.newtea_by_hand);
        editTextTeeArt.setHint(R.string.tea_sort);
        textViewBrew.setText(String.valueOf(brewcount) + ". " + getResources().getString(R.string.newtea_count_brew));

        //Zwei Tempuräre Listen erstellen
        temperatureList = new ArrayList<Integer>();
        timeList = new ArrayList<String>();

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerTimeAdapter = ArrayAdapter.createFromResource(
                    this, R.array.sortsOfTea, R.layout.spinner_item_sortoftea);

        spinnerTimeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sortoftea);
        spinnerTeeArt.setAdapter(spinnerTimeAdapter);

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.getIntent().getBooleanExtra("showTea", false);
        //Falls Änderung, dann wird ein Wert übergeben.
        elementAt  = this.getIntent().getIntExtra("elementAt", -1);
        if(elementAt!=-1){
            Tea selectedTea = MainActivity.teaItems.getTeaItems().get(elementAt);
            //richtige SpinnerId bekommen
            int spinnerId = -1;
            String[] spinnerElements = getResources().getStringArray(R.array.sortsOfTea);

            for(int i=0; i<spinnerElements.length; i++){
                if(spinnerElements[i].equals(selectedTea.getSortOfTea())){
                    spinnerId = i;
                    break;
                }
            }
            //Werte werden für Änderungen gefüllt
            //wenn Spinner manuell gefüllt wurde
            if(spinnerId==-1){
                spinnerTeeArt.setVisibility(View.INVISIBLE);
                spinnerTeeArt.setSelection(spinnerElements.length-1);
                textViewTeeArt.setVisibility(View.INVISIBLE);
                checkboxTeeArt.setVisibility(View.VISIBLE);
                checkboxTeeArt.setChecked(true);
                editTextTeeArt.setVisibility(View.VISIBLE);
                editTextTeeArt.setText(selectedTea.getSortOfTea());
            }else {
                spinnerTeeArt.setSelection(spinnerId);
            }
            editTextName.setText(selectedTea.getName());
            if(selectedTea.getTemperature().get(0)!=-500) editTextTemperatur.setText(String.valueOf(selectedTea.getTemperature().get(0)));
            temperatureList = selectedTea.getTemperature();
            if(!selectedTea.getTime().get(0).equals("-")) editTextZiehzeit.setText(selectedTea.getTime().get(0));
            timeList = selectedTea.getTime();
            if(selectedTea.getTeelamass()!=-500) editTextTeelamass.setText(String.valueOf(selectedTea.getTeelamass()));
            //Button Text ändern
            addTea.setText(R.string.newtea_button_edit);

        }

        //Spinner Teeart hat sich verändert
        spinnerTeeArt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sort sort = Sort.values()[position];
                if(sort.equals(Sort.Other)){
                    checkboxTeeArt.setVisibility(View.VISIBLE);
                }else{
                    checkboxTeeArt.setVisibility(View.INVISIBLE);
                }
                //Tipps für Temperatur und Ziehzeit anhand der Teesorte
                switch(sort){
                    case BlackTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_blacktea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_blacktea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_blacktea);
                        break;
                    case GreenTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_greentea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_greentea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_greentea);
                        break;
                    case YellowTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_yellowtea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_yellowtea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_yellowtea);
                        break;
                    case WhiteTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_whitetea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_whitetea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_whitetea);
                        break;
                    case OolongTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_oolongtea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_oolongtea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_oolongtea);
                        break;
                    case PuErhTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_puerhtea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_puerhtea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_puerhtea);
                        break;
                    case HerbalTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_herbaltea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_herbaltea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_herbaltea);
                        break;
                    case FruitTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_fruittea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_fruittea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_fruittea);
                        break;
                    case RooibusTea:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_rooibustea);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_rooibustea);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_rooibustea);
                        break;
                    case Other:
                        editTextTemperatur.setHint(R.string.newtea_hint_temperature_other);
                        editTextTeelamass.setHint(R.string.newtea_hint_teelamass_other);
                        editTextZiehzeit.setHint(R.string.newtea_hint_time_other);
                        break;
                    default: break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Checkbox Teeart wurde angeklickt
        checkboxTeeArt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textViewTeeArt.setVisibility(View.INVISIBLE);
                    spinnerTeeArt.setVisibility(View.INVISIBLE);
                    editTextTeeArt.setVisibility(View.VISIBLE);
                }else{
                    textViewTeeArt.setVisibility(View.VISIBLE);
                    spinnerTeeArt.setVisibility(View.VISIBLE);
                    editTextTeeArt.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Hier wird mit Texterkennung der Name des Tee's ermittelt
        scanName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.settings.isOcrAlert()) {
                    dialogBeforeScan();
                }else {
                    startCameraActivity();
                }
            }
        });

        //Left und Rightarrow ->Alle Werte bis auf der letzte sind Valide!
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = false;
                //wenn man am Ende der Liste ist, ist es möglich den eingebenen Wert zu löschen
                if(temperatureList.size() <= brewcount &&
                        editTextTemperatur.getText().toString().equals("") &&
                        editTextZiehzeit.getText().toString().equals("")){
                    if(temperatureList.size() == brewcount){
                        temperatureList.remove(brewcount-1);
                    }
                    check = true;
                }else {
                    check = checkTemperatureAndTime();
                }
                if(check){
                    brewcount--;
                    textViewBrew.setText(String.valueOf(brewcount) + ". " + getResources().getString(R.string.newtea_count_brew));
                    if(temperatureList.size()<brewcount){
                        editTextTemperatur.setText("");
                        editTextZiehzeit.setText("");
                    }else{
                        if(temperatureList.get(brewcount-1)!=-500) {
                            editTextTemperatur.setText(String.valueOf(temperatureList.get(brewcount - 1)));
                        }else {
                            editTextTemperatur.setText("");
                        }
                        if(!timeList.get(brewcount-1).equals("-")) {
                            editTextZiehzeit.setText(timeList.get(brewcount - 1));
                        }else {
                            editTextZiehzeit.setText("");
                        }
                    }
                    if(brewcount<=1){
                        leftArrow.setEnabled(false);
                    }else{
                        rightArrow.setEnabled(true);
                    }
                }
            }
        });

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = checkTemperatureAndTime();
                if(check) {
                    brewcount++;
                    textViewBrew.setText(String.valueOf(brewcount) + ". " + getResources().getString(R.string.newtea_count_brew));
                    if(temperatureList.size()<brewcount){
                        editTextTemperatur.setText("");
                        editTextZiehzeit.setText("");
                    }else{
                        if(temperatureList.get(brewcount-1)!=-500) {
                            editTextTemperatur.setText(String.valueOf(temperatureList.get(brewcount - 1)));
                        }else {
                            editTextTemperatur.setText("");
                        }
                        if(!timeList.get(brewcount-1).equals("-")) {
                            editTextZiehzeit.setText(timeList.get(brewcount - 1));
                        }else {
                            editTextZiehzeit.setText("");
                        }
                    }
                    if (brewcount >= 5) {
                        rightArrow.setEnabled(false);
                    } else {
                        leftArrow.setEnabled(true);
                    }
                }
            }
        });

        //Alles wurde Eingeben und die Werte werden aufgenommen
        addTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Der Name muss eingegeben werden
                if(!editTextName.getText().toString().equals("")) {
                    //Attribute auslesen
                    boolean sortValid = true;
                    String sortOfTea = "";
                    if(checkboxTeeArt.isChecked()){
                        sortOfTea = editTextTeeArt.getText().toString();
                        sortValid = !(sortOfTea.length()>30);
                        if(sortOfTea.length()==0){
                            sortOfTea = "-";
                        }
                    }else {
                        sortOfTea = (String) spinnerTeeArt.getSelectedItem();
                    }
                    //Ist der Name Valide
                    String name = editTextName.getText().toString();
                    boolean nameValid = nameValid(name);

                    //Ist die Temperatur nicht gesetzt, so ist sie -500
                    int temperature = -500;
                    boolean temperatureValid = temperatureValid(editTextTemperatur.getText().toString());
                    if (temperatureValid && !editTextTemperatur.getText().toString().equals("")) {
                        temperature = Integer.parseInt(editTextTemperatur.getText().toString());
                    }

                    //Ist Zeit nicht gesetzt so ist sie -
                    String time = "-";
                    boolean timeValid = timeValid(editTextZiehzeit.getText().toString());
                    if(timeValid && !editTextZiehzeit.getText().toString().equals("")){
                        time = editTextZiehzeit.getText().toString();
                    }

                    //Ist teelamass nicht gesetzt so ist es -500
                    int teelamass = -500;
                    boolean teelamassValid = teelamassValid(editTextTeelamass.getText().toString());
                    if(teelamassValid && !editTextTeelamass.getText().toString().equals("")){
                        teelamass = Integer.parseInt(editTextTeelamass.getText().toString());
                    }

                    //Temperatur muss zwischen 100 und 0 sein und die Zeit braucht das richtige Format
                    if(!sortValid) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_30Char, Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (!nameValid) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_name_just_exist, Toast.LENGTH_SHORT);
                        toast.show();
                    } else if ((temperature > 100 || temperature < 0) && (temperature != -500 || !temperatureValid)) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_temperature, Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (!timeValid) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
                        toast.show();
                    } else if(!teelamassValid){
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_amount, Toast.LENGTH_SHORT);
                        toast.show();
                    }else if(((temperatureList.size() > brewcount && (time.equals("-") || temperature == -500)) ||
                            (temperatureList.size() <= brewcount && (!time.equals("-") && temperature == -500)) ||
                            (temperatureList.size() <= brewcount && (time.equals("-") && temperature != -500))) &&
                            temperatureList.size()!=0 && !(temperatureList.size()==1 && brewcount==1)){
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_data_for_this_brew, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        //Time und Temperature
                        if(temperatureList.size() > brewcount) {
                            temperatureList.set(brewcount - 1, temperature);
                            timeList.set(brewcount - 1, time);
                        }else if(temperatureList.size() == 0){
                            temperatureList.add(temperature);
                            timeList.add(time);
                        }else if(temperatureList.size() == 1 && brewcount == 1){
                            temperatureList.set(brewcount-1, temperature);
                            timeList.set(brewcount-1, time);
                        }else if(temperatureList.size()==brewcount && temperature==-500 && time.equals("-")) {
                            temperatureList.remove(brewcount-1);
                            timeList.remove(brewcount-1);
                        }else if(temperatureList.size()<=brewcount && temperature!=-500 && !time.equals("-")){
                            temperatureList.add(temperature);
                            timeList.add(time);
                        }

                        if (!(elementAt == -1)) {
                            //Tee wird geändert
                            MainActivity.teaItems.getTeaItems().get(elementAt).setName(name);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setSortOfTea(sortOfTea);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTemperature(temperatureList);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTime(timeList);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTeelamass(teelamass);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setCurrentDate();
                            //teaItems persistent speichern
                            MainActivity.teaItems.sort();
                            if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_change, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            //erstelle Tee
                            Tea tea = new Tea(name, sortOfTea, temperatureList, timeList, teelamass);
                            tea.setCurrentDate();
                            MainActivity.teaItems.getTeaItems().add(tea);
                            //teaItems persistent speichern
                            MainActivity.teaItems.sort();
                            if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_save, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        if(!showTea) {
                            //Adapter wird aktualisiert
                            MainActivity.adapter.notifyDataSetChanged();
                            //wechsel das Fenster
                            finish();
                        } else{
                            //Adapter wird aktualisiert
                            MainActivity.adapter.notifyDataSetChanged();
                            //Neues Intent anlegen
                            Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
                            //find out elementAt by Name
                            elementAt = MainActivity.teaItems.getPositionByName(name);
                            showteaScreen.putExtra("elementAt", elementAt);
                            // Intent starten und zur zweiten Activity wechseln
                            startActivity(showteaScreen);
                            finish();
                        }
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_name, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_new_tea, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(showTea) {
            //Neues Intent anlegen
            Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
            showteaScreen.putExtra("elementAt", elementAt);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(showteaScreen);
            finish();
            return true;
        }
        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    //Valid Functions
    private boolean checkTemperatureAndTime(){
        boolean checkValid = true;
        String tmpTemperature = editTextTemperatur.getText().toString();
        int temperature = -500;
        String time = editTextZiehzeit.getText().toString();

        if(tmpTemperature.equals("") || time.equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_data_before_next_brew, Toast.LENGTH_SHORT);
            toast.show();
            checkValid = false;
        }else {
            boolean temperatureValid = temperatureValid(tmpTemperature);
            boolean timeValid = timeValid(time);
            if (temperatureValid) {
                temperature = Integer.parseInt(tmpTemperature);
            }
            if (!temperatureValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_temperature, Toast.LENGTH_SHORT);
                toast.show();
                checkValid = false;
            }
            if (!timeValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
                toast.show();
                checkValid = false;
            }
            if (temperature > 100 || temperature < 0) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_temperature, Toast.LENGTH_SHORT);
                toast.show();
                checkValid = false;
            }
            if (checkValid) {
                if (temperatureList.size() < brewcount) {
                    temperatureList.add(temperature);
                    timeList.add(time);
                } else {
                    temperatureList.set(brewcount - 1, temperature);
                    timeList.set(brewcount - 1, time);
                }
            }
        }
        return checkValid;
    }

    private boolean nameValid(String name){
        boolean nameValid = true;
        for(int i=0; i<MainActivity.teaItems.getTeaItems().size(); i++){
            if(name.equals(MainActivity.teaItems.getTeaItems().get(i).getName()) && elementAt!=i){
                nameValid = false;
                break;
            }
        }
        return nameValid;
    }

    private boolean temperatureValid(String temperature){
        boolean temperatureValid = true;
        if (!temperature.equals("")) {
            if (temperature.contains(".") || temperature.length() > 3) {
                temperatureValid = false;
            }
        }
        return temperatureValid;
    }

    private boolean teelamassValid(String teelamass){
        boolean teelamassValid = true;
        if (!teelamass.equals("")) {
            if (teelamass.contains(".") || teelamass.length() > 3) {
                teelamassValid = false;
            }
        }
        return teelamassValid;
    }

    private boolean timeValid(String time){
        boolean timeValid = true;
        //ist die Zeit gesetzt so wird sie geprüft
        if (!time.equals("")) {
            String[] split = time.split(":");
            if((time.contains(":") && split.length == 2)|| !time.contains(":")) {
                if (split.length <= 2) {
                    for (int i = 0; i < split.length; i++) {
                        if(!split[i].equals("")) {
                            if (split[i].length() <= 2) {
                                if (Integer.parseInt(split[i]) >= 60)
                                    timeValid = false;
                            } else timeValid = false;
                        }else timeValid = false;
                        if (i == 1 && split[i].length() != 2) timeValid = false;
                    }
                } else timeValid = false;
            } else timeValid = false;
        }
        return timeValid;
    }

    //Show the Dialog befor using the The Scan
    private void dialogBeforeScan(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogBeforeScan = inflater.inflate(R.layout.dialogbeforescan, null);
        TextView textViewDialogBeforeScan = (TextView) alertLayoutDialogBeforeScan.findViewById(R.id.textViewDialogBeforeScan);
        dontShowAgain = (CheckBox) alertLayoutDialogBeforeScan.findViewById(R.id.checkboxDialogBeforeScan);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogBeforeScan);
        adb.setTitle(R.string.newtea_dialog_before_scan_title);
        textViewDialogBeforeScan.setText(R.string.newtea_dialog_before_scan_text);
        dontShowAgain.setText(R.string.newtea_dialog_before_scan_check);
        adb.setPositiveButton(R.string.newtea_dialog_scan_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (dontShowAgain.isChecked()) {
                        MainActivity.settings.setOcrAlert(false);
                        MainActivity.settings.saveSettings(getApplicationContext());
                    }
                    startCameraActivity();
                }
        });
        adb.setNegativeButton(R.string.newtea_dialog_scan_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    /**
     * Take a Picture
     *
     * @throws Exception
     */
    private void startCameraActivity(){
        try{
            String IMGS_PATH = Environment.getExternalStorageDirectory().toString() + "/Teespeicher/imgs";
            prepareDirectory(IMGS_PATH);

            String img_path = IMGS_PATH + "/ocr.jpg";

            outputFileUri = Uri.fromFile(new File(img_path));

            final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            if(takePictureIntent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(takePictureIntent, PHOTO_REQUEST_CODE);
            }

        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Prepare directory on external storage
     *
     * @param path
     * @throws Exception
     */
    private void prepareDirectory(String path){
        File dir = new File(path);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                Log.e(TAG, "ERROR: Creation of directory. " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else{
            Log.i(TAG, "Created directory " + path);
        }
    }

    /**
     * exploit results
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Foto is ready
        if(requestCode == PHOTO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            CropImage.activity(outputFileUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        } else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                prepareTesseract();
                startOCR(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }

    private void prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch(Exception e) {
            e.printStackTrace();
        }
        copyTessDataFiles(TESSDATA);
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path
     */
    private void copyTessDataFiles(String path) {
        try {
            String fileList[] = getAssets().list(path);

            for(String fileName : fileList){

                //open file within the assets folder
                //if it ist not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if(!(new File(pathToDataFile)).exists()){

                    InputStream in = getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    //Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while((len = in.read(buf)) > 0){
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d(TAG, "Copied " + fileName + " to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
        }
    }

    /**
     *
     * @param imgUri
     */
    private void startOCR(Uri imgUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);

            result = extractText(bitmap);

            //Text ist in Result
            editText(result);   //Text in Alertfenster

        } catch(Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    private String extractText(Bitmap bitmap){
        try {
            tessBaseAPI = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseAPI == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }

        if(MainActivity.settings.getLanguage().equals("de")){
            lang = "deu";
        }else if(MainActivity.settings.getLanguage().equals("en")){
            lang = "eng";
        }

        tessBaseAPI.init(DATA_PATH, lang);

        //       //EXTRA SETTINGS
        //        //For example if we only want to detect numbers
        //        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
        //
        //        //blackList Example
        //        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
        //                "YTRWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");

        Log.d(TAG, "Training file loaded");
        tessBaseAPI.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseAPI.getUTF8Text();
        } catch(Exception e) {
            Log.e(TAG, "Error in recognizing text");
        }
        tessBaseAPI.end();
        return extractedText;
    }

    private void editText (String result){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);alert.setTitle(R.string.newtea_dialog_after_scan_title);

        //result to Camelcase
        result = convertToTitleCase(result);

        //Set an EditText view to geht user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(8);
        input.setMinLines(6);
        input.setMaxLines(10);
        input.setGravity(Gravity.TOP|Gravity.LEFT);
        input.setSingleLine(false);
        input.setMovementMethod(new ScrollingMovementMethod());
        input.setText(result);
        alert.setView(input);

        alert.setPositiveButton(R.string.newtea_dialog_scan_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String tmp = input.getText().toString();
                tmp = tmp.replaceAll("\n", " ");
                editTextName.setText(tmp);
            }
        });
        alert.setNegativeButton(R.string.newtea_dialog_scan_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Canceled.
            }
        });
        alert.show();
    }

    private String convertToTitleCase(String words){
        words = words.toLowerCase();
        StringBuffer result = new StringBuffer("");
        Boolean blank = true;
        for(int i=0; i<words.length(); i++){
            if(blank){
                result.append(String.valueOf(words.charAt(i)).toUpperCase());
                blank = false;
            }else if(words.charAt(i)==' ' || words.charAt(i)=='\n' || words.charAt(i)=='-'){
                result.append(words.charAt(i));
                blank = true;
            }else {
                result.append(words.charAt(i));
            }
        }
        return result.toString();
    }
}
