package bert.ui.roomList.deviceList.auditWizard;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import bert.data.proj.RoomAudit;
import bert.ui.R;
import bert.ui.roomList.roomListActivity.AuditRoomListActivity;

import java.util.HashMap;

/**
 * Created by liamcook on 5/1/15.
 * @author lcook
 * @author afiolmahon
 */
public class AuditTallyBoxGVA extends ArrayAdapter<String> {
//FIXME audit tally box GVA creates 5 views of position 0 for some reason, related to first item increment not updating consistently?
    private HashMap<String, View> categoryCells = new HashMap<>();
    private AuditRoomListActivity activity;
    private String projectID;
    private RoomAudit roomAudit;

    public AuditTallyBoxGVA(AuditRoomListActivity activity, RoomAudit roomAudit, String projectID) {
        super(activity, android.R.layout.simple_gallery_item, roomAudit.getCategoryNames());
        this.activity = activity;
        this.projectID = projectID;
        this.roomAudit = roomAudit;
    }

    @Override
    public int getCount() {
        return roomAudit.getCategoryNames().size() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridCell;
        LayoutInflater inflater = activity.getLayoutInflater();
        log("getting view of position: " + Integer.toString(position));
        try {
            String categoryName = roomAudit.getCategoryNames().get(position);
            if (categoryCells.containsKey(categoryName)) {
                gridCell = categoryCells.get(categoryName);
            } else {
                gridCell = inflater.inflate(R.layout.audit_wizard_component_cell_category, parent, false);

                TextView categoryNameTextView = (TextView) gridCell.findViewById(R.id.deviceTypeTextField);
                categoryNameTextView.setText(categoryName);

                TextView categoryTotalTextView = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
                categoryTotalTextView.setText(Integer.toString(roomAudit.getCategoryCount(categoryName)));

                Button incrementButton = (Button) gridCell.findViewById(R.id.incrementButton);
                incrementButton.setOnClickListener(new IncrementButtonListener(categoryName, 1, this));

                Button decrementButton = (Button) gridCell.findViewById(R.id.decrementButton);
                decrementButton.setOnClickListener(new IncrementButtonListener(categoryName, -1, this));

                categoryCells.put(categoryName, gridCell);
            }
        } catch (IndexOutOfBoundsException e) {
            gridCell = inflater.inflate(R.layout.audit_wizard_component_cell_add_category, parent, false);
            gridCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    log("creating new category");
                    Intent intent = new Intent(activity, AddCategoryFrameActivity.class);
                    intent.putExtra(AddCategoryFrameActivity.ARG_PROJECT_ID, projectID);
                    intent.putExtra(AddCategoryFrameActivity.ARG_BUILDING_ID, roomAudit.getBuildingID());
                    activity.startActivityForResult(intent, 1);
                }
            });
        }
        return gridCell;
    }

    protected void incrementCategory(String categoryName, int amount) {
        int updatedCount = roomAudit.getCategoryCount(categoryName) + amount;
        if (updatedCount >= 0) {
            setCountForCategory(categoryName, updatedCount);
        }
    }

    private void setCountForCategory(String categoryName, int count) {
        log(categoryName + " count = " + count);
        roomAudit.setCategoryCount(categoryName, count);
        View gridCell = categoryCells.get(categoryName);
        TextView categoryTotalTextView = (TextView) gridCell.findViewById(R.id.deviceCounterTextField);
        categoryTotalTextView.setText(String.valueOf(count));
    }

    private void log(String message) {
        Log.d("AuditTallyBox", message);
    }
}

class IncrementButtonListener implements View.OnClickListener {
    private int incrementAmount;
    private AuditTallyBoxGVA owner;
    private String categoryName;

    public IncrementButtonListener(String categoryName, int incrementAmount, AuditTallyBoxGVA owner) {
        this.categoryName = categoryName;
        this.owner = owner;
        this.incrementAmount = incrementAmount;
    }

    @Override
    public void onClick(View v) {
        owner.incrementCategory(categoryName, incrementAmount);
    }
}