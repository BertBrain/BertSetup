package bert.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import bert.ui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liamcook on 5/1/15.
 */
public class AuditTallyBoxGVA extends ArrayAdapter<String> {

    List<AuditTallyBox>tallyBoxViews;

    List<String> deviceTypes;
    Activity activity;
    int recourseId;

    public AuditTallyBoxGVA(Activity activity, int recourseId, List<String> deviceTypes) {
        super(activity, recourseId, deviceTypes);
        this.deviceTypes = deviceTypes;
        this.activity = activity;
        this.recourseId = recourseId;
        tallyBoxViews = new ArrayList<AuditTallyBox>();
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        View gridCell;
        if (view == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            gridCell = inflater.inflate(R.layout.fragment_grid_cell, parent, false);

            TextView deviceTypeTextField = (TextView) gridCell.findViewById(R.id.deviceTypeTextField);
          deviceTypeTextField.setText(deviceTypes.get(position));

            //deviceTypeCounter = (TextView) getView().findViewById(R.id.deviceCounterTextField);
            Button incrementButton = (Button) gridCell.findViewById(R.id.incrementButton);
            Button decrementButton = (Button) gridCell.findViewById(R.id.decrementButton);

            System.out.println("creating click listeners");
            incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    System.out.println("incrementing");
                    //increment();

                }
            });

            decrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("decrementing");
                    //decrement();

                }
            });

        } else {
            gridCell = convertView;
        }

        //view = activity.findViewById(R.id.gridCellLayout);
        System.out.println(gridCell);
        //System.out.println(view);
        return gridCell;
    }
}
