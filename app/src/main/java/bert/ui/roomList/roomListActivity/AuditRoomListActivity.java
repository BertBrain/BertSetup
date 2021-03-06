package bert.ui.roomList.roomListActivity;

import bert.ui.roomList.deviceList.auditWizard.AuditWizardFragment;

/**
 * Created by liamcook on 6/16/15.
 */
public class AuditRoomListActivity  extends RoomListActivity {

    public void loadRoom(String roomID) {
        //TODO: add load method to audit wizard fragment
        AuditWizardFragment fragment = AuditWizardFragment.newInstance(projectID, buildingID, roomID);
        loadFragment(fragment);
        setTitle("Auditing room: " + roomID);
    }

    public void addRoom() {
        AuditWizardFragment fragment = AuditWizardFragment.newInstance(projectID, buildingID);
        loadFragment(fragment);
        setTitle("Auditing a new Room");
    }

    public void setDefaultTitle() {
        //TODO
        setTitle("Auditing building");
    }
}
