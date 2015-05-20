package bert.ui.roomList;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    private HashMap<Category, View> categoryCells = new HashMap<>();
    private static HashMap<Category, Integer> counts;
    private List<Category> deviceTypes;
    private RoomListActivity activity;
    private AuditWizardFragment parent;
    private int projectID;
    private int buildingID;

    public AuditTallyBoxGVA(AuditWizardFragment parent, RoomListActivity activity, int recourseId, List<Category> deviceTypes, int projectID, int buildingID) {
        super(activity, recourseId, deviceTypes);
        this.parent = parent;
        this.deviceTypes = deviceTypes;
        this.activity = activity;
        this.projectID = projectID;
        this.buildingID = buildingID;
    }

    public HashMap<Category, Integer> getCounts() {
        return this.counts;
    }

    @Override
    public int getCount() {
        return deviceTypes.size() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridCell;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (position == deviceTypes.size()) {
            gridCell = inflater.inflate(R.layout.fragment_audit_wizard_add_category_cell, parent, false);
            gridCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    log("creating new category");
                    createNewCategory();
                }
            });
        } else {
            if (!categoryCells.keySet().contains(deviceTypes.get(position))) {
                log("creating grid cell at position for category: " + deviceTypes.get(position).getName());
                if (!counts.keySet().contains(deviceTypes.get(position))){
                    counts.put(deviceTypes.get(position), 0);
                }

                gridCell = inflater.inflate(R.layout.fragment_audit_wizard_category_cell, parent, false);

                TextView deviceTypeTextField = (TextView) gridCell.findViewById(R.id.deviceTypeTextField);
                deviceTypeTextField.setText(deviceTypes.get(position).getName());

                Button incrementButton = (Button) gridCell.findViewById(R.id.incrementButton);
                Button decrementButton = (Button) gridCell.findViewById(R.id.decrementButton);

                TextView deviceTypeCounter = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
                deviceTypeCounter.setText(counts.get(deviceTypes.get(position)).toString());

                log("creating click listeners");
                incrementButton.setOnClickListener(new buttonListener(deviceTypes.get(position), 1, this));
                decrementButton.setOnClickListener(new buttonListener(deviceTypes.get(position), -1, this));
                categoryCells.put(deviceTypes.get(position), gridCell);
            } else {
                gridCell = categoryCells.get(deviceTypes.get(position));
            }
        }
        log("grid cell to return: " + gridCell);
        return gridCell;
    }

    static public void resetCounts(){
        counts = new HashMap<>();
    }

    void createNewCategory() {
        Intent intent = new Intent(activity, AddCategoryFrameActivity.class);
        intent.putExtra(AddCategoryFrameActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(AddCategoryFrameActivity.ARG_BUILDING_ID, buildingID);
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

        log("count for: " + deviceType.getName() + " is now: " + counts.get(deviceType));
    }

    void setCountForDeviceType(Category deviceType, int count) {
        log("setting count for device type: " + deviceType.getName() + " " + count);
        View gridCell = categoryCells.get(deviceType);
        TextView deviceTypeCounter = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
        log("deviceTypeCounter old count" + deviceTypeCounter.getText().toString());
        deviceTypeCounter.setText(String.valueOf(count));
    }

    public int updateBertTotal() {
        int totalCount = 0;
        for (Integer i : counts.values()) {
            totalCount += i;
        }
        parent.setCanFinish(totalCount != 0);
        parent.setBertTotalCounter(totalCount);
        return totalCount;
    }

    private void log(String message) {
        Log.d("AuditTallyBox", message);
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