package bert.data.proj;

/**
 * Created by liamcook on 5/13/15.
 */
public class Time {

    //TODO convert to use 1 integer internally
    private int hour;
    public int minute;
    public boolean isAM;

    public Time(int hour, int minute) {
        isAM = hour < 13;
        this.hour = isAM ? hour : hour - 12;
        this.minute = minute;
    }

    public static void main(String[] args) {
        Time t = new Time(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        int i = t.getMinutes();
        Time t2 = new Time(i);
        System.out.println(t2.description());
    }

    public Time(int minutes) {
        if (minutes >= 720) {
            this.isAM = false;
            minutes -= 720;
        } else {
            this.isAM = true;
        }

        this.hour = minutes / 60;
        this.minute = minutes % 60;
    }

    public int getMinutes() {
        int minutes = (isAM) ? 0 : 720;
        minutes += (hour * 60);
        minutes += minute;
        return minutes;
    }

    public void set(int hour, int minute) {
        isAM = hour < 13;
        this.hour = isAM ? hour : hour - 12;
        this.minute = minute;
    }

    public int hour24() {
        return hour + (isAM ? 0 : 12);
    }

    public int hour12() {
        return hour;
    }

    public String description() {
        return hour + " : " + (minute < 10 ? "0" + minute : minute) + (isAM ? "  AM" : "  PM");
    }

    public boolean greaterThan(Time time) {
        System.out.println("this time: " + this.hour24());
        System.out.println("time time: " + time.hour24());
        if (this.hour24() > time.hour24()) {
            return true;
        }
        if (this.hour24() == time.hour24()) {
            return this.minute > time.minute;
        }
        if (this.hour24() < time.hour24()) {
            return false;
        }
        return false;
    }

}