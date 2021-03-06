package bert.ui.roomList.deviceList.deviceEditor;


import android.os.Bundle;
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
import bert.ui.common.ProjectChildEditorFragment;
import bert.utility.Cleaner;
import bert.ui.R;
import bert.ui.roomList.roomListActivity.InstallRoomListActivity;

public class DeviceAddFragment extends ProjectChildEditorFragment {
    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";
    private static final String ARG_LOCATION = "location";

    private String projectID;
    private String buildingID;

    private InstallRoomListActivity activity;
    private Building building;
    private String location;

    private Button addDeviceDoneButton;
    private Button addDeviceCancelButton;

    private EditText nameEditText;
    private EditText macAddressEditText;
    private Spinner categorySpinner;
    private ArrayAdapter<String> categoryAdapter;

    public static DeviceAddFragment newInstance(String projectID, String buildingID, String location) {
        DeviceAddFragment fragment = new DeviceAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (InstallRoomListActivity)getActivity();
        if (getArguments() != null) {
            projectID = getArguments().getString(ARG_PROJECT_ID);
            buildingID = getArguments().getString(ARG_BUILDING_ID);
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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cleanedString = Cleaner.clean(nameEditText.getText().toString());
                addDeviceDoneButton.setEnabled(Cleaner.isValid(cleanedString));
            }
        });
        categoryAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, building.getCategoryNames());
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void finish() {
        BertUnit bert = new BertUnit(
                nameEditText.getText().toString(),
                location,
                macAddressEditText.getText().toString(),
                buildingID,
                categoryAdapter.getItem(categorySpinner.getSelectedItemPosition())
        );
        project.addBert(bert);
        activity.loadListView();
        activity.deviceListFragment.onResume();
        activity.deviceListFragment.openNewestDeviceDetailFragment();
    }

    public DeviceAddFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.device_add_fragment, container, false);
    }
}
