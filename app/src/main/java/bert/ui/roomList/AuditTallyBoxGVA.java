package bert.ui.roomList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import bert.database.BertUnit;
import bert.database.Category;
import bert.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by liamcook on 5/1/15.
 */
public class AuditTallyBoxGVA extends ArrayAdapter<Category> {

    HashMap<Category, View> cells = new HashMap<Category, View>();
    HashMap<Category, Integer> counts = new HashMap<Category, Integer>();
    List<Category> deviceTypes;
    Activity activity;
    int recourseId;

    public AuditTallyBoxGVA(Activity activity, int recourseId, List<Category> deviceTypes) {
        super(activity, recourseId, deviceTypes);
        this.deviceTypes = deviceTypes;
        this.activity = activity;
        this.recourseId = recourseId;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        View gridCell;
        if (view == null) {
            counts.put(deviceTypes.get(position), 0);

            LayoutInflater inflater = activity.getLayoutInflater();
            gridCell = inflater.inflate(R.layout.fragment_grid_cell, parent, false);

            TextView deviceTypeTextField = (TextView) gridCell.findViewById(R.id.deviceTypeTextField);
            deviceTypeTextField.setText(deviceTypes.get(position).getName());

            Button incrementButton = (Button) gridCell.findViewById(R.id.incrementButton);
            Button decrementButton = (Button) gridCell.findViewById(R.id.decrementButton);

            System.out.println("creating click listeners");
            incrementButton.setOnClickListener(new buttonListener(deviceTypes.get(position), 1, this));
            decrementButton.setOnClickListener(new buttonListener(deviceTypes.get(position), -1, this));

            cells.put(deviceTypes.get(position), gridCell);

        } else {
            gridCell = convertView;
        }

        System.out.println(gridCell);
        return gridCell;
    }

    void addToDeviceType(Category deviceType, int numberToAdd) {
        int oldCount = counts.get(deviceType);
        if (oldCount > 0 || numberToAdd > 0){
            counts.put(deviceType, oldCount + numberToAdd);
        }
        System.out.println("count for: " + deviceType.getName() + " is now: " + counts.get(deviceType));
        setCountForDeviceType(deviceType, counts.get(deviceType));
    }

    void setCountForDeviceType(Category deviceType, int count) {
        View gridCell = cells.get(deviceType);
        TextView deviceTypeCounter = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
        deviceTypeCounter.setText(String.valueOf(count));
    }

    public HashMap<Category, Integer> getCounts(){
        return this.counts;
    }


}

class buttonListener implements View.OnClickListener {

    int incrementAmount = 0;
    AuditTallyBoxGVA owner;
    Category deviceType;

    public buttonListener(Category deviceType, int incrementAmount, AuditTallyBoxGVA owner) {
        this.deviceType = deviceType;
        this.owner = owner;
        this.incrementAmount = incrementAmount;
    }


    @Override
    public void onClick(View v) {
        System.out.println(incrementAmount);
        owner.addToDeviceType(deviceType, incrementAmount);
    }
}