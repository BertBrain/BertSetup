package bert.ui.buildingList;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import bert.data.proj.Time;
import bert.ui.BertAlert;

/**
 * Created by liamcook on 5/14/15.
 */
public class TimeRangeDisplay implements TimePickerDialog.OnTimeSetListener {

    private Activity activity;

    private TextView startTimeDisplay;
    private TextView endTimeDisplay;
    private TextView activeTimeDisplay;

    private Time startTime;
    private Time endTime;
    private Time activeTime;

    public TimeRangeDisplay(Activity activity, TextView startTimeDisplay, Time nStartTime, TextView endTimeDisplay, Time nEndTime) {
        this.activity = activity;
        this.startTimeDisplay = startTimeDisplay;
        this.endTimeDisplay = endTimeDisplay;
        this.startTime = nStartTime;
        this.endTime = nEndTime;

        this.startTimeDisplay.setText(startTime.description());
        this.startTimeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked start time display");
                launchTimePicker((TextView) v, startTime);
            }
        });

        this.endTimeDisplay.setText(endTime.description());
        this.endTimeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked end time display");
                launchTimePicker((TextView)v, endTime);
            }
        });
    }

    private void launchTimePicker(TextView v, Time initialTime) {
        activeTimeDisplay = v;
        activeTime = initialTime;
        (new TimePickerDialog(activity, this, initialTime.getHour24(), initialTime.getMinute(), false)).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        activeTime.set(hourOfDay, minute); //activetime = new Time() disconnets active time from the start time or end time
        if (endTime.greaterThan(startTime)) {
            activeTimeDisplay.setText(activeTime.description());
        } else {
            log("starting end/start time alert");
            BertAlert.show(activity, "End time cannot be before start time", "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    launchTimePicker(activeTimeDisplay, activeTime);
                }
            });
        }
    }

    public Time getStartTime() { return  startTime; }
    public Time getEndTime() {return  endTime; }

    private void log(String message) {
        Log.d("TimeRangeDisplay", message);
    }
}
