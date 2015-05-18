package bert.ui.buildingList;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import bert.data.proj.Time;
import bert.ui.BertAlert;
import bert.ui.R;

/**
 * Created by liamcook on 5/14/15.
 */
public class TimeRangeDisplay implements TimePickerDialog.OnTimeSetListener {
    Activity activity;

    private TextView startTimeDisplay;
    private TextView endTimeDisplay;
    private TextView activeTimeDisplay;

    private Time startTime;
    private Time endTime;
    private Time activeTime;

    public TimeRangeDisplay(Activity activity, TextView startTimeDisplay, Time nStartTime, TextView endTimeDisplay, Time nEndTime){
        this.activity = activity;
        this.startTimeDisplay = startTimeDisplay;
        this.endTimeDisplay = endTimeDisplay;
        this.startTime = nStartTime;
        this.endTime = nEndTime;

        startTimeDisplay.setText(startTime.description());
        startTimeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked start time display");
                launchTimePicker((TextView) v, startTime);
            }
        });

        endTimeDisplay.setText(endTime.description());
        endTimeDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked end time display");
                launchTimePicker((TextView)v, endTime);

            }
        });
    }

    private void launchTimePicker(TextView v, Time initialTime){
        activeTimeDisplay = v;
        activeTime = initialTime;

        (new TimePickerDialog(activity, this, initialTime.hour24(), initialTime.minute, false)).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
        System.out.println(hourOfDay);
        activeTime.set(hourOfDay, minute); //activetime = new Time() disconnets active time from the start time or end time
        if (endTime.greaterThan(startTime)) {
            activeTimeDisplay.setText(activeTime.description());
        } else {
            System.out.println("starting end/start time alert");
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
}
