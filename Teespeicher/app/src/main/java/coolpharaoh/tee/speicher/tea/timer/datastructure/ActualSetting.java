package coolpharaoh.tee.speicher.tea.timer.datastructure;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by paseb on 13.02.2016.
 * Class, that describes all the Settings of the app.
 */
public class ActualSetting {
    private String musicChoice;
    private String musicName;
    private boolean vibration;
    private boolean notification;
    private String temperatureUnit;
    private String language;
    private boolean ocrAlert;
    private boolean showteaAlert;
    //false = sort by Date
    private boolean sort;

    public String getMusicChoice() {
        return musicChoice;
    }

    public void setMusicChoice(String musicChoice) {
        this.musicChoice = musicChoice;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getTemperatureUnit(){
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit){
        this.temperatureUnit = temperatureUnit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isOcrAlert() {
        return ocrAlert;
    }

    public void setOcrAlert(boolean ocrAlert){
        this.ocrAlert = ocrAlert;
    }

    public boolean isShowteaAlert(){
        return showteaAlert;
    }

    public void setShowteaAlert(boolean showteaAlert){
        this.showteaAlert = showteaAlert;
    }

    public boolean isSort() {
        return sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public ActualSetting(){
        setDefault();
    }

    public void setDefault(){
        musicChoice = "content://settings/system/ringtone";
        musicName = "Sunbeam";
        vibration = false;
        notification = true;
        temperatureUnit = "Celsius";
        language = "de";
        ocrAlert = true;
        showteaAlert = true;
        sort = false;
    }

    //Save and Load Data
    public boolean saveSettings(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("ActualSetting", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(musicChoice);
            os.writeObject(musicName);
            os.writeBoolean(vibration);
            os.writeBoolean(notification);
            os.writeObject(temperatureUnit);
            os.writeBoolean(ocrAlert);
            os.writeBoolean(showteaAlert);
            os.writeObject(language);
            os.writeBoolean(sort);
            os.close();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadSettings(Context context) {
        try {
            FileInputStream fis = context.openFileInput("ActualSetting");
            ObjectInputStream is = new ObjectInputStream(fis);
            musicChoice = (String) is.readObject();
            musicName = (String) is.readObject();
            vibration = is.readBoolean();
            notification = is.readBoolean();
            temperatureUnit = (String) is.readObject();
            ocrAlert = is.readBoolean();
            showteaAlert = is.readBoolean();
            language = (String) is.readObject();
            sort = is.readBoolean();
            is.close();
            fis.close();
            return true;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadOldSettings(Context context) {
        try {
            FileInputStream fis = context.openFileInput("ActualSetting");
            ObjectInputStream is = new ObjectInputStream(fis);
            musicChoice = (String) is.readObject();
            musicName = (String) is.readObject();
            vibration = is.readBoolean();
            notification = is.readBoolean();
            temperatureUnit = (String) is.readObject();
            ocrAlert = is.readBoolean();
            language = (String) is.readObject();
            sort = is.readBoolean();
            is.close();
            fis.close();
            return true;
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
