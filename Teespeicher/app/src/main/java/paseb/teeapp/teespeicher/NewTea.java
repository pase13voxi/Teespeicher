package paseb.teeapp.teespeicher;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class NewTea extends AppCompatActivity {

    Spinner spinnerTeeArt;
    EditText editTextName;
    EditText editTextTemperatur;
    EditText editTextZiehzeit;
    EditText editTextTeelamass;
    int elementAt;
    boolean showTea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tea);

        //Toolbar definieren und erstellen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Eingabefelder bestimmen
        spinnerTeeArt = (Spinner) findViewById(R.id.spinnerTeeart);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextTemperatur = (EditText) findViewById(R.id.editTextTemperatur);
        editTextZiehzeit = (EditText) findViewById(R.id.editTextZiehzeit);
        editTextTeelamass = (EditText) findViewById(R.id.editTextTeelamass);
        Button addTea = (Button) findViewById(R.id.buttonfertig);

        //showTea wird übergeben, falls die Navigation von showTea erfolgt
        showTea = this.getIntent().getBooleanExtra("showTea", false);
        //Falls Änderung, dann wird ein Wert übergeben.
        elementAt  = this.getIntent().getIntExtra("elementAt", -1);
        if(elementAt!=-1){
            Tea selectedTea = MainActivity.teaItems.getTeaItems().get(elementAt);
            //richtige SpinnerId bekommen
            int spinnerId = 0;
            String[] spinnerElements = getResources().getStringArray(R.array.sortsOfTea);
            for(int i=0; i<spinnerElements.length; i++){
                if(spinnerElements[i].equals(selectedTea.getSortOfTea())){
                    spinnerId = i;
                    break;
                }
            }
            //Werte werden für Änderungen gefüllt
            spinnerTeeArt.setSelection(spinnerId);
            editTextName.setText(selectedTea.getName());
            if(selectedTea.getTemperature()!=-500) editTextTemperatur.setText(String.valueOf(selectedTea.getTemperature()));
            if(!selectedTea.getTime().equals("-")) editTextZiehzeit.setText(selectedTea.getTime());
            if(selectedTea.getTeelamass()!=-500) editTextTeelamass.setText(String.valueOf(selectedTea.getTeelamass()));
            addTea.setText("Ändern");
        }

        //Alles wurde Eingeben und die Werte werden aufgenommen
        addTea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Der Name muss eingegeben werden
                if(!editTextName.getText().toString().equals("")) {
                    //Attribute auslesen
                    String sortOfTea = (String) spinnerTeeArt.getSelectedItem();
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
                    if (!nameValid) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Diesen Namen gibt es schon.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (pointExist) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Bitte kein Punkt.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if ((temperature > 100 || temperature < 0) && (temperature != -500 || tempbiggerThanInt)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Die Temperatur sollte zwischen 0 °C und 100 °C liegen", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (!timeValid) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Bitte halten Sie das Format bei der Ziehzeit ein.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if(teelabiggerThanInt){
                        Toast toast = Toast.makeText(getApplicationContext(), "Die Tl/L sollten nicht vierstellig sein.", Toast.LENGTH_SHORT);
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
                                Toast toast = Toast.makeText(getApplicationContext(), "Änderung konnte nicht durchgeführt werden.", Toast.LENGTH_SHORT);
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
                                Toast toast = Toast.makeText(getApplicationContext(), "Speichern konnte nicht durchgeführt werden.", Toast.LENGTH_SHORT);
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
                    Toast toast = Toast.makeText(getApplicationContext(), "Geben Sie bitte den Namen an.", Toast.LENGTH_SHORT);
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
}
