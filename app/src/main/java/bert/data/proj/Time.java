package bert.data.proj;

import java.io.Serializable;

/**
 * Created by liamcook on 5/13/15.
 */
public class Time implements Serializable{


    private int hour;
    public int minute;
    public boolean isAM;

    public Time(int hour, int minute){
        isAM = hour < 13;
        this.hour = isAM ? hour : hour - 12;
        this.minute = minute;
    }

    public int hour24(){
        return hour + (isAM ? 0 : 12);
    }

    public int hour12(){
        return hour;
    }

    public String description(){
        return hour + " : " + (minute < 10 ? "0" + minute : minute) + (isAM ? "  AM" : "  PM");
    }

    public boolean greaterThan(Time time){
        if (this.hour24() > time.hour24()) {
            return true;
        }
        if (this.hour24() == time.hour24()){
            return this.minute > time.minute;
        }
        if (this.hour24() < time.hour24()){
            return false;
        }
        return false;
    }
}