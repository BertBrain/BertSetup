package bert.ui.roomList.roomListActivity;

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
import bert.ui.common.SelectableListGVA;
import bert.ui.roomList.deviceList.deviceEditor.DeviceListFragment;

public abstract class GeneralRoomListActivity extends ActionBarActivity {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";
    public static final String ARG_BUILDING_ID = "BUILDING_ID";

    protected String projectID;
    protected String buildingID;

    protected Project project;

    public DeviceListFragment deviceListFragment;

    private Button newRoomButton;
    private SelectableListGVA roomListAdapter;
    private ListView roomListView;
    public InputMethodManager inputManager;

    abstract public void loadRoom(String roomID);
    abstract public void addRoom();
    abstract public void setDefaultTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        if (savedInstanceState == null) {//if restoring, don't replace
            Bundle extras = getIntent().getExtras();
            projectID = extras.getString(ARG_PROJECT_ID);
            buildingID = extras.getString(ARG_BUILDING_ID);
        }
        inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        openNoSelection();
    }

    @Override
    public void onResume() {
        super.onResume();
        project = ProjectProvider.getInstance().getProject(projectID);
        newRoomButton = (Button) findViewById(R.id.create_list_item_button);
        newRoomButton.setText("New Room");
        newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRoom();
                if (roomListAdapter != null){
                    roomListAdapter.clear();
                }
            }
        });

        loadListView();

        setDefaultTitle();
    }

    public void loadListView() {
        final List<String> roomNames = project.getRoomNamesInBuilding(buildingID);

        if (roomNames.size() != 0) {
            roomListAdapter = new SelectableListGVA(this, roomNames);
            roomListView = (ListView) findViewById(R.id.item_list_view);
            roomListView.setAdapter(roomListAdapter);
            roomListAdapter.setOnClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    loadRoom(roomNames.get(position));
                }
            });
        }
    }

    public void openNoSelection() {
        loadFragment(NoSelectionFragment.newInstance("Select or Create a Room"));
    }

    protected void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_layout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
