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
    private boolean animation;
    private String temperatureUnit;
    private String language;
    private boolean showteaAlert;
    private boolean mainProblemAlert;
    private boolean mainRateAlert;
    private int mainRatecounter;
    //0 = sort by Date, 1 = sort alphabethically, 2 = sort by variety
    private int sort;

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

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isShowteaAlert() {
        return showteaAlert;
    }

    public void setShowteaAlert(boolean showteaAlert) {
        this.showteaAlert = showteaAlert;
    }

    public boolean isMainProblemAlert() {
        return mainProblemAlert;
    }

    public void setMainProblemAlert(boolean mainProblemAlert) {
        this.mainProblemAlert = mainProblemAlert;
    }

    public boolean isMainRateAlert() {
        return mainRateAlert;
    }

    public void setMainRateAlert(boolean mainRateAlert) {
        this.mainRateAlert = mainRateAlert;
    }

    public int getMainRatecounter() {
        return mainRatecounter;
    }

    public void incrementMainRatecounter() {
        mainRatecounter++;
    }

    public void resetMainRatecounter() {
        this.mainRatecounter = 0;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public ActualSetting() {
        setDefault();
    }

    public void setDefault() {
        musicChoice = "content://settings/system/ringtone";
        musicName = "Default";
        vibration = false;
        notification = true;
        animation = true;
        temperatureUnit = "Celsius";
        language = "de";
        showteaAlert = true;
        mainProblemAlert = true;
        mainRateAlert = true;
        mainRatecounter = 0;
        sort = 0;
    }

    //Save and Load Data
    public boolean saveSettings(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput("ActualSetting1.0", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(musicChoice);
            os.writeObject(musicName);
            os.writeBoolean(vibration);
            os.writeBoolean(notification);
            os.writeBoolean(animation);
            os.writeObject(temperatureUnit);
            os.writeBoolean(showteaAlert);
            os.writeBoolean(mainProblemAlert);
            os.writeBoolean(mainRateAlert);
            os.writeInt(mainRatecounter);
            os.writeObject(language);
            os.writeInt(sort);
            os.close();
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO
    //kann später entfernt werden
    public boolean loadOldSettings(Context context) {
        try {
            FileInputStream fis = context.openFileInput("ActualSetting");
            ObjectInputStream is = new ObjectInputStream(fis);
            musicChoice = (String) is.readObject();
            musicName = (String) is.readObject();
            vibration = is.readBoolean();
            notification = is.readBoolean();
            animation = is.readBoolean();
            temperatureUnit = (String) is.readObject();
            is.readBoolean();
            showteaAlert = is.readBoolean();
            language = (String) is.readObject();
            sort = is.readInt();
            is.close();
            fis.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loadSettings(Context context) {
        try {
            FileInputStream fis = context.openFileInput("ActualSetting1.0");
            ObjectInputStream is = new ObjectInputStream(fis);
            musicChoice = (String) is.readObject();
            musicName = (String) is.readObject();
            vibration = is.readBoolean();
            notification = is.readBoolean();
            animation = is.readBoolean();
            temperatureUnit = (String) is.readObject();
            showteaAlert = is.readBoolean();
            mainProblemAlert = is.readBoolean();
            mainRateAlert = is.readBoolean();
            mainRatecounter = is.readInt();
            language = (String) is.readObject();
            sort = is.readInt();
            is.close();
            fis.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
