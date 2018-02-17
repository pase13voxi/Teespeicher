package coolpharaoh.tee.speicher.tea.timer.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.listadapter.ListRowItem;
import coolpharaoh.tee.speicher.tea.timer.listadapter.SettingListAdapter;

public class Settings extends AppCompatActivity {

    private enum ListItems {
        Alarm, Vibration, Notification, Animation, TemperatureUnit, Hints, FactorySettings
    }

    private ArrayList<ListRowItem> settingList;
    private SettingListAdapter adapter;
    private AlertDialog radioButtonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Toolbar als ActionBar festlegen
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView mToolbarCustomTitle = (TextView) findViewById(R.id.toolbar_title);
        mToolbarCustomTitle.setText(R.string.settings_heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //write ListView
        settingList = new ArrayList<>();

        ListRowItem itemSound = new ListRowItem(getResources().getString(R.string.settings_alarm),MainActivity.settings.getMusicName());
        settingList.add(itemSound);

        //Decision for Animation, Vibration and Notification
        String[] itemsOnOff = getResources().getStringArray(R.array.settings_options);

        //Get Option for the Vibration
        int vibrationOption = -1;
        if(MainActivity.settings.isVibration()){vibrationOption = 0;
        }else {vibrationOption = 1;}
        ListRowItem itemVibration = new ListRowItem(getResources().getString(R.string.settings_vibration),itemsOnOff[vibrationOption]);
        settingList.add(itemVibration);

        //Get Option for the Notification
        int notificationOption = -1;
        if(MainActivity.settings.isNotification()){notificationOption = 0;
        }else {notificationOption = 1;}
        ListRowItem itemNotification = new ListRowItem(getResources().getString(R.string.settings_notification),itemsOnOff[notificationOption]);
        settingList.add(itemNotification);

        //Get Option for the Animation
        int animationOption = -1;
        if(MainActivity.settings.isAnimation()){animationOption = 0;
        }else {animationOption = 1;}
        ListRowItem itemAnimation = new ListRowItem(getResources().getString(R.string.settings_animation),itemsOnOff[animationOption]);
        settingList.add(itemAnimation);

        //TemperatureUnit
        ListRowItem itemTemperatureUnit = new ListRowItem(getResources().getString(R.string.settings_temperature_unit),MainActivity.settings.getTemperatureUnit());
        settingList.add(itemTemperatureUnit);

        //Hints
        ListRowItem itemHints = new ListRowItem(getResources().getString(R.string.settings_show_hints),getResources().getString(R.string.settings_show_hints_description));
        settingList.add(itemHints);

        //Factory Setting
        ListRowItem itemFactorySettings = new ListRowItem(getResources().getString(R.string.settings_factory_settings),getResources().getString(R.string.settings_factory_settings_description));
        settingList.add(itemFactorySettings);


        //Liste mit Adapter verknüpfen
        adapter = new SettingListAdapter(this, settingList);
        //Adapter dem Listview hinzufügen
        ListView listViewSetting = (ListView) findViewById(R.id.listView_settings);
        listViewSetting.setAdapter(adapter);

        listViewSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItems item = ListItems.values()[position];
                switch(item){
                    case Alarm:
                        settingAlarm();
                        break;
                    case Vibration:
                        settingVibration();
                        break;
                    case Notification:
                        settingNotification();
                        break;
                    case Animation:
                        settingAnimation(view);
                        break;
                    case TemperatureUnit:
                        settingTemperatureUnit();
                        break;
                    case Hints:
                        settingHints();
                        break;
                    case FactorySettings:
                        settingFactorySettings(view);
                        break;
                }

            }
        });
    }

    private void settingAlarm() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.settings_alarm_selection_title);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
        startActivityForResult(intent, 5);
    }

    private void settingVibration() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem = -1;
        if(MainActivity.settings.isVibration()){checkedItem = 0;
        }else {checkedItem = 1;}

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_vibration);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        MainActivity.settings.setVibration(true);
                        break;
                    case 1:
                        MainActivity.settings.setVibration(false);
                        break;
                }
                MainActivity.settings.saveSettings(getApplicationContext());
                settingList.get(1).setDescription(items[item]);
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingNotification() {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem = -1;
        if(MainActivity.settings.isNotification()){checkedItem = 0;
        }else {checkedItem = 1;}

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_notification);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        MainActivity.settings.setNotification(true);
                        break;
                    case 1:
                        MainActivity.settings.setNotification(false);
                        break;
                }
                MainActivity.settings.saveSettings(getApplicationContext());
                settingList.get(2).setDescription(items[item]);
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingAnimation(View v) {
        final String[] items = getResources().getStringArray(R.array.settings_options);

        //Get CheckedItem
        int checkedItem = -1;
        if(MainActivity.settings.isAnimation()){checkedItem = 0;
        }else {checkedItem = 1;}

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_animation);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        MainActivity.settings.setAnimation(true);
                        break;
                    case 1:
                        MainActivity.settings.setAnimation(false);
                        break;
                }
                MainActivity.settings.saveSettings(getApplicationContext());
                settingList.get(3).setDescription(items[item]);
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingTemperatureUnit() {
        final String[] items = getResources().getStringArray(R.array.settings_temperature_units);

        //Get CheckedItem
        int checkedItem = -1;
        if(MainActivity.settings.getTemperatureUnit().equals(items[0])){checkedItem = 0;
        }else {checkedItem = 1;}

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,
                R.style.MaterialThemeDialog);
        builder.setTitle(R.string.settings_temperature_unit);
        builder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                MainActivity.settings.setTemperatureUnit(items[item]);
                MainActivity.settings.saveSettings(getApplicationContext());
                settingList.get(4).setDescription(items[item]);
                adapter.notifyDataSetChanged();
                radioButtonDialog.dismiss();
            }
        });
        radioButtonDialog = builder.create();
        radioButtonDialog.show();
    }

    private void settingHints() {
        MainActivity.settings.setShowteaAlert(true);
        MainActivity.settings.saveSettings(getApplicationContext());
        Toast toast = Toast.makeText(getApplicationContext(), R.string.settings_show_hints_text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void settingFactorySettings(View v) {
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
                        //Adapter hat sich verändert
                        MainActivity.adapter.notifyDataSetChanged();
                        //Felder ändern
                        /*buttonMusicChoice.setText(MainActivity.settings.getMusicName());
                        checkBoxVibration.setChecked(MainActivity.settings.isVibration());
                        checkBoxNotification.setChecked(MainActivity.settings.isNotification());
                        //richtige SpinnerId bekommen
                        int spinnerId = -1;
                        String[] spinnerElements = getResources().getStringArray(R.array.settings_temperature_units);
                        for(int i=0; i<spinnerElements.length; i++){
                            if(spinnerElements[i].equals(MainActivity.settings.getTemperatureUnit())){
                                spinnerId = i;
                                break;
                            }
                        }
                        //spinnerTempUnit.setSelection(spinnerId);*/
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage(R.string.settings_factory_settings_text)
                .setTitle(R.string.settings_factory_settings)
                .setPositiveButton(R.string.settings_factory_settings_ok, dialogClickListener)
                .setNegativeButton(R.string.settings_factory_settings_cancel, dialogClickListener).show();
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
                settingList.get(0).setDescription(name);
            }else {
                MainActivity.settings.setMusicChoice(null);
                MainActivity.settings.setMusicName("-");
                settingList.get(0).setDescription("-");
            }
            MainActivity.settings.saveSettings(getApplicationContext());
            adapter.notifyDataSetChanged();
        }
    }
}
