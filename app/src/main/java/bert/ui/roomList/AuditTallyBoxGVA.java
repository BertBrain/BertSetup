package bert.ui.roomList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import bert.data.proj.Category;
import bert.ui.R;

import java.util.HashMap;
import java.util.List;


/**
 * Created by liamcook on 5/1/15.
 */
public class AuditTallyBoxGVA extends ArrayAdapter<Category> {

    HashMap<Category, View> cells = new HashMap<Category, View>();
    static HashMap<Category, Integer> counts;
    List<Category> deviceTypes;
    Activity activity;
    AuditWizardView owner;
    private int projectID;

    public AuditTallyBoxGVA(AuditWizardView owner, Activity activity, int recourseId, List<Category> deviceTypes, int projectID) {
        super(activity, recourseId, deviceTypes);
        this.deviceTypes = deviceTypes;
        this.activity = activity;
        this.owner = owner;
        this.projectID = projectID;
    }

    @Override
    public int getCount(){
        return deviceTypes.size() + 1;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View gridCell;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (position == deviceTypes.size()) {
            gridCell = inflater.inflate(R.layout.fragment_add_category_cell, parent, false);
            gridCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("creating new category");
                    createNewCategory();
                }
            });
        } else {
            if (!cells.keySet().contains(deviceTypes.get(position))) {
                System.out.println("creating grid cell at position for category: " + deviceTypes.get(position).getName());

                if (!counts.keySet().contains(deviceTypes.get(position))){
                    counts.put(deviceTypes.get(position), 0);
                }

                gridCell = inflater.inflate(R.layout.fragment_grid_cell, parent, false);

                TextView deviceTypeTextField = (TextView) gridCell.findViewById(R.id.deviceTypeTextField);
                deviceTypeTextField.setText(deviceTypes.get(position).getName());

                Button incrementButton = (Button) gridCell.findViewById(R.id.incrementButton);
                Button decrementButton = (Button) gridCell.findViewById(R.id.decrementButton);

                TextView deviceTypeCounter = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
                deviceTypeCounter.setText(counts.get(deviceTypes.get(position)).toString());

                System.out.println("creating click listeners");
                incrementButton.setOnClickListener(new buttonListener(deviceTypes.get(position), 1, this));
                decrementButton.setOnClickListener(new buttonListener(deviceTypes.get(position), -1, this));



                cells.put(deviceTypes.get(position), gridCell);
            } else {
                gridCell = cells.get(deviceTypes.get(position));
            }
        }
        System.out.println("grid cell to return: " + gridCell);
        return gridCell;
    }

    static public void resetCounts(){
        counts = new HashMap<Category, Integer>();
    }

    void createNewCategory() {
        Intent intent = new Intent(activity, AddCategoryFrameActivity.class);
        intent.putExtra(AddCategoryFrameActivity.ARG_PROJECT_ID, projectID);
        activity.startActivityForResult(intent, 1);
    }

    void addToDeviceType(Category deviceType, int numberToAdd) {
        int oldCount = counts.get(deviceType);
        int newCount;
        if (oldCount > 0 || numberToAdd > 0) {
            newCount = oldCount + numberToAdd;
        } else {
            newCount = oldCount;
        }

        counts.put(deviceType, newCount);
        setCountForDeviceType(deviceType, newCount);

        System.out.println("count for: " + deviceType.getName() + " is now: " + counts.get(deviceType));
    }

    void setCountForDeviceType(Category deviceType, int count) {
        System.out.println("setting count for device type: " + deviceType.getName() + " " + count);
        View gridCell = cells.get(deviceType);
        System.out.println(gridCell);
        TextView deviceTypeCounter = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
        System.out.println("deviceTypeCounter old count" + deviceTypeCounter.getText().toString());
        deviceTypeCounter.setText(String.valueOf(count));
    }

    public HashMap<Category, Integer> getCounts(){
        return this.counts;
    }

    public void updateBertTotal() {
        int totalCount = 0;
        for (Integer i : counts.values()) {
            totalCount += i;
        }
        owner.setBertTotalCounter(totalCount);
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
        owner.updateBertTotal();
    }
}