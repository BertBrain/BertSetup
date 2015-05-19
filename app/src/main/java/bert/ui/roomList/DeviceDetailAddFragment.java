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

import java.util.ArrayList;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PROJECT = "project";
    private static final String ARG_BUILDING = "building";
    private static final String ARG_LOCATION = "loaciton";

    // TODO: Rename and change types of parameters
    private int projectID;
    private int buildingID;

    private Project project;
    private Building building;
    private String location;

    private Button addDeviceDoneButton;
    private Button addDeviceCancelButton;

    EditText nameTextField;
    EditText macAdressTextField;
    Spinner categorySpinner;

    static DeviceEditorFragment owner;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param projectID Parameter 1.
     * @param buildingID Parameter 2.
     * @return A new instance of fragment DeviceDetailAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceDetailAddFragment newInstance(DeviceEditorFragment owner, int projectID, int buildingID, String location) {
        DeviceDetailAddFragment fragment = new DeviceDetailAddFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT, projectID);
        args.putInt(ARG_BUILDING, buildingID);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        DeviceDetailAddFragment.owner = owner;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT);
            buildingID = getArguments().getInt(ARG_BUILDING);
            location = getArguments().getString(ARG_LOCATION);
            project = ProjectProvider.getInstance().getProjectList().get(projectID);
            building = project.getBuildings().get(buildingID);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        nameTextField = (EditText) getView().findViewById(R.id.addDeviceNameTextField);
        macAdressTextField = (EditText) getView().findViewById(R.id.addDeviceMacAdressTextField);
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

        nameTextField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cleanedString = Cleaner.clean(nameTextField.getText().toString());
                addDeviceDoneButton.setEnabled(Cleaner.isValid(cleanedString));
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, building.getCategoryNames());
        System.out.println("number of caterogies in building: " + building.getCategories().size());
        categorySpinner.setAdapter(adapter);
    }

    private void finish(){
        BertUnit bert = new BertUnit(nameTextField.getText().toString(), location, macAdressTextField.getText().toString(), buildingID, categorySpinner.getSelectedItemPosition());
        project.addBert(bert);
        owner.loadNewDevice();
    }

    public DeviceDetailAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_detail_add, container, false);
    }


}
