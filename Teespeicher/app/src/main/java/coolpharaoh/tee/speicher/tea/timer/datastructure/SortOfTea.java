package coolpharaoh.tee.speicher.tea.timer.datastructure;

import android.content.Context;
import android.graphics.Color;

import java.io.Serializable;

import coolpharaoh.tee.speicher.tea.timer.R;

/**
 * Created by paseb on 18.02.2017.
 * Class, that describes the variety of a tea.
 */

public class SortOfTea implements Serializable {

    private String type;

    public SortOfTea(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public static int getVariatyColor(Variety type){
        switch(type){
            case BlackTea:
                return Color.argb(255, 20, 20, 80);
            case GreenTea:
                return Color.argb(255, 154, 205, 50);
            case YellowTea:
                return Color.argb(255, 255, 194, 75);
            case WhiteTea:
                return Color.argb(255, 255, 249, 150);
            case OolongTea:
                return Color.argb(255, 255, 165, 0);
            case PuErhTea:
                return Color.argb(255, 139, 37, 0);
            case HerbalTea:
                return Color.argb(255, 67, 153, 54);
            case FruitTea:
                return Color.argb(255, 255, 42, 22);
            case RooibusTea:
                return Color.argb(255, 250, 90, 0);
            default:
                return Color.argb(255, 127, 127, 186);
        }
    }

    public static String getHintTemperature(Context context, Variety type, String unit) {
        switch (type) {
            case BlackTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_blacktea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_blacktea);
            case GreenTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_greentea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_greentea);
            case YellowTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_yellowtea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_yellowtea);
            case WhiteTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_whitetea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_whitetea);
            case OolongTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_oolongtea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_oolongtea);
            case PuErhTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_puerhtea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_puerhtea);
            case HerbalTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_herbaltea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_herbaltea);
            case FruitTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_fruittea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_fruittea);
            case RooibusTea:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_rooibustea) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_rooibustea);
            default:
                return unit.equals("Celsius") ?
                        context.getResources().getString(R.string.newtea_hint_celsius_other) :
                        context.getResources().getString(R.string.newtea_hint_fahrenheit_other);
        }
    }

    public static String getHintAmount(Context context, Variety type, String unit) {
        switch (type) {
            case BlackTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_blacktea) :
                        context.getResources().getString(R.string.newtea_hint_gr_blacktea);
            case GreenTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_greentea) :
                        context.getResources().getString(R.string.newtea_hint_gr_greentea);
            case YellowTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_yellowtea) :
                        context.getResources().getString(R.string.newtea_hint_gr_yellowtea);
            case WhiteTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_whitetea) :
                        context.getResources().getString(R.string.newtea_hint_gr_whitetea);
            case OolongTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_oolongtea) :
                        context.getResources().getString(R.string.newtea_hint_gr_oolongtea);
            case PuErhTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_puerhtea) :
                        context.getResources().getString(R.string.newtea_hint_gr_puerhtea);
            case HerbalTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_herbaltea) :
                        context.getResources().getString(R.string.newtea_hint_gr_herbaltea);
            case FruitTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_fruittea) :
                        context.getResources().getString(R.string.newtea_hint_gr_fruittea);
            case RooibusTea:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_rooibustea) :
                        context.getResources().getString(R.string.newtea_hint_gr_rooibustea);
            default:
                return unit.equals("Ts") ?
                        context.getResources().getString(R.string.newtea_hint_ts_other) :
                        context.getResources().getString(R.string.newtea_hint_gr_other);
        }
    }

    public static String getHintTime(Context context, Variety type) {
        switch (type) {
            case BlackTea:
                return context.getResources().getString(R.string.newtea_hint_time_blacktea);
            case GreenTea:
                return context.getResources().getString(R.string.newtea_hint_time_greentea);
            case YellowTea:
                return context.getResources().getString(R.string.newtea_hint_time_yellowtea);
            case WhiteTea:
                return context.getResources().getString(R.string.newtea_hint_time_whitetea);
            case OolongTea:
                return context.getResources().getString(R.string.newtea_hint_time_oolongtea);
            case PuErhTea:
                return context.getResources().getString(R.string.newtea_hint_time_puerhtea);
            case HerbalTea:
                return context.getResources().getString(R.string.newtea_hint_time_herbaltea);
            case FruitTea:
                return context.getResources().getString(R.string.newtea_hint_time_fruittea);
            case RooibusTea:
                return context.getResources().getString(R.string.newtea_hint_time_rooibustea);
            default:
                return context.getResources().getString(R.string.newtea_hint_time_other);
        }
    }

}
