package bert.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.TextView;

import bert.ui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liamcook on 5/1/15.
 */
public class AuditTallyBoxGVA extends ArrayAdapter<String> {

    List<String> deviceTypes;
    Activity activity;
    int recourseId;

    public AuditTallyBoxGVA(Activity activity, int recourseId, List<String> deviceTypes) {
        super(activity, recourseId, deviceTypes);
        this.deviceTypes = deviceTypes;
        this.activity = activity;
        this.recourseId = recourseId;
    }



    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;


        //if (view == null){
            LayoutInflater inflater = activity.getLayoutInflater();
            View gridCell = inflater.inflate(R.layout.fragment_grid_cell, parent, false);

        TextView deviceTypeTextField = (TextView) gridCell.findViewById(R.id.deviceTypeTextField);
        deviceTypeTextField.setText(deviceTypes.get(position));

        //} else {

        //}

        //view = activity.findViewById(R.id.gridCellLayout);
        System.out.println(gridCell);
        //System.out.println(view);
        return gridCell;
    }
}
