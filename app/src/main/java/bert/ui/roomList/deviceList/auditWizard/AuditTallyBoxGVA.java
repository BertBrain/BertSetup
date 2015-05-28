package bert.ui.roomList.deviceList.auditWizard;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import bert.ui.R;
import bert.ui.roomList.RoomListActivity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liamcook on 5/1/15.
 */
public class AuditTallyBoxGVA extends ArrayAdapter<String> {

    private HashMap<String, View> categoryCells = new HashMap<>();
    private static HashMap<String, Integer> categoryCounts;
    private List<String> categoryNames;
    private RoomListActivity activity;
    private AuditWizardFragment parent;
    private int projectID;
    private String buildingID;

    public AuditTallyBoxGVA(AuditWizardFragment parent, RoomListActivity activity, int recourseId, List<String> categoryNames, int projectID, String buildingID) {
        super(activity, recourseId, categoryNames);
        this.parent = parent;
        this.categoryNames = categoryNames;
        this.activity = activity;
        this.projectID = projectID;
        this.buildingID = buildingID;
    }

    public HashMap<String, Integer> getCounts() {
        return this.categoryCounts;
    }

    @Override
    public int getCount() {
        return categoryNames.size() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridCell;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (position == categoryNames.size()) {
            gridCell = inflater.inflate(R.layout.fragment_audit_wizard_add_category_cell, parent, false);
            gridCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    log("creating new category");
                    createNewCategory();
                }
            });
        } else {
            if (!categoryCells.keySet().contains(categoryNames.get(position))) {
                log("creating grid cell at position for category: " + categoryNames.get(position));
                if (!categoryCounts.keySet().contains(categoryNames.get(position))){
                    categoryCounts.put(categoryNames.get(position), 0);
                }

                gridCell = inflater.inflate(R.layout.fragment_audit_wizard_category_cell, parent, false);

                TextView deviceTypeTextField = (TextView) gridCell.findViewById(R.id.deviceTypeTextField);
                deviceTypeTextField.setText(categoryNames.get(position));

                Button incrementButton = (Button) gridCell.findViewById(R.id.incrementButton);
                Button decrementButton = (Button) gridCell.findViewById(R.id.decrementButton);

                TextView deviceTypeCounter = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
                deviceTypeCounter.setText(categoryCounts.get(categoryNames.get(position)).toString());

                log("creating click listeners");
                incrementButton.setOnClickListener(new buttonListener(categoryNames.get(position), 1, this));
                decrementButton.setOnClickListener(new buttonListener(categoryNames.get(position), -1, this));
                categoryCells.put(categoryNames.get(position), gridCell);
            } else {
                gridCell = categoryCells.get(categoryNames.get(position));
            }
        }
        log("grid cell to return: " + gridCell);
        return gridCell;
    }

    static public void resetCounts(){
        categoryCounts = new HashMap<>();
    }

    void createNewCategory() {
        Intent intent = new Intent(activity, AddCategoryFrameActivity.class);
        intent.putExtra(AddCategoryFrameActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(AddCategoryFrameActivity.ARG_BUILDING_ID, buildingID);
        activity.startActivityForResult(intent, 1);
    }

    void addCategoryName(String categoryName, int numberToAdd) {
        int oldCount = categoryCounts.get(categoryName);
        int newCount;
        if (oldCount > 0 || numberToAdd > 0) {
            newCount = oldCount + numberToAdd;
        } else {
            newCount = oldCount;
        }

        categoryCounts.put(categoryName, newCount);
        setCountForCategoryName(categoryName, newCount);

        log("count for: " + categoryName + " is now: " + categoryCounts.get(categoryName));
    }

    void setCountForCategoryName(String categoryName, int count) {
        log("setting count for device type: " + categoryName + " " + count);
        View gridCell = categoryCells.get(categoryName);
        TextView deviceTypeCounter = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
        log("deviceTypeCounter old count" + deviceTypeCounter.getText().toString());
        deviceTypeCounter.setText(String.valueOf(count));
    }

    public int updateBertTotal() {
        int totalCount = 0;
        for (Integer i : categoryCounts.values()) {
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
    String categoryName;

    public buttonListener(String categoryName, int incrementAmount, AuditTallyBoxGVA owner) {
        this.categoryName = categoryName;
        this.owner = owner;
        this.incrementAmount = incrementAmount;
    }

    @Override
    public void onClick(View v) {
        System.out.println(incrementAmount);
        owner.addCategoryName(categoryName, incrementAmount);
        owner.updateBertTotal();
    }
}