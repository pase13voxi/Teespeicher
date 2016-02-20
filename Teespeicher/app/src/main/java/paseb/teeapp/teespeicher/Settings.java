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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class Settings extends AppCompatActivity {

    private Toolbar toolbar;
    private Button buttonMusicChoice;
    private CheckBox checkBoxVibration;
    private CheckBox checkBoxNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Bedienelemente holen
        buttonMusicChoice = (Button) findViewById(R.id.buttonMusicChoice);
        checkBoxVibration = (CheckBox) findViewById(R.id.checkBoxVibration);
        checkBoxNotification = (CheckBox) findViewById(R.id.checkBoxNotification);
        Button buttonDefaultSettings = (Button) findViewById(R.id.buttonDefaultSettings);

        //Vorhandene Option festlegen
        buttonMusicChoice.setText(MainActivity.settings.getMusicName());
        checkBoxVibration.setChecked(MainActivity.settings.isVibration());
        checkBoxNotification.setChecked(MainActivity.settings.isNotification());

        buttonMusicChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Alarmsound auswählen");
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
                builder.setMessage("Es werden alle Tees gelöscht und die Einstellungen zurückgesetzt.").setPositiveButton("OK", dialogClickListener)
                        .setNegativeButton("Abbrechen", dialogClickListener).show();
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
                MainActivity.settings.setMusicName("Keine");
                buttonMusicChoice.setText("Keine");
            }
            MainActivity.settings.saveSettings(getApplicationContext());
        }
    }
}
