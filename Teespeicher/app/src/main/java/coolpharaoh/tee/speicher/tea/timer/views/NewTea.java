package coolpharaoh.tee.speicher.tea.timer.views;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tooltip.Tooltip;

import net.margaritov.preference.colorpicker.ColorPickerDialog;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Amount;
import coolpharaoh.tee.speicher.tea.timer.datastructure.AmountGramm;
import coolpharaoh.tee.speicher.tea.timer.datastructure.AmountTs;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Coloring;
import coolpharaoh.tee.speicher.tea.timer.datastructure.N2Tea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.SortOfTea;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Temperature;
import coolpharaoh.tee.speicher.tea.timer.datastructure.TemperatureCelsius;
import coolpharaoh.tee.speicher.tea.timer.datastructure.TemperatureFahrenheit;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Time;
import coolpharaoh.tee.speicher.tea.timer.datastructure.Variety;


public class NewTea extends AppCompatActivity implements View.OnLongClickListener{

    private Variety variety = Variety.BlackTea;
    ColorPickerDialog colorPickerDialog;
    int color = SortOfTea.getVariatyColor(Variety.BlackTea);
    private int brewIndex = 0;
    private int brewSize = 1;
    private ArrayList<Temperature> temperatureList;
    private ArrayList<Time> coolDownTimeList;
    private ArrayList<Time> timeList;
    private String amountUnit = "Ts";


