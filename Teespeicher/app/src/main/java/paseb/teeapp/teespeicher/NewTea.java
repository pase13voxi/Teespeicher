package paseb.teeapp.teespeicher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class NewTea extends AppCompatActivity {

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
        if(MainActivity.settings.getLanguage().equals("de")){
            mToolbarCustomTitle.setText(R.string.newtea_heading);
        }else if(MainActivity.settings.getLanguage().equals("en")){
            mToolbarCustomTitle.setText(R.string.newtea_heading_en);
        }
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
        Button scanName = (Button) findViewById(R.id.buttonScan);
        Button addTea = (Button) findViewById(R.id.buttonfertig);

        //übersetzung englisch deutsch
        if(MainActivity.settings.getLanguage().equals("de")){
            textViewTeeArt.setText(R.string.tea_sort);
            editTextName.setHint("Name");
            addTea.setText(R.string.newtea_button_create);
            spinnerTeeArt.setPrompt("Teesorte");
            checkboxTeeArt.setText(R.string.newtea_by_hand);
            editTextTeeArt.setHint(R.string.tea_sort);
        }else if(MainActivity.settings.getLanguage().equals("en")){
            textViewTeeArt.setText(R.string.tea_sort_en);
            editTextName.setHint("Name");
            addTea.setText(R.string.newtea_button_create_en);
            spinnerTeeArt.setPrompt("Tea variety");
            checkboxTeeArt.setText(R.string.newtea_by_hand_en);
            editTextTeeArt.setHint(R.string.tea_sort_en);
        }

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerTimeAdapter = null;
        if(MainActivity.settings.getLanguage().equals("de")) {
            spinnerTimeAdapter = ArrayAdapter.createFromResource(
                    this, R.array.sortsOfTea, R.layout.spinner_item_sortoftea);
        }else if(MainActivity.settings.getLanguage().equals("en")){
            spinnerTimeAdapter = ArrayAdapter.createFromResource(
                    this, R.array.sortsOfTea_en, R.layout.spinner_item_sortoftea);
        }
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
            String[] spinnerElements = null;
            if(MainActivity.settings.getLanguage().equals("de")) {
                spinnerElements = getResources().getStringArray(R.array.sortsOfTea);
            }else if(MainActivity.settings.getLanguage().equals("en")){
                spinnerElements = getResources().getStringArray(R.array.sortsOfTea_en);
            }
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
            if(selectedTea.getTemperature()!=-500) editTextTemperatur.setText(String.valueOf(selectedTea.getTemperature()));
            if(!selectedTea.getTime().equals("-")) editTextZiehzeit.setText(selectedTea.getTime());
            if(selectedTea.getTeelamass()!=-500) editTextTeelamass.setText(String.valueOf(selectedTea.getTeelamass()));
            if(MainActivity.settings.getLanguage().equals("de")){
                addTea.setText("Ändern");
            }else if(MainActivity.settings.getLanguage().equals("en")){
                addTea.setText("Edit");
            }
        }

        //Spinner Teeart hat sich verändert
        spinnerTeeArt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinnerTeeArt.getSelectedItem().toString();
                if(selectedItem.equals("Sonstiges")||selectedItem.equals("Other")){
                    checkboxTeeArt.setVisibility(View.VISIBLE);
                }else{
                    checkboxTeeArt.setVisibility(View.INVISIBLE);
                }
                //Tipps für Temperatur und Ziehzeit anhand der Teesorte
                switch(selectedItem){
                    case "Schwarzer Tee":
                        editTextTemperatur.setHint("Temperatur (95 - 100°C)");
                        editTextTeelamass.setHint("Menge  (3 - 5Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (3 - 5min)");
                        break;
                    case "Black tea":
                        editTextTemperatur.setHint("Temperature (95 - 100°C)");
                        editTextTeelamass.setHint("Amount  (3 - 5Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (3 - 5min)");
                        break;
                    case "Grüner Tee":
                        editTextTemperatur.setHint("Temperatur (60 - 90°C)");
                        editTextTeelamass.setHint("Menge (6 - 8Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (1 - 3min)");
                        break;
                    case "Green tea":
                        editTextTemperatur.setHint("Temperature (60 - 90°C)");
                        editTextTeelamass.setHint("Amount (6 - 8Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (1 - 3min)");
                        break;
                    case "Gelber Tee":
                        editTextTemperatur.setHint("Temperatur (75°C)");
                        editTextTeelamass.setHint("Menge (4 - 5Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (1 - 3min)");
                        break;
                    case "Yellow tea":
                        editTextTemperatur.setHint("Temperature (75°C)");
                        editTextTeelamass.setHint("Amount (4 - 5Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (1 - 3min)");
                        break;
                    case "Weißer Tee":
                        editTextTemperatur.setHint("Temperatur (70 - 80°C)");
                        editTextTeelamass.setHint("Menge (3 - 4Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (1 - 4min)");
                        break;
                    case "White tea":
                        editTextTemperatur.setHint("Temperature (70 - 80°C)");
                        editTextTeelamass.setHint("Amount (3 - 4Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (1 - 4min)");
                        break;
                    case "Oolong":
                        editTextTemperatur.setHint("Temperatur (75 - 90°C)");
                        editTextTeelamass.setHint("Menge (4 - 6Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (0:30 - 4min)");
                        break;
                    case "Oolong tea":
                        editTextTemperatur.setHint("Temperature (75 - 90°C)");
                        editTextTeelamass.setHint("Amount (4 - 6Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (0:30 - 4min)");
                        break;
                    case "Pu Erh Tee":
                        editTextTemperatur.setHint("Temperatur (100°C)");
                        editTextTeelamass.setHint("Menge (4 - 6Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (5 - 15sek)");
                        break;
                    case "Pu-erh tea":
                        editTextTemperatur.setHint("Temperature (100°C)");
                        editTextTeelamass.setHint("Amount (4 - 6Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (5 - 15sec)");
                        break;
                    case "Kräutertee":
                        editTextTemperatur.setHint("Temperatur (100°C)");
                        editTextTeelamass.setHint("Menge (4 - 6Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (5 - 10min)");
                        break;
                    case "Herbal tea":
                        editTextTemperatur.setHint("Temperature (100°C)");
                        editTextTeelamass.setHint("Amount (4 - 6Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (5 - 10min)");
                        break;
                    case "Früchtetee":
                        editTextTemperatur.setHint("Temperatur (100°C)");
                        editTextTeelamass.setHint("Menge (8 - 10Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (5 - 10min)");
                        break;
                    case "Fruit tea":
                        editTextTemperatur.setHint("Temperature (100°C)");
                        editTextTeelamass.setHint("Amount (8 - 10Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (5 - 10min)");
                        break;
                    case "Roibuschtee":
                        editTextTemperatur.setHint("Temperatur (100°C)");
                        editTextTeelamass.setHint("Menge (4 - 6Tl/L)");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\" (5 - 10min)");
                        break;
                    case "Rooibus tea":
                        editTextTemperatur.setHint("Temperature (100°C)");
                        editTextTeelamass.setHint("Amount (4 - 6Ts/L)");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\" (5 - 10min)");
                        break;
                    case "Sonstiges" :
                        editTextTemperatur.setHint("Temperatur (°C)");
                        editTextTeelamass.setHint("Menge Tl/L");
                        editTextZiehzeit.setHint("Ziehzeit \"mm:ss\" oder \"mm\"");
                        break;
                    case "Other" :
                        editTextTemperatur.setHint("Temperature (°C)");
                        editTextTeelamass.setHint("Amount Ts/L");
                        editTextZiehzeit.setHint("Steeping time \"mm:ss\" or \"mm\"");
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
                    String name = editTextName.getText().toString();
                    boolean nameValid = true;
                    for(int i=0; i<MainActivity.teaItems.getTeaItems().size(); i++){
                        if(name.equals(MainActivity.teaItems.getTeaItems().get(i).getName()) && elementAt!=i){
                            nameValid = false;
                            break;
                        }
                    }
                    //Ist die Temperatur nicht gesetzt, so ist sie -500
                    int temperature = -500;
                    boolean pointExist = false;
                    boolean tempbiggerThanInt = false;
                    if (!editTextTemperatur.getText().toString().equals("")) {
                        if (editTextTemperatur.getText().toString().contains(".")) {
                            pointExist = true;
                        } else if (editTextTemperatur.getText().toString().length() > 3) {
                            tempbiggerThanInt = true;
                        } else {
                            temperature = Integer.parseInt(editTextTemperatur.getText().toString());
                        }
                    }
                    //Ist Zeit nicht gesetzt so ist sie -
                    String time = "-";
                    Boolean timeValid = true;
                    //ist die Zeit gesetzt so wird sie geprüft
                    if (!editTextZiehzeit.getText().toString().equals("")) {
                        time = editTextZiehzeit.getText().toString();
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
                    //Ist teelamass nicht gesetzt so ist es -500
                    int teelamass = -500;
                    boolean teelabiggerThanInt = false;
                    if (!editTextTeelamass.getText().toString().equals("")) {
                        if (editTextTeelamass.getText().toString().contains(".")) {
                            pointExist = true;
                        } else if (editTextTeelamass.getText().toString().length() > 3) {
                            teelabiggerThanInt = true;
                        } else {
                            teelamass = Integer.parseInt(editTextTeelamass.getText().toString());
                        }
                    }

                    //Temperatur muss zwischen 100 und 0 sein und die Zeit braucht das richtige Format
                    if(!sortValid) {
                        Toast toast= null;
                        if(MainActivity.settings.getLanguage().equals("de")) {
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_30Char, Toast.LENGTH_SHORT);
                        }else if(MainActivity.settings.getLanguage().equals("en")){
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_30Char_en, Toast.LENGTH_SHORT);
                        }
                        toast.show();
                    } else if (!nameValid) {
                        Toast toast = null;
                        if(MainActivity.settings.getLanguage().equals("de")) {
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_name_just_exist, Toast.LENGTH_SHORT);
                        }else if(MainActivity.settings.getLanguage().equals("en")){
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_name_just_exist_en, Toast.LENGTH_SHORT);
                        }
                        toast.show();
                    } else if (pointExist) {
                        Toast toast = null;
                        if(MainActivity.settings.getLanguage().equals("de")) {
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_point, Toast.LENGTH_SHORT);
                        }else if(MainActivity.settings.getLanguage().equals("en")){
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_point_en, Toast.LENGTH_SHORT);
                        }
                        toast.show();
                    } else if ((temperature > 100 || temperature < 0) && (temperature != -500 || tempbiggerThanInt)) {
                        Toast toast = null;
                        if(MainActivity.settings.getLanguage().equals("de")) {
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_temperature, Toast.LENGTH_SHORT);
                        }else if(MainActivity.settings.getLanguage().equals("en")){
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_wrong_temperature_en, Toast.LENGTH_SHORT);
                        }
                        toast.show();
                    } else if (!timeValid) {
                        Toast toast = null;
                        if(MainActivity.settings.getLanguage().equals("de")) {
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format, Toast.LENGTH_SHORT);
                        }else if(MainActivity.settings.getLanguage().equals("en")){
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_time_format_en, Toast.LENGTH_SHORT);
                        }
                        toast.show();
                    } else if(teelabiggerThanInt){
                        Toast toast = null;
                        if(MainActivity.settings.getLanguage().equals("de")) {
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_amount, Toast.LENGTH_SHORT);
                        }else if(MainActivity.settings.getLanguage().equals("en")){
                            toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_amount_en, Toast.LENGTH_SHORT);
                        }
                        toast.show();
                    } else {
                        if (!(elementAt == -1)) {
                            //Tee wird geändert
                            MainActivity.teaItems.getTeaItems().get(elementAt).setName(name);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setSortOfTea(sortOfTea);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTemperature(temperature);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTime(time);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setTeelamass(teelamass);
                            MainActivity.teaItems.getTeaItems().get(elementAt).setCurrentDate();
                            //teaItems persistent speichern
                            MainActivity.teaItems.sort();
                            if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {
                                Toast toast = null;
                                if(MainActivity.settings.getLanguage().equals("de")) {
                                    toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_change, Toast.LENGTH_SHORT);
                                }else if(MainActivity.settings.equals("en")){
                                    toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_change_en, Toast.LENGTH_SHORT);
                                }
                                toast.show();
                            }
                        } else {
                            //erstelle Tee
                            Tea tea = new Tea(name, sortOfTea, temperature, time, teelamass);
                            tea.setCurrentDate();
                            MainActivity.teaItems.getTeaItems().add(tea);
                            //teaItems persistent speichern
                            MainActivity.teaItems.sort();
                            if (!MainActivity.teaItems.saveCollection(getApplicationContext())) {
                                Toast toast = null;
                                if(MainActivity.settings.getLanguage().equals("de")) {
                                    toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_save, Toast.LENGTH_SHORT);
                                }else if(MainActivity.settings.getLanguage().equals("en")){
                                    toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_cant_save_en, Toast.LENGTH_SHORT);
                                }
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
                    Toast toast = null;
                    if(MainActivity.settings.getLanguage().equals("de")) {
                        toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_name, Toast.LENGTH_SHORT);
                    }else if(MainActivity.settings.getLanguage().equals("en")){
                        toast = Toast.makeText(getApplicationContext(), R.string.newtea_error_no_name_en, Toast.LENGTH_SHORT);
                    }
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

    //Show the Dialog befor using the The Scan
    private void dialogBeforeScan(){

        LayoutInflater inflater = getLayoutInflater();
        View alertLayoutDialogBeforeScan = inflater.inflate(R.layout.dialogbeforescan, null);
        TextView textViewDialogBeforeScan = (TextView) alertLayoutDialogBeforeScan.findViewById(R.id.textViewDialogBeforeScan);
        dontShowAgain = (CheckBox) alertLayoutDialogBeforeScan.findViewById(R.id.checkboxDialogBeforeScan);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertLayoutDialogBeforeScan);
        if(MainActivity.settings.getLanguage().equals("de")) {
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
        } else if(MainActivity.settings.getLanguage().equals("en")){
            adb.setTitle(R.string.newtea_dialog_before_scan_title_en);
            textViewDialogBeforeScan.setText(R.string.newtea_dialog_before_scan_text_en);
            dontShowAgain.setText(R.string.newtea_dialog_before_scan_check_en);
            adb.setPositiveButton(R.string.newtea_dialog_scan_ok_en, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (dontShowAgain.isChecked()) {
                        MainActivity.settings.setOcrAlert(false);
                        MainActivity.settings.saveSettings(getApplicationContext());
                    }
                    startCameraActivity();
                }
            });
            adb.setNegativeButton(R.string.newtea_dialog_scan_cancel_en, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        if(MainActivity.settings.getLanguage().equals("de")){
            alert.setTitle(R.string.newtea_dialog_after_scan_title);
        }else if(MainActivity.settings.getLanguage().equals("en")){
            alert.setTitle(R.string.newtea_dialog_after_scan_title_en);
        }
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

        if(MainActivity.settings.getLanguage().equals("de")) {
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
        }else if(MainActivity.settings.getLanguage().equals("en")){
            alert.setPositiveButton(R.string.newtea_dialog_scan_ok_en, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String tmp = input.getText().toString();
                    tmp = tmp.replaceAll("\n", " ");
                    editTextName.setText(tmp);
                }
            });
            alert.setNegativeButton(R.string.newtea_dialog_scan_cancel_en, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Canceled.
                }
            });
        }
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
