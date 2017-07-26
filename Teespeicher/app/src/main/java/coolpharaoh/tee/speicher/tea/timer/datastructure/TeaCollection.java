package coolpharaoh.tee.speicher.tea.timer.datastructure;

import android.content.Context;
import android.content.res.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import coolpharaoh.tee.speicher.tea.timer.views.MainActivity;
import coolpharaoh.tee.speicher.tea.timer.R;

/**
 * Created by paseb on 06.02.2016.
 * This is a class, where every tea is listed in an Arraylist.
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
        oldTeaItems = new ArrayList<>();
        teaItems = new ArrayList<>();
    }

    //Save and Load Data
    public boolean saveCollection(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("teacollection_1.1", Context.MODE_PRIVATE);
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
    @SuppressWarnings("unchecked")
    public boolean loadOldCollection(Context context) {
        try {
            FileInputStream fis = context.openFileInput("teacollection_1.0");
            ObjectInputStream is = new ObjectInputStream(fis);
            oldTeaItems = (ArrayList<Tea>) is.readObject();
            if(oldTeaItems == null) {
                oldTeaItems = new ArrayList<>();
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
    //kann später entfernt werden
    public void convertCollectionToNew(Context context){
        for(int i=0; i<oldTeaItems.size(); i++){
            ArrayList<Temperature> temperaturesOld = oldTeaItems.get(i).getTemperature();
            ArrayList<NTemperature> temperaturesNew = new ArrayList<>();
            ArrayList<Time> steepingTimesNew = new ArrayList<>();
            for(int o=0; o<temperaturesOld.size(); o++){
                NTemperature tmpTemperature = new NTemperatureCelsius(temperaturesOld.get(o).getCelsius());
                temperaturesNew.add(tmpTemperature);
                Time tmpTime = new Time(NTemperature.celsiusToSteepingTime(temperaturesOld.get(o).getCelsius()));
                steepingTimesNew.add(tmpTime);
            }
            teaItems.add(new NTea(nextId(),oldTeaItems.get(i).getName(), oldTeaItems.get(i).getSortOfTea(),
                    temperaturesNew, steepingTimesNew, oldTeaItems.get(i).getTime(),
                    oldTeaItems.get(i).getAmount(), oldTeaItems.get(i).getColor()));
            teaItems.get(i).setDate(oldTeaItems.get(i).getDate());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadCollection(Context context) {
        try {
            FileInputStream fis = context.openFileInput("teacollection_1.1");
            ObjectInputStream is = new ObjectInputStream(fis);
            teaItems = (ArrayList<NTea>) is.readObject();
            if(teaItems == null) {
                teaItems = new ArrayList<>();
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
        teaItems = new ArrayList<>();
    }

    public void sort(){
        if(MainActivity.settings.isSort()){
            Collections.sort(teaItems,NTea.TeaSortSortofTea);
        }else{
            Collections.sort(teaItems,NTea.TeaSortDate);
        }
    }

    public UUID nextId(){
        UUID id = UUID.randomUUID();
        for (NTea teaItem : teaItems) {
            if(id.equals(teaItem.getId())){
                id = nextId();
                break;
            }
        }
        return id;
    }

    public int getPositionById(UUID id){
        for(int i=0; i<teaItems.size(); i++){
            if(id.equals(teaItems.get(i).getId())){
                return i;
            }
        }
        return -1;
    }

    public void translateSortOfTea(Context context, String languageFrom, String languageTo){
        String[] varietiesFrom = getOtherSortsOfTea(context, languageFrom);
        String[] varietiesTo = getOtherSortsOfTea(context, languageTo);
        for(int i=0; i<teaItems.size(); i++){
            String sortOfTea = teaItems.get(i).getSortOfTea().getType();
            for(int a=0; a<varietiesFrom.length; a++){
                if(sortOfTea.equals(varietiesFrom[a])){
                    teaItems.get(i).setSortOfTea(new SortOfTea(varietiesTo[a]));
                    break;
                }
            }
        }
        saveCollection(context);
    }

    //Teesorten von einer anderen Sprache
    private String[] getOtherSortsOfTea(Context context, String language) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(language));
        return context.createConfigurationContext(configuration).getResources().getStringArray(R.array.sortsOfTea);
    }
}