package bert.ui.roomList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import bert.data.proj.Project;
import bert.data.ProjectProvider;
import bert.ui.common.NoSelectionFragment;
import bert.ui.R;
import bert.ui.roomList.deviceList.DeviceListFragment;
import bert.ui.roomList.deviceList.auditWizard.AuditTallyBoxGVA;
import bert.ui.roomList.deviceList.auditWizard.AuditWizardFragment;

public class RoomListActivity extends ActionBarActivity {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";
    public static final String ARG_BUILDING_ID = "BUILDING_ID";

    private int projectID;
    private String buildingID;

    private Project project;

    public DeviceListFragment deviceListFragment;

    private Button startAuditButton;
    private ArrayAdapter<String> roomListAdapter;
    private ListView roomListView;
    public InputMethodManager inputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        if (savedInstanceState == null) {//if restoring, don't replace
            Bundle extras = getIntent().getExtras();
            projectID = extras.getInt(ARG_PROJECT_ID);
            buildingID = extras.getString(ARG_BUILDING_ID);
        }
        inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        openNoSelection();
    }

    @Override
    public void onResume() {
        super.onResume();
        project = ProjectProvider.getInstance().getProject(projectID);
        startAuditButton = (Button) findViewById(R.id.create_list_item_button);
        startAuditButton.setText("Audit Wizard");
        startAuditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAuditWizardFragment();
            }
        });

        final List<String> roomNames = project.getRoomNamesInBuilding(buildingID);

        if (roomNames.size() != 0) {
            roomListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, roomNames);
            roomListView = (ListView) findViewById(R.id.item_list_view);
            roomListView.setAdapter(roomListAdapter);
            roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openDeviceListFragment(roomNames.get(position));
                }
            });
        }
        setDefaultTitle();
    }

    public void openAuditWizardFragment() {
        AuditWizardFragment fragment = AuditWizardFragment.newInstance(projectID, buildingID);
        loadFragment(fragment);
        setTitle("Audit a new Room");
    }

    public void openDeviceListFragment(String locationName) {
        deviceListFragment = DeviceListFragment.newInstance(projectID, buildingID, locationName);
        loadFragment(deviceListFragment);
        setTitle("Room " + locationName + " (" + project.getBertsByRoom(buildingID, locationName).size() + " Berts)");
    }

    public void openNoSelection() {
        loadFragment(NoSelectionFragment.newInstance("Select or Create a Room"));
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_layout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setDefaultTitle() {
        int rooms = project.getRoomNamesInBuilding(buildingID).size();
        String roomName = (rooms == 1) ? " Room)" : " Rooms)";
        this.setTitle(project.getProjectName() + " " + buildingID + " (" + rooms + roomName);
    }
}