    private TextView textViewTeaSort;
    private Spinner spinnerTeaVariety;
    private CheckBox checkboxTeaSort;
    private EditText editTextTeaSort;
    private Button buttonColor;
    private GradientDrawable buttonColorSape;
    private EditText editTextName;
    private EditText editTextTemperature;
    private Button buttonShowCoolDowntime;
    private EditText editTextCoolDownTime;
    private Button buttonAutofillCoolDownTime;
    private EditText editTextSteepingTime;
    private EditText editTextAmount;
    private Spinner spinnerAmount;
    private TextView textViewBrew;
    private Button leftArrow;
    private Button rightArrow;
    private Button deleteBrew;
    private Button addBrew;
    private UUID elementId;
    private int elementAt = -1;
    private boolean showTea, colorChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tea);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Toolbar definieren und erstellen
        Toolbar toolbar = findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.newtea_heading);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Eingabefelder bestimmen
        textViewTeaSort = findViewById(R.id.textViewTeaSort);
        spinnerTeaVariety = findViewById(R.id.spinnerTeaSort);
        checkboxTeaSort = findViewById(R.id.checkBoxSelfInput);
        editTextTeaSort = findViewById(R.id.editTextSelfInput);
        buttonColor = findViewById(R.id.buttonColor);
        buttonColorSape = (GradientDrawable) buttonColor.getBackground();
        editTextName = findViewById(R.id.editTextName);
        editTextTemperature = findViewById(R.id.editTextTemperature);
        buttonShowCoolDowntime = findViewById(R.id.buttonShowCoolDownTime);
        editTextCoolDownTime = findViewById(R.id.editTextCoolDownTime);
        buttonAutofillCoolDownTime = findViewById(R.id.buttonAutofillCoolDownTime);
        editTextSteepingTime = findViewById(R.id.editTextTime);
        editTextAmount = findViewById(R.id.editTextAmount);
        spinnerAmount = findViewById(R.id.spinnerAmountUnit);
        textViewBrew = findViewById(R.id.textViewCountBrew);
        leftArrow = findViewById(R.id.buttonArrowLeft);
        rightArrow = findViewById(R.id.buttonArrowRight);
        deleteBrew = findViewById(R.id.buttonDeleteBrew);
        addBrew = findViewById(R.id.buttonAddBrew);

        //feste Texte setzten
        textViewTeaSort.setText(R.string.tea_sort);
        editTextName.setHint(getResources().getString(R.string.newtea_hint_name));
        spinnerTeaVariety.setPrompt(getResources().getString(R.string.tea_sort));
        checkboxTeaSort.setText(R.string.newtea_by_hand);
        editTextTeaSort.setHint(R.string.tea_sort);
        buttonColorSape.setColor(color);
        textViewBrew.setText(getResources().getString(R.string.newtea_count_brew,brewIndex + 1,". "));

        //drei tempuräre Listen erstellen
        temperatureList = new ArrayList<>();
        coolDownTimeList = new ArrayList<>();
        timeList = new ArrayList<>();
        addNewBrew();

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerVarietyAdapter = ArrayAdapter.createFromResource(
                this, R.array.sortsOfTea, R.layout.spinner_item_varietyoftea);

        spinnerVarietyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_varietyoftea);
        spinnerTeaVariety.setAdapter(spinnerVarietyAdapter);

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerAmountAdapter = ArrayAdapter.createFromResource(
                this, R.array.newtea_amount, R.layout.spinner_item_amount_unit);

        spinnerAmountAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_amount_unit);
        spinnerAmount.setAdapter(spinnerAmountAdapter);

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.getIntent().getBooleanExtra("showTea", false);
        //Falls Änderung, dann wird ein Wert übergeben.
        elementId = (UUID) this.getIntent().getSerializableExtra("elementId");
        if (elementId != null) {
            elementAt = MainActivity.teaItems.getPositionById(elementId);
            N2Tea selectedTea = MainActivity.teaItems.getTeaItems().get(elementAt);
            //richtige SpinnerId bekommen
            int spinnerId = -1;
            String[] spinnerElements = getResources().getStringArray(R.array.sortsOfTea);

            for (int i = 0; i < spinnerElements.length; i++) {
                if (spinnerElements[i].equals(selectedTea.getSortOfTea().getType())) {
                    spinnerId = i;
                    break;
                }
            }
            //Werte werden für Änderungen gefüllt
            //wenn Spinner manuell gefüllt wurde
            if (spinnerId == -1) {
                spinnerTeaVariety.setVisibility(View.INVISIBLE);
                spinnerTeaVariety.setSelection(spinnerElements.length - 1);
                textViewTeaSort.setVisibility(View.INVISIBLE);
                checkboxTeaSort.setVisibility(View.VISIBLE);
                checkboxTeaSort.setChecked(true);
                editTextTeaSort.setVisibility(View.VISIBLE);
                editTextTeaSort.setText(selectedTea.getSortOfTea().getType());
            } else {
                spinnerTeaVariety.setSelection(spinnerId);
            }
            color = selectedTea.getColoring().getColor();
            buttonColorSape.setColor(color);
            colorChange = true;
            editTextName.setText(selectedTea.getName());
            //richtige SpinnerId bekommen
            amountUnit = selectedTea.getAmount().getUnit();
            switch (amountUnit) {
                case "Ts":
                    spinnerAmount.setSelection(0);
                    break;
                case "Gr":
                    spinnerAmount.setSelection(1);
                    break;
            }
            if (selectedTea.getAmount().getValue() != -500)
                editTextAmount.setText(String.valueOf(selectedTea.getAmount().getValue()));

            temperatureList = selectedTea.getTemperature();
            if (temperatureList.get(brewIndex).getCelsius() != -500) {
                editTextTemperature.setText(String.valueOf(getTemperature(0)));
            }
            coolDownTimeList = selectedTea.getCoolDownTime();
            if (!coolDownTimeList.get(brewIndex).getTime().equals("-")) {
                editTextCoolDownTime.setText(selectedTea.getCoolDownTime().get(0).getTime());
            }
            timeList = selectedTea.getTime();
            if (!timeList.get(brewIndex).getTime().equals("-"))
                editTextSteepingTime.setText(selectedTea.getTime().get(0).getTime());

            //Damit die brewConsole funktioniert muss dieser Wert gesetzt werden
            brewSize = timeList.size();
            refreshBrewConsole();

        }

        //Spinner Teeart hat sich verändert
        spinnerTeaVariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                variety = Variety.values()[position];
                //Farbe soll am Anfang nicht geändert werden, wenn der Tee geändert wird
                if (!colorChange) {
                    color = SortOfTea.getVariatyColor(variety);
                    buttonColorSape.setColor(color);
                } else {
                    colorChange = false;
                }
                if (variety.equals(Variety.Other)) {
                    checkboxTeaSort.setVisibility(View.VISIBLE);
                } else {
                    checkboxTeaSort.setVisibility(View.INVISIBLE);
                }
                sethints();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Checkbox Teeart wurde angeklickt
        checkboxTeaSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewTeaSort.setVisibility(View.INVISIBLE);
                    spinnerTeaVariety.setVisibility(View.INVISIBLE);
                    editTextTeaSort.setVisibility(View.VISIBLE);
                } else {
                    textViewTeaSort.setVisibility(View.VISIBLE);
                    spinnerTeaVariety.setVisibility(View.VISIBLE);
                    editTextTeaSort.setVisibility(View.INVISIBLE);
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
        buttonColor.setOnLongClickListener(this);

        //unit hat sich verändert
        spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        amountUnit = "Ts";
                        break;
                    case 1:
                        amountUnit = "Gr";
                        break;
                }
                sethints();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeBrew()) {
                    brewIndex--;
                    refreshBrewConsole();
                    refreshBrewInformation();
                }
            }
        });
        leftArrow.setOnLongClickListener(this);

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeBrew()) {
                    brewIndex++;
                    refreshBrewConsole();
                    refreshBrewInformation();
                }
            }
        });
        rightArrow.setOnLongClickListener(this);

        deleteBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBrew();
                if ((brewIndex + 1) == brewSize) {
                    brewIndex--;
                }
                brewSize--;
                refreshBrewConsole();
                refreshBrewInformation();
            }
        });
        deleteBrew.setOnLongClickListener(this);

        addBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeBrew()) {
                    addNewBrew();
                    brewIndex++;
                    brewSize++;
                    refreshBrewConsole();
                    clearBrewInformation();
                }
            }
        });
        addBrew.setOnLongClickListener(this);

        buttonShowCoolDowntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCoolDownTime.getVisibility() == View.VISIBLE) {
                    buttonShowCoolDowntime.setBackground(getResources().getDrawable(R.drawable.button_arrowdown));
                    editTextCoolDownTime.setVisibility(View.GONE);
                    buttonAutofillCoolDownTime.setVisibility(View.GONE);
                } else {
                    buttonShowCoolDowntime.setBackground(getResources().getDrawable(R.drawable.button_arrowup));
                    editTextCoolDownTime.setVisibility(View.VISIBLE);
                    buttonAutofillCoolDownTime.setVisibility(View.VISIBLE);
                }
            }
        });
        buttonShowCoolDowntime.setOnLongClickListener(this);

        buttonAutofillCoolDownTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ist die Temperatur nicht gesetzt, so ist sie -500
                int temperatureCelsius = -500;
                boolean temperatureValid = temperatureStringValid(editTextTemperature.getText().toString());
                if (temperatureValid && !editTextTemperature.getText().toString().equals("")) {
                    temperatureCelsius = Integer.parseInt(editTextTemperature.getText().toString());
                }
                //Falls nötig in Celsius umwandeln
                if(MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) {
                    temperatureCelsius = Temperature.fahrenheitToCelsius(temperatureCelsius);
                }
                if (temperatureCelsius != -500 && temperatureCelsius != 100) {
                    editTextCoolDownTime.setText(Temperature.celsiusToCoolDownTime(temperatureCelsius));
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_auto_cooldown_time, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        buttonAutofillCoolDownTime.setOnLongClickListener(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_tea, menu);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                View view = findViewById(R.id.action_done);

                if (view != null) {
                    view.setOnLongClickListener(NewTea.this);
                }
            }
        });

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_done){
            addTea();
        }else if (showTea) {
            //Neues Intent anlegen
            Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
            showteaScreen.putExtra("elementId", elementId);
            // Intent starten und zur zweiten Activity wechseln
            startActivity(showteaScreen);
            finish();
            return true;
        } else if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void addTea(){
        //Der Name muss eingegeben werden
        if (!editTextName.getText().toString().equals("")) {
            //Attribute auslesen
            boolean sortValid = true;
            String sortOfTea;
            if (checkboxTeaSort.isChecked()) {
                sortOfTea = editTextTeaSort.getText().toString();
                sortValid = !(sortOfTea.length() > 30);
                if (sortOfTea.length() == 0) {
                    sortOfTea = "-";
                }
            } else {
                sortOfTea = (String) spinnerTeaVariety.getSelectedItem();
            }
            //Ist der Name Valide
            String name = editTextName.getText().toString();
            boolean nameValid = nameValid(name);

            //Ist teelamass nicht gesetzt so ist es -500
            int amount = -500;
            boolean amountValid = amountValid(editTextAmount.getText().toString());
            if (amountValid && !editTextAmount.getText().toString().equals("")) {
                amount = Integer.parseInt(editTextAmount.getText().toString());
            }

            //Überprüfe ob alle Werte Valide sind
            if (!sortValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_30Char, Toast.LENGTH_SHORT);
                toast.show();
            } else if (!nameValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_name, Toast.LENGTH_SHORT);
                toast.show();
            } else if (!amountValid) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_amount, Toast.LENGTH_SHORT);
                toast.show();
            } else if (changeBrew()){

                if (!(elementAt == -1)) {
                    //Tee wird geändert
                    MainActivity.teaItems.getTeaItems().get(elementAt).setName(name);
                    MainActivity.teaItems.getTeaItems().get(elementAt).setSortOfTea(new SortOfTea(sortOfTea));
                    MainActivity.teaItems.getTeaItems().get(elementAt).setTemperature(temperatureList);
                    MainActivity.teaItems.getTeaItems().get(elementAt).setCoolDownTime(coolDownTimeList);
                    MainActivity.teaItems.getTeaItems().get(elementAt).setTime(timeList);
                    MainActivity.teaItems.getTeaItems().get(elementAt).setAmount(createAmount(amount));
                    MainActivity.teaItems.getTeaItems().get(elementAt).setColoring(new Coloring(color));
                    MainActivity.teaItems.getTeaItems().get(elementAt).setCurrentDate();
                    //teaItems persistent speichern
                    MainActivity.teaItems.sort();
                    if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_change, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    //erstelle Tee
                    N2Tea tea = new N2Tea(MainActivity.teaItems.nextId(), name, new SortOfTea(sortOfTea), temperatureList, coolDownTimeList, timeList,
                            createAmount(amount), new Coloring(color));
                    tea.setCurrentDate();
                    MainActivity.teaItems.getTeaItems().add(tea);
                    //teaItems persistent speichern
                    MainActivity.teaItems.sort();
                    if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_save, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                if (!showTea) {
                    //wechsel das Fenster
                    finish();
                } else {
                    //Neues Intent anlegen
                    Intent showteaScreen = new Intent(NewTea.this, ShowTea.class);
                    //find out elementAt by Name
                    showteaScreen.putExtra("elementId", elementId);
                    // Intent starten und zur zweiten Activity wechseln
                    startActivity(showteaScreen);
                    finish();
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_name, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private boolean nameValid(String name) {
        boolean nameValid = true;
        /*Eventuell später hier Bedingungen platzieren*/
        return nameValid;
    }

    private boolean temperatureStringValid(String temperature) {
        boolean temperatureValid = true;
        if (!temperature.equals("")) {
            if (temperature.contains(".") || temperature.length() > 3) {
                temperatureValid = false;
            } else {
                int checktemperature = 0;
                if (MainActivity.settings.getTemperatureUnit().equals("Celsius"))
                    checktemperature = Integer.parseInt(temperature);
                else if (MainActivity.settings.getTemperatureUnit().equals("Fahrenheit"))
                    checktemperature = Temperature.fahrenheitToCelsius(Integer.parseInt(temperature));

                if (checktemperature > 100 || checktemperature < 0) {
                    temperatureValid = false;
                }
            }
        }
        return temperatureValid;
    }

    private boolean amountValid(String teelamass) {
        boolean amountValid = true;
        if (!teelamass.equals("")) {
            if (teelamass.contains(".") || teelamass.length() > 3) {
                amountValid = false;
            }
        }
        return amountValid;
    }

    private boolean timeValid(String time) {
        boolean timeValid;
        //ist die Zeit gesetzt so wird sie geprüft
        timeValid = time.length() < 6;
        if (timeValid && !time.equals("")) {
            boolean formatMinutes = Pattern.matches("\\d\\d", time) || Pattern.matches("\\d", time);
            boolean formatSeconds = Pattern.matches("\\d\\d:\\d\\d", time) || Pattern.matches("\\d:\\d\\d", time);
            if (formatMinutes) {
                timeValid = Integer.parseInt(time) < 60;
            } else if (formatSeconds) {
                String[] split = time.split(":");
                timeValid = Integer.parseInt(split[0]) < 60 && Integer.parseInt(split[1]) < 60;
            } else {
                timeValid = false;
            }
        }
        return timeValid;
    }

    private Temperature createTemperature(int value) {
        switch (MainActivity.settings.getTemperatureUnit()) {
            case "Celsius":
                return new TemperatureCelsius(value);
            case "Fahrenheit":
                return new TemperatureFahrenheit(value);
            default:
                return null;
        }
    }

    private int getTemperature(int index) {
        switch (MainActivity.settings.getTemperatureUnit()) {
            case "Celsius":
                return temperatureList.get(index).getCelsius();
            case "Fahrenheit":
                return temperatureList.get(index).getFahrenheit();
            default:
                return -500;
        }
    }

    private Amount createAmount(int value) {
        switch (amountUnit) {
            case "Ts":
                return new AmountTs(value);
            case "Gr":
                return new AmountGramm(value);
            default:
                return null;
        }
    }

    private void sethints() {
        //set Hint for variety
        editTextTemperature.setHint(SortOfTea.getHintTemperature(getApplicationContext(), variety,
                MainActivity.settings.getTemperatureUnit()));
        editTextAmount.setHint(SortOfTea.getHintAmount(getApplicationContext(), variety, amountUnit));
        editTextSteepingTime.setHint(SortOfTea.getHintTime(getApplicationContext(), variety));
    }

    private void refreshBrewConsole() {
        //Show Delete or Not
        if (brewSize > 1) {
            deleteBrew.setVisibility(View.VISIBLE);
        } else {
            deleteBrew.setVisibility(View.GONE);
        }
        //show Add or Not
        if (((brewIndex + 1) == brewSize) && (brewSize < 20)) {
            addBrew.setVisibility(View.VISIBLE);
        } else {
            addBrew.setVisibility(View.GONE);
        }
        //enable Left
        if (brewIndex == 0) {
            leftArrow.setEnabled(false);
        } else {
            leftArrow.setEnabled(true);
        }
        //enable Right
        if ((brewIndex + 1) == brewSize) {
            rightArrow.setEnabled(false);
        } else {
            rightArrow.setEnabled(true);
        }
        //show Text
        textViewBrew.setText(getResources().getString(R.string.newtea_count_brew,brewIndex + 1,". "));
    }

    private void refreshBrewInformation() {
        clearBrewInformation();
        int tmpTemperature = getTemperature(brewIndex);
        if (tmpTemperature != -500) {
            editTextTemperature.setText(String.valueOf(tmpTemperature));
        }

        String tmpCoolDownTime = coolDownTimeList.get(brewIndex).getTime();
        if (!tmpCoolDownTime.equals("-")) {
            editTextCoolDownTime.setText(tmpCoolDownTime);
        }

        String tmpSteepingTime = timeList.get(brewIndex).getTime();
        if (!tmpSteepingTime.equals("-")) {
            editTextSteepingTime.setText(tmpSteepingTime);
        }
    }

    private void clearBrewInformation() {
        editTextTemperature.setText("");
        editTextCoolDownTime.setText("");
        editTextSteepingTime.setText("");
    }

    private void addNewBrew() {
        temperatureList.add(createTemperature(-500));
        coolDownTimeList.add(new Time("-"));
        timeList.add(new Time("-"));
    }

    private void deleteBrew() {
        temperatureList.remove(brewIndex);
        coolDownTimeList.remove(brewIndex);
        timeList.remove(brewIndex);
    }

    private boolean changeBrew() {
        boolean works = true;

        //Ist die Temperatur nicht gesetzt, so ist sie -500
        int temperature = -500;
        boolean temperatureValid = temperatureStringValid(editTextTemperature.getText().toString());
        if (temperatureValid && !editTextTemperature.getText().toString().equals("")) {
            temperature = Integer.parseInt(editTextTemperature.getText().toString());
        }

        //Ist Zeit nicht gesetzt so ist sie -
        String coolDownTime = "-";
        boolean coolDownTimeValid = timeValid(editTextCoolDownTime.getText().toString());
        if (coolDownTimeValid && !editTextCoolDownTime.getText().toString().equals("")) {
            coolDownTime = editTextCoolDownTime.getText().toString();
        }

        //Ist Zeit nicht gesetzt so ist sie -
        String time = "-";
        boolean timeValid = timeValid(editTextSteepingTime.getText().toString());
        if (timeValid && !editTextSteepingTime.getText().toString().equals("")) {
            time = editTextSteepingTime.getText().toString();
        }

        if (!temperatureValid) {
            works = false;
            if (MainActivity.settings.getTemperatureUnit().equals("Celsius")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_celsius, Toast.LENGTH_SHORT);
                toast.show();
            } else if (MainActivity.settings.getTemperatureUnit().equals("Fahrenheit")) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_fahrenheit, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (!coolDownTimeValid) {
            works = false;
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cooldown_time, Toast.LENGTH_SHORT);
            toast.show();
        } else if (!timeValid) {
            works = false;
            Toast toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            temperatureList.set(brewIndex, createTemperature(temperature));
            coolDownTimeList.set(brewIndex, new Time(coolDownTime));
            timeList.set(brewIndex, new Time(time));
        }
        return works;
    }

    //choose which tooltip will be shown
    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.buttonColor) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.newtea_tooltip_choosecolor));
        }else if (view.getId() == R.id.buttonArrowLeft) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.newtea_tooltip_arrowleft));
        } else if (view.getId() == R.id.buttonArrowRight) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.newtea_tooltip_arrowright));
        } else if (view.getId() == R.id.buttonAddBrew) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.newtea_tooltip_addbrew));
        } else if (view.getId() == R.id.buttonDeleteBrew) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.newtea_tooltip_deletebrew));
        } else if (view.getId() == R.id.buttonShowCoolDownTime) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.newtea_tooltip_showcooldowntime));
        } else if (view.getId() == R.id.buttonAutofillCoolDownTime) {
            showTooltip(view, Gravity.TOP,getResources().getString(R.string.newtea_tooltip_autofillcooldowntime));
        } else if (view.getId() == R.id.action_done) {
            showTooltip(view, Gravity.BOTTOM,getResources().getString(R.string.newtea_tooltip_done));
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
