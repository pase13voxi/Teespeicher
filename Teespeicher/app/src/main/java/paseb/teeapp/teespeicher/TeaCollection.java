package paseb.teeapp.teespeicher;

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

/**
 * Created by paseb on 06.02.2016.
 */
public class TeaCollection {

    //Attribute
    private ArrayList<Tea> teaItems;

    //Getter und Setter
    public ArrayList<Tea> getTeaItems() {
        return teaItems;
    }

    public void setTeaItems(ArrayList<Tea> teaItems) {
        this.teaItems = teaItems;
    }

    //Konstruktor
    public TeaCollection(){
        teaItems = new ArrayList<Tea>();
    }

    //Save and Load Data
    public boolean saveCollection(Context context){
        try {
            FileOutputStream fos = context.openFileOutput("TeaCollection", Context.MODE_PRIVATE);
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

    public boolean loadCollection(Context context) {
        try {
            FileInputStream fis = context.openFileInput("TeaCollection");
            ObjectInputStream is = new ObjectInputStream(fis);
            teaItems = (ArrayList<Tea>) is.readObject();
            if(teaItems == null) {
                teaItems = new ArrayList<Tea>();
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
        teaItems = new ArrayList<Tea>();
    }

    public void sort(){
        if(MainActivity.settings.isSort()){
            Collections.sort(teaItems,Tea.TeaSortSortofTea);
        }else{
            Collections.sort(teaItems,Tea.TeaSortDate);
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