package paseb.teeapp.teespeicher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textViewMusic;
    private Button buttonMusicChoice;
    private CheckBox checkBoxVibration;
    private CheckBox checkBoxNotification;
    private TextView textViewLanguage;
    private Spinner spinnerLanguage;
    private Button buttonShowhints;
    private Button buttonDefaultSettings;
    private TextView mToolbarCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        mToolbarCustomTitle = (TextView) findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Bedienelemente holen
        textViewMusic = (TextView) findViewById(R.id.textViewMusic);
        buttonMusicChoice = (Button) findViewById(R.id.buttonMusicChoice);
        checkBoxVibration = (CheckBox) findViewById(R.id.checkBoxVibration);
        checkBoxNotification = (CheckBox) findViewById(R.id.checkBoxNotification);
        textViewLanguage = (TextView) findViewById(R.id.textViewLanguage);
        spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguage);
        buttonShowhints = (Button) findViewById(R.id.buttonShowHints);
        buttonDefaultSettings = (Button) findViewById(R.id.buttonDefaultSettings);

        //Übersetzung Englisch Deutsch
        translateActivity();

        //Setzte Spinner Groß
        ArrayAdapter<CharSequence> spinnerTimeAdapter = ArrayAdapter.createFromResource(
                    this, R.array.settings_languages, R.layout.spinner_item_language);
        spinnerTimeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_language);
        spinnerLanguage.setAdapter(spinnerTimeAdapter);

        //Vorhandene Option festlegen
        buttonMusicChoice.setText(MainActivity.settings.getMusicName());
        checkBoxVibration.setChecked(MainActivity.settings.isVibration());
        checkBoxNotification.setChecked(MainActivity.settings.isNotification());
        if(MainActivity.settings.getLanguage().equals("de")){
            spinnerLanguage.setSelection(1);
        }else if(MainActivity.settings.getLanguage().equals("en")){
            spinnerLanguage.setSelection(0);
        }

        buttonMusicChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                if(MainActivity.settings.getLanguage().equals("de")) {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.setting_alarm_selection_title);
                }else if(MainActivity.settings.getLanguage().equals("en")){
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.setting_alarm_selection_title_en);
                }
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                startActivityForResult(intent, 5);
            }
        });

        checkBoxVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.settings.setVibration(isChecked);
                MainActivity.settings.saveSettings(getApplicationContext());
            }
        });

        checkBoxNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.settings.setNotification(isChecked);
                MainActivity.settings.saveSettings(getApplicationContext());
            }
        });

        //Spinner Teeart hat sich verändert
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinnerLanguage.getSelectedItem().toString();
                if(selectedItem.equals("Deutsch")){
                    MainActivity.settings.setLanguage("de");
                    MainActivity.settings.saveSettings(getApplicationContext());
                    MainActivity.teaItems.translateSortOfTea(MainActivity.settings.getLanguage(), getApplicationContext());
                    MainActivity.teaItems.saveCollection(getApplicationContext());
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.newTea.setText(R.string.main_create_tea);
                }else if(selectedItem.equals("English")){
                    MainActivity.settings.setLanguage("en");
                    MainActivity.settings.saveSettings(getApplicationContext());
                    MainActivity.teaItems.translateSortOfTea(MainActivity.settings.getLanguage(), getApplicationContext());
                    MainActivity.teaItems.saveCollection(getApplicationContext());
                    MainActivity.adapter.notifyDataSetChanged();
                    MainActivity.newTea.setText(R.string.main_create_tea_en);
                }
                //Übersetzung Englisch Deutsch
                translateActivity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonShowhints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.settings.setOcrAlert(true);
                MainActivity.settings.saveSettings(getApplicationContext());
                Toast toast = null;
                if(MainActivity.settings.getLanguage().equals("de")) {
                    toast = Toast.makeText(getApplicationContext(), R.string.settings_show_hints_text, Toast.LENGTH_SHORT);
                } else if(MainActivity.settings.getLanguage().equals("en")){
                    toast = Toast.makeText(getApplicationContext(), R.string.settings_show_hints_text_en, Toast.LENGTH_SHORT);
                }
                toast.show();
            }
        });

        buttonDefaultSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Einstellungen zurücksetzen und Tees löschen
                                MainActivity.settings.setDefault();
                                MainActivity.settings.saveSettings(getApplicationContext());
                                MainActivity.teaItems.deleteCollection();
                                MainActivity.teaItems.saveCollection(getApplicationContext());
                                MainActivity.adapter.notifyDataSetChanged();
                                //Felder ändern
                                buttonMusicChoice.setText(MainActivity.settings.getMusicName());
                                checkBoxVibration.setChecked(MainActivity.settings.isVibration());
                                checkBoxNotification.setChecked(MainActivity.settings.isNotification());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                if(MainActivity.settings.getLanguage().equals("de")) {
                    builder.setMessage(R.string.settings_factory_settings_text)
                            .setPositiveButton("OK", dialogClickListener)
                            .setNegativeButton("Abbrechen", dialogClickListener).show();
                }else if(MainActivity.settings.getLanguage().equals("en")){
                    builder.setMessage(R.string.settings_factory_settings_text_en)
                            .setPositiveButton("OK", dialogClickListener)
                            .setNegativeButton("Cancel", dialogClickListener).show();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5)
        {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            String name = ringtone.getTitle(this);
            if (uri != null) {
                MainActivity.settings.setMusicChoice(uri.toString());
                MainActivity.settings.setMusicName(name);
                buttonMusicChoice.setText(name);
            }else {
                MainActivity.settings.setMusicChoice(null);
                MainActivity.settings.setMusicName("-");
                buttonMusicChoice.setText("-");
            }
            MainActivity.settings.saveSettings(getApplicationContext());
        }
    }

    //Übersetzte auf Englisch oder Deutsch
    private void translateActivity(){
        if(MainActivity.settings.getLanguage().equals("de")){
            mToolbarCustomTitle.setText(R.string.settings_heading);
            textViewMusic.setText(R.string.settings_alarm);
            checkBoxVibration.setText(R.string.settings_vibration);
            checkBoxNotification.setText(R.string.settings_notification);
            textViewLanguage.setText(R.string.settings_language);
            spinnerLanguage.setPrompt("Sprache");
            buttonShowhints.setText(R.string.settings_show_hints);
            buttonDefaultSettings.setText(R.string.settings_factory_settings);
        }else if(MainActivity.settings.getLanguage().equals("en")){
            mToolbarCustomTitle.setText(R.string.settings_heading_en);
            textViewMusic.setText(R.string.settings_alarm_en);
            checkBoxVibration.setText(R.string.settings_vibration_en);
            checkBoxNotification.setText(R.string.settings_notification_en);
            textViewLanguage.setText(R.string.settings_language_en);
            spinnerLanguage.setPrompt("Language");
            buttonShowhints.setText(R.string.settings_show_hints_en);
            buttonDefaultSettings.setText(R.string.settings_factory_settings_en);
        }
    }
}
