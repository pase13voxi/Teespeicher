package coolpharaoh.tee.speicher.tea.timer.views;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.ArrayList;
import java.util.regex.Pattern;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Amount;
import coolpharaoh.tee.speicher.tea.timer.datastructure.AmountGramm;
import coolpharaoh.tee.speicher.tea.timer.datastructure.AmountTs;
import coolpharaoh.tee.speicher.tea.timer.datastructure.SortOfTea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.NTea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.NTemperature;
import coolpharaoh.tee.speicher.tea.timer.datastructure.NTemperatureCelsius;
import coolpharaoh.tee.speicher.tea.timer.datastructure.NTemperatureFahrenheit;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Time;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Variety;


public class NewTea extends AppCompatActivity {

    private Variety variety = Variety.BlackTea;ColorPickerDialog colorPickerDialog;
    int color = SortOfTea.getVariatyColor(Variety.BlackTea);
    private int brewcount = 1;
    private ArrayList<NTemperature> temperatureList;
    private ArrayList<Time> steepingTimeList;
    private ArrayList<Time> timeList;
    private String amountUnit = "Ts";


    private TextView textViewTeeArt;
    private Spinner spinnerTeeArt;
    private CheckBox checkboxTeeArt;
    private EditText editTextTeeArt;
    private Button buttonColor;
    private GradientDrawable buttonColorSape;
    private EditText editTextName;
    private EditText editTextTemperatur;
    private EditText editTextZiehzeit;
    private EditText editTextTeelamass;
    private Spinner spinnerAmount;
    private TextView textViewBrew;
    private Button leftArrow;
    private Button rightArrow;
    private int elementAt;
    private boolean showTea, colorChange;

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
        buttonColor = (Button) findViewById(R.id.buttonColor);
        buttonColorSape = (GradientDrawable)buttonColor.getBackground();
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextTemperatur = (EditText) findViewById(R.id.editTextTemperatur);
        editTextZiehzeit = (EditText) findViewById(R.id.editTextZiehzeit);
        editTextTeelamass = (EditText) findViewById(R.id.editTextTeelamass);
        spinnerAmount = (Spinner) findViewById(R.id.spinnerAmountUnit);
        textViewBrew = (TextView) findViewById(R.id.textViewCountBrew);
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
        buttonColorSape.setColor(color);
        textViewBrew.setText(String.valueOf(brewcount) + ". " + getResources().getString(R.string.newtea_count_brew));

        //Zwei Tempuräre Listen erstellen
        temperatureList = new ArrayList<>();
        timeList = new ArrayList<>();

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerSortAdapter = ArrayAdapter.createFromResource(
                    this, R.array.sortsOfTea, R.layout.spinner_item_sortoftea);

        spinnerSortAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_sortoftea);
        spinnerTeeArt.setAdapter(spinnerSortAdapter);

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerAmountAdapter = ArrayAdapter.createFromResource(
                this, R.array.newtea_amount, R.layout.spinner_item_amount_unit);

        spinnerAmountAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_amount_unit);
        spinnerAmount.setAdapter(spinnerAmountAdapter);

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.getIntent().getBooleanExtra("showTea", false);
        //Falls Änderung, dann wird ein Wert übergeben.
        elementAt  = this.getIntent().getIntExtra("elementAt", -1);
        if(elementAt!=-1){
            NTea selectedTea = MainActivity.teaItems.getTeaItems().get(elementAt);
            //richtige SpinnerId bekommen
            int spinnerId = -1;
            String[] spinnerElements = getResources().getStringArray(R.array.sortsOfTea);

            for(int i=0; i<spinnerElements.length; i++){
                if(spinnerElements[i].equals(selectedTea.getSortOfTea().getType())){
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
                editTextTeeArt.setText(selectedTea.getSortOfTea().getType());
            }else {
                spinnerTeeArt.setSelection(spinnerId);
            }
            color = selectedTea.getColor();
            buttonColorSape.setColor(color);
            colorChange = true;
            editTextName.setText(selectedTea.getName());
            temperatureList = selectedTea.getTemperature();
            //richtige SpinnerId bekommen
            amountUnit = selectedTea.getAmount().getUnit();
            switch(amountUnit){
                case "Ts": spinnerAmount.setSelection(0); break;
                case "Gr": spinnerAmount.setSelection(1); break;
            }
            if(selectedTea.getTemperature().get(0).getCelsius()!=-500){
                editTextTemperatur.setText(String.valueOf(getTemperature(0)));
            }
            if(!selectedTea.getTime().get(0).getTime().equals("-")) editTextZiehzeit.setText(selectedTea.getTime().get(0).getTime());
            timeList = selectedTea.getTime();
            if(selectedTea.getAmount().getValue()!=-500) editTextTeelamass.setText(String.valueOf(selectedTea.getAmount().getValue()));
            //Button Text ändern
            addTea.setText(R.string.newtea_button_edit);

        }

        //Spinner Teeart hat sich verändert
        spinnerTeeArt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                variety = Variety.values()[position];
                //Farbe soll am Anfang nicht geändert werden, wenn der Tee geändert wird
                if(!colorChange){
                    color = SortOfTea.getVariatyColor(variety);
                    buttonColorSape.setColor(color);
                }else{
                    colorChange = false;
                }
                if(variety.equals(Variety.Other)){
                    checkboxTeeArt.setVisibility(View.VISIBLE);
                }else{
                    checkboxTeeArt.setVisibility(View.INVISIBLE);
                }
                sethints();
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

        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorPickerDialog = new ColorPickerDialog(NewTea.this, color);
                colorPickerDialog.setTitle(getResources().getString(R.string.newtea_color_dialog_title));
                colorPickerDialog.setOnColorChangedListener(new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int c) {
                        color = c;
                        buttonColorSape.setColor(color);
                    }
                });
                colorPickerDialog.show();
            }
        });

        //unit hat sich verändert
        spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0: amountUnit = "Ts"; break;
                    case 1: amountUnit = "Gr"; break;
                }
                sethints();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Left und Rightarrow ->Alle Werte bis auf der letzte sind Valide!
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check;
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
                        if(temperatureList.get(brewcount-1).getCelsius()!=-500) {
                            editTextTemperatur.setText(String.valueOf(getTemperature(brewcount - 1)));
                        }else {
                            editTextTemperatur.setText("");
                        }
                        if(!timeList.get(brewcount-1).getTime().equals("-")) {
                            editTextZiehzeit.setText(timeList.get(brewcount - 1).getTime());
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
                        if(temperatureList.get(brewcount-1).getCelsius()!=-500) {
                            editTextTemperatur.setText(String.valueOf(getTemperature(brewcount - 1)));
                        }else {
                            editTextTemperatur.setText("");
                        }
                        if(!timeList.get(brewcount-1).getTime().equals("-")) {
                            editTextZiehzeit.setText(timeList.get(brewcount - 1).getTime());
                        }else {
                            editTextZiehzeit.setText("");
                        }
                    }
                    if (brewcount >= 20) {
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
                    String sortOfTea;
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
                    int checktemperature = 0;
                    boolean temperatureValid = temperatureValid(editTextTemperatur.getText().toString());
                    if (temperatureValid && !editTextTemperatur.getText().toString().equals("")) {
                        temperature = Integer.parseInt(editTextTemperatur.getText().toString());
                    }
                    if (MainActivity.settings.getTemperatureUnit().equals("Celsius")) checktemperature = temperature;
                    else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) checktemperature = NTemperature.fahrenheitToCelsius(temperature);

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
                    } else if ((checktemperature > 100 || checktemperature < 0) && (temperature != -500 || !temperatureValid)) {
                        if(MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_celsius, Toast.LENGTH_SHORT);
                            toast.show();
                        }else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")){
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_fahrenheit, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else if (!timeValid) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
                        toast.show();
                    } else if(!teelamassValid){
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_amount, Toast.LENGTH_SHORT);
                        toast.show();
                    }else if(((temperatureList.size() == brewcount && ((time.equals("-") && temperature != -500)||(!time.equals("-") && temperature == -500))) ||
                            (temperatureList.size() > brewcount && (time.equals("-") || temperature == -500)) ||
                            (temperatureList.size() < brewcount && ((time.equals("-") && temperature != -500)||(!time.equals("-") && temperature == -500)))) &&
                            temperatureList.size()!=0 && !(temperatureList.size()==1 && brewcount==1)){
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_data_for_this_brew, Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else {
                        //Time und Temperature
                        if(temperatureList.size()==0 && brewcount==1){
                            NTemperature temperatureNew = createTemperature(temperature);
                            temperatureList.add(temperatureNew);
                            steepingTimeList.add(new Time(NTemperature.celsiusToSteepingTime(temperatureNew.getCelsius())));
                            timeList.add(new Time(time));
                        }else if(temperatureList.size()==1 && brewcount==1){
                            NTemperature temperatureNew = createTemperature(temperature);
                            temperatureList.set(brewcount-1,temperatureNew);
                            steepingTimeList.set(brewcount-1,new Time(NTemperature.celsiusToSteepingTime(temperatureNew.getCelsius())));
                            timeList.set(brewcount-1,new Time(time));
                        }else if(temperatureList.size()==brewcount){
                            if(temperature==-500 && time.equals("-")){
                                temperatureList.remove(brewcount-1);
                                steepingTimeList.remove(brewcount-1);
                                timeList.remove(brewcount-1);
                            }else {
                                NTemperature temperatureNew = createTemperature(temperature);
                                temperatureList.set(brewcount-1,temperatureNew);
                                steepingTimeList.set(brewcount-1,new Time(NTemperature.celsiusToSteepingTime(temperatureNew.getCelsius())));
                                timeList.set(brewcount-1,new Time(time));
                            }
                        }else if(temperatureList.size()>brewcount){
                            if(temperature!=-500 && !time.equals("-")){
                                NTemperature temperatureNew = createTemperature(temperature);
                                temperatureList.set(brewcount-1,temperatureNew);
                                steepingTimeList.set(brewcount-1,new Time(NTemperature.celsiusToSteepingTime(temperatureNew.getCelsius())));
                                timeList.set(brewcount-1,new Time(time));
                            }
                        }else if(temperatureList.size()<brewcount){
                            if(temperature!=-500 && !time.equals("-")){
                                NTemperature temperatureNew = createTemperature(temperature);
                                temperatureList.add(temperatureNew);
                                steepingTimeList.add(new Time(NTemperature.celsiusToSteepingTime(temperatureNew.getCelsius())));
                                timeList.add(new Time(time));
                            }
                        }

                        if (!(elementAt == -1)) {
                            //Tee wird geändert
                            MainActivity.teaItems.getTeaItems().get(elementAt).setName(name);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setSortOfTea(new SortOfTea(sortOfTea));
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTemperature(temperatureList);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTime(timeList);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setAmount(createAmount(teelamass));
                            MainActivity.teaItems.getTeaItems().get(elementAt).setColor(color);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setCurrentDate();
                            //teaItems persistent speichern
                            MainActivity.teaItems.sort();
                            if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_change, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            //erstelle Tee
                            NTea tea = new NTea(name, new SortOfTea(sortOfTea), temperatureList, steepingTimeList, timeList,
                                    createAmount(teelamass), color);
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
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_data_before_next_brew, Toast.LENGTH_LONG);
            toast.show();
            checkValid = false;
        }else {
            boolean temperatureValid = temperatureValid(tmpTemperature);
            boolean timeValid = timeValid(time);
            if (temperatureValid) {
                temperature = Integer.parseInt(tmpTemperature);
            }
            if (!temperatureValid) {
                if(MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_celsius, Toast.LENGTH_SHORT);
                    toast.show();
                }else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_fahrenheit, Toast.LENGTH_SHORT);
                    toast.show();
                }
                checkValid = false;
            }
            if (!timeValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
                toast.show();
                checkValid = false;
            }
            int checktemperature = 0;
            if(MainActivity.settings.getTemperatureUnit().equals("Celsius")) checktemperature = temperature;
            else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) checktemperature = NTemperature.fahrenheitToCelsius(temperature);
            if (checktemperature > 100 || checktemperature < 0) {
                if(MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_celsius, Toast.LENGTH_SHORT);
                    toast.show();
                }else if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")){
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_fahrenheit, Toast.LENGTH_SHORT);
                    toast.show();
                }
                checkValid = false;
            }
            if (checkValid) {
                if (temperatureList.size() < brewcount) {
                    temperatureList.add(createTemperature(temperature));
                    timeList.add(new Time(time));
                } else {
                    temperatureList.set(brewcount - 1, createTemperature(temperature));
                    timeList.set(brewcount - 1, new Time(time));
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
        boolean timeValid;
        //ist die Zeit gesetzt so wird sie geprüft
        timeValid = time.length()<6;
        if (timeValid && !time.equals("")) {
            boolean formatMinutes = Pattern.matches("\\d\\d",time)||Pattern.matches("\\d",time);
            boolean formatSeconds = Pattern.matches("\\d\\d:\\d\\d",time) || Pattern.matches("\\d:\\d\\d",time);
            if(formatMinutes){
                timeValid = Integer.parseInt(time) < 60;
            }else if(formatSeconds){
                String[] split = time.split(":");
                timeValid = Integer.parseInt(split[0])<60 && Integer.parseInt(split[1])<60;
            }else{
                timeValid = false;
            }
        }
        return timeValid;
    }

    private NTemperature createTemperature(int value){
        switch(MainActivity.settings.getTemperatureUnit()){
            case "Celsius": return new NTemperatureCelsius(value);
            case "Fahrenheit": return new NTemperatureFahrenheit(value);
            default: return null;
        }
    }

    private int getTemperature(int index){
        switch(MainActivity.settings.getTemperatureUnit()){
            case "Celsius": return temperatureList.get(index).getCelsius();
            case "Fahrenheit": return temperatureList.get(index).getFahrenheit();
            default: return -500;
        }
    }

    private Amount createAmount(int value){
        switch(amountUnit){
            case "Ts": return new AmountTs(value);
            case "Gr": return new AmountGramm(value);
            default: return null;
        }
    }

    private void sethints(){
        //set Hint for variety
        editTextTemperatur.setHint(SortOfTea.getHintTemperature(getApplicationContext(), variety,
                MainActivity.settings.getTemperatureUnit()));
        editTextTeelamass.setHint(SortOfTea.getHintAmount(getApplicationContext(), variety, amountUnit));
        editTextZiehzeit.setHint(SortOfTea.getHintTime(getApplicationContext(), variety));
    }
}
