package bert.ui.roomList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.BertUnit;
import bert.data.proj.Building;
import bert.data.proj.Project;
import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceDetailEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceDetailEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailEditFragment extends Fragment {
    private static final String ARG_PROJECT_KEY = "PROJECT_ID";
    private static final String ARG_BUILDING_KEY = "BUILDING_ID";
    private static final String ARG_BERT_KEY = "BERTKEY";
    private static final String ARG_LOCATION_KEY = "LOCATIONKEY";

    private int projectID;
    private int buildingID;
    private int bertID;

    private RoomListActivity activity;
    private Project project;
    private Building building;
    private String location;
    private BertUnit bert;

    private EditText deviceNameTextField;
    private EditText macAddressTextField;
    private EditText roomTextField;
    private TextView buildingTextField;
    private Spinner categorySelector;
    private Button deleteButton;
    private Button saveButton;

    public static DeviceDetailEditFragment newInstance(int projectID, int buildingID, String location, int bertID) {
        DeviceDetailEditFragment fragment = new DeviceDetailEditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_KEY, projectID);
        args.putInt(ARG_BUILDING_KEY, buildingID);
        args.putString(ARG_LOCATION_KEY, location);
        args.putInt(ARG_BERT_KEY, bertID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_KEY);
            buildingID = getArguments().getInt(ARG_BUILDING_KEY);
            this.location = getArguments().getString(ARG_LOCATION_KEY);
            bertID = getArguments().getInt(ARG_BERT_KEY);
            project = ProjectProvider.getInstance().getProjectList().get(projectID);
            building = project.getBuildings().get(buildingID);
            bert = project.getBertsByLocation(buildingID, location).get(bertID);
            activity = (RoomListActivity)getActivity();
        }
    }

    public DeviceDetailEditFragment() {}

    @Override
    public void onResume() {
        super.onResume();
        macAddressTextField = (EditText) getView().findViewById(R.id.macAddress);
        deviceNameTextField = (EditText) getView().findViewById(R.id.deviceName);
        roomTextField = (EditText) getView().findViewById(R.id.room);
        buildingTextField = (TextView) getView().findViewById(R.id.currentBuildingDisplay);
        categorySelector = (Spinner) getView().findViewById(R.id.detailViewCategorySelector);

        saveButton = (Button) getView().findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        deleteButton = (Button) getView().findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Are you sure you want to delete bert?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bert.deleteBert();
                        activity.createLocationlistView();
                        activity.openDeviceEditorView(location);
                        FileProvider.saveProject(project);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();
            }
        });

        ArrayAdapter<String> categoryTableAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, building.getCategoryNames());
        categorySelector.setAdapter(categoryTableAdapter);

        roomTextField.setText(location);

        macAddressTextField.setText(bert.getMAC());

        deviceNameTextField.setText(bert.getName());

        categorySelector.setSelection(bert.getCategoryID());
    }

    private void saveChanges() {
        bert.setMAC(macAddressTextField.getText().toString());
        bert.setName(deviceNameTextField.getText().toString());
        bert.setCategoryID(categorySelector.getSelectedItemPosition());
        FileProvider.saveProject(project);
        activity.deviceEditorFragment.onResume();
        onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_detail_edit, container, false);
    }

    public void onButtonPressed(Uri uri) {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
