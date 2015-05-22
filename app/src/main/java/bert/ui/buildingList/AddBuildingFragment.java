package bert.ui.buildingList;

import android.app.Activity;
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
import android.widget.TextView;

import java.util.List;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.data.proj.Category;
import bert.data.proj.CategoryPresets;
import bert.data.proj.Project;
import bert.data.proj.Time;
import bert.data.proj.exceptions.DuplicateBuildingInProjectException;
import bert.data.utility.Cleaner;
import bert.ui.BertAlert;
import bert.ui.R;

public class AddBuildingFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "PROJECT_ID";

    private int projectID;
    private Project project;

    private Time defaultStartTime = new Time(9, 0);
    private Time defaultEndTime = new Time(17, 0);
    private ArrayAdapter<String> buildingTypeArrayAdapter;

    private EditText buildingNameTextField;
    private Button addBuildingButton;
    private Spinner buildingTypeSpinner;
    private TextView startTimeDisplay;
    private TextView endTimeDisplay;
    private TimeRangeDisplay timeDisplay;

    public static AddBuildingFragment newInstance(int projectID) {
        AddBuildingFragment fragment = new AddBuildingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        fragment.setArguments(args);
        return fragment;
    }

    public AddBuildingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            project = ProjectProvider.getInstance().getProject(projectID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimeDisplay = (TextView) getView().findViewById(R.id.start_time_textfield);
        endTimeDisplay = (TextView) getView().findViewById(R.id.end_time_text_field);
        timeDisplay = new TimeRangeDisplay(
                getActivity(),
                startTimeDisplay,
                defaultStartTime,
                endTimeDisplay,
                defaultEndTime
        );

        buildingTypeSpinner = (Spinner)getView().findViewById(R.id.building_type_spinner);
        buildingTypeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, CategoryPresets.getNames());
        buildingTypeSpinner.setAdapter(buildingTypeArrayAdapter);

        buildingNameTextField = (EditText) getView().findViewById(R.id.add_building_name_textfield);
        buildingNameTextField.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addBuildingButton.setEnabled(Cleaner.isValid(buildingNameTextField.getText().toString()));
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        addBuildingButton = (Button) getView().findViewById(R.id.add_building_button);
        addBuildingButton.setEnabled(false);
        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBuilding();
            }
        });
    }

    private void addBuilding() {
        try {
            String newName = buildingNameTextField.getText().toString();
            Time startTime = timeDisplay.getStartTime();
            Time endTime = timeDisplay.getEndTime();
            List<Category> presetCategories = CategoryPresets.getPresets().get(buildingTypeSpinner.getSelectedItem());
            Building building = new Building(newName, startTime, endTime, presetCategories);
            project.addBuilding(building);
            project.save();
            BuildingListActivity activity = (BuildingListActivity) getActivity();
            activity.loadListView();
            activity.openBuildingDetailView(project.highestBuildingIndex());
        } catch(DuplicateBuildingInProjectException e) {
            BertAlert.show(getActivity(), "This Building Already Exists");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_building_add, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
