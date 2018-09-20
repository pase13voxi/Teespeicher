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

import coolpharaoh.tee.speicher.tea.timer.R;
import coolpharaoh.tee.speicher.tea.timer.views.MainActivity;

/**
 * Created by paseb on 06.02.2016.
 * This is a class, where every tea is listed in an Arraylist.
 */
public class TeaCollection {

    // TODO Auto-generated method stub
    //kann später entfernt werden
    private ArrayList<Tea> oldTeaItems;
    private ArrayList<NTea> old2TeaItems;
    private ArrayList<N2Tea> teaItems;

    public ArrayList<N2Tea> getTeaItems() {
        return teaItems;
    }

    public void setTeaItems(ArrayList<N2Tea> teaItems) {
        this.teaItems = teaItems;
    }

    //Konstruktor
    public TeaCollection(){
        oldTeaItems = new ArrayList<>();
        old2TeaItems = new ArrayList<>();
        teaItems = new ArrayList<>();
    }

    //Save and Load Data
    public boolean saveCollection(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("teacollection_1.5", Context.MODE_PRIVATE);
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
            FileInputStream fis = context.openFileInput("teacollection_1.2");
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
    @SuppressWarnings("unchecked")
    public boolean loadOld2Collection(Context context) {
        try {
            FileInputStream fis = context.openFileInput("teacollection_1.3");
            ObjectInputStream is = new ObjectInputStream(fis);
            old2TeaItems = (ArrayList<NTea>) is.readObject();
            if(old2TeaItems == null) {
                old2TeaItems = new ArrayList<>();
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
    public void convertCollectionToNew(){
        for(int i=0; i<oldTeaItems.size(); i++){
            teaItems.add(new N2Tea(nextId(),oldTeaItems.get(i).getName(), oldTeaItems.get(i).getSortOfTea(),
                    oldTeaItems.get(i).getTemperature(), oldTeaItems.get(i).getCoolDownTime(), oldTeaItems.get(i).getTime(),
                    oldTeaItems.get(i).getAmount(), new Coloring(oldTeaItems.get(i).getColor())));
            teaItems.get(i).setDate(oldTeaItems.get(i).getDate());
            teaItems.get(i).setNote(oldTeaItems.get(i).getNote());
            teaItems.get(i).setCounter(oldTeaItems.get(i).getCounter());
        }
    }

    // TODO Auto-generated method stub
    //kann später entfernt werden
    public void convertCollection2ToNew(){
        for(int i=0; i<old2TeaItems.size(); i++){
            teaItems.add(new N2Tea(nextId(),old2TeaItems.get(i).getName(), old2TeaItems.get(i).getSortOfTea(),
                    old2TeaItems.get(i).getTemperature(), old2TeaItems.get(i).getCoolDownTime(), old2TeaItems.get(i).getTime(),
                    old2TeaItems.get(i).getAmount(), old2TeaItems.get(i).getColoring()));
            teaItems.get(i).setDate(old2TeaItems.get(i).getDate());
            teaItems.get(i).setNote(old2TeaItems.get(i).getNote());
            teaItems.get(i).setCounter(old2TeaItems.get(i).getCounter());
        }
    }

    @SuppressWarnings("unchecked")
    public boolean loadCollection(Context context) {
        try {
            FileInputStream fis = context.openFileInput("teacollection_1.5");
            ObjectInputStream is = new ObjectInputStream(fis);
            teaItems = (ArrayList<N2Tea>) is.readObject();
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
        teaItems.clear();
    }

    public void sort(){
        switch(MainActivity.settings.getSort()){
            case 0:
                Collections.sort(teaItems,N2Tea.TeaSortDate);
                break;
            case 1:
                Collections.sort(teaItems,N2Tea.TeaSortTea);
                break;
            case 2:
                Collections.sort(teaItems,N2Tea.TeaSortTea);
                Collections.sort(teaItems,N2Tea.TeaSortSortofTea);
                break;
        }
    }

    public UUID nextId(){
        UUID id = UUID.randomUUID();
        for (N2Tea teaItem : teaItems) {
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