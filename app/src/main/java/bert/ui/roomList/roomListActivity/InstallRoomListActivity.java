package bert.ui.roomList.roomListActivity;

import android.app.Activity;
import android.app.AlertDialog;

import java.util.List;

import bert.ui.common.BertEditTextAlert;
import bert.ui.roomList.deviceList.deviceEditor.DeviceListFragment;

/**
 * Created by liamcook on 6/16/15.
 */
public class InstallRoomListActivity extends GeneralRoomListActivity implements BertEditTextAlert.AlertResponder {

    public void loadRoom(String roomID) {
        deviceListFragment = DeviceListFragment.newInstance(projectID, buildingID, roomID);
        loadFragment(deviceListFragment);
        setTitle("Installing Berts in Room: " + roomID + " (" + project.getBertsByRoom(buildingID, roomID).size() + " Berts)");
    }

    public void addRoom() {
        BertEditTextAlert.show(this, "Enter New Room Name", "", "Create New Room", "Cancel", this);
    }

    public void setDefaultTitle() {
        //TODO: make this more descriptive
        setTitle("Installing Berts");
    }

    public void okPressed(String result) {
        //TODO: add room to listview
        loadRoom(result);
    }

    public void cancelPressed(String result) {

    }
}
