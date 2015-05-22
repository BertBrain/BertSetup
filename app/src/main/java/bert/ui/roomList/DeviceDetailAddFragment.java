package bert.ui.roomList;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import bert.data.ProjectProvider;
import bert.data.proj.BertUnit;
import bert.data.proj.Building;
import bert.data.proj.Project;
import bert.data.utility.Cleaner;
import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceDetailAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailAddFragment extends Fragment {
    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";
    private static final String ARG_LOCATION = "location";

    private int projectID;
    private int buildingID;

    private RoomListActivity activity;
    private Project project;
    private Building building;
    private String location;

    private Button addDeviceDoneButton;
    private Button addDeviceCancelButton;

    private EditText nameEditText;
    private EditText macAddressEditText;
    private Spinner categorySpinner;

    public static DeviceDetailAddFragment newInstance(int projectID, int buildingID, String location) {
        DeviceDetailAddFragment fragment = new DeviceDetailAddFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putInt(ARG_BUILDING_ID, buildingID);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (RoomListActivity)getActivity();
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            buildingID = getArguments().getInt(ARG_BUILDING_ID);
            location = getArguments().getString(ARG_LOCATION);

            project = ProjectProvider.getInstance().getProject(projectID);
            building = project.getBuilding(buildingID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nameEditText = (EditText) getView().findViewById(R.id.addDeviceNameTextField);
        macAddressEditText = (EditText) getView().findViewById(R.id.addDeviceMacAddressTextField);
        categorySpinner = (Spinner) getView().findViewById(R.id.addDeviceCategorySpinner);
        addDeviceDoneButton = (Button) getView().findViewById(R.id.addDeviceDoneButton);
        addDeviceCancelButton = (Button) getView().findViewById(R.id.addDeviceCancelButton);

        addDeviceDoneButton.setEnabled(false);
        addDeviceDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cleanedString = Cleaner.clean(nameEditText.getText().toString());
                addDeviceDoneButton.setEnabled(Cleaner.isValid(cleanedString));
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, building.getCategoryNames());
        categorySpinner.setAdapter(adapter);
    }

    private void finish() {
        BertUnit bert = new BertUnit(
                nameEditText.getText().toString(),
                location,
                macAddressEditText.getText().toString(),
                buildingID,
                categorySpinner.getSelectedItemPosition(),
                false
        );
        project.addBert(bert);
        activity.deviceListFragment.onResume();
        activity.deviceListFragment.openNewestDeviceDetailFragment();
    }

    public DeviceDetailAddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_add, container, false);
    }
}
