package paseb.teeapp.teespeicher;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by paseb on 13.02.2016.
 */
public class ActualSetting {
    private String musicChoice;
    private String musicName;
    private boolean vibration;
    private boolean notification;
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
