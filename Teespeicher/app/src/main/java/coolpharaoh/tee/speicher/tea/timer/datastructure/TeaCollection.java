package coolpharaoh.tee.speicher.tea.timer.datastructure;

import android.content.Context;
import android.content.res.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import coolpharaoh.tee.speicher.tea.timer.Tea;
import coolpharaoh.tee.speicher.tea.timer.views.MainActivity;
import coolpharaoh.tee.speicher.tea.timer.R;

/**
 * Created by paseb on 06.02.2016.
 */
public class TeaCollection {

    //Attribute
    //kann später entfernt werden
    private ArrayList<Tea> oldTeaItems;
    private ArrayList<NTea> teaItems;

    public ArrayList<NTea> getTeaItems() {
        return teaItems;
    }

    public void setTeaItems(ArrayList<NTea> teaItems) {
        this.teaItems = teaItems;
    }

    //Konstruktor
    public TeaCollection(){
        oldTeaItems = new ArrayList<Tea>();
        teaItems = new ArrayList<NTea>();
    }

    //Save and Load Data
    public boolean saveCollection(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("teacollection", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(teaItems);
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


    // TODO Auto-generated method stub
    //kann später entfernt werden
    public boolean loadOldCollection(Context context) {
        try {
            FileInputStream fis = context.openFileInput("TeaCollection");
            ObjectInputStream is = new ObjectInputStream(fis);
            oldTeaItems = (ArrayList<Tea>) is.readObject();
            if(oldTeaItems == null) {
                oldTeaItems = new ArrayList<Tea>();
            }
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
    // TODO Auto-generated method stub
    //Funktioniert noch nicht da die Datei nicht gefunden wird
    public boolean deleteOldCollection(){
        File file = new File("TeaCollection.ser");
        if(file.delete()){
            return true;
        }else{
            return false;
        }
    }

    // TODO Auto-generated method stub
    //kann später entfernt werden
    public void convertCollectionToNew(){
        for(int i=0; i<oldTeaItems.size(); i++){
            ArrayList<Temperature> tempList = new ArrayList<>();
            ArrayList<Time> timeList = new ArrayList<>();
            for(int o=0; o<oldTeaItems.get(i).getTemperature().size();o++){
                tempList.add(new TemperatureCelsius(oldTeaItems.get(i).getTemperature().get(o)));
                timeList.add(new Time(oldTeaItems.get(i).getTime().get(o)));
            }
            teaItems.add(new NTea(oldTeaItems.get(i).getName(), oldTeaItems.get(i).getSortOfTea(), tempList,
                    timeList, new AmountTs(oldTeaItems.get(i).getTeelamass())));
            teaItems.get(i).setDate(oldTeaItems.get(i).getDate());
        }
    }

    public boolean loadCollection(Context context) {
        try {
            FileInputStream fis = context.openFileInput("teacollection");
            ObjectInputStream is = new ObjectInputStream(fis);
            teaItems = (ArrayList<NTea>) is.readObject();
            if(teaItems == null) {
                teaItems = new ArrayList<NTea>();
            }
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

    public void deleteCollection(){
        teaItems = new ArrayList<NTea>();
    }

    public void sort(){
        if(MainActivity.settings.isSort()){
            Collections.sort(teaItems,NTea.TeaSortSortofTea);
        }else{
            Collections.sort(teaItems,NTea.TeaSortDate);
        }
    }

    public int getPositionByName(String name){
        for(int i=0; i<teaItems.size(); i++){
            if(name.equals(teaItems.get(i).getName())){
                return i;
            }
        }
        return -1;
    }

    //Muss eventuell für mehr Sprachensupport umgeschrieben werden
    public void translateSortOfTea(String language, Context context){
        String[] sorts_en = getOtherSortsOfTea("en",context);
        String[] sorts_de = getOtherSortsOfTea("de",context);
        for(int i=0; i<teaItems.size(); i++){
            String sortOfTea = teaItems.get(i).getSortOfTea();
            if(language.equals("de")){
                for(int a=0; a<sorts_en.length; a++){
                    if(sortOfTea.equals(sorts_en[a])){
                        teaItems.get(i).setSortOfTea(sorts_de[a]);
                        break;
                    }
                }
            }else if(language.equals("en")){
                for(int a=0; a<sorts_de.length; a++){
                    if(sortOfTea.equals(sorts_de[a])){
                        teaItems.get(i).setSortOfTea(sorts_en[a]);
                        break;
                    }
                }
            }
        }
        saveCollection(context);
    }

    //Teesorten von einer anderen Sprache
    private String[] getOtherSortsOfTea(String language, Context context) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(language));
        String[] sortsOfTea = context.createConfigurationContext(configuration).getResources().getStringArray(R.array.sortsOfTea);
        return sortsOfTea;
    }
}