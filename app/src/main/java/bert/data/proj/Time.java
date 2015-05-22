package bert.data.proj;

/**
 * Created by liamcook on 5/13/15.
 * @author lcook
 * @author afiolmahon
 */
public class Time {

    private int rawTime;

    public Time(int hour, int minute) {
        set(hour, minute);
    }

    public int getHour24() {
        return rawTime / 60;
    }

    public int getMinute() {
        return rawTime % 60;
    }

    public Time subtract(Time time) {
        int minuteDifference = this.getRaw() - time.getRaw();
        return new Time(minuteDifference);
    }

    public Time(int rawTime) {
        this.rawTime = rawTime;
    }

    public int getRaw() {
        return this.rawTime;
    }

    public void set(int hour, int minute) {
        rawTime = (hour * 60);
        rawTime += minute;
    }

    public String description() {
        boolean isPM = (getHour24() > 12);
        String hourDisplay = Integer.toString(getHour24() - (isPM ? 12 : 0));
        if (hourDisplay.length() < 2) {
            hourDisplay = "0" + hourDisplay;
        }
        String minuteDisplay = Integer.toString(getMinute());
        if (minuteDisplay.length() < 2) {
            minuteDisplay = "0" + minuteDisplay;
        }
        return hourDisplay + " : "  + minuteDisplay + ((isPM) ? " PM" : " AM");
    }

    public boolean greaterThan(Time time) {
        return this.getRaw() > time.getRaw();
    }
}